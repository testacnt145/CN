package com.chattynotes.camera;

import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MimeType;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgID;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.AndUriUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.util.ShareUtil;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.media.MediaUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.R;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.chattynotes.emojicon.EmojiconGridView;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.emojicon.emoji.Emojicon;

public class ImagePreviewActivity extends AppCompatActivity {
	//PURPOSE_OF_THE_ACTIVITY__
	//this activity is used to send image via Custom Camera
	//Coming From Activity (1)
		// 1 - CameraActivity
	
	Uri imageUri;
	String imageName;
	long chatID;
	String chatName;
	
	private ImageLoaderPath imageLoader;
	ImageView imgView = null;
	
	//detect whether user navigates to this activity via HOME SCREEN (camera clicked) or not
	Boolean __via_HOME_SCREEN = false;

	EmojiconsPopup popup;
	FrameLayout frameMediaCaption;
	View rootView;
	ConversationTextEntry editText;
	ImageView btn_emoji;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_image);
		if(checkMultiplePermissionsSilent())
			onCreate___();
		else {
			Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	void onCreate___() {
		Bundle bundleImage = getIntent().getBundleExtra(IntentKeys.BUNDLE_IMAGE);
		imageName = bundleImage.getString(IntentKeys.MEDIA_NAME);
		imageUri = Uri.parse(bundleImage.getString(IntentKeys.SHARE_MSG_IMAGE_URI));
		//This is a uri scheme 'content', and we only displays image with 'file' uri so convert
		if (imageUri!=null && imageUri.getScheme()!=null && imageUri.getScheme().equals("content")) {
			//LogUtil.e(getClass().getSimpleName(), imageUri.toString() + ":1");
			//LogUtil.e(getClass().getSimpleName(), imageUri.getPath() + ":2");
			String p = AndUriUtil.getRealPathFromURI(this, imageUri);
			if (p != null) {
				File f = new File(p);
				imageUri = Uri.fromFile(f);
				//LogUtil.e(getClass().getSimpleName(), imageUri.toString() + ":3");
				//LogUtil.e(getClass().getSimpleName(), imageUri.getPath() + ":4");
			} /*else {
				//[todo_: NPE_| null pointer in skype, google || bug_: ]
				//LogUtil.e(getClass().getSimpleName(), imageUri.toString() + ":5");
				//LogUtil.e(getClass().getSimpleName(), imageUri.getPath() + ":6");
				Toast.makeText(this, "Error loading image...", Toast.LENGTH_SHORT).show();
				finish();
			}*/
		}

		//working well with all apps except Skype,

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_send_image);
		setSupportActionBar(toolbar);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(R.layout.toolbar__2_titles);

			TextView t1 = (TextView) findViewById(R.id.toolbar_title_1);
			TextView t2 = (TextView) findViewById(R.id.toolbar_title_2);
			t1.setText(getString(R.string.send_image));

			if(bundleImage.getBoolean(IntentKeys.IS_CAMERA_ACCESS_VIA_HOME_SCREEN)) {
				__via_HOME_SCREEN = true;
				t2.setVisibility(View.GONE);
			} else {
				chatName = bundleImage.getString(IntentKeys.CHAT_NAME);
				chatID = bundleImage.getLong(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
				t2.setText(chatName);
			}
		}

		//display image
		imgView = (ImageView)findViewById(R.id.image);
		imageLoader = new ImageLoaderPath(this);
		imageLoader.displayImage(imageUri.getPath(), ImageLoaderPath.DECODE_IMAGE_FROM_FILE, imgView, MediaUtil.MEDIA_IMAGE_TO_LOAD_WIDTH, MediaUtil.MEDIA_IMAGE_TO_LOAD_HEIGHT);

		//media_caption
		frameMediaCaption= (FrameLayout) findViewById(R.id.layoutMediaCaption);
		//makes it visible only if permission is granted
		frameMediaCaption.setVisibility(View.VISIBLE);
		rootView 		= findViewById(R.id.rootView);
		editText 		= (ConversationTextEntry)findViewById(R.id.editText);
		btn_emoji 		= (ImageButton) findViewById(R.id.btnEmoji);
		initializeEmojiPopup();
	}


//________________________________________________________________________________________________________________
	public void onClickBtnSend(View view) {
		if(!__via_HOME_SCREEN) {
			imageLoader.clearCache();
			view.setVisibility(View.GONE);
			view.setEnabled(false);
			BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(imageUri);
			if(options != null && MediaUtil.saveMediaImageToExtStg_Sent(imageUri, imageName, options.outWidth, options.outHeight)) {
				MediaThumbUtil.saveThumbMediaImage(imageUri, imageName, chatID, options.outWidth, options.outHeight);
				lbmNewNoteMedia(imageName);
			} else
				Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
			setResult(RESULT_CANCELED, new Intent());
			finish();
		} else {
			//deal this image as a Shared Image and call the Forward Activity
			setResult(RESULT_CANCELED, new Intent());
			finish();
			ShareUtil.shareMediaCustomCamera(this, imageUri, MimeType.MIME_TYPE_IMAGE_JPEG);
		}
	}

	public void onClickBtnEmoji(View view) {
		//If popup is not showing => emoji keyboard is not visible, we need to show it
		if(!popup.isShowing()) {
			if(popup.isKeyBoardOpen()) {
				//If keyboard is visible, simply show the emoji popup
				popup.showAtBottom();
				btn_emoji.setImageResource(R.drawable.input_kbd_white);
			} else {
				//else, open the text keyboard first and immediately after that show the emoji popup
				editText.setFocusableInTouchMode(true);
				editText.requestFocus();
				popup.showAtBottomPending();
				final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
				btn_emoji.setImageResource(R.drawable.input_kbd_white);
			}
		} else {//If popup is showing, simply dismiss it to show the underlying text keyboard
			popup.dismiss();
		}
	}
	
//________________________________________________________________________________________________________________
	//NOTIFYING_ACTIVITY_FROM_ACTIVITY
	//Activities to notify : Conversation
	void lbmNewNoteMedia(String _imageName) {
		//[jugaar_: CHECKED_BEFORE_BROADCAST__]
		if(AppUtil.isUserOrGroupActiveMessageRead(chatID)) {
			Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_MEDIA);
			//bundle
			Bundle sendMediaBundle = new Bundle();
			sendMediaBundle.putString(IntentKeys.MEDIA_NAME, _imageName);
			sendMediaBundle.putString(IntentKeys.MEDIA_CAPTION, editText.getText().toString());
			sendMediaBundle.putInt(IntentKeys.MSG_KIND, MsgKind.M1_IMAGE);
			sendMediaBundle.putInt(IntentKeys.MEDIA_STATUS, MediaStatus.COMPLETED);
			sendMediaBundle.putString(IntentKeys.MSG_ID, MsgConstant.DEFAULT_MSG_ID());
			in.putExtra(IntentKeys.BUNDLE_SEND_MEDIA, sendMediaBundle);
			LocalBroadcastManager.getInstance(this).sendBroadcast(in);
		}
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	boolean checkMultiplePermissionsSilent() {
		List<String> _permissionList = new ArrayList<>();
		_permissionList.add(Manifest.permission.CAMERA);
		_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		_permissionList.add(Manifest.permission.RECORD_AUDIO);
		return PermissionUtil.checkMultiplePermissionsSilent(this, _permissionList);
	}

//__________________________________________________________________________________________________
	void initializeEmojiPopup() {
		// Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
		popup = new EmojiconsPopup(rootView, this);

		//Will automatically set size according to the soft keyboard size
		popup.setSizeForSoftKeyboard();

		//If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				btn_emoji.setImageResource(R.drawable.input_emoji_white);
			}
		});

		//If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
			@Override
			public void onKeyboardOpen(int keyBoardHeight) {
			}

			@Override
			public void onKeyboardClose() {
				if (popup.isShowing())
					popup.dismiss();
			}
		});

		//On emoji clicked, add it to editText
		popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
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
		popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {
			@Override
			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				editText.dispatchKeyEvent(event);
			}
		});
	}
		
}
