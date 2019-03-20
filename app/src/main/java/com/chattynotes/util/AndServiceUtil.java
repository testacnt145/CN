package com.chattynotes.util;

import android.content.Context;
//import android.content.Intent;
//import com.chattynotes.services.ServiceApp;

public class AndServiceUtil {

    public static void startServiceApp(Context _ctx) {
        /*if(ServiceApp.runningInstanceOfService == null)
            _ctx.startService(new Intent(_ctx, ServiceApp.class));*/
    }

    public static void stopServiceApp(Context _ctx) {
        /*if(ServiceApp.runningInstanceOfService != null) {
			ServiceApp.runningInstanceOfService = null;
            _ctx.stopService(new Intent(_ctx, ServiceApp.class));
        }*/
    }
}
