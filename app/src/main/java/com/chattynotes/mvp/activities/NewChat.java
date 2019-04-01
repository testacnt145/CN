package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.Msg;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgID;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.customviews.crop_biokys.BiokysCropImage;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.chattynotes.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import com.chattynotes.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import com.chattynotes.emojicon.emoji.Emojicon;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.util.IntentUtil;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.media.MediaUtil;
import com.chattynotes.util.NetworkUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.StorageUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.support.annotation.NonNull;
import io.realm.Realm;

public class NewChat extends AppCompatActivity {

	private final int CHAT_IMAGE_REQUEST = 1;
	private final int CROP_CHAT_IMAGE = 2;
	
	//Name
	EmojiconsPopup popup;
	View rootView;
	ConversationTextEntry editText;
	ImageView btn_emoji;
	TextView txtViewCount; 
	
	//Image
	ImageView imgView = null;
	Boolean _isImageChoosed = false;
	private Uri cameraUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat);

		//Group Subject
		rootView 		= findViewById(R.id.rootView);
		editText 		= (ConversationTextEntry)findViewById(R.id.editText);
		txtViewCount 	= (TextView)findViewById(R.id.txtViewCount);
		btn_emoji 		= (ImageButton) findViewById(R.id.btnEmoji);
		
		txtViewCount.setText(String.format(Locale.getDefault(), "%d", AppConst.MAX_CHAT_NAME_LENGTH - editText.length()));
		
		initializeEmojiPopup();
		editTextListeners();	

		//Group Photo
		imgView = (ImageView) findViewById(R.id.image);
		imgView.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
				List<String> _permissionList = new ArrayList<>();
				_permissionList.add(Manifest.permission.CAMERA);
				_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
				boolean checkPermission = PermissionUtil.checkMultiplePermissions(NewChat.this, _permissionList, getString(R.string.permission_change_chat_image_request), getString(R.string.permission_change_chat_image), R.drawable.permission_cam, R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_MULTIPLE_CHAT_IMAGE);
				if(checkPermission)
					btnEditImage();
 			}
		});
	}

	void btnEditImage() {
		//**** for external storage
		//Save image here instead of camera default folder
		cameraUri = PathUtil.createCameraChatImageUri();
		Intent intent = IntentUtil.profileImagePickerList(this, cameraUri);
		if (intent != null)
			startActivityForResult(intent, CHAT_IMAGE_REQUEST);
		else
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
		switch(resultCode) {
			case RESULT_CANCELED :
				break;
		//________________________________________
		case RESULT_OK :
			switch(requestCode) {
				//......................
				case CHAT_IMAGE_REQUEST:
					if(data == null || data.getData() == null)//in case of default camera application
						fromCamera();
		            else if(data.getData() != null)
		               	fromGallery(data.getData());
					break;
				
				//......................
				case CROP_CHAT_IMAGE:
					imgView.setImageBitmap(null); //so that it can remove cache, and show reflection
					imgView.setImageURI(PathUtil.getInternalChatImageTempUri());
					break;
			}
			break;
		//________________________________________
		}
	}
	
	void fromCamera() {
		callCropActivity(cameraUri);
	}
	
	void fromGallery(Uri _uri) {
        callCropActivity(_uri);
// [todo_: PATH-2-URI]
//		String _path;
//
//		try {
//			String[] filePath = { MediaStore.Images.Media.DATA };
//	        Cursor c = getContentResolver().query(_uri, filePath, null, null, null);
//	        c.moveToFirst();
//	        int columnIndex = c.getColumnIndex(filePath[0]);
//	        _path = c.getString(columnIndex);
//	        c.close();
//	    } catch(Exception e) {
//			LogUtil.eException(getClass().getSimpleName(), "Exception", "fromGallery", e.toString());
//			_path = _uri.getPath();//must be any camera image
//		}
//		
//		call_crop_activity(_path);
	}
	
	void callCropActivity(Uri uri) {
		BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(uri);
		if(options != null) {
			if(options.outWidth<MediaUtil.CHAT_IMAGE_MIN_WIDTH || options.outHeight<MediaUtil.CHAT_IMAGE_MIN_HEIGHT) {
				new AlertDialog.Builder(this)
				.setMessage("This image is too small. Please select a photo with height and width of at least 192 pixels.")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
			} else {
				_isImageChoosed = true;
				Intent intent = new Intent(this, BiokysCropImage.class);
				intent.putExtra(IntentKeys.MEDIA_IMAGE_URI, uri.toString());
				//intent.putExtra(getString(R.string.image_output_x)	, 200);
				//intent.putExtra(getString(R.string.image_output_y)	, 200);
				intent.putExtra(IntentKeys.MEDIA_IMAGE_IS_CIRCLE_CROP	, false);
				//intent.putExtra(getString(R.string.image_scale)		, true);
				intent.putExtra(IntentKeys.MEDIA_IMAGE_MIN_BOUND		, MediaUtil.CHAT_IMAGE_MIN_BOUND);
				intent.putExtra(IntentKeys.MEDIA_IMAGE_MAX_BOUND		, MediaUtil.CHAT_IMAGE_MAX_BOUND);
				intent.putExtra(IntentKeys.MEDIA_IMAGE_TEMP_FILE		, PathUtil.createInternalChatImageTempFile().toString()); //path where we want our image to be saved
				startActivityForResult(intent, CROP_CHAT_IMAGE);
			}
		} else
			NetworkUtil.fileNotFoundToast();
	}

//__________________________________________________________________________________________________
	public void onClickCreate(View view) {
		createNewChat();
	}

	public void createNewChat() {
		if (editText.length() >= AppConst.MIN_CHAT_NAME_LENGTH) {
			Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
			QueryNotesDB realmQuery = new QueryNotesDB(realm);

			long chatID = realmQuery.getIncrementedChatId();
			String chatName = editText.getText().toString();
			long msgTimestamp = MsgConstant.DEFAULT_MSG_TIMESTAMP(); //we want exact milliseconds
//---------------------------->>> DATABASE
			Notes note = new Notes();
			note.setNoteId(realmQuery.getIncrementedNoteId());
			note.setChatId(chatID);
			note.setMsg(Msg._CHAT_CREATED);
			note.setMsgId(MsgID.DEFAULT);
			note.setMsgTimestamp(msgTimestamp);
			note.setMediaStatus(MediaStatus.COMPLETED);
			Chats chat = new Chats();
			chat.setChatId(chatID);
			chat.setChatName(chatName);
			chat.setTimestamp(msgTimestamp);
			//chat.setNote(null); //null: very imp otherwise Primary Key error due to duplicate note
			realmQuery.insertChat(chat);
			realmQuery.insertNote(note);
			realm.close();
//---------------------------->>> UPDATING CHAT SCREEN
			chat.setNote(note); //set here for chat list
			lbmNewNoteChat(chat);
//---------------------------->>> IF CHAT IMAGE ATTACHED
			if (_isImageChoosed) {
				Uri tempChatImageUri = PathUtil.getInternalChatImageTempUri();
				BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(tempChatImageUri);
				if (options != null)
					MediaThumbUtil.saveThumbChatImage(tempChatImageUri, String.valueOf(chatID), options.outWidth, options.outHeight);
				StorageUtil.copyChatImageExtStg(String.valueOf(chatID));
			}
//---------------------------->>> FINISH ACTIVITY
			this.finish();
		} else {
			new AlertDialog.Builder(this)
			.setTitle("Chat Name")
			.setMessage("Length of chat name should be atleast 1")
			.setPositiveButton(android.R.string.yes, null)
			.show();
		}
	}

//-------------------------------------------------------------------
	@Override
	protected void onDestroy() {
		super.onDestroy();
		StorageUtil.deleteChatThumbTempIntStg();
	}

//__________________________________________________________________________________________________________ 
	void editTextListeners() {
		editText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				txtViewCount.setText(String.format(Locale.getDefault(), "%d", AppConst.MAX_CHAT_NAME_LENGTH - editText.length()));
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
		
		
		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Add new Line on enter key press
					createNewChat();
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
			if(popup.isKeyBoardOpen()) {
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
	//[os_marshmallow_: permission]
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PermissionUtil.PERMISSION_MULTIPLE_CHAT_IMAGE:
				Map<String, Integer> permPhoto = new HashMap<>();
				permPhoto.put(Manifest.permission.CAMERA				, PackageManager.PERMISSION_GRANTED);
				permPhoto.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				//replace the permission with appropriate result
				for (int i=0; i<permissions.length; i++)
					permPhoto.put(permissions[i], grantResults[i]);
				//check and display appropriate message
				if(permPhoto.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
						&& permPhoto.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					//all permissions not granted
					permissionNotGranted();
				} else if(permPhoto.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_camera, Toast.LENGTH_SHORT).show();
				} else if(permPhoto.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_storage, Toast.LENGTH_SHORT).show();
				} else { //all permissions granted
					btnEditImage();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	void permissionNotGranted() {
		Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
	}

	//_____________________________________________________________________________ NOTIFYING_ACTIVITY_FROM_ACTIVITY
	//Activities to Notify (1) : Chats
	void lbmNewNoteChat(Chats chats) {
		Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_CHAT);
		in.putExtra(IntentKeys.PARCELABLE_CHAT_ITEM, chats);
		LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	}
}
