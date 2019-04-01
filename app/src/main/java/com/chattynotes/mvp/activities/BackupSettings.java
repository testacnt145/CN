package com.chattynotes.mvp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.chattynoteslite.R;
import com.chattynotes.mvp.presenter.BackupSettingsPresenter;
import com.chattynotes.mvp.repository.JavaStringRepository;
import com.chattynotes.mvp.repository.data.BackupOptions;
import com.chattynotes.mvp.view.BackupSettingsView;

public class BackupSettings extends AppCompatActivity implements BackupSettingsView, AdapterView.OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup_settings);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_backup_settings);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);
		mvp();
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
	//__________________________________________________________________________________________________
	void mvp() {
		BackupSettingsView backupView = this;
		JavaStringRepository backupRepository = new BackupOptions();
		BackupSettingsPresenter presenter = new BackupSettingsPresenter(backupView, backupRepository);
		presenter.loadBackupOption();
	}

	@Override
	public void displayBackupOptions(String[] backupOptions) {
		ListView listview = (ListView) findViewById(R.id.list_backup_settings);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, backupOptions);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent in;
		switch(position) {
			case 0:
				in = new Intent(this, BackupSettingsLocal.class);
				startActivity(in);
				break;
		}
	}


}