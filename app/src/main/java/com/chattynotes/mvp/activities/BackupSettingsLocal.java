package com.chattynotes.mvp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.chattynoteslite.R;
import com.chattynotes.constant.AppVersionCode;
import com.chattynotes.constant.BackupType;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.DateUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.json.JsonUtil;
import com.chattynotes.util.storage.json.model.BackupModel;
import com.chattynotes.util.storage.json.model.ChatDetail;
import com.chattynotes.util.storage.json.model.LastBackupNote;
import java.io.File;
import java.util.ArrayList;
import android.support.annotation.NonNull;

import io.realm.Realm;

public class BackupSettingsLocal extends AppCompatActivity {

	boolean checkPermission = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup_settings_local);

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
		new AlertDialog.Builder(this)
				.setTitle("Confirm restore?")
				.setMessage("All your current notes will be replaced, are you sure?")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(DBUtil.restoreDB()) {
							lbmRestoreChat();
							Toast.makeText(BackupSettingsLocal.this, getString(R.string.backup_restore_success), Toast.LENGTH_SHORT).show();
							activitySwitchNext();

						} else
							Toast.makeText(BackupSettingsLocal.this, getString(R.string.backup_restore_fail), Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
	}

	public void onClickPermissionBackup(View view) {
		checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_backup_request), getString(R.string.permission_storage_backup), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
	}

	public void onClickBackup(View view) {
		if(PermissionUtil.checkPermissionSilent(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
			QueryNotesDB realmQuery = new QueryNotesDB(realm);
			boolean isDatabaseEmpty = realmQuery.getChatCount()==0;
			realm.close();
			if(!isDatabaseEmpty) {
				File f = PathUtil.getExternalDatabaseInfoFile();
				if (f.exists()) {
					new AlertDialog.Builder(this)
							.setTitle("Backup on local storage")
							.setMessage("Your previous backup will be replaced, are you sure?")
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									backupLocal();
									Toast.makeText(BackupSettingsLocal.this, getString(R.string.backup_new_success),Toast.LENGTH_SHORT).show();
									finish();
								}
							})
							.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							})
							.show();
				} else {
					backupLocal();
					Toast.makeText(BackupSettingsLocal.this, getString(R.string.backup_new_success),Toast.LENGTH_SHORT).show();
					finish();
				}
			} else
				Toast.makeText(this, "No notes found", Toast.LENGTH_SHORT).show();

		} else
			Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();

	}

	private void backupLocal() {
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		ArrayList<ChatDetail> chatDetails = realmQuery.getChatDetail();
		LastBackupNote lastBackupNote = realmQuery.getLastBackupNote();
		BackupModel backupModel = new BackupModel();
		backupModel.setVersionCode(AppVersionCode.getApkVersionCode(this));
		backupModel.setType(BackupType.LOCAL);
		backupModel.setChatCount(realmQuery.getChatCount());
		backupModel.setMsgCount(realmQuery.getNotesCount());
		backupModel.setBackupTimestamp(DateUtil.getDateInMilliseconds());
		backupModel.setChatDetails(chatDetails);
		backupModel.setLastBackupNote(lastBackupNote);
		String json = JsonUtil.convertJavaToJson(backupModel);
		realm.close();
		//must close the DB before backup
		DBUtil.backupDB();
		DBUtil.backupDBInfo(json);
	}

	void activitySwitchNext() {
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

	void lbmRestoreChat() {
		Intent in = new Intent(IntentKeys.LBM_RESTORE_CHAT);
		LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	}

}
