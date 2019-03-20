package com.chattynotes.mvp.activities;

import com.chattynotes.constant.IntentKeys;
import com.chattynotes.util.WallpaperUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WallpaperPicker extends AppCompatActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getAction().equals(WallpaperUtil.WALLPAPER_DEFAULT_ACTION)) {
			  Intent in = new Intent();
			  in.putExtra(IntentKeys.WALLPAPER_NUMBER, WallpaperUtil.DEFAULT_WALLPAPER_NUMBER);
			  setResult(RESULT_OK, in);
			  finish();
		} else if (getIntent().getAction().equals(WallpaperUtil.WALLPAPER_NONE_ACTION)) {
			  Intent in = new Intent();
			  in.putExtra(IntentKeys.WALLPAPER_NUMBER, WallpaperUtil.NONE_WALLPAPER_NUMBER);
			  setResult(RESULT_OK, in);
			  finish();
		} else if (getIntent().getAction().equals(WallpaperUtil.WALLPAPER_COLOR_ACTION)) {
			//do not put extra
			setResult(RESULT_OK, new Intent());
			finish();
		} else {
			//do not put extra
			setResult(RESULT_OK, new Intent());
			finish();
		}
	}
}
