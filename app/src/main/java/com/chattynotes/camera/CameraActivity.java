package com.chattynotes.camera;

import com.chattynotes.mvp.activities.MainActivity;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MimeType;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.util.AndroidUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.media.MediaUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.ShortcutUtil;
import com.chattynotes.util.storage.StorageUtil;
import com.chattynotes.util.VideoUtil;
import com.chattynotes.R;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;

@SuppressWarnings("deprecation")
public class CameraActivity extends Activity implements MediaRecorder.OnInfoListener {


	Boolean _longClick = false;
	boolean cameraFront = false;
	int flashMode = 0; // 0=off, 1=on, 2=auto
	boolean recording = false;
	boolean mScreenOrientationLocked = false;
	boolean IS_FRONT_CAMERA_ON;

	CameraPreview mCameraPreview;
	Camera camera;
	Parameters parameters;
	PictureCallback mPicture;
	CamcorderProfile profile;

	MediaRecorder mediaRecorder;
	CountDownTimer __cDownTimer = null;

	TextView aB_txtViewTitle;
	TextView aB_txtViewTimer;
	ProgressBar aB_progressBar;
	ImageView aB_recordIcon;

	SurfaceView surfaceView;
	ImageButton btn_flash;
	ImageButton btn_capture;

	Formatter mFormatter;
	StringBuilder mFormatBuilder;
	
	//Conversation Activity Requirements
	String chatName;
	long chatID;
	
	String imageName;
	String videoName;


	//detect whether user navigates to this activity via HOME SCREEN (camera clicked) or not
	Boolean __via_HOME_SCREEN = false;
	
//_______________________________________________________________________________________________________________
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreate___();
	}

	void onCreate___() {
		Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
			//Case: When user long press the 'Chatty Notes Camera' to install shortcut
			//just install the shortcut and finish the activity
			ShortcutUtil.createCameraShortcut(this);
			finish();
		} else {
			if (checkMultiplePermissionsSilent()) {
				//2 Cases: make sure to check if app is registered or not
				//1) User coming from application
				//2) user clicked on shortcut that is on Home screen (Show Contact List)

				//_______________________________________________________________________________________________________
				// https://stackoverflow.com/questions/11562051/change-activitys-theme-programmatically
				setTheme(R.style.TransparentThemeGray); // **** set the Theme
				setContentView(R.layout.activity_camera);

				imageName = PathUtil.generateFileNameUnix(MimeType.MEDIA_IMAGE);
				videoName = PathUtil.generateFileNameUnix(MimeType.MEDIA_VIDEO);

				if (intent.hasExtra(IntentKeys.IS_CAMERA_ACCESS_VIA_HOME_SCREEN)) {
					//___________ via Home Screen (Camera Clicked)
					__via_HOME_SCREEN = true;
					if (AppUtil.isRegisteredAndOnChatScreen()) {
						actionBar("Send photo or video");
						imageActionBar();
					} else {
						finish();
						startActivity(new Intent(this, MainActivity.class)); //move to currently available activity (RegEnterNumber, RegVerifyNumber, RegFinal etc)
					}
				} else {
					//____________ Normal Result
					chatName = getIntent().getStringExtra(IntentKeys.CHAT_NAME);
					chatID = getIntent().getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
					actionBar("Send to " + chatName);
					imageActionBar();
				}

				mFormatBuilder = new StringBuilder();
				mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

				btn_capture = (ImageButton) findViewById(R.id.captureBtn);
				btn_flash = (ImageButton) findViewById(R.id.flashBtn);
				surfaceView = (SurfaceView) findViewById(R.id.camera_preview);

				camera = getCameraInstance();
				mCameraPreview = new CameraPreview(this, camera, surfaceView);
				mPicture = getPictureCallback();

				listenersBtnCapture(btn_capture);
			} else{
				Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	// _______________________________
	void actionBar(String title) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false); // Back Button
			actionBar.setDisplayShowHomeEnabled(false); // Image
			actionBar.setDisplayShowTitleEnabled(false);
			//Custom
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(R.layout.action_bar_custom_camera);
			// ______________ custom Layout
			aB_txtViewTitle = (TextView) findViewById(R.id.ab_Title);
			aB_txtViewTimer = (TextView) findViewById(R.id.aB_recordTimer);
			aB_recordIcon = (ImageView) findViewById(R.id.ab_recordIcon);
			aB_progressBar = (ProgressBar) findViewById(R.id.aB_progressBar);
			aB_txtViewTitle.setText(title);
		}
	}

	void imageActionBar() {
		aB_progressBar.setVisibility(View.GONE);
		aB_txtViewTimer.setVisibility(View.GONE);
		aB_recordIcon.setVisibility(View.GONE);
	}

	void videoActionBar() {
		aB_progressBar.setVisibility(View.VISIBLE);
		aB_txtViewTimer.setVisibility(View.VISIBLE);
		aB_recordIcon.setVisibility(View.VISIBLE);
	}

//_______________________________________________________________________________________________________________
	@Override
	protected void onPause() {
		super.onPause();
		if(checkMultiplePermissionsSilent())
			releaseCamera();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(checkMultiplePermissionsSilent())
			imageActionBar();
	}

	public void onResume() {
		super.onResume();
		btn_capture.setVisibility(View.VISIBLE); //on returning back from ImagePreviewActivity
		if (checkMultiplePermissionsSilent())
			onResume___();
	}

	void onResume___() {
		btn_capture.setBackgroundResource(R.drawable.btn_shutter_default);
		if (!hasCamera(this)) {
			Toast.makeText(this, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG).show();
			finish();
		}
		if (camera == null) {
			// if the front facing camera does not exist
			if (findFrontFacingCamera() < 0) {
				Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
			}
			if (IS_FRONT_CAMERA_ON) {
				camera = Camera.open(findFrontFacingCamera());
				btn_flash.setVisibility(View.INVISIBLE);
			} else {
				camera = Camera.open(findBackFacingCamera());
				btn_flash.setVisibility(View.VISIBLE);
			}
			_longClick = false;
			mPicture = getPictureCallback();
			mCameraPreview.refreshCamera(camera);
			unlockScreenOrientation();
		}
	}

//_______________________________________________________________________________________________________________
	void listenersBtnCapture(ImageButton _btn_capture) {

		// Long Click
		_btn_capture.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				_longClick = true;
				v.setBackgroundResource(R.drawable.btn_shutter_video_pressed);
				if (!recording) {
					if (!prepareMediaRecorder()) {
						Toast.makeText(CameraActivity.this, "Camera Failed", Toast.LENGTH_LONG).show();
						closingStuff();
						closingStuffError();
					}

					Runnable __runnable = new Runnable() {
						public void run() {
							try {
								mediaRecorder.start();
								startTimerAndProgress();
								videoActionBar();
							} catch (Exception ignored) {
							}
						}
					};
					// work on UiThread for better performance
					AndroidUtil.runOnUIThread(__runnable);
					recording = true;
					lockScreenOrientation();
				}
				return false;
			}
		});

		// Touch Click
		_btn_capture.setOnTouchListener(new View.OnTouchListener() {

			private Rect rect; // Variable rect to hold the bounds of the view

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());// Construct a Rect of the view's bounds
				}
				if (event.getAction() == (MotionEvent.ACTION_MOVE) && (_longClick)) {
					if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
						Toast.makeText(CameraActivity.this, "Canceled", Toast.LENGTH_LONG).show();
						closingStuff();
						closingStuffError();
					}
				}
				return false;
			}
		});
	}

//_____________________________________________________________________________________________________________ BUTTON CLICKS
	public void btnCapture(View view) {
		btn_capture.setVisibility(View.GONE); // to avoid multiple times clicking crash issue
		if (!_longClick) {
			// do Camera Work
			view.setBackgroundResource(R.drawable.btn_shutter_pressed);
			camera.takePicture(shutterCallback, null, mPicture);
		} else {
			// do Video work
			if (recording) {
				view.setBackgroundResource(R.drawable.btn_shutter_default);
				try {
					doneRecording();
				} catch (Exception e) {
					closingStuff();
					closingStuffError();
				}
			}
		}
	}

	//_______________________________
	public void btnFlash(View view) {
		parameters = camera.getParameters();
		switch (flashMode) {
			case 0:// off
				parameters.setFlashMode(Parameters.FLASH_MODE_ON);
				btn_flash.setBackgroundResource(R.drawable.camera_flash_on_selector);
				flashMode = 1;
				break;
			case 1:// on
				parameters.setFocusMode(Parameters.FLASH_MODE_AUTO);
				btn_flash.setBackgroundResource(R.drawable.camera_flash_auto_selector);
				flashMode = 2;
				break;
			case 2:// auto
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				btn_flash.setBackgroundResource(R.drawable.camera_flash_off_selector);
				flashMode = 0;
				break;
		}
		camera.setParameters(parameters);
	}

	//_______________________________
	public void btnSwitch(View view) {
		// get the number of cameras
		int camerasNumber = Camera.getNumberOfCameras();
		if (camerasNumber > 1) {
			// release the old camera instance
			// switch camera, from the front and the back and vice versa
			releaseCamera();
			chooseCamera();
		} else
			Toast.makeText(this, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG).show();
	}

//_____________________________________________________________________________________________________________ UI HELPER METHODS
	private void lockScreenOrientation() {
		if (!mScreenOrientationLocked) {
			final int orientation = getResources().getConfiguration().orientation;
			final int rotation = getWindowManager().getDefaultDisplay().getOrientation();
			if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
				if (orientation == Configuration.ORIENTATION_PORTRAIT) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				} else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
			} else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270) {
				if (orientation == Configuration.ORIENTATION_PORTRAIT) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
				} else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
				}
			}
			mScreenOrientationLocked = true;
		}
	}

	private void unlockScreenOrientation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		mScreenOrientationLocked = false;
	}

//_____________________________________________________________________________________________________________ CAMERA HELPER METHODS
	private Camera getCameraInstance() {
		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			// cannot get camera or does not exist
		}
		return camera;
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}

	private int findBackFacingCamera() {
		int cameraId = -1;
		// Search for the back facing camera
		// get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		// for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				break;
			}
		}
		return cameraId;
	}

	private boolean hasCamera(Context context) {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA); // check if the device has camera
	}

	public int chooseCamera() {
		// if the camera preview is the front
		int cameraId;
		if (cameraFront) {
			cameraId = findBackFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview
				btn_flash.setVisibility(View.VISIBLE);

				camera = Camera.open(cameraId);
				mPicture = getPictureCallback();
				mCameraPreview.refreshCamera(camera);
				IS_FRONT_CAMERA_ON = false;
			}
		} else {
			cameraId = findFrontFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview
				btn_flash.setVisibility(View.INVISIBLE);
				camera = Camera.open(cameraId);
				mPicture = getPictureCallback();
				mCameraPreview.refreshCamera(camera);
				IS_FRONT_CAMERA_ON = true;
			}
		}
		return cameraId;
	}

	private PictureCallback getPictureCallback() {
		return new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				//playSound();
				Uri cameraImageUri = PathUtil.createCameraMediaImageUri(imageName);
				//File cameraImageFile = new File(cameraImageUri.getPath());
				if(MediaUtil.saveCustomCameraImageWithCorrectRotation(data, cameraImageUri, cameraFront))
					callImagePreviewActivity(cameraImageUri);
				else {
					Toast.makeText(CameraActivity.this, "Error taking image, Camera failed", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		};
	}

	void callImagePreviewActivity(Uri _uri) {
		Intent in = new Intent(this, ImagePreviewActivity.class);
		Bundle bundleImage = new Bundle();
		if(!__via_HOME_SCREEN) {
			bundleImage.putString(IntentKeys.CHAT_NAME, chatName);
			bundleImage.putLong(IntentKeys.CHAT_ID, chatID);
		} else
			bundleImage.putBoolean(IntentKeys.IS_CAMERA_ACCESS_VIA_HOME_SCREEN, true);
		bundleImage.putString(IntentKeys.MEDIA_NAME	, imageName);
		bundleImage.putString(IntentKeys.SHARE_MSG_IMAGE_URI, _uri.toString());
		in.putExtra(IntentKeys.BUNDLE_IMAGE, bundleImage);
		startActivityForResult(in, AppConst.GARBAGE_REQUEST);
	}

	
	void callVideoPreviewActivity() {
		Uri videoUri = VideoUtil.getVideoContentUri(PathUtil.createExternalMediaVideoFile_Sent(videoName));
		Intent in = new Intent(this, VideoPreviewActivity.class);
		Bundle bundleVideo = new Bundle();
		if(!__via_HOME_SCREEN) {
			bundleVideo.putString(IntentKeys.CHAT_NAME, chatName);
			bundleVideo.putLong(IntentKeys.CHAT_ID, chatID);
		} else {
			bundleVideo.putBoolean(IntentKeys.IS_CAMERA_ACCESS_VIA_HOME_SCREEN, true);
        }
		bundleVideo.putString(IntentKeys.MEDIA_NAME, videoName);
		bundleVideo.putString(IntentKeys.MEDIA_VIDEO_URI, videoUri.toString());
		in.putExtra(IntentKeys.BUNDLE_VIDEO, bundleVideo);
		startActivityForResult(in, AppConst.GARBAGE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent != null)//means,  either Send or Cancel is pressed
			if(resultCode == RESULT_CANCELED) {
				//LogUtil.e(getClass().getSimpleName(), "RESULT_CANCELED");
				Intent in = new Intent();
				setResult(RESULT_CANCELED, in);
				finish();
			}
	}

//_____________________________________________________________________________________________________________ MEDIA RECORDER
	private boolean prepareMediaRecorder() {
		mediaRecorder = new MediaRecorder();
		camera.unlock();
		mediaRecorder.setCamera(camera);
		mediaRecorder.setOnInfoListener(this);
		//check for the audio permission to record video with sound
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
		mediaRecorder.setProfile(profile);
		mediaRecorder.setVideoEncodingBitRate(AppConst.MAX_CAM_VID_ENCODING_BIT_RATE);
		if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			if (cameraFront) {
				mediaRecorder.setOrientationHint(270);
			} else {
				mediaRecorder.setOrientationHint(90);// plays the video
			}
		} else {
			if (cameraFront) {
				mediaRecorder.setOrientationHint(180);
			} else {
				mediaRecorder.setOrientationHint(180);
			}
		}
		Uri cameraVideoUri = PathUtil.createCameraMediaVideoUri(videoName);
		File cameraVideoFile = new File(cameraVideoUri.getPath());
		mediaRecorder.setOutputFile(cameraVideoFile.getAbsolutePath());
		// mediaRecorder.setMaxDuration(120000); //Set max duration 2 minutes
		mediaRecorder.setMaxFileSize(AppConst.MAX_CAM_VIDEO_FILE_SIZE);
		aB_progressBar.setProgress(0);
		aB_progressBar.setMax(AppConst.MAX_CAM_VIDEO_FILE_SIZE);
		mediaRecorder.setPreviewDisplay(CameraPreview.mSurfaceHolder.getSurface());

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED)
			doneRecording();
	}

// ____________________________________________________________________________________________________________
	void doneRecording() {
		closingStuff();
		callVideoPreviewActivity();
	}

	void closingStuff() {
		btn_capture.setBackgroundResource(R.drawable.btn_shutter_default);

		if (mediaRecorder != null)
			try {
				mediaRecorder.stop();
			} catch (Exception ignored) {
			}

		releaseMediaRecorder();
		releaseCamera();
		unlockScreenOrientation();
		recording = false;
		_longClick = false;
		__cDownTimer.cancel();
		__cDownTimer = null;
	}

	void closingStuffError() {
		StorageUtil.deleteMediaVideoExtStg(videoName);
		finish();
	}

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); //clear recorder configuration
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	private void releaseCamera() {
		if (camera != null) {
			camera.lock();
			camera.release();
			camera = null;
		}
	}

//_____________________________________________________________________________________________________________ OTHER HELPER METHODS
	public String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		mFormatBuilder.setLength(0);
		if (hours > 0)
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		else
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
	}

	void startTimerAndProgress() {
		if (__cDownTimer == null) {
			__cDownTimer = new CountDownTimer(AppConst.MAX_CAM_VID_TIME, 500) { // run timer to 1 hour, and update every half second
				float count = 0;
				Uri cameraVideoUri = PathUtil.createCameraMediaVideoUri(videoName);
				File cameraVideoFile = new File(cameraVideoUri.getPath());

				@Override
				public void onTick(long millisUntilFinished) {
					aB_progressBar.setProgress((int) cameraVideoFile.length());
					count += 0.5;
					aB_txtViewTimer.setText(stringForTime(((int) count) * 1000));
				}

				@Override
				public void onFinish() {
				}
			};
			__cDownTimer.start();
		}
	}
	
	
//_______________________________________________________________________________________________________________________ CAMERA SOUND
	//https://stackoverflow.com/questions/2364892/how-to-play-native-camera-sound-on-android
	//not working in HUAWEI API 14
//	MediaPlayer mp = null;
//	public void playSound() {
//	    AudioManager meng = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//	    int volume = meng.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
//	    if (volume != 0) {
//	        if (mp == null) {
//	        	mp = MediaPlayer.create(this, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
//	        	mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//	        		public void onCompletion(MediaPlayer player) {
//	        			mp.stop();
//	        			mp.release();
//	        			mp = null;       
//	        		}
//	        	});
//	        }
//	        if(mp!=null)
//	        	mp.start();
//		}
//	}
	
	//https://stackoverflow.com/questions/10891742/android-takepicture-not-making-sound
	private final ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };


//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	boolean checkMultiplePermissionsSilent() {
		List<String> _permissionList = new ArrayList<>();
		_permissionList.add(Manifest.permission.CAMERA);
		_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		_permissionList.add(Manifest.permission.RECORD_AUDIO);
		return PermissionUtil.checkMultiplePermissionsSilent(this, _permissionList);
	}
}
