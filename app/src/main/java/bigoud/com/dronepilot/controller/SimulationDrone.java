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
    private volatile float heading = 0.0f;
    private volatile boolean inAir = false;

    @Override
    public void onConnect(DroneTask<ConnectResult> result)
    {
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        result.setResult(new ConnectResult(new Position(dronePos)));
        result.setMessage("OK");
        result.setSuccess(true);
    }

    @Override
    public void onInitFlight(DroneTask<InitFlightResult> result)
    {
        try {Thread.sleep(2000);} catch (InterruptedException e) {}
        Position oldPos = new Position(this.dronePos);
        this.dronePos.height += 4.0;
        result.setResult(new InitFlightResult(new Position(dronePos), oldPos, heading));
        result.setMessage("OK");
        result.setSuccess(true);
    }

    @Override
    public void onMoveTo(DroneTask<MoveToResult> result, Position pos)
    {
        MoveToResult res = new MoveToResult();
        res.oldPosition = new Position(this.dronePos);

        while(true)
        {
            if(pos.latitude == dronePos.latitude && pos.longitude == dronePos.longitude)
                break;

            double latDelta = pos.latitude - dronePos.latitude;
            if(latDelta > 0.0005)
                latDelta = 0.0005;
            else if(latDelta < -0.0005)
                latDelta = -0.0005;

            double longDelta = pos.longitude - dronePos.longitude;
            if(longDelta > 0.0005)
                longDelta = 0.0005;
            else if(longDelta < -0.0005)
                longDelta = -0.0005;

            try {Thread.sleep(2000);} catch (InterruptedException e) {}
            this.dronePos.latitude += latDelta;
            this.dronePos.longitude += longDelta;
        }

        res.position = new Position(this.dronePos);
        result.setSuccess(true);
        result.setResult(res);
        result.setMessage("OK");
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
        return new Position(dronePos);
    }

    @Override
    public void getVideo()
    {

    }
}
