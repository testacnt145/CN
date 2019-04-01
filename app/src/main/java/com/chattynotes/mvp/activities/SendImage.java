package com.chattynotes.mvp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import java.io.File;
import com.chattynoteslite.R;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.media.MediaUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.AndUriUtil;
import com.chattynotes.util.AppUtil;
import android.support.annotation.NonNull;

public class SendImage extends AppCompatActivity {
	//PURPOSE_OF_THE_ACTIVITY__
	//this activity is used to send SINGLE image via '3rd party', 'Share apps' and 'Camera'
	//Coming From Activity (1)
		// 1 - Conversation  [3] (3rd Party Image App {Photos}, 3rd Party Camera App{Retrica}, Sharing Image From 3rd Party{Facebook})
	
	Uri imageUri;
	String imageName;
	String chatName = "";
	long chatID;

	EmojiconsPopup popup;
	FrameLayout frameMediaCaption;
	View rootView;
	ConversationTextEntry editText;
	ImageView btn_emoji;

	private ImageLoaderPath imageLoader;
	ImageView imgView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_image);

		//if permission go on, else finish the activity not here but at method onRequestPermissionsResult
		boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
		if (checkPermission)
			onCreate___();
	}

	void onCreate___() {
		Bundle bundleImage = getIntent().getBundleExtra(IntentKeys.BUNDLE_IMAGE);
		chatName = bundleImage.getString(IntentKeys.CHAT_NAME);
		chatID = bundleImage.getLong(IntentKeys.CHAT_ID);
		imageName = bundleImage.getString(IntentKeys.MEDIA_NAME);
		imageUri = Uri.parse(bundleImage.getString(IntentKeys.SHARE_MSG_IMAGE_URI));

		//This is a uri scheme 'content', and we only displays image with 'file' uri so convert
		if (imageUri!=null && imageUri.getScheme()!=null) {
			if(imageUri.getScheme().equals("content")) {
				String p = AndUriUtil.getRealPathFromURI(this, imageUri);
				if (p != null) {
					File f = new File(p);
					imageUri = Uri.fromFile(f);
				}
			}
		}

		final Toolbar toolbar = findViewById(R.id.toolbar_send_image);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(R.layout.toolbar__2_titles);
			((TextView) findViewById(R.id.toolbar_title_1)).setText(getString(R.string.send_image));
			((TextView) findViewById(R.id.toolbar_title_2)).setText(String.format("%s", chatName));
		}

		//display image
		imgView = findViewById(R.id.image);
		imageLoader = new ImageLoaderPath(this);
		imageLoader.displayImage(imageUri.getPath(), ImageLoaderPath.DECODE_IMAGE_FROM_FILE, imgView, MediaUtil.MEDIA_IMAGE_TO_LOAD_WIDTH, MediaUtil.MEDIA_IMAGE_TO_LOAD_HEIGHT);
        //media_caption
		frameMediaCaption= findViewById(R.id.layoutMediaCaption);
		//makes it visible only if permission is granted
		frameMediaCaption.setVisibility(View.VISIBLE);
        rootView 		= findViewById(R.id.rootView);
        editText 		= findViewById(R.id.editText);
        btn_emoji 		= (ImageButton) findViewById(R.id.btnEmoji);
        initializeEmojiPopup();
	}

//________________________________________________________________________________________________________________
	public void onClickBtnSend(View view) {
		imageLoader.clearCache();
		view.setVisibility(View.GONE);
		view.setEnabled(false);
		BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(imageUri);
		if(options != null && MediaUtil.saveMediaImageToExtStg_Sent(imageUri, imageName, options.outWidth, options.outHeight)) { //Save cropped,scaled image to external storage
			MediaThumbUtil.saveThumbMediaImage(imageUri, imageName, chatID, options.outWidth, options.outHeight);
			lbmNewNoteMedia(imageName);
		} else
			Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
		finish();
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
				assert inputMethodManager != null;
				inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                btn_emoji.setImageResource(R.drawable.input_kbd_white);
            }
        } else {//If popup is showing, simply dismiss it to show the underlying text keyboard
            popup.dismiss();
        }
    }

//__________________________________________________________________________________________________
	//NOTIFYING_ACTIVITY_FROM_ACTIVITY
	//Activities to notify : Conversation
	void lbmNewNoteMedia(String imageName) {
		//[jugaar_: CHECKED_BEFORE_BROADCAST__]
		if(AppUtil.isUserOrGroupActiveMessageRead(chatID)) {
			Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_MEDIA);
			//bundle
			Bundle sendMediaBundle = new Bundle();
			sendMediaBundle.putString(IntentKeys.MEDIA_NAME, imageName);
			sendMediaBundle.putString(IntentKeys.MEDIA_CAPTION, editText.getText().toString());
			sendMediaBundle.putInt	 (IntentKeys.MSG_KIND, MsgKind.M1_IMAGE);
			sendMediaBundle.putInt(IntentKeys.MEDIA_STATUS, MediaStatus.COMPLETED);
			sendMediaBundle.putString(IntentKeys.MSG_ID, MsgConstant.DEFAULT_MSG_ID());
			in.putExtra(IntentKeys.BUNDLE_SEND_MEDIA, sendMediaBundle);
			LocalBroadcastManager.getInstance(this).sendBroadcast(in);
		}
	}

//________________________________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case PermissionUtil.PERMISSION_GALLERY_IMAGE:
					onCreate___();
					break;
				default:
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		} else {
			//LogUtil.e(getClass().getSimpleName(), "onRequestPermissionsResult: Permission was not granted");
			Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

//__________________________________________________________________________________________________
	void initializeEmojiPopup() {
		// Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
		popup = new EmojiconsPopup(rootView, this);

		//Will automatically set size according to the soft keyboard size
		popup.setSizeForSoftKeyboard();

		//If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(() -> btn_emoji.setImageResource(R.drawable.input_emoji_white));

		//If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
			@Override
			public void onKeyboardOpen(int keyBoardHeight) {
			}

			@Override
			public void onKeyboardClose() {
				if(popup.isShowing())
					popup.dismiss();
			}
		});

		//On emoji clicked, add it to editText
		popup.setOnEmojiconClickedListener(emojicon -> {
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
        });

		//On backspace clicked, emulate the KEYCODE_DEL key event
		popup.setOnEmojiconBackspaceClickedListener(v -> {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        });
	}
		
}
