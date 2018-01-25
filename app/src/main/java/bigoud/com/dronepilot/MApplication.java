package bigoud.com.dronepilot;

/**
 * Created by brand on 22/01/2018.
 */

import android.app.Application;
import android.content.Context;
import com.secneo.sdk.Helper;
public class MApplication extends Application {
    private UIApplication uiApplication;
    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        Helper.install(MApplication.this);
        if (uiApplication == null) {
            uiApplication = new UIApplication();
            uiApplication.setContext(this);
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        uiApplication.onCreate();
    }
}