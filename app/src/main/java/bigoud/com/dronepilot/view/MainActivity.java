package bigoud.com.dronepilot.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bigoud.com.dronepilot.R;
import bigoud.com.dronepilot.Test;
import bigoud.com.dronepilot.controller.SDK.utils.ModuleVerificationUtil;
import bigoud.com.dronepilot.model.MavicProInstance;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button newC=null;
    private Button histo=null;
    private Button test=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // When the compile and target version is higher than 22, please request the
        // following permissions at runtime to ensure the
        // SDK work well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }
        setContentView(R.layout.activity_mainmenu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        newC = findViewById(R.id.newC);
        newC.setOnClickListener(this);
        histo = findViewById(R.id.histo);
        histo.setOnClickListener(this);
        test = findViewById(R.id.test);
        test.setOnClickListener(this);
        MavicProInstance mpi = MavicProInstance.getInstance();
        mpi.startSDKRegistration(getApplicationContext());
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
                break;
            case R.id.test:
                if(ModuleVerificationUtil.isFlightControllerAvailable()) {
                    Intent Test = new Intent(this, Test.class);
                    startActivity(Test);
                }
                break;
        }
    }
}