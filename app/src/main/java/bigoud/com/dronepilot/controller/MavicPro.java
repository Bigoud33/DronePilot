package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.model.Position;

/**
 * Created by aeres on 2/12/2018.
 */

public class MavicPro extends VirtualDrone
{
    @Override
    public boolean connect()
    {
        return false;
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

    @Override
    public boolean initFlight()
    {
        return false;
    }

    @Override
    public boolean goToPoint(Position pos)
    {
        return false;
    }

    @Override
    public boolean lookAt(Position pos)
    {
        return false;
    }

    @Override
    public boolean takePhoto()
    {
        return false;
    }

    @Override
    public boolean returnHome()
    {
        return false;
    }
}
