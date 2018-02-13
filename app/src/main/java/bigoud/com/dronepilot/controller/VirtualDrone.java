package bigoud.com.dronepilot.controller;

import bigoud.com.dronepilot.model.Position;
import bigoud.com.dronepilot.model.drone.*;

/**
 * Created by aeres on 2/12/2018.
 */

public abstract class VirtualDrone
{
    private volatile DroneTask currentTask = null;

    public abstract void onConnect(DroneTask<ConnectResult> result);
    public final DroneTask<ConnectResult> connect()
    {
        this.cancelCurrent();
        final DroneTask<ConnectResult> task = new DroneTask<ConnectResult>();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onConnect(task);
                task.setDone(true);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onInitFlight(DroneTask<InitFlightResult> result);
    public final DroneTask<InitFlightResult> initFlight()
    {
        this.cancelCurrent();
        final DroneTask<InitFlightResult> task = new DroneTask<InitFlightResult>();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onInitFlight(task);
                task.setDone(true);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onMoveTo(DroneTask<MoveToResult> result, Position pos);
    public final DroneTask<MoveToResult> moveTo(final Position pos)
    {
        this.cancelCurrent();
        final DroneTask<MoveToResult> task = new DroneTask<MoveToResult>();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onMoveTo(task, pos);
                task.setDone(true);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onLookAt(DroneTask<LookAtResult> result, Position pos);
    public final DroneTask<LookAtResult> lookAt(final Position pos)
    {
        this.cancelCurrent();
        final DroneTask<LookAtResult> task = new DroneTask<LookAtResult>();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onLookAt(task, pos);
                task.setDone(true);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onReturnHome(DroneTask<ReturnHomeResult> result);
    public final DroneTask<ReturnHomeResult> returnHome()
    {
        this.cancelCurrent();
        final DroneTask<ReturnHomeResult> task = new DroneTask<ReturnHomeResult>();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onReturnHome(task);
                task.setDone(true);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract void onTakePhoto(DroneTask<TakePhotoResult> result);
    public final DroneTask<TakePhotoResult> takePhoto()
    {
        this.cancelCurrent();
        final DroneTask<TakePhotoResult> task = new DroneTask<TakePhotoResult>();
        task.run(new Runnable()
        {
            @Override
            public void run()
            {
                onTakePhoto(task);
                task.setDone(true);
            }
        });

        this.currentTask = task;
        return task;
    }

    public abstract Position getPosition();
    public abstract void getVideo();

    private final void cancelCurrent()
    {
        if(!this.currentTask.isDone())
        {
            this.currentTask.cancel();
            this.currentTask = null;
        }
    }
}
