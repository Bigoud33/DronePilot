package bigoud.com.dronepilot.model;

import dji.sdk.base.BaseProduct;
import dji.sdk.products.Aircraft;

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

    private Aircraft aircraft = null;

    public Aircraft getAircraft()
    {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft)
    {
        this.aircraft = aircraft;
    }
}
