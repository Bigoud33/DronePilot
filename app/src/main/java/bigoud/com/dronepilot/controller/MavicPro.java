package bigoud.com.dronepilot.controller;

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
    public void onInitFlight(DroneTask<InitFlightResult> result)
    {
        fc.startTakeoff(new CommonCallbacks.CompletionCallback()
        {
            @Override
            public void onResult(DJIError djiError)
            {
                if(djiError != null)
                {
                    
                }
            }
        });
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
