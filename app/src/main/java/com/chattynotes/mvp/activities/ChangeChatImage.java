package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.customviews.crop_biokys.BiokysCropImage;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.media.MediaUtil;
import com.chattynotes.util.NetworkUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.StorageUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

public class ChangeChatImage extends Activity {
	//PURPOSE_OF_THE_ACTIVITY__
	//CROP THE CHAT IMAGE
	// No need to implement permission as image is stored in PathUtil.getInternalChatImageTempUri()

	//The purpose of this activity is
	//input (2): 
	
	// - WhatsApp Profile Picture Cropping
	// Minimum profile photo size supported is : 192x192
	// It scales profile in square
	
	// - WhatsApp Image Send Cropping
	// Minimum profile photo size supported is : 25x25
		
	private String 	imageName;
	private static final int CROP_IMAGE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_chat_image);
		imageName = getIntent().getStringExtra(IntentKeys.MEDIA_NAME);
		Uri image_uri = Uri.parse(getIntent().getStringExtra(IntentKeys.MEDIA_IMAGE_URI));
		callCropActivity(image_uri);
	}
	
	void callCropActivity(Uri _image_uri) {
		Intent intent = new Intent(this, BiokysCropImage.class);
		intent.putExtra(IntentKeys.MEDIA_IMAGE_URI				, _image_uri.toString());
		//intent.putExtra(getString(R.string.image_output_x)	, 200);
		//intent.putExtra(getString(R.string.image_output_y)	, 200);
		intent.putExtra(IntentKeys.MEDIA_IMAGE_IS_CIRCLE_CROP	, false);
		//intent.putExtra(getString(R.string.image_scale)		, true);
		intent.putExtra(IntentKeys.MEDIA_IMAGE_MIN_BOUND		, MediaUtil.CHAT_IMAGE_MIN_BOUND);
		intent.putExtra(IntentKeys.MEDIA_IMAGE_MAX_BOUND		, MediaUtil.CHAT_IMAGE_MAX_BOUND);
		intent.putExtra(IntentKeys.MEDIA_IMAGE_TEMP_FILE		, PathUtil.createInternalChatImageTempFile().toString()); //path where we want our image to be saved
		startActivityForResult(intent, CROP_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode) {
			case RESULT_CANCELED :
				finish();
				break;
			case RESULT_OK :
				btnOk();
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	

//_______________________________________________________________________________________________________
	void btnOk() {
		//At this point, we have cropped image at PathUtil.getInternalChatImageTempUri
		Uri tempChatImageUri = PathUtil.getInternalChatImageTempUri();
		BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(tempChatImageUri);
		if(options != null) {
			//delete internal old thumb
			StorageUtil.deleteChatThumbIntStg(imageName);
			//save internal new thumb
			MediaThumbUtil.saveThumbChatImage(tempChatImageUri, imageName, options.outWidth, options.outHeight);
			Intent in = new Intent();
			//this is the image path of scaled image .. upload this image
			in.putExtra(IntentKeys.MEDIA_IMAGE_URI, tempChatImageUri.toString());
			setResult(RESULT_OK, in);
			finish();
		} else {
			NetworkUtil.fileNotFoundToast();
			finish();
		}
	}
}
