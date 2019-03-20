package com.chattynotes.util;

import com.chattynotes.application.App;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class AndroidUtil {
	
	public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }
	
	public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            App.applicationHandler.post(runnable);
        } else {
            App.applicationHandler.postDelayed(runnable, delay);
        }
    }
	
//	public static void cancelRunOnUIThread(Runnable runnable) {
//        App.applicationHandler.removeCallbacks(runnable);
//  }
	
//______________________________________________________________________________________________________
	public static float density = 1;
	public static int statusBarHeight = 0;
	public static Point displaySize = new Point();
    
	static {
        density = App.applicationContext.getResources().getDisplayMetrics().density;
        checkDisplaySize();
    }
    
	@SuppressWarnings("deprecation")
	public static void checkDisplaySize() {
        try {
            WindowManager manager = (WindowManager)App.applicationContext.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    if(android.os.Build.VERSION.SDK_INT < 13) {
                        displaySize.set(display.getWidth(), display.getHeight());
                    } else {
                        display.getSize(displaySize);
                    }
                    //LogUtil.e("getDisplaySize", "display size = " + displaySize.x + " " + displaySize.y);
                }
            }
        } catch (Exception e) {
        	//LogUtil.eException("AndroidUtil", "Exception", "checkDisplaySize()", e.toString());
        }
    }

	public static int dp(int value) {
		return (int)(Math.max(1, density * value));
    }
	
	public static int getCurrentActionBarHeight() {
       if (App.applicationContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return dp(48);
        } else {
            return dp(56);
        }
    }

}
