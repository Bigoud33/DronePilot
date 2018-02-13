package bigoud.com.dronepilot.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bigoud.com.dronepilot.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button newC=null;
    private Button histo=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        newC = findViewById(R.id.newC);
        newC.setOnClickListener(this);
        histo = findViewById(R.id.histo);
        histo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.newC:
                Intent Maps = new Intent(this, Maps.class);
                startActivity(Maps);
                break;
            case R.id.histo:
                Intent Histo = new Intent(this, Histo.class);
                startActivity(Histo);
        }
    }
}