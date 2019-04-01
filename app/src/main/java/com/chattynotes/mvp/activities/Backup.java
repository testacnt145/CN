package com.chattynotes.mvp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.chattynoteslite.R;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.json.JsonUtil;
import com.chattynotes.util.storage.json.model.BackupModel;
import java.io.File;
import android.support.annotation.NonNull;

public class Backup extends AppCompatActivity {

	boolean checkPermission = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);

		//if permission go on, else finish the activity not here but at method onRequestPermissionsResult
		checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_backup_request), getString(R.string.permission_storage_backup), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
		//if (checkPermission)
			onCreate___(); //helpful when [ 1)permission already granted] [ 2) if user click cancel on dialog]
	}

	//__________________________________________________________________________________________________
	private void onCreate___() {
		if(checkPermission) {
			findViewById(R.id.backup_permission).setVisibility(View.GONE);
			File f = PathUtil.getExternalDatabaseInfoFile();
			if (f.exists())
				displayOldBackupDetail();
			else
				displayNoBackupFound();
		} else {
			findViewById(R.id.backup_permission).setVisibility(View.VISIBLE);
		}
	}

	private void displayNoBackupFound() {
		findViewById(R.id.backup_found).setVisibility(View.GONE);
		findViewById(R.id.backup_not_found).setVisibility(View.VISIBLE);
	}

	private void displayOldBackupDetail() {
		BackupModel backupModel = JsonUtil.convertJsonToJava();
		if(backupModel!=null) {
			findViewById(R.id.backup_not_found).setVisibility(View.GONE);
			findViewById(R.id.backup_found).setVisibility(View.VISIBLE);
			TextView tv = (TextView) findViewById(R.id.backup_found_text_view);
			tv.setText(BackupModel.getBackupModel(backupModel));
		} else
			displayNoBackupFound();
	}

	//__________________________________________________________________________________________________
	public void onClickViewBackupInfo(View view) {
		startActivity(new Intent(this, BackupInfo.class));
	}

	public void onClickRestoreBackup(View view) {
		if(DBUtil.restoreDB()) {
            Toast.makeText(this, getString(R.string.backup_restore_success), Toast.LENGTH_SHORT).show();
            activitySwitchNext();
        } else
            Toast.makeText(this, getString(R.string.backup_restore_fail), Toast.LENGTH_SHORT).show();

	}

	public void onClickPermissionBackup(View view) {
		checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_backup_request), getString(R.string.permission_storage_backup), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
	}

	public void onClickContinue(View view) {
		File f = PathUtil.getExternalDatabaseInfoFile();
		if (f.exists()) {
			new AlertDialog.Builder(this)
				.setTitle("Backup not restore")
				.setMessage("Backup not restored, are you sure you want to leave backup screen?")
				.setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						activitySwitchNext();
					}
				})
				.setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
					}
				})
				.show();
		} else
			activitySwitchNext();
	}

	void activitySwitchNext() {
		Prefs.putStage(this, MainActivity.STAGE_CHAT);
		Intent intent = new Intent(this, Chat.class);
		startActivity(intent);
		finish();
	}

	//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case PermissionUtil.PERMISSION_GALLERY_IMAGE:
					checkPermission = true;
					break;
				default:
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		} else {
			checkPermission = false;
		}
		onCreate___();
	}

}
