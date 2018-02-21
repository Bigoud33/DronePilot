package bigoud.com.dronepilot.model.drone;

/**
 * Created by aeres on 2/13/2018.
 */

public class DroneTask<V>
{
    private Thread thread = null;
    private volatile V result = null;
    private volatile boolean done = false;
    private volatile boolean success = true;
    private volatile String message = "";

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
        this.thread.interrupt();
        try {this.thread.join();} catch (InterruptedException e) {}
    }

    public final V getResult()
    {
        return this.result;
    }

    public final void setResult(V result)
    {
        this.result = result;
    }

    public final boolean isDone()
    {
        return this.done;
    }

    public final void setDone(boolean done)
    {
        this.done = done;
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
