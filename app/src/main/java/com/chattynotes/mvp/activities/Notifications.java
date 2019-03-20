package com.chattynotes.mvp.activities;

import com.chattynotes.R;
import com.chattynotes.preferences.PDefaultValue;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.adapters.listNotications.AdapterNotifications;
import com.chattynotes.adapters.listNotications.InterfaceNotifications;
import com.chattynotes.adapters.listNotications.NotificationCheckBoxItem;
import com.chattynotes.adapters.listNotications.NotificationDoubleLineItem;
import com.chattynotes.adapters.listNotications.NotificationHeadingItem;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;

public class Notifications extends AppCompatActivity implements OnItemClickListener {

	ArrayList<InterfaceNotifications> notificationList = new ArrayList<>();
	ListView listview = null;
	AdapterNotifications adapter;
	final int SOUND_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notifications);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		/* 0 */ notificationList.add(new NotificationCheckBoxItem(Prefs.getIsAllowConversationSound(this)));
		/* 1 */ notificationList.add(new NotificationHeadingItem("Notifications"));
		/* 2 */ notificationList.add(new NotificationDoubleLineItem("Notification Tone", "Select a sound to play for a new notification message."));
		/* 3 */ notificationList.add(new NotificationDoubleLineItem("Vibrate: " + Prefs.getVibrate(this), "Vibrate device when new message arrives while application is running."));
		/* 4 */ notificationList.add(new NotificationDoubleLineItem("Light: " + getLightNameFromPrefs(Prefs.getLight(this)), "Choose a colour to blink the notification light when a new message is received."));

		listview = (ListView) findViewById(R.id.list_notifications);
    	adapter = new AdapterNotifications(this, notificationList);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(this);
	}
	
	public void checkBoxClick(View v) {
		NotificationCheckBoxItem c = (NotificationCheckBoxItem) notificationList.get(0);
		if(c.isChecked){
        	Prefs.putIsAllowConversationSound(this, false);
    		c.isChecked = false;
        } else {
        	Prefs.putIsAllowConversationSound(this, PDefaultValue.DEFAULT_IS_CONVERSATION_SOUND);
    		c.isChecked = true;
        }
    }
	
//_________________________________________________________________________________________________________________________
	@Override
	 protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
	     if (resultCode == Activity.RESULT_OK) {
	    	 if(requestCode == SOUND_REQUEST) {
	    		 Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
	    		 if (uri != null)
	    			 Prefs.putSound(this,  uri.toString());
	    		 else
	    			 Prefs.putSound(this, PDefaultValue.DEFAULT_SOUND);
	    	 }
	     }
	 }
//_________________________________________________________________________________________________________________________
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
		switch(position) {
			case 2://tones
				showAndroidNotificationsSounds();
				break;
			case 3://vibrate
				showNotificationsVibrate(3);
				break;
			case 4://light
				showNotificationsLights(4);
				break;
		}
	}
	
//_________________________________________________________________________________________________________________________
	//http://stackoverflow.com/questions/2724871/how-to-bring-up-list-of-available-notification-sounds-on-android
	void showAndroidNotificationsSounds() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Notification Tone");
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(Prefs.getSound(this)));
		this.startActivityForResult(intent, SOUND_REQUEST);
	}
	
//_________________________________________________________________________________________________________________________
	//user alert dialog with radio button
	void showNotificationsLights(final int list_position) {
		int selection = getLightPositionFromPrefs(Prefs.getLight(Notifications.this));
		final CharSequence[] items={"None","White","Red","Yellow","Green","Cyan","Blue","Purple"};
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setTitle("Light");
    	dialog.setNegativeButton("Cancel", null);
    	dialog.setSingleChoiceItems(items, selection, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				
				switch(position) {
					case 0: //None
						finishLightSelection(dialog, list_position, 0xff000000, items[0]);
						break;
					case 1: //White
						finishLightSelection(dialog, list_position, 0xffffffff, items[1]);
						break;
					case 2: //Red
						finishLightSelection(dialog, list_position, 0xffff0000, items[2]);
						break;
					case 3: //Yellow
						finishLightSelection(dialog, list_position, 0xffffff00, items[3]);
						break;
					case 4: //Green
						finishLightSelection(dialog, list_position, 0xff00ff00, items[4]);
						break;
					case 5: //Cyan
						finishLightSelection(dialog, list_position, 0xff00ffff, items[5]);
						break;
					case 6: //Blue
						finishLightSelection(dialog, list_position, 0xff0000ff, items[6]);
						break;
					case 7: //Purple
						finishLightSelection(dialog, list_position, 0xff551a8b, items[7]);
						break;
				}
			}
		});
    	dialog.show();
	}
	
	void finishLightSelection(DialogInterface dialog, int position, int hex, CharSequence color_name) {
		Prefs.putLight(Notifications.this, hex);
		((NotificationDoubleLineItem)notificationList.get(position)).firstLine = "Light: " + color_name;
		dialog.dismiss();
		adapter.notifyDataSetChanged();
	}
	
	int getLightPositionFromPrefs(int value) {
		switch(value) {
			case 0xff000000://None
				return  0;
			case 0xffffffff://White
				return  1;
			case 0xffff0000://Red
				return  2;
			case 0xffffff00://Yellow
				return  3;
			case 0xff00ff00://Green
				return  4;
			case 0xff00ffff://Cyan
				return  5;
			case 0xff0000ff://Blue
				return  6;
			case 0xff551a8b://Purple
				return  7;
			default:
				return 0;
		}
	}
		
	String getLightNameFromPrefs(int value) {
		switch(value) {
			case 0xff000000://None
				return  "None";
			case 0xffffffff://White
				return  "White";
			case 0xffff0000://Red
				return  "Red";
			case 0xffffff00://Yellow
				return  "Yellow";
			case 0xff00ff00://Green
				return  "Green";
			case 0xff00ffff://Cyan
				return  "Cyan";
			case 0xff0000ff://Blue
				return  "Blue";
			case 0xff551a8b://Purple
				return  "Purple";
			default:
				return "None";
		}
	}
	
	
//_________________________________________________________________________________________________________________________
	//user alert dialog with radio button
	void showNotificationsVibrate(final int list_position) {
		int selection = getVibratePositionFromPrefs(Prefs.getVibrate(Notifications.this));
		final CharSequence[] items={"Off","Default","Short","Long"};
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setTitle("Vibrate");
    	dialog.setNegativeButton("Cancel", null);
    	dialog.setSingleChoiceItems(items, selection, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				
				switch(position) {
					case 0: //Off
						finishVibrateSelection(dialog, list_position, items[0]);
						break;
					case 1: //Default
						finishVibrateSelection(dialog, list_position, items[1]);
						break;
					case 2: //Short
						finishVibrateSelection(dialog, list_position, items[2]);
						break;
					case 3: //Long
						finishVibrateSelection(dialog, list_position, items[3]);
						break;
				}
			}
		});
    	dialog.show();
	}
	
	void finishVibrateSelection(DialogInterface dialog, int position, CharSequence options) {
		Prefs.putVibrate(Notifications.this, String.valueOf(options));
		((NotificationDoubleLineItem)notificationList.get(position)).firstLine = "Vibrate: " + options;
		dialog.dismiss();
		adapter.notifyDataSetChanged();
	}

	int getVibratePositionFromPrefs(String value) {
		switch(value) {
			case "Off":
				return  0;
			case "Default":
				return  1;
			case "Short":
				return  2;
			case "Long":
				return  3;
			default:
				return 0;
		}
	}

}
