package bigoud.com.dronepilot.model.drone;

import bigoud.com.dronepilot.model.Position;

/**
 * Created by aeres on 2/13/2018.
 */

public class InitFlightResult
{
    public Position position = null;
    public Position oldPosition = null;
    public float heading = 0.0f;

    public InitFlightResult(Position pos, Position oldPos, float heading)
    {
        this.position = pos;
        this.oldPosition = oldPos;
        this.heading = heading;
    }
}
