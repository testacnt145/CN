package com.chattynotes.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.chattynotes.BuildConfig;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
//import com.facebook.stetho.Stetho;
//import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
//import java.io.UnsupportedEncodingException;
//import java.util.regex.Pattern;

public class App extends Application {
//http://stackoverflow.com/questions/21818905/get-application-context-from-non-activity-singleton-class
	
	public static Context applicationContext;
	public static String PACKAGE_NAME;
	public static volatile Handler applicationHandler = null;
    
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        //[release_: (1) uncomment for release build]
        if(!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        applicationContext = getApplicationContext();
        PACKAGE_NAME = getPackageName();
        applicationHandler = new Handler(applicationContext.getMainLooper());



//        //[stetho_: ]
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(
//                                Stetho.defaultDumperPluginsProvider(this)
//                        )
//                        .enableWebKitInspector(
//                                RealmInspectorModulesProvider
//                                        .builder(this)
//                                        .databaseNamePattern(getPattern())
//                                        .build())
//                        .build());
    }

    public static Context getContext() {
        return applicationContext;
    }

    public static String getQuantityString(int id, int quantity) {
        return applicationContext.getResources().getQuantityString(id, quantity);
    }



//
//    //for viewing database with extension other than .realm Stetho
//    //https://github.com/uPhyca/stetho-realm/issues/22
//    private Pattern getPattern() {
//        //accept both db and realm
//        return Pattern.compile(".+\\.db|.+\\.realm");
//
//    }
}
