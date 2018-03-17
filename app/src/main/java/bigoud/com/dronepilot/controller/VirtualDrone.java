package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.model.Position;
import dji.sdk.camera.VideoFeeder;

/**
 * Created by aeres on 2/12/2018.
 */

public abstract class VirtualDrone
{
    public abstract void onConnect(DroneTask result);
    public final DroneTask connect()
    {
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onConnect(task);
            }
        });

        return task;
    }

    public abstract void onDisconnect(DroneTask result);
    public final DroneTask disconnect()
    {
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onDisconnect(task);
            }
        });

        return task;
    }

    public abstract void onInitFlight(DroneTask result);
    public final DroneTask initFlight()
    {
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onInitFlight(task);
            }
        });

        return task;
    }

    public abstract void onMoveTo(DroneTask result, Position pos);
    public final DroneTask moveTo(final Position pos)
    {
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onMoveTo(task, pos);
            }
        });

        return task;
    }

    public abstract void onLookAt(DroneTask result, Position pos, boolean continuous);
    public final DroneTask lookAt(final Position pos, final boolean continuous)
    {
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onLookAt(task, pos, continuous);
            }
        });

        return task;
    }

    public abstract void onReturnHome(DroneTask result);
    public final DroneTask returnHome()
    {
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onReturnHome(task);
            }
        });

        return task;
    }

    public abstract void onTakePhoto(DroneTask result);
    public final DroneTask takePhoto()
    {
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onTakePhoto(task);
            }
        });

        return task;
    }

    public abstract Position getPosition();
    public abstract float getHeading();
    public abstract VideoFeeder.VideoFeed getVideo();
}
