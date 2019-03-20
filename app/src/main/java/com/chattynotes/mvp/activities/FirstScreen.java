package com.chattynotes.mvp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chattynotes.R;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;

public class FirstScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_screen);

		/*
		 * check whether application has come to this activity and its not the first time
		 * @cond1	if		, app come to this activity for the very first time, RUN THIS ACTIVITY
		 * @cond2	else if , its not first time, so app come from delete account may be, so check
		 * @cond3		//if	, [os_marshmallow_: run this activity]
		 * @cond4		//else	, Move to Next activity
		 */
//		if(Prefs.getIsPermissionFirstTime(this)) { //@cond1
//			//RUN THIS ACTIVITY
//		} else { //@cond2
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //@cond3
//				//RUN THIS ACTIVITY
//			} else //@cond4
//				activitySwitchNext();
//		}

		//Prefs.getIsPermissionFirstTime(this) remains true for [os_marshmallow_: ] therefore, confusing code,
		//therefore, always run this activity
	}


//__________________________________________________________________________________________________
	public void onClickAgreeAndContinue(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (Prefs.getIsPermissionFirstTime(this)) {
				PermissionUtil.alertPermissionFirstTime(this); //ask for all the permission
				Prefs.putIsPermissionFirstTime(this, false);
			} else
				activitySwitchNext();
		} else
			activitySwitchNext();
	}

	void activitySwitchNext() {
		PathUtil.createAppFolder();
		Prefs.putStage(this, MainActivity.STAGE_BACKUP);
		Intent intent = new Intent(this, Backup.class);
		startActivity(intent);
		finish();
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		//we are 100% sure that user has selected either Allow or Deny from permission dialog
		//therefor put false
		activitySwitchNext();
	}
}
