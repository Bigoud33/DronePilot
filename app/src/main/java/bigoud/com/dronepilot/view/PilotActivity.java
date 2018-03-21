package bigoud.com.dronepilot.view;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import bigoud.com.dronepilot.R;
import bigoud.com.dronepilot.controller.DroneTask;
import bigoud.com.dronepilot.controller.MavicPro;
import bigoud.com.dronepilot.controller.SimulationDrone;
import bigoud.com.dronepilot.controller.VirtualDrone;
import bigoud.com.dronepilot.model.Position;
import dji.sdk.camera.VideoFeeder;

public class PilotActivity extends AppCompatActivity
{
    private final VirtualDrone drone = new MavicPro();
    private ArrayList<Position> positions = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        this.positions = (ArrayList<Position>)getIntent().getSerializableExtra("points");
        DroneTask connect = drone.connect();
        connect.join();

        VideoFeeder.VideoFeed feed = drone.getVideo();
        if(feed != null)
        {
            ((VideoFeedView)findViewById(R.id.video_feed)).registerLiveVideo(feed, true);
        }

        this.shoot();
    }

    private void shoot()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d("DronePilot", "Starting");

                // Calculates center point;
                Position middle = new Position();

                DroneTask initFlight = drone.initFlight();
                initFlight.join();

                if(!initFlight.isSuccess())
                {
                    Log.e("DronePilot", initFlight.getMessage());
                    return;
                }

                Log.d("DronePilot", "Flight initiated");

                int current = 0;
                for(Position pos : PilotActivity.this.positions)
                {
                    Log.d("DronePilot", "Moving to position " + Integer.toString(current) + "/" + Integer.toString(PilotActivity.this.positions.size()));
                    current++;

                    DroneTask moveTask = drone.moveTo(pos);
                    moveTask.join();

                    if(!moveTask.isSuccess())
                    {
                        Log.e("DronePilot", moveTask.getMessage());
                        drone.returnHome();
                        return;
                    }

                    Log.d("DronePilot", "Moved to position, starting taking photo...");

                    DroneTask lookTask = drone.lookAt(middle, false);
                    lookTask.join();

                    if(!lookTask.isSuccess())
                    {
                        Log.e("DronePilot", lookTask.getMessage());
                        drone.returnHome();
                        return;
                    }

                    Log.d("DronePilot", "Looking at building");

                    DroneTask photoTask = drone.takePhoto();
                    photoTask.join();

                    if(!photoTask.isSuccess())
                    {
                        Log.e("DronePilot", photoTask.getMessage());
                        drone.returnHome();
                        return;
                    }

                    Log.d("DronePilot", "Took photo");
                }

                Log.d("DronePilot", "Ended. Going back home.");

                DroneTask homeTask = drone.returnHome();
                homeTask.join();

                if(!homeTask.isSuccess())
                {
                    Log.e("DronePilot", homeTask.getMessage());
                }

                Log.d("DronePilot", "Landed successfully");
            }
        }).start();
    }
}
