package bigoud.com.dronepilot.model;

import java.io.Serializable;

/**
 * Created by aeres on 2/12/2018.
 */

public class Position implements Serializable
{
    public volatile double latitude;
    public volatile double longitude;
    public volatile double height;

    public Position()
    {
        this.latitude = 0;
        this.longitude = 0;
        this.height = 0;
    }

    public Position(double latitude, double longitude, double height)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
    }

    public Position(Position other)
    {
        this.latitude = other.latitude;
        this.longitude = other.longitude;
        this.height = other.height;
    }

    @Override
    public String toString()
    {
        return "lat: " + this.latitude + ", long: " + this.longitude + ", height: " + this.height;
    }
}
