package com.chattynotes.preferences;

import com.chattynotes.mvp.activities.MainActivity;
import com.chattynotes.application.App;
import com.chattynotes.constant.AppVersionCode;
import com.chattynotes.util.WallpaperUtil;
import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
	//saved in internal location : com.chattynotes/shared_prefs/com.chattynotes.xml

	private static SharedPreferences get(final Context context) {
        return context.getSharedPreferences(App.PACKAGE_NAME, Context.MODE_PRIVATE);
    }

	private static String getPref(final Context context, String pref, String def) {
		SharedPreferences prefs = Prefs.get(context);
		String val = prefs.getString(pref, def);

		if (val == null || val.equals("") || val.equals("null"))
			return def;
		else
			return val;
	}

	private static void putPref(final Context context, String pref, String val) {
		SharedPreferences prefs = Prefs.get(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(pref, val);
		editor.apply();
	}
	
	private static int getIntPref(final Context context, String pref, int def) {
		SharedPreferences prefs = Prefs.get(context);
        return prefs.getInt(pref, def);
	}

	private static void putIntPref(final Context context, String pref, int val) {
		SharedPreferences prefs = Prefs.get(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putInt(pref, val);
		editor.apply();
	}

//	private static long getLongPref(final Context context, String pref, long def) {
//		SharedPreferences prefs = Prefs.get(context);
//        return prefs.getLong(pref, def);
//	}
//
//	private static void putLongPref(final Context context, String pref, long val) {
//		SharedPreferences prefs = Prefs.get(context);
//		SharedPreferences.Editor editor = prefs.edit();
//
//		editor.putLong(pref, val);
//		editor.apply();
//    }

	private static Boolean getBooleanPref(final Context context, String pref, Boolean def) {
		SharedPreferences prefs = Prefs.get(context);
        return prefs.getBoolean(pref, def);
	}

	private static void putBooleanPref(final Context context, String pref, Boolean val) {
		SharedPreferences prefs = Prefs.get(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putBoolean(pref, val);
		editor.apply();
    }

	//================================================================================================
	private static int encryptInteger(int i) {
		return (((i + 4) * 37) - 63);
	}

	private static int decryptInteger(int i) {
		return (((i + 63) / 37) - 4);
	}

//	private static String encryptString(Context context, String str) {
//		try {
//			str = android.util.Base64.encodeToString((str + getSalt(context)).getBytes("UTF-8"), android.util.Base64.DEFAULT);
//		} catch (Exception e) {
//		}
//		return str;
//	}
//
//	private static String decryptString(Context context, String str) {
//		String decodeString = "";
//		try {
//			byte[] data = android.util.Base64.decode(str, android.util.Base64.DEFAULT);
//			decodeString = new String(data, "UTF-8");
//		} catch (Exception e) {
//		}
//		return decodeString.substring(0, decodeString.length() - getSalt(context).length());
//	}
//
//	private static String getSalt(final Context context) {
//		//In cryptography, a salt is random data that is used as an additional input to a one-way function that hashes a password or passphrase.
//		return Prefs.getPref(context, "X", "is");//salt = is
//	}

	//++++++++++++++++++++++ ENCRYPTED PREFERENCES START (1)++++++++++++++++++++++++++++++++++++++++

	//________________________________________________________________________________________ STAGE
	public static int getStage(final Context context) {
		int encryptedValue = Prefs.getIntPref(context, PKey.STAGE, encryptInteger(MainActivity.STAGE_FIRST_SCREEN));
		return decryptInteger(encryptedValue);
	}

	public static void putStage(final Context context, int stage) {
		Prefs.putIntPref(context, PKey.STAGE, encryptInteger(stage));
	}

	//_______________ NOT ENCRYPTED
    public static int getAppVersionCode(final Context context) {
		return Prefs.getIntPref(context, PKey.APP_VERSION_CODE, AppVersionCode.VERSION_CODE_FIRST_TIME);
	}

	public static void putAppVersionCode(final Context context, int version_code) {
		Prefs.putIntPref(context, PKey.APP_VERSION_CODE, version_code);
	}

	public static boolean getIsDisplayNewVersionBanner(final Context context) {
		return Prefs.getBooleanPref(context, PKey.IS_DISPLAY_NEW_VERSION_BANNER, PDefaultValue.IS_DISPLAY_NEW_VERSION_BANNER);
	}

	public static void putIsDisplayNewVersionBanner(final Context context, Boolean value) {
		Prefs.putBooleanPref(context, PKey.IS_DISPLAY_NEW_VERSION_BANNER, value);
	}

	//________________________________________________________________________________________ PERMISSION FIRST TIME
	public static boolean getIsPermissionFirstTime(final Context context) {
		return Prefs.getBooleanPref(context, PKey.IS_PERMISSION_FIRST_TIME, PDefaultValue.IS_PERMISSION_FIRST_TIME);
	}

	public static void putIsPermissionFirstTime(final Context context, Boolean value) {
		Prefs.putBooleanPref(context, PKey.IS_PERMISSION_FIRST_TIME, value);
	}

//	//________________________________________________________________________________________ VERSION
//	public static String getApplicationVersion(final Context context) {
//		String encryptedValue = Prefs.getPref(context, PKey.APPLICATION_VERSION, encryptString(context, AppVersion.v_FRESH));//default AppVersion.v_FRESH
//		return decryptString(context, encryptedValue);
//
//		//old return Prefs.getPref(context, "APPLICATION_VERSION", context.getString(R.string.app_version_name));
//		//new return Prefs.getPref(context, "APPLICATION_VERSION", AppVersion.v_FRESH);//default APK version should be AppVersion.v_FRESH
//	}
//
//	public static void putApplicationVersion(final Context context, String version) {
//		Prefs.putPref(context, PKey.APPLICATION_VERSION, encryptString(context, version));
//	}
//
//	//___
//	public static int getApplicationVersionCode(final Context context) {
//		int encryptedValue = Prefs.getIntPref(context, PKey.APP_VERSION_CODE, encryptInteger(0));//default 0
//		return decryptInteger(encryptedValue);
//	}
//
//	public static void putApplicationVersionCode(final Context context, int version_code) {
//		Prefs.putIntPref(context, PKey.APP_VERSION_CODE, encryptInteger(version_code));
//	}

	//------------------------- ENCRYPTED PREFERENCES END (1) --------------------------------------

//____________________________________________________________________________________________ CHAT SETTINGS
	//----------------- Enter Key (false = New Line, true = Send Button)
//	public static Boolean getIsEnterKeySend(final Context context) {
//		return Prefs.getBooleanPref(context, PKey.IS_ENTER_KEY_SEND, PDefaultValue.DEFAULT_IS_ENTER_KEY_SEND);
//	}
//
//	public static void putIsEnterKeySend(final Context context, Boolean enter_key) {
//		Prefs.putBooleanPref(context, PKey.IS_ENTER_KEY_SEND, enter_key);
//	}

	
	//----------------- Font Size
	public static final int FONT_SMALL 	= 14;
	public static final int FONT_MEDIUM = 16;
	public static final int FONT_LARGE 	= 19;
	public static int getFontSize(final Context context) {
		return Prefs.getIntPref(context, PKey.FONT_SIZE, FONT_MEDIUM);
		//old method
		//when we were setting font size using
		//txtView.setTextAppearance(context, Prefs.getFontSize(context));
		//  android.R.style.TextAppearance_Small
		//  android.R.style.TextAppearance_Medium
		//  android.R.style.TextAppearance_Large
	}
	private static final int FONT_EMOJI_SMALL 	= 22;
	private static final int FONT_EMOJI_MEDIUM 	= 25;
	private static final int FONT_EMOJI_LARGE 	= 28;
	public static int getEmojiconSize(final Context context) {
		switch (Prefs.getIntPref(context, PKey.FONT_SIZE, FONT_MEDIUM)) {
			case FONT_SMALL:
				return FONT_EMOJI_SMALL;
			case FONT_MEDIUM:
				return FONT_EMOJI_MEDIUM;
			case FONT_LARGE:
				return FONT_EMOJI_LARGE;
		}
		return FONT_EMOJI_MEDIUM;
	}
	private static final int FONT_DATE_SMALL 	= 12;
	private static final int FONT_DATE_MEDIUM 	= 14;
	private static final int FONT_DATE_LARGE 	= 17;
	public static int getDateBubbleFontSize(final Context context) {
		switch (Prefs.getIntPref(context, PKey.FONT_SIZE, FONT_MEDIUM)) {
			case FONT_SMALL:
				return FONT_DATE_SMALL;
			case FONT_MEDIUM:
				return FONT_DATE_MEDIUM;
			case FONT_LARGE:
				return FONT_DATE_LARGE;
		}
		return FONT_DATE_MEDIUM;
	}
	
	public static void putFontSize(final Context context, int size) {
		Prefs.putIntPref(context, PKey.FONT_SIZE, size);
	}
	
	//----------------- Wallpaper
	//0 = none
	//1 = default
	public static int getWallpaperNumber(final Context context) {
		return Prefs.getIntPref(context, PKey.WALLPAPER, WallpaperUtil.DEFAULT_WALLPAPER_NUMBER);
	}
	
	public static void putWallpaperNumber(final Context context, int wallpaper_number) {
		Prefs.putIntPref(context, PKey.WALLPAPER, wallpaper_number);
	}
//____________________________________________________________________________________________ NOTIFICATION SETTINGS
	//----------------- Conversation Sound
	public static Boolean getIsAllowConversationSound(final Context context) {
		return Prefs.getBooleanPref(context, PKey.IS_CONVERSATION_SOUND, PDefaultValue.DEFAULT_IS_CONVERSATION_SOUND);
	}

	public static void putIsAllowConversationSound(final Context context, Boolean is_con_sound) {
		Prefs.putBooleanPref(context, PKey.IS_CONVERSATION_SOUND, is_con_sound);
	}
	
	//----------------- Sounds
	public static String getSound(final Context context) {
		return Prefs.getPref(context, PKey.SOUND, PDefaultValue.DEFAULT_SOUND);
	}
	
	public static void putSound(final Context context, String sound) {
		Prefs.putPref(context, PKey.SOUND, sound);
	}
	
	//----------------- Vibrate
	//Off, Short, Long, Default
	public static String getVibrate(final Context context) {
		return Prefs.getPref(context, PKey.VIBRATE, PDefaultValue.DEFAULT_VIBRATE);
	}
	
	public static void putVibrate(final Context context, String vibrate) {
		Prefs.putPref(context, PKey.VIBRATE, vibrate);
	}
	
	//----------------- Light
	public static int getLight(final Context context) {
		return Prefs.getIntPref(context, PKey.LIGHT, PDefaultValue.DEFAULT_LIGHT);
	}
	
	public static void putLight(final Context context, int light) {
		Prefs.putIntPref(context, PKey.LIGHT, light);
	}
}
