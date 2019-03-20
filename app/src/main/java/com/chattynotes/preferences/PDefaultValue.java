package com.chattynotes.preferences;

import android.app.Notification;
import android.media.RingtoneManager;

public final class PDefaultValue {

    //used to delete pref file
    //after versionCode 2000 and so on
    public static final String PREFERENCE_FILENAME_DRAFT        = "p_dm";

    public static final Boolean DEFAULT_IS_CONVERSATION_SOUND   = true;
    public static final String DEFAULT_SOUND                    = (RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).toString();
    static final String DEFAULT_VIBRATE                         = "Default";
    static final int DEFAULT_LIGHT                              = Notification.DEFAULT_LIGHTS;
    static final Boolean IS_PERMISSION_FIRST_TIME               = true;
    static final Boolean IS_DISPLAY_NEW_VERSION_BANNER          = false;
}
