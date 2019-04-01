package com.chattynotes.mvp.activities;

import java.io.File;
import java.util.ArrayList;

import com.chattynoteslite.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.adapters.listForward.model.InterfaceForward;
import com.chattynotes.adapters.listForward.AdapterForwardPager;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.util.AndIntentUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.VideoUtil;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import io.realm.Realm;

public class Forward extends AppCompatActivity {
	//Purpose : (Forward Message, Share Image/Text from other Applications)
	//this activity is used for : (Forward Message, Share Image/Text from other Applications, Send Image via Custom Camera)
		//Coming From Activity (1)
			// 1 - ImagePreviewActivity
			// 2 - VideoPreviewActivity

	//1- https://www.whatsapp.com/faq/android/28000012
	//2- http://developer.android.com/training/sharing/send.html
	//3- http://developer.android.com/training/sharing/receive.html
	
	//if isSharedMessage
	//=true (means applications like face-book, twitter shared the data)
	//=false (means forward message)
	Boolean isSharedMessage = false; 
	Bundle sharedBundle = null;//used in case of sharing only

	// Tab Icons
	private int[] tabIcons = { R.drawable.ic_note };

	//All Lists
	ArrayList<InterfaceForward> recentList 	= new ArrayList<>();

	//______________________________________________________________________________________
	//ViewPager with TabLayout : Tutorial used
	//http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
	//issue : last item not visible
	//http://stackoverflow.com/questions/34663756/why-my-listview-in-swiperefreshlayout-dont-show-the-last-element-on-android/34664873?noredirect=1#comment57109424_34664873

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forward);
		if (AppUtil.isRegisteredAndOnChatScreen())
			onCreate___();
		else {
			finish();
			AndIntentUtil.restartApp(this);
		}
	}

	void onCreate___() {
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_forward);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			//Change Title from here, not from manifest..
			//Because if use 'Forward' in Manifest, it will display 'Forward' when share from other Apps in App List
			actionBar.setTitle(getString(R.string.title_activity_forward));
		}


		// Get intent, action and MIME type
	    Intent intent = getIntent();
	    String action = intent.getAction();
	    String type = intent.getType();

	    if (Intent.ACTION_SEND.equals(action) && type != null) {
	    	if ("text/plain".equals(type)) {
	    		isSharedMessage = true;
		        //______________________________________________________ TEXT
	        	String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
	        	if (sharedText != null) {
	        		//LogUtil.e("SHARE TXT >>", "txt");
	        		sharedBundle = new Bundle();
		        	sharedBundle.putString(IntentKeys.SHARE_MSG_TEXT, sharedText);
	        	} else {	
	        		Toast.makeText(this, "Nothing to share", Toast.LENGTH_SHORT).show();
	        		finish();
	        	}
	        } else if (type.startsWith("image/")) {
	        	isSharedMessage = true;
		        //______________________________________________________ IMAGE
	        	Uri sharedImageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
	        	if (sharedImageUri != null) {
	        		//LogUtil.e("SHARE IMG >>", sharedImageUri.toString() + ":");
	        		sharedBundle = new Bundle();
    				sharedBundle.putString(IntentKeys.SHARE_MSG_IMAGE_URI, sharedImageUri.toString());
//	Not saving here, as WhatsApp just passed the URI from here 				
//	        		try {
//	        			//Loading Bitmap will cause out of memory exception
//		        	    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), sharedImageUri);
//	        			BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaDataFromUri(sharedImageUri);
//		    			if(options != null) {
//		    				MediaUtil.saveTempSharedImageToInternalStorage_Sent(sharedImageUri, options.outWidth, options.outHeight);
//		    				sharedBundle = new Bundle();
//		    				sharedBundle.putString(getString(R.string.shared_image_uri)	, PathUtil.internal_temporary_shared_image_path);
//		    			}
//	        		} catch (Exception e) {
//	        			Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
//	 	        		finish();
//	        	    }
	        	} else {
	        		Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
	        		finish();
	        	}
	        } else if (type.startsWith("video/")) {
	        	isSharedMessage = true;
		        //______________________________________________________ VIDEO
	        	Uri sharedVideoUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
	        	if (sharedVideoUri != null) {
	        		if(sharedVideoUri.getScheme()!=null && sharedVideoUri.getScheme().equals("file")) {
	        			//WhatsApp-Video: file:///sdcard/WhatsApp/Media/WhatsApp%20Video/Sent/VID-20150901-WA0000.mp4
	        			//only content URI that is videos in media store will work : content://media/external/video/media/16258
	        			
	        			//LogUtil.e("SHARE VID >>", sharedVideoUri.toString() + ":");
		        		
	        			//Answer Nizam : https://stackoverflow.com/questions/6301215/converting-file-scheme-to-content-scheme
	        			//File f1 = new File(sharedVideoUri.toString());
	        			File f	= new File(sharedVideoUri.getPath());
	        			//LogUtil.e("SHARE VID >>", f1.toString());	// file:/storage/emulated/0/Video/SummerSlam%202015.mp4 __  file:/sdcard/WhatsApp/Media/WhatsApp%20Video/Sent/VID-20150901-WA0000.mp4
	        			//LogUtil.e("SHARE VID >>", f2.toString());	// /storage/emulated/0/Video/SummerSlam 2015.mp4        __  /sdcard/WhatsApp/Media/WhatsApp Video/Sent/VID-20150901-WA0000.mp4
	        			sharedVideoUri = VideoUtil.getVideoContentUri(f);
	        			
	        			//now at this point sharedVideoUri can be null, because File might not be a part of media store, Case with WhatsApp Videos
	        			if(sharedVideoUri==null) {	
	    	        		Toast.makeText(this, "Error Opening Video", Toast.LENGTH_SHORT).show();
	    	        		finish();
	    					//https://stackoverflow.com/questions/8282569/oncreate-flow-continues-after-finish
	    					return;
	    	        	}
		        	}
	        		
	        		//LogUtil.e("SHARE VID >>", sharedVideoUri.toString() + ":");
	        		sharedBundle = new Bundle();
    				sharedBundle.putString(IntentKeys.MEDIA_VIDEO_URI, sharedVideoUri.toString());
				} else {
	        		Toast.makeText(this, "No Video Found", Toast.LENGTH_SHORT).show();
	        		finish();
	        	}
	        } else {//something unique has triggered
	        	Toast.makeText(this, "Nothing to share", Toast.LENGTH_SHORT).show();
	        	finish();
        	}
	    } /*else {
	        // Handle other intents, such as being started from the home screen (Forward)
		}*/
		
		
		//LOADING ALL LISTS
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		recentList.addAll(realmQuery.getChatList());
		realm.close();

		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		AdapterForwardPager adapter = new AdapterForwardPager(getSupportFragmentManager(), recentList, isSharedMessage, sharedBundle);
		viewPager.setAdapter(adapter);
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.getTabAt(0).setIcon(tabIcons[0]);
	}

//________________________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
}
