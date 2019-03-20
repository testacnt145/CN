package com.chattynotes.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceApp extends Service {

	public static ServiceApp runningInstanceOfService;

	public ServiceApp() {
		//LogUtil.e(getClass().getSimpleName(), "1 - Constructor (default)");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		//LogUtil.e(getClass().getSimpleName(), "2 - onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//LogUtil.e(getClass().getSimpleName(), "3 - onStartCommand");
		if (runningInstanceOfService == null) { //only do stuff when null, to make sure everything runs once only
			//cancel the sound notifications
			//clearNotificationSoundFromStatusBar();
			runningInstanceOfService = this;
			workOnServiceStart();
		}
		return Service.START_STICKY;
	}

	//this will run every time when service will start
	void workOnServiceStart() {
	}

}