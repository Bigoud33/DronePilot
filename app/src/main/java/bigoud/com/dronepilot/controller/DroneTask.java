package bigoud.com.dronepilot.controller;

/**
 * Created by aeres on 2/13/2018.
 */

public class DroneTask
{
    private Thread thread = null;
    private volatile boolean success = true;
    private volatile String message = "";
    private volatile boolean running = true; // Used by some calls

    public void run(Runnable runnable)
    {
        this.thread = new Thread(runnable);
        this.thread.start();
    }

    public void join()
    {
        try {this.thread.join();} catch (InterruptedException e) {}
    }

    public void interrupt()
    {
        this.thread.interrupt();
    }

    public void cancel()
    {
        this.running = false;
        this.thread.interrupt();
        try {this.thread.join();} catch (InterruptedException e) {}
    }

    public boolean isAlive()
    {
        return this.thread.isAlive();
    }

    public boolean isRunning()
    {
        return this.running;
    }

    public final boolean isSuccess()
    {
        return this.success;
    }

    public final void setSuccess(boolean success)
    {
        this.success = success;
    }

    public final String getMessage()
    {
        return this.message;
    }

    public final void setMessage(String message)
    {
        this.message = message;
    }
}
