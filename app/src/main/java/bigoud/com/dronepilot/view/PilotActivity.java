package bigoud.com.dronepilot.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.ArrayList;

import bigoud.com.dronepilot.R;
import bigoud.com.dronepilot.controller.DroneTask;
import bigoud.com.dronepilot.controller.SimulationDrone;
import bigoud.com.dronepilot.controller.VirtualDrone;
import bigoud.com.dronepilot.model.Position;

public class PilotActivity extends AppCompatActivity
{
    private final VirtualDrone drone = new SimulationDrone();
    private ArrayList<Position> positions = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot);

        this.positions = (ArrayList<Position>)getIntent().getSerializableExtra("points");
        DroneTask connect = drone.connect();
        connect.join();

    }

    private void log(String message)
    {
        EditText logger = findViewById(R.id.console);
        logger.append(message + "\n");
    }
}
