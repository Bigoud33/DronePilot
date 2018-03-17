package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.controller.SDK.controller.SDKApplication;
import bigoud.com.dronepilot.model.MavicProInstance;
import bigoud.com.dronepilot.model.Position;
import dji.common.error.DJIError;
import dji.common.flightcontroller.RTKState;
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
    private Position pos = new Position();
    private volatile float heading = 0.0f;

    private Thread controlThread = null;
    private volatile boolean controlThreadRunning = false;
    private volatile float controlLeftX = 0.0f;
    private volatile float controlLeftY = 0.0f;
    private volatile float controlRightX = 0.0f;
    private volatile float controlRightY = 0.0f;

    public MavicPro() throws Exception
    {
        if(MavicProInstance.getInstance().getAircraft() == null)
            throw new Exception("Aircraft not set");

        this.fc = MavicProInstance.getInstance().getAircraft().getFlightController();

        fc.getRTK().setStateCallback(new RTKState.Callback()
        {
            @Override
            public void onUpdate(RTKState rtkState)
            {
                MavicPro.this.pos.longitude = rtkState.getMobileStationLocation().getLongitude();
                MavicPro.this.pos.latitude = rtkState.getMobileStationLocation().getLatitude();
                MavicPro.this.pos.height = rtkState.getMobileStationAltitude();
                MavicPro.this.heading = rtkState.getHeading();
            }
        });
    }

    @Override
    public void onConnect(DroneTask result)
    {
        if(!MavicProInstance.getInstance().getAircraft().isConnected())
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
        CommonCallbacks.CompletionCallback callback = new CommonCallbacks.CompletionCallback()
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

                this.notify();
            }
        };

        fc.startTakeoff(callback);
        try {callback.wait();} catch (InterruptedException e) {}

        if(!result.isSuccess())
            return;

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

        while(delta > 0.00008)
        {
            double opposite = Math.abs(this.pos.longitude - pos.longitude);
            double adjacent = Math.abs(this.pos.latitude - pos.latitude);
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
        while(result.isRunning() && (!continuous || Math.abs(delta) > 1.0))
        {
            double opposite = Math.abs(this.pos.longitude - pos.longitude);
            double adjacent = Math.abs(this.pos.latitude - pos.latitude);
            double hypotenuse = Math.sqrt(Math.pow(opposite, 2) + Math.pow(adjacent, 2));
            double cos = adjacent / hypotenuse;
            double sin = opposite / hypotenuse;

            float angle = (float)Math.acos(cos);
            if(sin < 0)
                angle += 180.0;

            angle = 360 - angle; // because SDK doesn't use trigonometric rotation
            delta = this.heading - angle;
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

        CommonCallbacks.CompletionCallback callback = new CommonCallbacks.CompletionCallback()
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

                this.notify();
            }
        };

        this.fc.startTakeoff(callback);
        try {callback.wait();} catch (InterruptedException e) {}

        if(!result.isSuccess())
            return;

        final CommonCallbacks.CompletionCallback landCB = new CommonCallbacks.CompletionCallback()
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

                this.notify();
            }
        };

        fc.startLanding(new CommonCallbacks.CompletionCallback()
        {
            @Override
            public void onResult(DJIError djiError)
            {
                if(djiError != null)
                {
                    result.setSuccess(false);
                    result.setMessage(djiError.toString());
                    landCB.notify();
                }
                else
                {
                    fc.confirmLanding(landCB);
                }
            }
        });

        try {landCB.wait();} catch (InterruptedException e) {}
    }

    @Override
    public void onTakePhoto(DroneTask result)
    {

    }

    @Override
    public Position getPosition()
    {
        return pos;
    }

    @Override
    public float getHeading()
    {
        return heading;
    }

    @Override
    public VideoFeeder.VideoFeed getVideo()
    {
        return VideoFeeder.getInstance().getPrimaryVideoFeed();
    }
}
