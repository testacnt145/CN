package com.chattynotes.constant;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.chattynotes.R;
import com.chattynotes.application.App;

public class AppVersionCode {

    public final static int VERSION_CODE_FIRST_TIME = -1;
    public final static int VERSION_CODE_SQL_TO_REALM_MIGRATION_REQUIRED = 2000;

    //__________________________________________________________________________________________________________________
    public static int getApkVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(App.PACKAGE_NAME, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //throw new RuntimeException("Could not get package name: " + e);
        }
        return 0;
    }
}
