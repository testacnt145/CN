package com.chattynotes.mvp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chattynoteslite.BuildConfig;
import com.chattynoteslite.R;
import com.chattynotes.constant.AppVersionCode;

public class About extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		if(BuildConfig.DEBUG) {
			TextView t = ((TextView)findViewById(R.id.about_build_type));
			t.setVisibility(View.VISIBLE);
			//http://stackoverflow.com/a/23432225/4754141
			String text = String.format(getString(R.string.about_build_type), BuildConfig.BUILD_TYPE, AppVersionCode.getApkVersionCode(this));
			t.setText(text);
		}
	}
}
