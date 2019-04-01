package com.chattynotes.util;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;

import com.chattynoteslite.R;
import com.chattynotes.camera.CameraActivity;
import com.chattynotes.constant.IntentKeys;

public final class ShortcutUtil {

    //useless not working
    //http://stackoverflow.com/questions/6988511/how-to-add-apps-shortcut-to-the-home-screen
//	public static void createAppShortcut(Activity ctx) {
//		//create shortcut if requested
//		ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(ctx, R.drawable.icon);
//		//activity 2 launch
//		Intent launchIntent = new Intent(ctx, MainActivity.class);
//		launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//		launchIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//
//		Intent in = new Intent();
//		in.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
//		in.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.app_name));
//		in.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
//
//		ctx.sendBroadcast(in);
//	}


	//http://www.kind-kristiansen.no/2010/android-adding-desktop-shortcut-support-to-your-app/
	public static void createCameraShortcut(Activity ctx) {
		//create shortcut if requested
		ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(ctx, R.mipmap.launcher_camera);
		//activity 2 launch
		Intent launchIntent = new Intent(ctx, CameraActivity.class);
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		launchIntent.putExtra(IntentKeys.IS_CAMERA_ACCESS_VIA_HOME_SCREEN, true);//we have set this Flag so that we can determine that the app is open from home screen or normal view.

		Intent in = new Intent();
		in.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
		in.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.title_activity_camera));
		in.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		ctx.setResult(Activity.RESULT_OK, in);
	}
	
//	//100% working method
//	//http://www.kind-kristiansen.no/2010/android-adding-desktop-shortcut-support-to-your-app/
//	public static void createCameraShortcut(Activity ctx) {
//		//create shortcut if requested
//		ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(ctx, R.mipmap.launcher_camera);
//		Intent i = new Intent();
//		//activity 2 launch
//		Intent launchIntent = new Intent(ctx, CameraActivity.class);
//		launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		i.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
//		i.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.title_activity_camera));
//		i.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
//		ctx.setResult(Activity.RESULT_OK, i);
//	}

}
