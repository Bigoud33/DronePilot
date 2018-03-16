package bigoud.com.dronepilot.controller;

import android.util.Log;

import bigoud.com.dronepilot.model.MavicProInstance;
import bigoud.com.dronepilot.model.Position;
import dji.common.error.DJIError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.RTKState;
import dji.common.util.CommonCallbacks;
import dji.sdk.flightcontroller.FlightController;

/**
 * Created by aeres on 2/12/2018.
 */

public class MavicPro extends VirtualDrone
{
    private FlightController fc = null;
    private Position pos = new Position();
    private float heading = 0.0f;

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
    public void onInitFlight(final DroneTask result)
    {
        LocationCoordinate3D pos3D = fc.getState().getAircraftLocation();

        CommonCallbacks.CompletionCallback callback = new CommonCallbacks.CompletionCallback()
        {
            @Override
            public void onResult(DJIError djiError)
            {
                if(djiError != null)
                {
                    Log.e("DronePilot", djiError.toString());
                }
                else
                {
                    /*LocationCoordinate3D pos3D = fc.getState().getAircraftLocation();
                    Position oldPos = new Position(pos3D.getLatitude(), pos3D.getLongitude(), pos3D.getAltitude());

                    result.setMessage("OK");
                    result.setSuccess(true);*/
                }

                this.notify();
            }
        };

        fc.startTakeoff(callback);
        try {callback.wait();} catch (InterruptedException e) {}

        result.setSuccess(true);
        result.setMessage("OK");
    }

    @Override
    public void onMoveTo(DroneTask result, Position pos)
    {

    }

    @Override
    public void onLookAt(DroneTask result, Position pos)
    {

    }

    @Override
    public void onReturnHome(DroneTask result)
    {

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
    public void getVideo()
    {

    }
}
