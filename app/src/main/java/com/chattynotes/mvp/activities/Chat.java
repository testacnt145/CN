package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.ItemType;
import com.chattynotes.constant.keyboard.WSCKeys;
import com.chattynotes.constant.keyboard.WSRKeys;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.adapters.listChat.AdapterChat;
import com.chattynotes.adapters.listChat.ComparatorChatTime;
import com.chattynotes.adapters.listChat.model.InterfaceChat;
import com.chattynotes.constant.keyboard.Keyboard;
import com.chattynotes.swipe.rfit.API;
import com.chattynotes.swipe.rfit.converter.StringConverterFactory;
import com.chattynotes.util.AndServiceUtil;
import com.chattynotes.util.LogUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.media.MediaTextUtil;
import com.chattynotes.util.NetworkUtil;
import com.chattynotes.util.PasswordUtil;
import com.chattynotes.util.storage.StorageUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONObject;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Chat extends AppCompatActivity implements OnItemClickListener, Callback<String> {

//____________________________________________________________________ 
	//Coming From Activity (1)
		// 1 - Main Activity (Normal)

	// Shared Message leads to MainActivity as it might be possible that Applications is not registered
	// * - Fragment Recent/Contact/Group (Sharing text/Image from other applications (twitter,fb))

	ArrayList<InterfaceChat> chatList = new ArrayList<>();
	final int REQ_INFO = 1;
	ListView listview = null;
	AdapterChat adapter;
	TextView emptyChat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chats);
		setSupportActionBar(toolbar);

		emptyChat = (TextView) findViewById(R.id.chat_empty);
		listview = (ListView) findViewById(R.id.list_chats);
		adapter = new AdapterChat(this, chatList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		registerForContextMenu(listview);
		//Search able Activity
		listview.setTextFilterEnabled(true);
		handleIntent(getIntent());

		getChatList();
		updateUI();
		if(Prefs.getIsDisplayNewVersionBanner(this))
			findViewById(R.id.banner_new_version).setVisibility(View.VISIBLE);

//---------------------------->>> SHARED TEXT/IMAGE MESSAGE FROM OTHER APPS
		//opening shared bundle and putting all its data in conversation bundle
		Bundle sharedBundle = getIntent().getBundleExtra(IntentKeys.BUNDLE_SHARED);
		if(sharedBundle != null) {
			Bundle conversationBundle = new Bundle();
			//mandatory
			conversationBundle.putLong(IntentKeys.CHAT_ID, sharedBundle.getLong(IntentKeys.CHAT_ID));
			//text
			if (sharedBundle.containsKey(IntentKeys.SHARE_MSG_TEXT)) {
				conversationBundle.putString(IntentKeys.SHARE_MSG_TEXT, sharedBundle.getString(IntentKeys.SHARE_MSG_TEXT));
			}
			//image
			else if(sharedBundle.containsKey(IntentKeys.SHARE_MSG_IMAGE_URI)) {
				conversationBundle.putString(IntentKeys.MEDIA_NAME			, sharedBundle.getString(IntentKeys.MEDIA_NAME));
				conversationBundle.putString(IntentKeys.SHARE_MSG_IMAGE_URI	, sharedBundle.getString(IntentKeys.SHARE_MSG_IMAGE_URI));
			}
			//video
			else if(sharedBundle.containsKey(IntentKeys.MEDIA_VIDEO_URI)) {
				conversationBundle.putString(IntentKeys.MEDIA_NAME		, sharedBundle.getString(IntentKeys.MEDIA_NAME));
				conversationBundle.putString(IntentKeys.MEDIA_VIDEO_URI	, sharedBundle.getString(IntentKeys.MEDIA_VIDEO_URI));
			}
			PasswordUtil.verifyPassword(this, conversationBundle, false);
		}

//---------------------------->>> LOCAL BROADCAST MANAGER (to receive intents when activity is running)
		lbmRegister();

		wscAndroidVersionSupport();
	}

//_____________________________________________________________________________________________________________
	@Override
    protected void onNewIntent(Intent in) {
		//Search able Activity
        handleIntent(in);
    }

	 private void handleIntent(Intent in) {
    	if (Intent.ACTION_SEARCH.equals(in.getAction())) {
            String query = in.getStringExtra(SearchManager.QUERY);
            adapter.getFilter().filter(query);
            //use the query to search your data somehow
            //if(query.equals("QUERY"))
        }
    }

//______________________________________________________________________________________________________________
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		InterfaceChat item = (InterfaceChat)arg0.getAdapter().getItem(position);
		if (item.itemType().equals(ItemType.CHAT)) {
			Chats chat = (Chats)item;
			long chatID = chat.getChatId();
			//bundle
			Bundle conversationBundle =  new Bundle();
			conversationBundle.putLong(IntentKeys.CHAT_ID, chatID);
			PasswordUtil.verifyPassword(this, conversationBundle, false);
		}
	}

	public void btnFab(View view) {
		Intent i = new Intent(Chat.this, NewChat.class);
		startActivity(i);
	}

//______________________________________________________________________________________________________________
	SearchView searchView;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_chat, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Chat.SEARCH_SERVICE);
	    searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
               adapter.getFilter().filter(query);
               return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        //SearchView.OnCloseListener textChangeOnCloseListener = new SearchView.OnCloseListener()
        //{
		//	@Override
		//	public boolean onClose() {
		//		adapter.getFilter().filter("");
		//		return false;
		//	}
        //};
        //searchView.setOnCloseListener(textChangeOnCloseListener);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_settings) {
			Intent in = new Intent(Chat.this, Settings.class);
			startActivity(in);
			return true;
		} /*else if (id == R.id.menu_starred) {
			Intent in = new Intent(Chat.this, StarredNotes.class);
			startActivity(in);
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent in) {
	    if (resultCode == RESULT_CANCELED) {
			switch(requestCode) {
				case REQ_INFO:
					//in case of back button is pressed, intent == null so Exception
					if(in != null && in.getExtras() != null) {
						long chatID = in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
						//bundle
						Bundle conversationBundle =  new Bundle();
						conversationBundle.putLong(IntentKeys.CHAT_ID, chatID);
						PasswordUtil.verifyPassword(this, conversationBundle, false);
					}
		  			break;
			}
		}
	}

//______________________________________________________________________________________________________________
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		//AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String[] menuItems = { "View info", "Delete chat", "Email chat"};
		/*ChatItem chatItem = (ChatItem)chatList.get(info.position);
		if(PrefsEncrypt.getChatEncrypted(chatItem.number, this).equals(PDefaultValue.DEFAULT_ENCRYPT_MESSAGE))
			menuItems[4] = "Encrypt chat";
	 	else
	 		menuItems[4] = "Decrypt chat";
		*/
		for (int i=0; i<menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		//[bug_: UI_List_| selecting wrong item]
		//https://stackoverflow.com/a/2322018/4754141
		int searchListItemPosition = info.position;
		Chats chat = (Chats)adapter.getItem(searchListItemPosition);
		int chatListItemPosition = adapter.getPosition(chat);
		switch (item.getItemId()) {
		case 0: // View
			searchView.setIconified(true);//close search view
			viewInfo(chat);
			return true;
		case 1: // Delete
			searchView.setIconified(true);//close search view
			deleteChat(chat, chatListItemPosition);
			return true;
		case 2: // Email
			searchView.setIconified(true);//close search view
			emailChat(chat);
			return true;
		/*case 3: // Encrypt
			ChatItem cItemE = (ChatItem)chatList.get(info.position);
			if(item.getTitle().equals("Encrypt chat"))
				encryptChat(cItemE.number);
			else
				decryptChat(cItemE.number);
			return true;*/
		default:
			return super.onContextItemSelected(item);
		}
	}

//________________________________________________________________________________________________________________
	/*
	private void encryptChat(final String _number) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Encrypt Chat");
		alert.setMessage("Enter new password...");
		final View view = View.inflate(this, R.layout.dialog_password, null);
		final EditText input = (EditText)view.findViewById(R.id.eT_password);
		final CheckBox cBox = (CheckBox)view.findViewById(R.id.cBox_password);
		cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				} else {
					input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		alert.setView(view);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
				if(input.length() >= AppConst.PASSWORD_LENGTH) {
					Toast.makeText(Chat.this, "Success", Toast.LENGTH_SHORT).show();
					PrefsEncrypt.putChatEncrypted(Chat.this, _number, input.getText().toString());
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(Chat.this, "Length of password should be greater than 2", Toast.LENGTH_SHORT).show();
				}
			}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	private void decryptChat(final String _number) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Decrypt Chat");
		alert.setMessage("Are you sure?");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			PrefsEncrypt.putChatEncrypted(Chat.this, _number, PDefaultValue.DEFAULT_ENCRYPT_MESSAGE);
			adapter.notifyDataSetChanged();
			}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
	*/

	private void viewInfo(Chats chat) {
		String chatName = chat.getChatName();
		long chatID = chat.getChatId();
		Intent in = new Intent(this, ChatInfo.class);
		Bundle infoBundle =  new Bundle();
		infoBundle.putString(IntentKeys.CHAT_NAME, chatName);
		infoBundle.putLong(IntentKeys.CHAT_ID, chatID);
		in.putExtra(IntentKeys.BUNDLE_INFO, infoBundle);
		startActivity(in);
	}

//________________________________________________________________________________________________________________
	private void deleteChat(final Chats chat, final int position) {
		//custom layout
		LayoutInflater inflater = this.getLayoutInflater();
		View custom_view = inflater.inflate(R.layout.dialog_text, (ViewGroup) findViewById(android.R.id.content), false);
		final TextView txtView = (TextView) custom_view.findViewById(R.id.dialog_text_view);

		final AlertDialog alertDialog = new AlertDialog.Builder(this)
			.setView(custom_view)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	deleteWork(chat, position);
		    }})
			.setNegativeButton(android.R.string.no, null)
			.create();
		String text = String.format(getString(R.string.delete_chat_with), chat.getChatName());
		txtView.setText(text);
		alertDialog.show();
	}

	void deleteWork(Chats chat, int position) {
		//delete database
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		realmQuery.deleteChat(chat.getChatId()); //make sure to delete group identifier message
		realm.close();
    	//delete a internal media thumbs
		StorageUtil.deleteChatThumbIntStg(Long.toString(chat.getChatId()));
		StorageUtil.deleteChatMediaThumbIntStg(chat.getChatId());
		//remove chat list item
		chatList.remove(position);
		updateUI();
		adapter.notifyDataSetChanged();
	}

//________________________________________________________________________________________________________________
	private void emailChat(Chats chat) {
		String chatName	= chat.getChatName();
		long chatID = chat.getChatId();
		if(PermissionUtil.checkPermissionSilent(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
			QueryNotesDB realmQuery = new QueryNotesDB(realm);
			String content = realmQuery.getEmailContent(chatID);
			realm.close();
			if (!MediaTextUtil.writeTextFile(this, chatName, content)) //written failed
				Toast.makeText(this, "Error mailing conversation", Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
	}

    //CALLED FROM (3)
    //Chats, Conversation, NewChat
	void updateChatScreen(Chats chat) {
		//Compare chatID With chatList, if exist
		//update the Message only
		//else insert new ChatItem in chatList

        //************************ UPDATE
        if(!chatList.isEmpty()) {
			for(int i =0; i<chatList.size(); i++) {
				Chats oldChat = (Chats)(chatList.get(i));
				if(oldChat.getChatId()==chat.getChatId()) {//****** ClassCastException Error
					//modern way
					chatList.set(i, chat);
					//new way
					//oldChat = new Chats(chat);
					//chatList.set(i, oldChat);
					//old method
//					oldChat.setChatId(chat.getChatId());
//					oldChat.setName(chat.getName());
//					oldChat.setTimestamp(chat.getTimestamp());
//					oldChat.setDraft(chat.getDraft());
//					oldChat.setPasswordStatus(chat.getPasswordStatus());
//					oldChat.setPassword(chat.getPassword());
//					oldChat.setPasswordHint(chat.getPasswordHint());
//					oldChat.setPasswordCode(chat.getPasswordCode());
//					oldChat.setNote(chat.getNote());
					//Sort The chatList by Time using CustomComparator
					Collections.sort(chatList, Collections.reverseOrder(new ComparatorChatTime()));
					adapter.notifyDataSetChanged();

					return; //do not run the code below
				}
			}
		}

        //************************ NEW INSERTION
		chatList.add(0, chat);
		updateUI();

        //Sort The chatList by Time using CustomComparator
        Collections.sort(chatList, Collections.reverseOrder(new ComparatorChatTime()));
        //MultiThreading Exception: http://stackoverflow.com/questions/3132021/android-listview-illegalstateexception-the-content-of-the-adapter-has-changed
		//therefore notify list
		adapter.notifyDataSetChanged();
	}

//__________________________________________________________________________________________________
	private void getChatList() {
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		chatList.addAll(realmQuery.getChatList());
		realm.close();
	}

	private void updateUI() {
		if(chatList.isEmpty())
			displayEmptyChatList();
		else
			removeEmptyChatList();
	}

	private void displayEmptyChatList() {
		listview.setVisibility(View.GONE);
		emptyChat.setVisibility(View.VISIBLE);
	}

	void removeEmptyChatList() {
		listview.setVisibility(View.VISIBLE);
		emptyChat.setVisibility(View.GONE);
	}

//__________________________________________________________________________________________________
	@Override
	protected void onResume() {
		super.onResume();
		//Sort The chatList by Time using CustomComparator
		//This is very important, other wise unread message will always show up
		Collections.sort(chatList, Collections.reverseOrder(new ComparatorChatTime()));
	    adapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lbmUnregister();
	}

//_____________________________________________________________________________________________________________________ wsMETHODS
	void wscAndroidVersionSupport() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Keyboard.baseUrl.getValue())
				.addConverterFactory(StringConverterFactory.create())
				.build();
		API api = retrofit.create(API.class);
		Call<String> call = api.androidVersionSupport(WSCKeys.VALUE_androidVersionSupport);
		call.enqueue(this);
	}


//_____________________________________________________________________________________________________________________ webServiceResponse
	@Override
	public void onResponse(Call<String> call, Response<String> response) {
		//LogUtil.e("Chats:wscAndroidVersionSupport", "success-" + response.body());
		if(response.body()!=null)
			wsrAndroidVersionSupport(response.body());
	}
	@Override
	public void onFailure(Call<String> call, Throwable t) {
		//LogUtil.e("Chats:wscAndroidVersionSupport", "failed-");
		//NetworkUtil.internetNotAvailableToast();
	}

	//we are 100% sure about API
	//[release_: change according to API]
	//very first time d.o.s = 1000.1000.1000
	//[imp_: first change api then change upload APK, otherwise it will show obsolete]
	private final static int API_D = 1000;
	private final static int API_O = 1000;
	private final static int API_S = 2301;
	void wsrAndroidVersionSupport(String response) {
		if(response.equals(WSRKeys.androidVersionSupport_FAILED)) {
			NetworkUtil.NoResponseToast();
		} else {
			//response of androidVersionSupport()
			//response = "{\"d\":37023,\"o\":37023,\"s\":44423}";//1000=37023, 1000=37023, 1200=44423
			try {
				int c = Prefs.getAppVersionCode(this);
				JSONObject jO = new JSONObject(response);
				int d =  decryptVersionCode(jO.getInt(WSRKeys.androidVersionSupport_d));
				int o =  decryptVersionCode(jO.getInt(WSRKeys.androidVersionSupport_o));
				int s =  decryptVersionCode(jO.getInt(WSRKeys.androidVersionSupport_s));
				LogUtil.e(response, c + "-->" + d + "|" + o + "|" + s);
				//make sure to check in order of d o s
				//hardcode checking
				if(d==0&&o==0&&s==0) {
					wsrMinimumDeletedVersion();
				} else if (c<d||d<API_D) {
					wsrMinimumDeletedVersion();
				} else if (c<o||o<API_O) {
					wsrMinimumObsoleteVersion();
				} else if (c<s||s<API_S) {
					if(c<s) {
						//LogUtil.e("normal", "this version support ended");
						wsrMinimumSupportedVersion();
					} else {
						//LogUtil.e("abnormal", "change");
						wsrMinimumObsoleteVersion();
					}
				} /* else
					LogUtil.e("normal", "Up2Date"); */
			} catch(Exception e) {
				//LogUtil.e("abnormal", "response");
				wsrMinimumSupportedVersion();
				//webServiceResponseMinimumDeletedVersion();
			}
		}
	}
	int decryptVersionCode(int versionCode) {
		//[api_: (versionCode*37)+23]
		return (versionCode-23)/37;
	}
	/*testing purpose
	int encryptVersionCode(int versionCode) {
		return (versionCode*37)+23;
	}*/

//__________________________________________________________________________________________________
	void wsrMinimumDeletedVersion() {
		//  1- delete all chats
		//  2- stop the services [same as obsolete]
		// 	3- save Prefs.putStage(this, MainActivity.STAGE_APP_VERSION_DELETE);
		//  4- move user to AppVersionIncorrect Activity [same as obsolete]
		Prefs.putIsDisplayNewVersionBanner(this, true);
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		realmQuery.deleteAllChats();
		realm.close();
		StorageUtil.deleteAllMediaThumbIntStg();
		AndServiceUtil.stopServiceApp(this);
		Prefs.putStage(Chat.this, MainActivity.STAGE_APP_VERSION_DELETE);
		//alert
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.app_version_min_delete_title));
		alert.setMessage(getString(R.string.app_version_min_delete));
		alert.setCancelable(false);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startAppVersionIncorrectActivity();
			}
		});
		alert.show();
	}

	void wsrMinimumObsoleteVersion() {
		//  1- stop the services
		// 	2- save Prefs.putStage(this, MainActivity.STAGE_APP_VERSION_OBSOLETE);
		//  3- move user to AppVersionIncorrect Activity
		Prefs.putIsDisplayNewVersionBanner(this, true);
		AndServiceUtil.stopServiceApp(this);
		Prefs.putStage(Chat.this, MainActivity.STAGE_APP_VERSION_OBSOLETE);
		//alert
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.app_version_min_obsolete_title));
		alert.setMessage(getString(R.string.app_version_min_obsolete));
		alert.setCancelable(false);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startAppVersionIncorrectActivity();
			}
		});
		alert.show();
	}

	void startAppVersionIncorrectActivity() {
		finish();
		Intent in = new Intent(Chat.this, AppVersionIncorrect.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);
	}

	void wsrMinimumSupportedVersion() {
		// 	1- just prompt the user with dialog if he wishes to update with OK button
		Prefs.putIsDisplayNewVersionBanner(this, true);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.app_version_min_support_title));
		alert.setMessage(getString(R.string.app_version_min_support));
		final View view = View.inflate(this, R.layout.dialog_update_app, null);
		final TextView txtView = (TextView)view.findViewById(R.id.txtView_ReleaseNote);
		txtView.setPaintFlags(txtView.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		txtView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(Chat.this, WebViewActivity.class);
				in.putExtra(IntentKeys.URL, Keyboard.baseUrl_release_notes.getValue());
				startActivity(in);
			}
		});
		alert.setView(view);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
//________________________________________________________________________________________________________________________ LBM
	private BroadcastReceiver lbmNewNoteChat = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			Chats chat = in.getParcelableExtra(IntentKeys.PARCELABLE_CHAT_ITEM);
			updateChatScreen(chat);
		}
	};

	private BroadcastReceiver lbmMsgStatus = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			long chatId = in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
			String msgId = in.getStringExtra(IntentKeys.MSG_ID);
			int msgStatus = in.getIntExtra(IntentKeys.MSG_STATUS, MsgConstant.DEFAULT_MSG_STATUS);
			if(!chatList.isEmpty())
				for(int i=0; i<chatList.size(); i++) {
					Chats chat = (Chats)chatList.get(i);
					if(chat.getChatId()==chatId && chat.getNote().getMsgId().equals(msgId)) { //ClassCastException Error
						Notes note = chat.getNote();
						note.setMsgStatus(msgStatus);
						chat.setNote(note);
						adapter.notifyDataSetChanged();
					}
				}
		}
	};

	private BroadcastReceiver lbmChangeName = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			long chatId  = in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
			String chatName = in.getStringExtra(IntentKeys.CHAT_NAME);
			if(!chatList.isEmpty())
				for(int i=0; i<chatList.size(); i++) {
					Chats chatItem = (Chats)chatList.get(i);
					if (chatItem.getChatId() == chatId) {
						chatItem.setChatName(chatName);
						adapter.notifyDataSetChanged();
					}
				}
		}
	};

	private BroadcastReceiver lbmChangeImage = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			//long chatID = in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
			adapter.notifyDataSetChanged();
		}
	};


	private BroadcastReceiver lbmDeleteAllChat = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			//1 - clear
			//2 - reload again i.e group identifier notes
			//3 - layout
			//chatList.clear();
			//chatList = Db.getChatList();

			//LIST_ADAPTER_ISSUES__ but .clear() is not working properly, it is showing weird behavior

			if(!chatList.isEmpty()) {
				for(int i=0; i<chatList.size(); i++) {
					chatList.remove(i);//remove that item
					i--; // as list is shortened on each removal
				}
				updateUI();
			}
			adapter.notifyDataSetChanged();
		}
	};

	private BroadcastReceiver lbmRestoreChat= new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			if(!chatList.isEmpty()) {
				for(int i=0; i<chatList.size(); i++) {
					chatList.remove(i);//remove that item
					i--; // as list is shortened on each removal
				}
			}
			getChatList();
			updateUI();
			adapter.notifyDataSetChanged();
		}
	};


	//________________________________________________________________________________________________________________________ REGISTER LBM
	void lbmRegister() {
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmChangeName,		new IntentFilter(IntentKeys.LBM_CHANGE_NAME));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmChangeImage, 	new IntentFilter(IntentKeys.LBM_CHANGE_IMAGE));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmNewNoteChat, 	new IntentFilter(IntentKeys.LBM_NEW_NOTE_CHAT));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmMsgStatus, 		new IntentFilter(IntentKeys.LBM_MSG_STATUS));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmDeleteAllChat,	new IntentFilter(IntentKeys.LBM_DELETE_ALL_CHAT));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmRestoreChat,	new IntentFilter(IntentKeys.LBM_RESTORE_CHAT));
	}

	//________________________________________________________________________________________________________________________ UNREGISTER LBM
	void lbmUnregister() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmChangeName);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmChangeImage);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmNewNoteChat);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmMsgStatus);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmDeleteAllChat);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmRestoreChat);

	}

}