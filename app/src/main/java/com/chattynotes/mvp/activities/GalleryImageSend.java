package com.chattynotes.mvp.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.chattynotes.R;
import com.chattynotes.application.App;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MimeType;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.adapters.gridGalleryImage.AdapterGalleryImageSend;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.media.MediaUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import com.chattynotes.emojicon.EmojiconGridView;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.emojicon.emoji.Emojicon;

public class GalleryImageSend extends AppCompatActivity implements OnItemClickListener {
	//Coming From Activity (1)
		// 1 - GalleryImageGrid

	//Conversation Activity Requirements
	String chatName;
	long chatID;
	
	//selected participant
	ArrayList<String> selectedImagePaths = new ArrayList<>();
	GridView gridview;

	//media captions
	ArrayList<String> mediaCaptionList = new ArrayList<>();

	private ImageLoaderPath imageLoader;
	String thumbPath = "";
	ImageView imgView = null;
	int selectedItem = 0;
	int plusBtnExists = 0;

	EmojiconsPopup popup;
	FrameLayout frameMediaCaption;
	View rootView;
	ConversationTextEntry editText;
	ImageView btn_emoji;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_image_send);

		imageLoader = new ImageLoaderPath(this);

		//if permission go on, else finish the activity not here but at method onRequestPermissionsResult
		boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
		if (checkPermission)
			onCreate___();
	}


	void onCreate___() {
		Bundle bundleImage 	= getIntent().getBundleExtra(IntentKeys.BUNDLE_IMAGE);
		chatName= bundleImage.getString(IntentKeys.CHAT_NAME);
		chatID 	= bundleImage.getLong(IntentKeys.CHAT_ID);
		String[] str 		= bundleImage.getStringArray(IntentKeys.MEDIA_IMAGE_URI_LIST);
	    
		//make a ArrayList<>, from String[]
		selectedImagePaths = new ArrayList<>(Arrays.asList(str != null ? str : new String[0]));

		//set empty media caption for each image
		for(int i=0; i<selectedImagePaths.size(); i++)
			mediaCaptionList.add(MsgConstant.DEFAULT_MEDIA_CAPTION);

		if(selectedImagePaths.size() < AppConst.MAX_IMAGE_AT_ONCE) {//Add Plus Button
			selectedImagePaths.add("R.drawable.ic_add_large");
			//confusion
			//adding plus button will make the selectedImagePaths.size() == AppConst.MAX_IMAGE_AT_ONCE
			//therefore we are doing it with boolean
			plusBtnExists = 1;
		}

		//Toolbar
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery_image_send);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(R.layout.toolbar__2_titles);
			((TextView) findViewById(R.id.toolbar_title_1)).setText(String.format("%s", getString(R.string.send_image)));
			((TextView) findViewById(R.id.toolbar_title_2)).setText(String.format("%s", chatName));
		}
		//display image
		imgView = (ImageView)findViewById(R.id.imgViewPhoto);
		thumbPath = "android.resource://" + App.PACKAGE_NAME + File.separator + R.drawable.media_empty_gallery;
		imageLoader.displayImageElseThumb(selectedImagePaths.get(selectedItem), 0, imgView, thumbPath, 300, 300);
				
		gridview = (GridView) findViewById(R.id.gridView_Send);
		gridview.setFastScrollEnabled(true);
		AdapterGalleryImageSend adapter = new AdapterGalleryImageSend(this, selectedImagePaths, imageLoader);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		gridview.setSelection(0);
		gridview.setSelected(true);

		//media_caption
		frameMediaCaption= (FrameLayout) findViewById(R.id.layoutMediaCaption);
		//makes it visible only if permission is granted
		frameMediaCaption.setVisibility(View.VISIBLE);
		rootView 		= findViewById(R.id.rootView);
		editText 		= (ConversationTextEntry)findViewById(R.id.editText);
		btn_emoji 		= (ImageButton) findViewById(R.id.btnEmoji);
		initializeEmojiPopup();

		editTextListeners();
	}

	//save the caption of each media in mediaCaptionList() as soon as there any change
	void editTextListeners() {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mediaCaptionList.set(selectedItem, editText.getText().toString());
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		if(plusBtnExists==1 && position==selectedImagePaths.size()-1) //plusBtnExists && clicked on last item
			finish();
		else if(selectedItem!=position) { //avoid clicking on same item again and again
			imageLoader.displayImageElseThumb(selectedImagePaths.get(position), 0, imgView, thumbPath, 300, 300);
			view.setPadding(15, 15, 15, 15);
	        view.setBackgroundColor(ContextCompat.getColor(GalleryImageSend.this, R.color.accent));// this is a selected position so make it red
	        parent.getChildAt(selectedItem).setPadding(0, 0, 0, 0);
	        parent.getChildAt(selectedItem).setBackgroundColor(ContextCompat.getColor(GalleryImageSend.this, android.R.color.transparent));
			//set selectedItem here as we want to set the padding 0 for old selected item
	        selectedItem = position;
			doMediaCaptionUIWork();
		}
	}

	void doMediaCaptionUIWork() {
		editText.setText(mediaCaptionList.get(selectedItem));
		editText.setSelection(editText.length());
	}

//__________________________________________________________________________________________________
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			gridview.setNumColumns(10);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			gridview.setNumColumns(5);
		}
	}

//__________________________________________________________________________________________________
	public void onClickBtnSend(View view) {

		imageLoader.clearCache();

		//disable this button
		view.setVisibility(View.GONE);
		view.setEnabled(false);

		//Avoid UI Blocking
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				for(int i=0; i<selectedImagePaths.size()-plusBtnExists; i++) {
					String mediaName =  PathUtil.generateFileNameUnix(MimeType.MEDIA_IMAGE);
					mediaName = mediaName.substring(0, mediaName.length()-1) + i;//substring because '+i' 0-9 was disturbing the timeStamp
					//parsing image_path to URI
					//http://stackoverflow.com/questions/3004713/get-content-uri-from-file-path-in-android
					Uri imageUri = Uri.fromFile(new File(selectedImagePaths.get(i)));
					BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(imageUri);
					//options.outWidth & options.outHeight is image original width and height
					if(options != null && MediaUtil.saveMediaImageToExtStg_Sent(imageUri, mediaName, options.outWidth, options.outHeight)) {//Save cropped,scaled image to external storage
						MediaThumbUtil.saveThumbMediaImage(imageUri, mediaName, chatID, options.outWidth, options.outHeight);
						lbmNewNoteMedia(mediaName, mediaCaptionList.get(i));
					}
				}
				return null;
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);

		setResult(RESULT_CANCELED, new Intent());
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
				inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
				btn_emoji.setImageResource(R.drawable.input_kbd_white);
			}
		} else {//If popup is showing, simply dismiss it to show the underlying text keyboard
			popup.dismiss();
		}
	}

	//NOTIFYING_ACTIVITY_FROM_ACTIVITY
	//Activities to notify : Conversation
	void lbmNewNoteMedia(String imageName, String mediaCaption) {
		//[jugaar_: CHECKED_BEFORE_BROADCAST__]
		if(AppUtil.isUserOrGroupActiveMessageRead(chatID)) {
			Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_MEDIA);
			//bundle
			Bundle sendMediaBundle = new Bundle();
			sendMediaBundle.putString(IntentKeys.MEDIA_NAME, imageName);
			sendMediaBundle.putString(IntentKeys.MEDIA_CAPTION, mediaCaption);
			sendMediaBundle.putInt(IntentKeys.MSG_KIND, MsgKind.M1_IMAGE);
			sendMediaBundle.putInt(IntentKeys.MEDIA_STATUS, MediaStatus.COMPLETED);
			in.putExtra(IntentKeys.BUNDLE_SEND_MEDIA, sendMediaBundle);
			LocalBroadcastManager.getInstance(this).sendBroadcast(in);
		}
	}

//__________________________________________________________________________________________________
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
	protected void onDestroy() {
		imageLoader.clearCache();
		super.onDestroy();
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case PermissionUtil.PERMISSION_GALLERY_IMAGE:
					//LogUtil.e(getClass().getSimpleName(), "onRequestPermissionsResult: PERMISSION_GALLERY_IMAGE");
					onCreate___();
					break;
				default:
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		} else {
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
				if(popup.isShowing())
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
