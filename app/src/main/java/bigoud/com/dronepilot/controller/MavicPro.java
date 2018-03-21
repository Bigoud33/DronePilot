package bigoud.com.dronepilot.controller;

import android.util.Log;

import java.util.concurrent.CountDownLatch;

import bigoud.com.dronepilot.controller.SDK.controller.SDKApplication;
import bigoud.com.dronepilot.model.MavicProInstance;
import bigoud.com.dronepilot.model.Position;
import dji.common.error.DJIError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.RTKState;
import dji.common.flightcontroller.virtualstick.RollPitchControlMode;
import dji.common.flightcontroller.virtualstick.VerticalControlMode;
import dji.common.flightcontroller.virtualstick.YawControlMode;
import dji.common.util.CommonCallbacks;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mobilerc.MobileRemoteController;

/**
 * Created by aeres on 2/12/2018.
 */

public class MavicPro extends VirtualDrone
{
    private FlightController fc = null;

    private Thread controlThread = null;
    private volatile boolean controlThreadRunning = false;
    private volatile float controlLeftX = 0.0f;
    private volatile float controlLeftY = 0.0f;
    private volatile float controlRightX = 0.0f;
    private volatile float controlRightY = 0.0f;

    public MavicPro()
    {
        this.fc = SDKApplication.getAircraftInstance().getFlightController();
    }

    @Override
    public void onConnect(DroneTask result)
    {
        if(!SDKApplication.getAircraftInstance().isConnected())
        {
            //TODO
            result.setSuccess(false);
            result.setMessage("TODO");
        }

        result.setSuccess(true);
        result.setMessage("OK");
    }

    @Override
    public void onDisconnect(DroneTask result)
    {
        if(this.controlThread != null)
            this.returnHome().join();

        //TODO

        result.setMessage("OK");
        result.setSuccess(true);
    }

    @Override
    public void onInitFlight(final DroneTask result)
    {
        final CountDownLatch lock2 = new CountDownLatch(1);

        fc.setVirtualStickModeEnabled(true, new CommonCallbacks.CompletionCallback()
        {
            @Override
            public void onResult(DJIError djiError)
            {
                if(djiError != null)
                {
                    result.setSuccess(false);
                    result.setMessage(djiError.toString());
                }
                else
                {
                    fc.setVirtualStickAdvancedModeEnabled(true);
                    result.setSuccess(true);
                    result.setMessage("OK");
                }

                lock2.countDown();
            }
        });

        try {lock2.await();} catch (InterruptedException e) {}
        if(!result.isSuccess())
            return;

        final CountDownLatch lock = new CountDownLatch(1);

        fc.startTakeoff(new CommonCallbacks.CompletionCallback()
        {
            @Override
            public void onResult(DJIError djiError)
            {
                if(djiError != null)
                {
                    result.setSuccess(false);
                    result.setMessage(djiError.toString());
                }
                else
                {
                    result.setSuccess(true);
                    result.setMessage("OK");
                }

                lock.countDown();
            }
        });

        try {lock.await();} catch (InterruptedException e) {}
        if(!result.isSuccess())
            return;

        fc.setRollPitchControlMode(RollPitchControlMode.ANGLE);
        fc.setYawControlMode(YawControlMode.ANGLE);
        fc.setVerticalControlMode(VerticalControlMode.VELOCITY);

        controlThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                MobileRemoteController mrc = SDKApplication.getAircraftInstance().getMobileRemoteController();

                while(controlThreadRunning)
                {
                    mrc.setLeftStickHorizontal(controlLeftX);
                    mrc.setLeftStickVertical(controlLeftY);
                    mrc.setRightStickHorizontal(controlRightX);
                    mrc.setRightStickVertical(controlRightY);

                    try {Thread.sleep(200);} catch (InterruptedException e) {}
                }
            }
        });

        controlThreadRunning = true;
        controlLeftX = 0.0f;
        controlLeftY = 0.0f;
        controlRightX = 0.0f;
        controlRightY = 0.0f;
        controlThread.start();

        result.setSuccess(true);
        result.setMessage("OK");
    }

    @Override
    public void onMoveTo(DroneTask result, Position pos)
    {
        DroneTask lookTask = this.lookAt(pos, true);
        double delta = 100;
        LocationCoordinate3D dronePos = null;

        while(delta > 0.00008)
        {
            dronePos = fc.getState().getAircraftLocation();
            double opposite = Math.abs(dronePos.getLongitude() - pos.longitude);
            double adjacent = Math.abs(dronePos.getLatitude() - pos.latitude);
            delta = Math.sqrt(Math.pow(opposite, 2) + Math.pow(adjacent, 2));

            try {Thread.sleep(100);} catch (InterruptedException e) {}
        }

        this.controlRightX = 0.0f;
        this.controlRightY = 0.0f;

        lookTask.cancel();

        result.setSuccess(true);
        result.setMessage("OK");
    }

    @Override
    public void onLookAt(DroneTask result, Position pos, boolean continuous)
    {
        float delta = 180;
        LocationCoordinate3D dronePos = null;

        while(result.isRunning() && (!continuous || Math.abs(delta) > 1.0))
        {
            dronePos = fc.getState().getAircraftLocation();
            double opposite = Math.abs(dronePos.getLongitude() - pos.longitude);
            double adjacent = Math.abs(dronePos.getLatitude() - pos.latitude);
            double hypotenuse = Math.sqrt(Math.pow(opposite, 2) + Math.pow(adjacent, 2));
            double cos = adjacent / hypotenuse;
            double sin = opposite / hypotenuse;

            float angle = (float)Math.acos(cos);
            if(sin < 0)
                angle += 180.0;

            angle = 360 - angle; // because SDK doesn't use trigonometric rotation
            delta = fc.getCompass().getHeading() - angle;
            if(delta > 180)
                delta = - (360 - delta);
            else if(delta < -180)
                delta = 360 + delta;

            this.controlLeftX = delta / 180;

            // CAMERA TODO

            try {Thread.sleep(500);} catch (InterruptedException e) {}
        }

        this.controlLeftX = 0.0f;

        result.setSuccess(true);
        result.setMessage("OK");
    }

    @Override
    public void onReturnHome(final DroneTask result)
    {
        if(this.controlThread == null)
            return;

        controlThreadRunning = false;
        try {controlThread.join();} catch (InterruptedException e) {}
        controlThread = null;

        final CountDownLatch lock = new CountDownLatch(1);

        this.fc.startGoHome(new CommonCallbacks.CompletionCallback()
        {
            @Override
            public void onResult(DJIError djiError)
            {
                if(djiError != null)
                {
                    result.setSuccess(false);
                    result.setMessage(djiError.toString());
                }
                else
                {
                    result.setSuccess(true);
                    result.setMessage("OK");
                }

                lock.countDown();
            }
        });

        try {lock.await();} catch (InterruptedException e) {}

        if(!result.isSuccess())
            return;

        final CountDownLatch lock2 = new CountDownLatch(1);

        fc.startLanding(new CommonCallbacks.CompletionCallback()
        {
            @Override
            public void onResult(DJIError djiError)
            {
                if(djiError != null)
                {
                    result.setSuccess(false);
                    result.setMessage(djiError.toString());
                    lock2.countDown();
                }
                else
                {
                    fc.confirmLanding(new CommonCallbacks.CompletionCallback()
                    {
                        @Override
                        public void onResult(DJIError djiError)
                        {
                            if(djiError != null)
                            {
                                result.setSuccess(false);
                                result.setMessage(djiError.toString());
                            }
                            else
                            {
                                result.setSuccess(true);
                                result.setMessage("OK");
                            }

                            lock2.countDown();
                        }
                    });
                }
            }
        });

        try {lock2.await();} catch (InterruptedException e) {}
    }

    @Override
    public void onTakePhoto(DroneTask result)
    {

    }

    @Override
    public Position getPosition()
    {
        Position pos = new Position();
        LocationCoordinate3D dronePos = fc.getState().getAircraftLocation();

        pos.longitude = dronePos.getLongitude();
        pos.latitude = dronePos.getLatitude();
        pos.height = dronePos.getAltitude();
        return pos;
    }

    @Override
    public float getHeading()
    {
        return fc.getCompass().getHeading();
    }

    @Override
    public VideoFeeder.VideoFeed getVideo()
    {
        return VideoFeeder.getInstance().getPrimaryVideoFeed();
    }
}
