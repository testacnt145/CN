package com.chattynotes.services;

import com.chattynotes.application.App;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.ffmpeg.FfmpegUtil;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import io.realm.Realm;

public class ServiceVideo extends Service {
	
//``````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````
	//http://stackoverflow.com/questions/13227697/how-to-restart-service-in-android-to-call-service-oncreate-again
	//used to start the service immediately if user stop the service application settings
	//it will not be helpful, if user force stop the application, as android doesn't provide any support to force stop applications
	public static boolean IS_RESTART_SERVICE_REQUIRED = false; 
	public static ServiceVideo runningInstanceOfVideoService;
//``````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleStart(intent);
		IS_RESTART_SERVICE_REQUIRED = true;
		return Service.START_STICKY;
	}


	void handleStart(Intent intent) {
		if(runningInstanceOfVideoService == null) {
			startForeground(AppConst.UNIQUE_NOTIFICATION_FOREGROUND_SERVICE_ID, updateNotification());
			runningInstanceOfVideoService = this;
			if(intent != null && intent.getExtras() != null) {
				if(!intent.getExtras().getBoolean(IntentKeys.MEDIA_VIDEO_IS_UPLOAD_ONLY)) {
					String msgID			= intent.getExtras().getString (IntentKeys.MSG_ID);
					String video_name 		= intent.getExtras().getString	(IntentKeys.MEDIA_NAME);
					String video_path		= intent.getExtras().getString	(IntentKeys.MEDIA_VIDEO_PATH);
					String startTime		= intent.getExtras().getString	(IntentKeys.MEDIA_VIDEO_START_TIME);
					long estimatedDuration	= intent.getExtras().getLong	(IntentKeys.MEDIA_VIDEO_DURATION);
					trimVideo(msgID, video_name, video_path, startTime, estimatedDuration);
				}
			}
		}
	}

	private Notification updateNotification() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
	    builder.setOngoing(true);
	    builder.setContentTitle("Chatty Notes Lite - Video");
		builder.setSmallIcon(android.R.drawable.stat_sys_upload);
	    Notification n = builder.build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(AppConst.UNIQUE_NOTIFICATION_FOREGROUND_SERVICE_ID, n);
        return n;
	}

//________________________________________________________________________________________________________________
	public void trimVideo(final String msgID, final String videoName, final String inputPath, final String startTime, final long estimatedDuration) {
		String outputPath = (PathUtil.createExternalMediaVideoFile_Sent(videoName)).getAbsolutePath();
		FFmpeg ffmpeg = FfmpegUtil.loadFfmpegBinary(ServiceVideo.this);
		String[] command = FfmpegUtil.trim(inputPath, outputPath, startTime, estimatedDuration);
		try {
			ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
				@Override
				public void onSuccess(String s) {
					//LogUtil.e("ServiceVideo", "Success: " + s);
					trimStatus(msgID, MediaStatus.COMPLETED);
				}
				@Override
				public void onFailure(String s) {
					//LogUtil.e("ServiceVideo", "Failed editing video: " + s);
					trimStatus(msgID, MediaStatus.COMPRESSING_MEDIA_FAILED);
					stopService();
					Toast.makeText(ServiceVideo.this, "Failed editing video", Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onProgress(String s) {
				}
				@Override
				public void onStart() {
				}
				@Override
				public void onFinish() {
				}
			});
		} catch (FFmpegCommandAlreadyRunningException ignored) {
			//LogUtil.e("ServiceVideo", "FFmpegCommandAlreadyRunningException: " + ignored);
		}
	}
	
	void trimStatus(final String msgID, int mediaStatus) {
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		realmQuery.updateMediaStatus(msgID, mediaStatus);
		Notes note = realmQuery.getSingleNote(msgID);
		if(note != null)
			lbmMediaStatus(note.getChatId(), msgID, mediaStatus);
		realm.close();
		stopService();

	}
	
//________________________________________________________________________________________________________________
	void lbmMediaStatus(long chatID, String msgID, int mediaStatus) {
		if(AppUtil.isUserActiveNotification(chatID)) {
			Intent in = new Intent(IntentKeys.LBM_MEDIA_STATUS);
			in.putExtra(IntentKeys.MSG_ID, msgID);
			in.putExtra(IntentKeys.MEDIA_STATUS, mediaStatus);
			LocalBroadcastManager.getInstance(this).sendBroadcast(in);
		}
	}
	
//________________________________________________________________________________________________________________	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (IS_RESTART_SERVICE_REQUIRED)
	       startService(new Intent(this, ServiceVideo.class));
	}
	
	void stopService() {
		IS_RESTART_SERVICE_REQUIRED = false;
		runningInstanceOfVideoService = null;
		stopService(new Intent(this, ServiceVideo.class));
		NotificationManager nMgr = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
	    nMgr.cancel(AppConst.UNIQUE_NOTIFICATION_FOREGROUND_SERVICE_ID);
	}
}
