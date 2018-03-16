package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.model.Position;

/**
 * Created by aeres on 2/12/2018.
 */

public abstract class VirtualDrone
{
    private volatile DroneTask currentTask = null;

    public abstract void onConnect(DroneTask result);
    public final DroneTask connect()
    {
        this.cancelCurrent();
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onConnect(task);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onInitFlight(DroneTask result);
    public final DroneTask initFlight()
    {
        this.cancelCurrent();
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onInitFlight(task);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onMoveTo(DroneTask result, Position pos);
    public final DroneTask moveTo(final Position pos)
    {
        this.cancelCurrent();
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onMoveTo(task, pos);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onLookAt(DroneTask result, Position pos);
    public final DroneTask lookAt(final Position pos)
    {
        this.cancelCurrent();
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onLookAt(task, pos);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onReturnHome(DroneTask result);
    public final DroneTask returnHome()
    {
        this.cancelCurrent();
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onReturnHome(task);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onTakePhoto(DroneTask result);
    public final DroneTask takePhoto()
    {
        this.cancelCurrent();
        final DroneTask task = new DroneTask();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onTakePhoto(task);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract Position getPosition();
    public abstract float getHeading();
    public abstract void getVideo();

    private final void cancelCurrent()
    {
        if(!this.currentTask.isRunning())
        {
            this.currentTask.cancel();
            this.currentTask = null;
        }
    }
}
