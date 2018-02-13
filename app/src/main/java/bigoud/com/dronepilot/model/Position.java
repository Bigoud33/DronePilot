package bigoud.com.dronepilot.model;

/**
 * Created by aeres on 2/12/2018.
 */

public class Position
{
    public double latitude;
    public double longitude;
    public double height;

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
