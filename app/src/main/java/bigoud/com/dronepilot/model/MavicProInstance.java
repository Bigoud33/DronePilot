package bigoud.com.dronepilot.model;

/**
 * Created by aeres on 2/28/2018.
 */

public class MavicProInstance
{
    private static MavicProInstance instance = null;

    public static MavicProInstance getInstance()
    {
        if(instance == null)
            instance = new MavicProInstance();

        return instance;
    }

    public MavicProInstance()
    {

    }
}
