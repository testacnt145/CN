package com.chattynotes.util.media;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.chattynotes.application.App;
import com.chattynoteslite.R;
import com.chattynotes.preferences.Prefs;

public class MediaAudioUtil {

    public static void playSound(int soundId) {

        // [os_marshmallow_: just after API Level 23]
        //http://stackoverflow.com/questions/33642484/android-notificationmanager-giving-me-no-valid-small-icon-error
        //problem with this is, it is showing 2 notifications icon, (1 real, 1 empty)
        //builder.setSmallIcon(R.drawable.notifybar);
        //therefore can't use that

		/*
		Uri soundUri = Uri.parse("android.resource://" + App.PACKAGE_NAME + "/res/" + soundId);
		NotificationCompat.Builder builder_sound = new NotificationCompat.Builder(App.applicationContext);
		builder_sound.setSound(soundUri);
		builder_sound.setVibrate(new long[] {0});
		Notification notification_sound = builder_sound.build();

		//play sound once
		//notification_sound.flags = Notification.FLAG_ONLY_ALERT_ONCE;

		NotificationManager notificationManager = (NotificationManager)App.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(Utilities.UNIQUE_SOUND_ID, notification_sound);
		*/

        try {
            Uri uri = Uri.parse("android.resource://" + App.PACKAGE_NAME + "/res/" + soundId);
            Ringtone r = RingtoneManager.getRingtone(App.applicationContext, uri);
            r.play();
        } catch (Exception ignored) {
        }
    }

    private static final int NOTIFICATION_ID = 1;
    public static void playNoteDoneSound(Context ctx, String sound, int light, String vibration, String chatName, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setSmallIcon(R.mipmap.notify);
        builder.setContentTitle(chatName);
        builder.setContentText(text);
        if (Prefs.getIsAllowConversationSound(ctx))
            builder.setSound(Uri.parse(sound));
        builder.setLights(light, 300, 500);
            switch (vibration) {
                case "Off":
                    builder.setVibrate(new long[]{0});
                    break;
                case "Default":
                    builder.setDefaults(Notification.DEFAULT_VIBRATE);
                    break;
                case "Short":
                    builder.setVibrate(new long[]{0, 300, 200, 30});
                    break;
                case "Long":
                    builder.setVibrate(new long[]{0, 1000, 500, 1000});
                    break;
            }
        Notification n = builder.build();
        NotificationManager mgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(NOTIFICATION_ID, n);
    }
}
