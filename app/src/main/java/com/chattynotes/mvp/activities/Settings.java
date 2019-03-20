package com.chattynotes.mvp.activities;

import com.chattynotes.R;
import com.chattynotes.adapters.listSettings.AdapterSettings;
import com.chattynotes.adapters.listSettings.SettingsItem;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;

public class Settings extends AppCompatActivity implements OnItemClickListener {
	
	ArrayList<SettingsItem> settingList = new ArrayList<>();
	ListView listview = null;
	AdapterSettings adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		settingList.add(new SettingsItem("About", R.drawable.ic_info));
		settingList.add(new SettingsItem("Chat settings", R.drawable.ic_note));
		settingList.add(new SettingsItem("Notifications", R.drawable.ic_notifications));
		settingList.add(new SettingsItem("Help", R.drawable.ic_help));
		listview = (ListView) findViewById(R.id.list_settings);
    	adapter = new AdapterSettings(this, settingList);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent i;
		switch(position) {
			case 0:
				i = new Intent(Settings.this, About.class);
				startActivity(i);
				break;
			case 1:
				i = new Intent(Settings.this, ChatSettings.class);
				startActivity(i);
				break;
			case 2:
				i = new Intent(Settings.this, Notifications.class);
				startActivity(i);
				break;
			case 3:
				i = new Intent(Settings.this, Help.class);
				startActivity(i);
				break;

		}
	}
}