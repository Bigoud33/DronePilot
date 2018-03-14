package bigoud.com.dronepilot.controller;

import android.util.Log;

import bigoud.com.dronepilot.model.MavicProInstance;
import bigoud.com.dronepilot.model.Position;
import bigoud.com.dronepilot.model.drone.ConnectResult;
import bigoud.com.dronepilot.model.drone.DroneTask;
import bigoud.com.dronepilot.model.drone.InitFlightResult;
import bigoud.com.dronepilot.model.drone.LookAtResult;
import bigoud.com.dronepilot.model.drone.MoveToResult;
import bigoud.com.dronepilot.model.drone.ReturnHomeResult;
import bigoud.com.dronepilot.model.drone.TakePhotoResult;
import dji.common.error.DJIError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.RTKState;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by aeres on 2/12/2018.
 */

public class MavicPro extends VirtualDrone
{
    private FlightController fc = null;
    private Position pos = new Position();

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
            }
        });
    }

    @Override
    public void onConnect(DroneTask<ConnectResult> result)
    {
        if(!MavicProInstance.getInstance().getAircraft().isConnected())
        {
            //TODO
            result.setSuccess(false);
            result.setMessage("TODO");
        }

        result.setSuccess(true);
        result.setMessage("OK");
        result.setResult(new ConnectResult(pos));
    }

    @Override
    public void onInitFlight(final DroneTask<InitFlightResult> result)
    {
        LocationCoordinate3D pos3D = fc.getState().getAircraftLocation();
        Position oldPos = new Position(pos3D.getLatitude(), pos3D.getLongitude(), pos3D.getAltitude());

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
                    LocationCoordinate3D pos3D = fc.getState().getAircraftLocation();
                    Position oldPos = new Position(pos3D.getLatitude(), pos3D.getLongitude(), pos3D.getAltitude());

                    result.setResult(new InitFlightResult(pos));
                    result.setMessage("OK");
                    result.setSuccess(true);
                }

                this.notify();
            }
        };

        fc.startTakeoff(callback);
        try {callback.wait();} catch (InterruptedException e) {}
    }

    @Override
    public void onMoveTo(DroneTask<MoveToResult> result, Position pos)
    {

    }

    @Override
    public void onLookAt(DroneTask<LookAtResult> result, Position pos)
    {

    }

    @Override
    public void onReturnHome(DroneTask<ReturnHomeResult> result)
    {

    }

    @Override
    public void onTakePhoto(DroneTask<TakePhotoResult> result)
    {

    }

    @Override
    public Position getPosition()
    {
        return pos;
    }

    @Override
    public void getVideo()
    {

    }
}
