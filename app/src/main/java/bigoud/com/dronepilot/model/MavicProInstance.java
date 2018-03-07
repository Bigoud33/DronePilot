package bigoud.com.dronepilot.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.concurrent.atomic.AtomicBoolean;

import bigoud.com.dronepilot.controller.SDK.controller.SDKApplication;
import bigoud.com.dronepilot.controller.SDK.utils.ToastUtils;
import bigoud.com.dronepilot.view.PilotActivity;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.log.DJILog;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;

import static bigoud.com.dronepilot.controller.SDK.controller.SDKApplication.TAG;

/**
 * Created by aeres on 2/28/2018.
 */

public class MavicProInstance
{

    private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);
    private static MavicProInstance instance = null;

    public static MavicProInstance getInstance()
    {
        if(instance == null)
            instance = new MavicProInstance();

        return instance;
    }

    private BaseComponent.ComponentListener mDJIComponentListener = new BaseComponent.ComponentListener() {

        @Override
        public void onConnectivityChange(boolean isConnected) {
            Log.d(TAG, "onComponentConnectivityChanged: " + isConnected);
            notifyStatusChange();
        }
    };
    private BaseProduct.BaseProductListener mDJIBaseProductListener = new BaseProduct.BaseProductListener() {

        @Override
        public void onComponentChange(BaseProduct.ComponentKey key,
                                      BaseComponent oldComponent,
                                      BaseComponent newComponent) {

            if (newComponent != null) {
                newComponent.setComponentListener(mDJIComponentListener);
            }
            Log.d(TAG,
                    String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s",
                            key,
                            oldComponent,
                            newComponent));

            notifyStatusChange();
        }

        @Override
        public void onConnectivityChange(boolean isConnected) {

            Log.d(TAG, "onProductConnectivityChanged: " + isConnected);

            notifyStatusChange();
        }
    };

    public MavicProInstance()
    {
        SDKApplication.getEventBus().register(this);

    }

    public void close(){
        SDKApplication.getEventBus().unregister(this);
    }

    public void startSDKRegistration(final Context context) {
        if (isRegistrationInProgress.compareAndSet(false, true)) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.setResultToToast("Registering, pls wait...");
                    DJISDKManager.getInstance().registerApp(context, new DJISDKManager.SDKManagerCallback() {
                        @Override
                        public void onRegister(DJIError djiError) {
                            if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                                DJILog.e("App registration", DJISDKError.REGISTRATION_SUCCESS.getDescription());
                                DJISDKManager.getInstance().startConnectionToProduct();
                                ToastUtils.setResultToToast("SDK Registered Successfully");
                            } else {
                                ToastUtils.setResultToToast("SDK Registration Failed. Please check the bundle ID and your network\n" +
                                        "        connectivity");
                            }
                            Log.v(TAG, djiError.getDescription());
                        }

                        @Override
                        public void onProductChange(BaseProduct oldProduct, BaseProduct newProduct) {
                            Log.d(TAG, String.format("onProductChanged oldProduct:%s, newProduct:%s", oldProduct, newProduct));
                            notifyStatusChange();
                        }
                    });
                }
            });
        }
    }

    private void notifyStatusChange() {
        SDKApplication.getEventBus().post(new ConnectivityChangeEvent());
    }

    public static class ConnectivityChangeEvent {
    }

}
