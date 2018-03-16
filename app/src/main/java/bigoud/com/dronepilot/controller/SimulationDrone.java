package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.model.Position;

/**
 * Created by aeres on 2/13/2018.
 */

public class SimulationDrone extends VirtualDrone
{
    private volatile Position dronePos = new Position(46.216050, 5.247543, 250);
    private volatile float heading = 0.0f;
    private volatile boolean inAir = false;

    @Override
    public void onConnect(DroneTask result)
    {
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        result.setMessage("OK");
        result.setSuccess(true);
    }

    @Override
    public void onInitFlight(DroneTask result)
    {
        try {Thread.sleep(2000);} catch (InterruptedException e) {}
        Position oldPos = new Position(this.dronePos);
        this.dronePos.height += 4.0;
        result.setMessage("OK");
        result.setSuccess(true);
    }

    @Override
    public void onMoveTo(DroneTask result, Position pos)
    {
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

        result.setSuccess(true);
        result.setMessage("OK");
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
        return new Position(dronePos);
    }

    @Override
    public float getHeading() {
        return heading;
    }

    @Override
    public void getVideo()
    {

    }
}
