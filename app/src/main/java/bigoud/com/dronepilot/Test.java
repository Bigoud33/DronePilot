package bigoud.com.dronepilot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bigoud.com.dronepilot.controller.SDK.utils.DialogUtils;
import bigoud.com.dronepilot.controller.SDK.utils.ModuleVerificationUtil;
import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import dji.sdk.flightcontroller.FlightController;

import static dji.midware.data.manager.P3.ServiceManager.getContext;

public class Test extends AppCompatActivity implements View.OnClickListener{

    private Button btnTakeOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTakeOff = findViewById(R.id.btn_take_off);
        btnTakeOff.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FlightController flightController = ModuleVerificationUtil.getFlightController();
        if (flightController == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_take_off:

                flightController.startTakeoff(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        DialogUtils.showDialogBasedOnError(getContext(), djiError);
                    }
                });
                break;
            default:
                break;
        }
    }
}
