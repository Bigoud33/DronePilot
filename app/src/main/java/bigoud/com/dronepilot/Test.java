package bigoud.com.dronepilot;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import bigoud.com.dronepilot.controller.SDK.controller.SDKApplication;
import bigoud.com.dronepilot.controller.SDK.utils.DialogUtils;
import bigoud.com.dronepilot.controller.SDK.utils.ModuleVerificationUtil;
import bigoud.com.dronepilot.controller.SDK.utils.ToastUtils;
import bigoud.com.dronepilot.model.MavicProInstance;
import dji.common.error.DJIError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.virtualstick.FlightControlData;
import dji.common.flightcontroller.virtualstick.RollPitchControlMode;
import dji.common.flightcontroller.virtualstick.VerticalControlMode;
import dji.common.flightcontroller.virtualstick.YawControlMode;
import dji.common.util.CommonCallbacks;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mobilerc.MobileRemoteController;

public class Test extends AppCompatActivity implements View.OnClickListener{

    private Button btnTakeOff;
    private Button btnLanding;
    private Button btnForward;
    private MobileRemoteController mrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTakeOff = findViewById(R.id.btn_take_off);
        btnTakeOff.setOnClickListener(this);
        btnLanding = findViewById(R.id.btn_landing);
        btnLanding.setOnClickListener(this);
        btnForward = findViewById(R.id.btn_forward);
        btnForward.setOnClickListener(this);
        MavicProInstance mpi = MavicProInstance.getInstance();
        FlightController flightController = ModuleVerificationUtil.getFlightController();
        flightController.setVirtualStickModeEnabled(true, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if(djiError!=null)
                    ToastUtils.setResultToToast(djiError.toString());
            }
        });
        flightController.setVirtualStickAdvancedModeEnabled(true);
        flightController.setRollPitchControlMode(RollPitchControlMode.ANGLE);
        flightController.setYawControlMode(YawControlMode.ANGLE);
        flightController.setVerticalControlMode(VerticalControlMode.VELOCITY);
        mrc = SDKApplication.getAircraftInstance().getMobileRemoteController();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        final FlightController flightController = ModuleVerificationUtil.getFlightController();
        if (flightController == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_take_off:
                flightController.startTakeoff(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
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
            case R.id.btn_forward:
                Log.d("BTN", "BTN");
                /*flightController.sendVirtualStickFlightControlData(new FlightControlData(0, 5, 0, 0), new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if(djiError!=null)
                            ToastUtils.setResultToToast(djiError.toString());
                    }
                });*/

                /*new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Log.d("BTN","BackGround");
                        FlightControlData fcd = new FlightControlData(0,0,0,0.5f);
                            while(flightController.getState().getAircraftLocation().getAltitude()<10) {
                            flightController.sendVirtualStickFlightControlData(fcd, new CommonCallbacks.CompletionCallback() {

                                @Override
                                public void onResult(DJIError djiError) {
                                    if (djiError != null)
                                        ToastUtils.setResultToToast(djiError.toString());
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            }
                        return null;
                    }
                }.execute();*/

                mrc.setLeftStickVertical(1.0f);

                break;
            default:
                break;
        }
    }
}
