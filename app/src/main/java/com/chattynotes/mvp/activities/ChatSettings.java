package com.chattynotes.mvp.activities;

import com.chattynotes.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.adapters.listChatSetings.AdapterChatSettings;
import com.chattynotes.adapters.listChatSetings.ChatSetNormalItem;
import com.chattynotes.adapters.listChatSetings.ChatSetTextItem;
import com.chattynotes.adapters.listChatSetings.InterfaceChatSettings;
import com.chattynotes.util.media.MediaTextUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.StorageUtil;
import com.chattynotes.util.WallpaperUtil;
import java.util.ArrayList;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;
import io.realm.Realm;
import petrov.kristiyan.colorpicker.ColorPicker;
import android.support.annotation.NonNull;

public class ChatSettings extends AppCompatActivity implements OnItemClickListener {
	
	final int REQ_WALLPAPER = 200;
	final int REQ_EMAIL = 300;
	
	ArrayList<InterfaceChatSettings> chatSettingList = new ArrayList<>();
	ListView listview = null;
	AdapterChatSettings adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_settings);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat_settings);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		chatSettingList.add(new ChatSetNormalItem("Font size: " + getFontNameFromPrefs(Prefs.getFontSize(this)) , "Font size for conversation screen"));//1
		chatSettingList.add(new ChatSetTextItem("Wallpaper"));//2
		chatSettingList.add(new ChatSetTextItem("Backup chat"));//3
		chatSettingList.add(new ChatSetTextItem("Email chat"));//4
		chatSettingList.add(new ChatSetTextItem("Delete all chats"));//5
		chatSettingList.add(new ChatSetTextItem("Counter"));//6

		listview = (ListView) findViewById(R.id.list_chat_settings);
    	adapter = new AdapterChatSettings(this, chatSettingList);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(this);
	}
	
//_________________________________________________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}

	int POSITION_FONT_SIZE = 0;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		switch(position) {
			case 0://font size
				showFontSize(POSITION_FONT_SIZE);
				break;
			case 1://wall-paper
				openWallpaperMenu();
				break;
			case 2://backup
				startActivity(new Intent(this, BackupSettings.class));
				break;
			case 3://email
				boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_email_request), getString(R.string.permission_storage_email), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_STORAGE_TEXT);
				if(checkPermission)
					startActivityForResult(new Intent(this, EmailChat.class), REQ_EMAIL);
				break;
			case 4://delete
				deleteAllChats();
				break;
			case 5://counter
				Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
				QueryNotesDB realmQuery = new QueryNotesDB(realm);
				int chatCount = realmQuery.getChatCount();
				int notesCount = realmQuery.getNotesCount();
				realm.close();
				new AlertDialog.Builder(this)
					.setTitle("Counter")
					.setMessage("Total chats: " + chatCount + "\nTotal notes: " + notesCount)
					.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int which) {
						}
					}).show();
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent in) {
		  if(resultCode == RESULT_OK) {
			  if(requestCode == REQ_WALLPAPER) {
				  if(in.hasExtra(IntentKeys.WALLPAPER_NUMBER)) {
					  Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
					  int wallpaperNumber = in.getIntExtra(IntentKeys.WALLPAPER_NUMBER, WallpaperUtil.DEFAULT_WALLPAPER_NUMBER);
					  Prefs.putWallpaperNumber(this, wallpaperNumber);
				  } else
					  dialogWallpaperColorChooser();
			  } else if(requestCode == REQ_EMAIL) {
			    	emailConversation(
			    			in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID),
			    			in.getStringExtra(IntentKeys.CHAT_NAME)
			    	);
			  }
		  }
	}

	void dialogWallpaperColorChooser() {
		//https://github.com/kristiyanP/colorpicker
		final ColorPicker colorPicker = new ColorPicker(this);
		colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
			@Override
			public void setOnFastChooseColorListener(int position, int color) {
				Toast.makeText(ChatSettings.this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
				Prefs.putWallpaperNumber(ChatSettings.this, position);
			}
			@Override
			public void onCancel(){
			}
		})
		.setDefaultColorButton(1)
		.setColumns(5)
		.setRoundColorButton(true)
		.setTitle("Choose color")
		.show();
	}
	
//_________________________________________________________________________________________________________________________________
	void openWallpaperMenu() {
		final ArrayList<Intent> intentList = new ArrayList<>();
		//picker
		//Intent p = new Intent(WallpaperUtil.WALLPAPER_PICKER_ACTION);
		//p.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_PICKER_PACKAGE_NAME));
		//intentList.add(p);
		//default
		Intent d = new Intent(WallpaperUtil.WALLPAPER_DEFAULT_ACTION);
		d.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_DEFAULT_PACKAGE_NAME));
		intentList.add(d);
		//none
		Intent n = new Intent(WallpaperUtil.WALLPAPER_NONE_ACTION);
		n.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_NONE_PACKAGE_NAME));
		intentList.add(n);
		//color
		Intent c = new Intent(WallpaperUtil.WALLPAPER_COLOR_ACTION);
		c.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_COLOR_PACKAGE_NAME));
		intentList.add(c);
		//....
		if(intentList.isEmpty())
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
		else {
			final Intent chooserIntent = Intent.createChooser(intentList.remove(intentList.size()-1), "Wallpaper");//this removes 'Always' & 'Only once' buttons
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
			startActivityForResult(chooserIntent, REQ_WALLPAPER);
		}
	}
	
//_________________________________________________________________________________________________________________________________
	private void emailConversation(long chatID, String chatName) {
		if(PermissionUtil.checkPermissionSilent(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
			QueryNotesDB realmQuery = new QueryNotesDB(realm);
			String content = realmQuery.getEmailContent(chatID);
			realm.close();
			if(!MediaTextUtil.writeTextFile(this, chatName, content)) //written failed
				Toast.makeText(this, "Error mailing conversation", Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
	}

//_________________________________________________________________________________________________________________________________
//	void backupConversation() {
//		ProgressDialog dialog = null;
//
//		try {
//			dialog = ProgressDialog.show(
//				ChatSettings.this,
//				"Backing up notes",
//                "Creating a backup on your internal shared storage.",
//                false,
//                true
//                );
//
//			File external =  new File(PathUtil.FOLDER_PATH_DATABASES);//external folder
//	        File internal = Environment.getDataDirectory();//internal folder
//
//	        if ((external.mkdirs() || external.exists()) && external.canWrite())
//	        {
//	        	//String phoneNumber = MainActivity.sharedpreferences.getString("phoneNumber","default");//923032549852
//	    		String intDBPath = "//data//" + App.PACKAGE_NAME + "//databases//Notes.db";
//	            //String extDBPath = phoneNumber + ".Notes.db";
//	    		String extDBPath = "Notes.db";
//	    		File internalDB = new File(internal, intDBPath);
//	            File externalDB = new File(external, extDBPath);
//
//	            if (internalDB.exists())
//	            {
//	                FileInputStream is = new FileInputStream(internalDB);
//					FileChannel src = is.getChannel();
//					FileOutputStream os = new FileOutputStream(externalDB);
//					FileChannel dst = os.getChannel();
//					dst.transferFrom(src, 0, src.size());
//	                src.close();	is.close();
//	                dst.close();	os.close();
//
//	                dialog.dismiss();//dismiss progress dialog
//
//	                new AlertDialog.Builder(ChatSettings.this)
//		  	        .setTitle("Success!")
//		  			.setMessage("Backup successfully created.")
//		  			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//		  				@Override
//		  				public void onClick(DialogInterface dialog, int which) {
//		  				}
//		  			}).show();
//	            }
//	        }
//	    } catch (Exception e) {
//	    	LogUtil.Exception(getClass().getSimpleName(), "Exception", "backupConversation", e.toString());
//
//			if (dialog != null)
//				dialog.dismiss();//dismiss progress dialog
//
//			new AlertDialog.Builder(ChatSettings.this)
//	          .setTitle("Fail!")
//			  .setMessage("Fail to create backup.")
//			  .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//				}
//			}).show();
//	    }
//	}
	
//_________________________________________________________________________________________________________________________________
	private void deleteAllChats() {
		new AlertDialog.Builder(this)
		.setMessage("Are you sure you want to clear ALL notes?")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	ProgressDialog _dialog = ProgressDialog.show(ChatSettings.this,null, "Deleting notes");
				Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
				QueryNotesDB realmQuery = new QueryNotesDB(realm);
				realmQuery.deleteAllChats();
				realm.close();
		    	//delete complete internal media thumbs with chat thumbs
		    	StorageUtil.deleteAllMediaThumbIntStg();
		        //remove chat list item
		    	lbmDeleteAllChat();
		    	Toast.makeText(ChatSettings.this, "All Chat Deleted", Toast.LENGTH_SHORT).show();
		        _dialog.dismiss();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
	}
	//_____________________________________________________________________________ NOTIFYING_ACTIVITY_FROM_ACTIVITY
	void lbmDeleteAllChat() {
		Intent in = new Intent(IntentKeys.LBM_DELETE_ALL_CHAT);
		LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	}
	
//_________________________________________________________________________________________________________________________________
	void showFontSize(final int list_position) {
		
		int selection;
		selection = getFontPositionFromPrefs(Prefs.getFontSize(this));
		
		final CharSequence[] items={"Small","Medium","Large"};
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setTitle("Font Size");
    	dialog.setNegativeButton("Cancel", null);
    	dialog.setSingleChoiceItems(items, selection, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {

				switch(position) {
					case 0: //Small
						finishFontSelection(dialog, list_position, items[0], Prefs.FONT_SMALL);
						break;
					case 1: //Medium
						finishFontSelection(dialog, list_position, items[1], Prefs.FONT_MEDIUM);
						break;
					case 2: //Large
						finishFontSelection(dialog, list_position, items[2], Prefs.FONT_LARGE);
						break;
				}
			}
		});
    	dialog.show();
	}
	
	void finishFontSelection(DialogInterface dialog, int position, CharSequence options, int size) {
		Prefs.putFontSize(this, size);
		chatSettingList.get(position).setHeading("Font size: " + options);
		dialog.dismiss();
		adapter.notifyDataSetChanged();
	}
	
	int getFontPositionFromPrefs(int value) {
		switch(value) {
			case Prefs.FONT_SMALL:
				return  0;
			case Prefs.FONT_MEDIUM:
				return  1;
			case Prefs.FONT_LARGE:
				return  2;
			default:
				return 0;
		}
	}

	String getFontNameFromPrefs(int value) {
		switch(value) {
			case Prefs.FONT_SMALL:
				return "Small";
			case Prefs.FONT_MEDIUM:
				return "Medium";
			case Prefs.FONT_LARGE:
				return "Large";
			default:
				return "Small";
		}
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case PermissionUtil.PERMISSION_STORAGE_TEXT:
					startActivityForResult(new Intent(this, EmailChat.class), REQ_EMAIL);
					break;
				default:
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		} else {
			Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
		}
	}
}
