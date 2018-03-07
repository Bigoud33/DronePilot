package bigoud.com.dronepilot;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import bigoud.com.dronepilot.controller.SDK.utils.DialogUtils;
import bigoud.com.dronepilot.controller.SDK.utils.ModuleVerificationUtil;
import bigoud.com.dronepilot.controller.SDK.utils.ToastUtils;
import bigoud.com.dronepilot.model.MavicProInstance;
import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import dji.sdk.flightcontroller.FlightController;

public class Test extends AppCompatActivity implements View.OnClickListener{

    private Button btnTakeOff;
    private Button btnLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTakeOff = findViewById(R.id.btn_take_off);
        btnTakeOff.setOnClickListener(this);
        btnLanding = findViewById(R.id.btn_landing);
        btnLanding.setOnClickListener(this);
        MavicProInstance mpi = MavicProInstance.getInstance();
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
                        //DialogUtils.showDialogBasedOnError(getApplicationContext(), djiError);
                        if(djiError!=null)
                            ToastUtils.setResultToToast(djiError.toString());
                    }
                });
                break;
            case R.id.btn_landing:
                flightController.startLanding(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError){
                        if(djiError!=null)
                            ToastUtils.setResultToToast(djiError.toString());
                    }
                });
                flightController.confirmLanding(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError){
                        if(djiError!=null)
                            ToastUtils.setResultToToast(djiError.toString());
                    }
                });
                break;
            default:
                break;
        }
    }
}
