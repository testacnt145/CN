package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.constant.AppVersionCode;
import com.chattynotes.constant.ChatID;
import com.chattynotes.constant.ChatName;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.Msg;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MsgFlow;
import com.chattynotes.constant.MsgID;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.preferences.PrefsDraft;
import com.chattynotes.util.AndServiceUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.WallpaperUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import io.realm.Realm;

public class MainActivity extends Activity {
	//Coming From Activity (2)
		// 1 - Normal Start
		// 2 - Fragment Recent/Contact/Group (Sharing text/Image from other applications (twitter,fb))

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreate();
		//startActivity(new Intent(this, AppVersionIncorrect.class)); //testing-purpose
		//insertNewReleaseNote("release note");
	}

	 void onCreate() {
		 //testing-purpose
		//StorageUtil.copyDatabaseExtStg();
		//StorageUtil.copyAllIntStgPicsToExtStgDatabaseFolder();

		//very first time (either fresh-install or updated)
		//if(-1<20170102)
		//fresh-install
		//if(-1<20170622)
		//updated
		//if(20170102<20170203)
		if(Prefs.getAppVersionCode(this)<AppVersionCode.getApkVersionCode(this)) {
			checkAppVersionCodeOnceInALife();
			checkStageOnceInALife();
		}
		checkStages();
	}

//__________________________________________________________________________________________________
	void checkAppVersionCodeOnceInALife() {
		int installed = Prefs.getAppVersionCode(this);
		if(installed == AppVersionCode.VERSION_CODE_FIRST_TIME) {
		//___________________
			/* very first time only, stuff required only once */
			//QueryDB appDb = new QueryDB(this);
			//appDb.createDatabase();
			//appDb.close();
			/* create application shortcut on home screen */
			//ShortcutUtil.createAppShortcut(this);
			/* Setting Default Preferences */
			Prefs.putWallpaperNumber(this, WallpaperUtil.DEFAULT_WALLPAPER_NUMBER);
			/* put this apk version code in preference */
			Prefs.putAppVersionCode(this, AppVersionCode.getApkVersionCode(this));
		} else if(installed < AppVersionCode.VERSION_CODE_SQL_TO_REALM_MIGRATION_REQUIRED) {
		//___________________ for old version users
			/* anonymous initialization of QueryNotesDB so that onUpgrade method can be called */
			new com.chattynotes.database.sq.QueryNotesDB(this);
			//https://stackoverflow.com/a/15699476
			PrefsDraft.deletePrefFile(this);
			//after migration, take user to 'Backup Activity', if he was on 'Chat Activity' previously
            if(Prefs.getStage(this) == STAGE_CHAT)
			    Prefs.putStage(this, STAGE_BACKUP);
		}

		//mandatory
		doWorkForLatestVersion();
	}

	void doWorkForLatestVersion() {
		addLatestReleaseNotes();
		//put this apk version code in Shared Preference
		Prefs.putAppVersionCode(this, AppVersionCode.getApkVersionCode(this));
		Prefs.putIsDisplayNewVersionBanner(this, false);
	}

	void addLatestReleaseNotes() {
		//this method will only add the release-notes of current version
		//1) if app is updated, old release-notes will already be in database,
		//2) fresh-install, only latest will be shown
		//[release_: [todo_: always comment out old]
		//v1.0.0.0
		//insertNewReleaseNote(getString(R.string.v1_0_0_0));
		//v1.1.0.0
		//insertNewReleaseNote(getString(R.string.v1_1_0_0));
		//v2.0.0.0
		//insertNewReleaseNote(getString(R.string.v2_0_0_0));
		//v2.3.0.1
		//insertNewReleaseNote(getString(R.string.v2_3_0_1));
		//v3.0.0.0
		//insertNewReleaseNote(getString(R.string.v3_0_0_0));
		//v3.0.0.1
		insertNewReleaseNote(getString(R.string.v3_0_0_1));
	}

	void insertNewReleaseNote(String msg) {
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		long msgTimestamp = MsgConstant.DEFAULT_MSG_TIMESTAMP(); //we want exact milliseconds

		//Insert or Replace [MsgID.DEFAULT chat identifier (created chat)] message
		Notes note = new Notes();
		Notes defaultNote = realmQuery.getSingleNoteChatCreated(ChatID.CHATTY_NOTES);
		if(defaultNote==null)
			note.setNoteId(realmQuery.getIncrementedNoteId());
		else
			note.setNoteId(defaultNote.getNoteId());
		note.setChatId(ChatID.CHATTY_NOTES);
		note.setMsg(Msg._CHAT_CREATED);
		note.setMsgId(MsgID.DEFAULT);
		note.setMsgTimestamp(msgTimestamp);
		note.setMediaStatus(MediaStatus.COMPLETED);
		Chats chat = new Chats();
		chat.setChatId(ChatID.CHATTY_NOTES);
		chat.setChatName(ChatName.CHATTY_NOTES);
		chat.setTimestamp(msgTimestamp);
		chat.setNote(note);
		realmQuery.insertOrUpdateChat(chat);

		Notes noteRelease = new Notes();
		noteRelease.setNoteId(realmQuery.getIncrementedNoteId());
		noteRelease.setChatId(ChatID.CHATTY_NOTES);
		noteRelease.setMsg(msg);
		noteRelease.setMsgFlow(MsgFlow.RECEIVE);
		noteRelease.setMsgTimestamp(msgTimestamp);
		noteRelease.setMediaStatus(MediaStatus.COMPLETED);
		realmQuery.insertNote(noteRelease);

		realm.close();


		//[sqlite_: old]
//		QueryNotesDB Db = new QueryNotesDB(this);
//		Db.open();
//		String msgID = AppUtil.generateMessageID();
//		long msgTimestamp = MsgConstant.DEFAULT_MSG_TIMESTAMP(); //we want exact milliseconds
//		Db.insertOrReplaceNewChat(msgTimestamp);
////---------------------------->>> DATABASE
//		ConversationItem conItem = new ConversationItem(
//				ChatID.CHATTY_NOTES,
//				msg,
//				msgID,
//				MsgFlow.RECEIVE,
//				MsgConstant.DEFAULT_MSG_STATUS,
//				MsgConstant.DEFAULT_MSG_KIND,
//				msgTimestamp,
//				MsgConstant.DEFAULT_MSG_TIMESTAMP_DONE,
//				MsgConstant.DEFAULT_MSG_STAR,
//				MediaStatus.COMPLETED,
//				MsgConstant.DEFAULT_MEDIA_CAPTION,
//				MsgConstant.DEFAULT_MEDIA_SIZE,
//				MsgConstant.DEFAULT_MEDIA_DURATION,
//				MsgConstant.DEFAULT_LINK_OBJECT
//		);
//		Db.insertNewNote(conItem, MsgConstant.DEFAULT_LINK_MESSAGE);
//		Db.close();
	}

	void checkStageOnceInALife() {
		switch(Prefs.getStage(this)) {
			case STAGE_APP_VERSION_OBSOLETE:
				//after user update, take him to 'Chat Activity'
				Prefs.putStage(this, STAGE_CHAT);
				break;
			case STAGE_APP_VERSION_DELETE:
				//after user update, take him to 'FirstScreen Activity'
				Prefs.putStage(this, STAGE_FIRST_SCREEN);
				break;
		}

	}

//__________________________________________________________________________________________________
	public static final int STAGE_FIRST_SCREEN			= -1;
	public static final int STAGE_BACKUP 				= 0;
	public static final int STAGE_CHAT 					= 1;
	public static final int STAGE_APP_VERSION_OBSOLETE 	= 1000;
	public static final int STAGE_APP_VERSION_DELETE 	= 1001;
	public void checkStages() {
		//LogUtil.e(getClass().getSimpleName(), "stage-" + Prefs.getStage(this));
		Intent in;
		switch(Prefs.getStage(this)) {
		case STAGE_FIRST_SCREEN:
			in = new Intent(this, FirstScreen.class);
			startActivity(in);
			finish();
			break;

		case STAGE_BACKUP :
			PathUtil.createAppFolder();
			in = new Intent(this, Backup.class);
			startActivity(in);
			finish();
			break;

		case STAGE_CHAT :
			PathUtil.createAppFolder();
			in = new Intent(this, Chat.class);
			//---------------------------->>> SHARED TEXT/IMAGE MESSAGE FROM OTHER APPS
			Bundle sharedBundle = getIntent().getBundleExtra(IntentKeys.BUNDLE_SHARED);
			if(sharedBundle != null)
				in.putExtra(IntentKeys.BUNDLE_SHARED, sharedBundle);
		    startActivity(in);
	        finish();
	        AndServiceUtil.startServiceApp(this);
	        break;

		case STAGE_APP_VERSION_OBSOLETE:
		case STAGE_APP_VERSION_DELETE:
			finish();
			in = new Intent(this, AppVersionIncorrect.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(in);
			break;

		default:
			//someone tries to mis-use, take back to registration process
			AppUtil.deleteAccount(this);
		}
	}
}

