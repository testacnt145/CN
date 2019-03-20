package com.chattynotes.mvp.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.chattynotes.R;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.json.JsonUtil;
import com.chattynotes.util.storage.json.model.BackupModel;

public class BackupInfo extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup_info);
		if(PermissionUtil.checkPermissionSilent(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			BackupModel backupModel = JsonUtil.convertJsonToJava();
			if (backupModel != null) {
				TextView tv = (TextView) findViewById(R.id.backup_found_text_view);
				tv.setText(BackupModel.getBackupModelDetail(backupModel));
			} else
				finish();
		} else {
			Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
