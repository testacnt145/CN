package com.chattynotes.mvp.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chattynoteslite.R;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.Msg;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.IntentUtil;
import com.chattynotes.util.media.MediaUtil;
import com.chattynotes.util.NetworkUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.StorageUtil;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import android.support.annotation.NonNull;

import io.realm.Realm;

public class ChatImageViewer extends AppCompatActivity {
	//PURPOSE_OF_THE_ACTIVITY__
	//1- SHOWS THE PROFILE PHOTO (EITHER SINGLE OR GROUP) FROM EXTERNAL STORAGE,
	// 			if storage permission not granted (show thumb)

	//Coming From Activity (2)
		// 1 - Info
		// 2 - AdapterChat

	long chatID;
	String chatName;
	private Uri cameraUri;
	private final int CHAT_IMAGE_REQUEST = 1;
	private final int CROP_CHAT_IMAGE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_image_viewer);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat_image_viewer);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		Bundle infoBundle = getIntent().getBundleExtra(IntentKeys.BUNDLE_INFO);
		if (infoBundle != null) {
			chatName = infoBundle.getString(IntentKeys.CHAT_NAME);
			chatID = infoBundle.getLong(IntentKeys.CHAT_ID);
		}
		ImageView imgView = (ImageView) findViewById(R.id.image);
		if(AppUtil.isChatWithChattyNotes(chatID))
			imgView.setImageResource(R.mipmap.icon_hd);
		else {
			if ((PathUtil.getInternalChatImageFile(String.valueOf(chatID)).exists())) {
				ImageLoaderPath imageLoader = new ImageLoaderPath(this);
				String external = PathUtil.getExternalChatImageUri(String.valueOf(chatID)).toString();
				String internal = PathUtil.getInternalChatImageUri(String.valueOf(chatID)).toString();
				imageLoader.displayImageElseThumb(external, 0, imgView, internal, 300, 300);
			} else
				imgView.setImageResource(R.drawable.avatar_large);
		}
    }

//__________________________________________________________________________________________________
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!AppUtil.isChatWithChattyNotes(chatID))
			getMenuInflater().inflate(R.menu.menu_chat_image_viewer, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} else if (id == R.id.menu_change_chat_image) {
			List<String> _permissionList = new ArrayList<>();
			_permissionList.add(Manifest.permission.CAMERA);
			_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			boolean checkPermission = PermissionUtil.checkMultiplePermissions(this, _permissionList, getString(R.string.permission_change_chat_image_request), getString(R.string.permission_change_chat_image), R.drawable.permission_cam, R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_MULTIPLE_CHAT_IMAGE);
			if (checkPermission)
				btnEditImage();
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}

//__________________________________________________________________________________________________
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			if(requestCode == CHAT_IMAGE_REQUEST) {
				if(data == null || data.getData() == null)//in case of default camera application
					fromCamera();
				else if(data.getData() != null)//therefore this line will cause exception, if(data == null) is not handled
			       	fromGallery(data.getData());
				/*
				if(data == null)
			    {
			    	fromCamera(); //default CAMERA APP, always here
			    }
			    else if(data.getData() != null)
			    {
			    	if(data.getAction()!= null)
			        {
			        	fromCamera();//Never use
			        }
			        else
			        {
			        	fromGallery(data.getData());
			        }
			    }*/
				
			}
			else if(requestCode == CROP_CHAT_IMAGE) {
				updateChatImage();
			}
		}
	}

	public void btnEditImage() {
		//**** for external storage
		//Save image here instead of camera default folder
		//String _path = PathUtil.FOLDER_PATH_CHAT_IMAGE + File.separator + image_name + MimeType.IMAGE;
		//save image with name "CameraImage.jpg", so it may not replace the real file until uploading is completed
		cameraUri = PathUtil.createCameraChatImageUri();
		Intent intent = IntentUtil.profileImagePickerList(this, cameraUri);
		if (intent != null)
			startActivityForResult(intent, CHAT_IMAGE_REQUEST);
		else
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
	}

	void fromCamera() {
		//LogUtil.e(getClass().getSimpleName(), "fromCamera");
		//**** for external storage
    	//1
    	//Bitmap srcBmp = (Bitmap) data.getExtras().get("data");
    	//imgBtnDP.setImageBitmap(scaleBitmap(srcBmp));
    	//2
    	//imgBtnDP.setImageURI(outputFileUri);
    	//3
		//imagePath = PathUtil.FOLDER_PATH_CHAT_IMAGE + File.separator + PHONE_NUMBER + MimeType.IMAGE;
    	//final File file = new File(_path);
    	//Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
    	//imgBtnDP.setImageBitmap(bitmap);
    	 //4
    	/*Uri selectedImage = data.getData();
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String galleryPath = c.getString(columnIndex);
        c.close();
        
        //Error is some devices
        //google => android nexus 4 camera image null pointer
        //http://stackoverflow.com/questions/20536603/null-pointer-after-capturing-image-using-android-camera
        */
		
		//pass this image , so that it can be cropped , scaled down and saved in the external storage
		//openCropActivity(cameraUri.getPath());
		openCropActivity(cameraUri);
	}
	
	void fromGallery(Uri _uri) {
// [todo_:  PATH-2-URI]
//		String _path;
//		try {
//			String[] filePath = { MediaStore.Images.Media.DATA };
//	        Cursor c = getContentResolver().query(_uri, filePath, null, null, null);
//	        c.moveToFirst();
//	        int columnIndex = c.getColumnIndex(filePath[0]);
//	        _path = c.getString(columnIndex);
//	        c.close();
//	        //LogUtil.e("Gallery image_path", _path);
//		} catch(Exception e) {
//			LogUtil.eException(getClass().getSimpleName(), "Exception", "fromGallery", e.toString());
//			_path = _uri.getPath();//must be any camera image
//		}
        
		//pass this image , so that it can be cropped , scaled down and saved in the external storage
		openCropActivity(_uri);
	}
	
	void openCropActivity(Uri _image_uri) {
		BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(_image_uri);
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
				Intent i = new Intent(this, ChangeChatImage.class);
		        i.putExtra(IntentKeys.MEDIA_IMAGE_URI, _image_uri.toString());
		        i.putExtra(IntentKeys.MEDIA_NAME, String.valueOf(chatID));
		        startActivityForResult(i, CROP_CHAT_IMAGE);
			}
		} else {
			NetworkUtil.fileNotFoundToast();
			finish();
		}
	}

//______________________________________________________________________________________________
	void updateChatImage() {
		StorageUtil.copyChatImageExtStg(String.valueOf(chatID));
		StorageUtil.deleteChatThumbTempIntStg();

		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		String msg = Msg._CHAT_IMAGE_CHANGE;
		long msgTimestamp = MsgConstant.DEFAULT_MSG_TIMESTAMP();
		Notes note = new Notes();
		note.setNoteId(realmQuery.getIncrementedNoteId());
		note.setChatId(chatID);
		note.setMsg(msg);
		note.setMsgKind(MsgKind._CHANGE_IMAGE);
		note.setMsgTimestamp(msgTimestamp);
		note.setMediaStatus(MediaStatus.COMPLETED);
		Chats chat = new Chats();
		chat.setChatId(chatID);
		chat.setChatName(chatName);
		chat.setNote(note);
		realmQuery.insertNote(note);
		realm.close();
		lbmNewNoteChat(chat);
		lbmNewNoteConversation(note);
		lbmChangeImage();
		Toast.makeText(this, "Chat image successfully changed", Toast.LENGTH_LONG).show();
		finish();

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
				} else {//all permissions granted
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


	//__________________________________________________________________________________________________
	void lbmChangeImage() {
		Intent in = new Intent(IntentKeys.LBM_CHANGE_IMAGE);
		in.putExtra(IntentKeys.CHAT_ID, chatID);
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
