package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.model.Position;

/**
 * Created by aeres on 2/12/2018.
 */

public abstract class VirtualDrone
{
    public abstract boolean connect();
    public abstract Position getPosition();
    public abstract void getVideo();
    public abstract boolean initFlight();
    public abstract boolean goToPoint(Position pos);
    public abstract boolean lookAt(Position pos);
    public abstract boolean takePhoto();
    public abstract boolean returnHome();
}
