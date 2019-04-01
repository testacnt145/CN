package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.Msg;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.chattynotes.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import com.chattynotes.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import com.chattynotes.emojicon.emoji.Emojicon;
import com.chattynotes.database.rl.QueryNotesDB;
import java.util.Locale;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.support.v7.app.ActionBar;
import io.realm.Realm;

public class ChangeChatName extends AppCompatActivity {

	EmojiconsPopup popup;
	View rootView;
	ConversationTextEntry editText;
	ImageView btn_emoji;
	TextView txtViewCount;
	String chatName;
	long chatID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_chat_name);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_change_chat_name);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		Bundle infoBundle = getIntent().getBundleExtra(IntentKeys.BUNDLE_INFO);
		if (infoBundle != null) {
			chatName = infoBundle.getString(IntentKeys.CHAT_NAME);
			chatID = infoBundle.getLong(IntentKeys.CHAT_ID);
		}

		rootView 		= findViewById(R.id.rootView);
		editText 		= (ConversationTextEntry)findViewById(R.id.editText);
		txtViewCount 	= (TextView)findViewById(R.id.txtViewCount);
		btn_emoji 		= (ImageButton) findViewById(R.id.btnEmoji);
		
		//open the keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		editText.setText(chatName);
		txtViewCount.setText(String.format(Locale.getDefault(), "%d", AppConst.MAX_CHAT_NAME_LENGTH - editText.length()));
		initializeEmojiPopup();
		editTextListeners();
	}
	
//_____________________________________________________________________________________________________________________
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu__tick, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} else if (id == R.id.menu_tick) {
			updateChatName();
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}


//-------------------------------------------------------------------
	void updateChatName() {
		if(editText.length() >= AppConst.MIN_CHAT_NAME_LENGTH) {
			closeKeyboard();
			String oldName = chatName;
			chatName = editText.getText().toString();
			String msg = Msg._CHAT_NAME_CHANGE(oldName,chatName);
			long msgTimestamp = MsgConstant.DEFAULT_MSG_TIMESTAMP();

			Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
			QueryNotesDB realmQuery = new QueryNotesDB(realm);

			Notes note = new Notes();
			note.setNoteId(realmQuery.getIncrementedNoteId());
			note.setChatId(chatID);
			note.setMsg(msg);
			note.setMsgKind(MsgKind._CHANGE_NAME);
			note.setMsgTimestamp(msgTimestamp);
			note.setMediaStatus(MediaStatus.COMPLETED);
			Chats chat = new Chats();
			chat.setChatId(chatID);
			chat.setChatName(chatName);
			chat.setNote(note);
			realmQuery.updateChatName(chat);
			realmQuery.insertNote(note);
			lbmNewNoteChat(chat);
			lbmNewNoteConversation(note);
			lbmChangeName();
			realm.close();

			Toast.makeText(this, "Chat name successfully changed", Toast.LENGTH_LONG).show();
			finish();
		} else {
			new AlertDialog.Builder(this)
					.setTitle("Chat Name")
					.setMessage("Length of chat name should be atleast 1")
					.setPositiveButton(android.R.string.yes, null)
					.show();
		}
	}
	
//-------------------------------------------------------------------
	private void closeKeyboard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);	
	}
	
//__________________________________________________________________________________________________________ 
	void editTextListeners() {
		editText.addTextChangedListener(new TextWatcher() {
	      public void afterTextChanged(Editable s) {
	    	  txtViewCount.setText(String.format(Locale.getDefault(), "%d", AppConst.MAX_CHAT_NAME_LENGTH - editText.length()));
	    	}
	      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	
	      public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		
		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					updateChatName();
					return true;
				}
				return false;
			}
		});
	}
	
//__________________________________________________________________________________________________________ EMOJICON
	public void onClickBtnEmoji(View view){
		//If popup is not showing => emoji keyboard is not visible, we need to show it
		if(!popup.isShowing()) {
			//If keyboard is visible, simply show the emoji popup
			if(popup.isKeyBoardOpen()){
				popup.showAtBottom();
				btn_emoji.setImageResource(R.drawable.input_kbd);
			} else { //else, open the text keyboard first and immediately after that show the emoji popup
				editText.setFocusableInTouchMode(true);
				editText.requestFocus();
				popup.showAtBottomPending();
				final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
				btn_emoji.setImageResource(R.drawable.input_kbd);
			}
		} else {//If popup is showing, simply dismiss it to show the undelying text keyboard 
			popup.dismiss();
		}
	}
		
	void initializeEmojiPopup() {
		// Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
		popup = new EmojiconsPopup(rootView, this);

		//Will automatically set size according to the soft keyboard size        
		popup.setSizeForSoftKeyboard();

		//If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				btn_emoji.setImageResource(R.drawable.input_emoji);
			}
		});

		//If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {
			@Override
			public void onKeyboardOpen(int keyBoardHeight) {
			}

			@Override
			public void onKeyboardClose() {
				if(popup.isShowing())
					popup.dismiss();
			}
		});

		//On emoji clicked, add it to edittext
		popup.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {
			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
	            if (editText == null || emojicon == null) {
	                return;
	            }

	            int start = editText.getSelectionStart();
	            int end = editText.getSelectionEnd();
	            if (start < 0) {
	            	editText.append(emojicon.getEmoji());
	            } else {
	            	editText.getText().replace(Math.min(start, end), Math.max(start, end), emojicon.getEmoji(), 0, emojicon.getEmoji().length());
	            }
	        }
		});

		//On backspace clicked, emulate the KEYCODE_DEL key event
		popup.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {
			@Override
			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				editText.dispatchKeyEvent(event);
			}
		});
	}

//__________________________________________________________________________________________________
	void lbmChangeName() {
		Intent in = new Intent(IntentKeys.LBM_CHANGE_NAME);
		in.putExtra(IntentKeys.CHAT_ID, chatID);
		in.putExtra(IntentKeys.CHAT_NAME, chatName);
		LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	}

	void lbmNewNoteChat(Chats chat) {
		Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_CHAT);
		in.putExtra(IntentKeys.PARCELABLE_CHAT_ITEM, chat);
		LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	}

	void lbmNewNoteConversation(Notes note) {
		Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_CONVERSATION);
		in.putExtra(IntentKeys.PARCELABLE_NOTE_ITEM, note);
		LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	}
}
