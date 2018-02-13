package bigoud.com.dronepilot.controller;

import android.util.Log;

import bigoud.com.dronepilot.model.Position;
import bigoud.com.dronepilot.model.drone.ConnectResult;
import bigoud.com.dronepilot.model.drone.DroneTask;
import bigoud.com.dronepilot.model.drone.InitFlightResult;
import bigoud.com.dronepilot.model.drone.LookAtResult;
import bigoud.com.dronepilot.model.drone.MoveToResult;
import bigoud.com.dronepilot.model.drone.ReturnHomeResult;
import bigoud.com.dronepilot.model.drone.TakePhotoResult;

/**
 * Created by aeres on 2/13/2018.
 */

public class SimulationDrone extends VirtualDrone
{
    private volatile Position dronePos = new Position(46.216050, 5.247543, 250);
    private volatile boolean inAir = false;

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
