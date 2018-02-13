package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.model.Position;
import bigoud.com.dronepilot.model.drone.ConnectResult;
import bigoud.com.dronepilot.model.drone.DroneTask;
import bigoud.com.dronepilot.model.drone.InitFlightResult;
import bigoud.com.dronepilot.model.drone.LookAtResult;
import bigoud.com.dronepilot.model.drone.MoveToResult;
import bigoud.com.dronepilot.model.drone.ReturnHomeResult;
import bigoud.com.dronepilot.model.drone.TakePhotoResult;

/**
 * Created by aeres on 2/12/2018.
 */

public class MavicPro extends VirtualDrone
{
    @Override
    public void onConnect(DroneTask<ConnectResult> result)
    {

    }

    @Override
    public void onInitFlight(DroneTask<InitFlightResult> result)
    {

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
        return null;
    }

    @Override
    public void getVideo()
    {

    }
}
