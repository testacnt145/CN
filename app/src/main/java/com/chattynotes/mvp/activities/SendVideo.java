package com.chattynotes.mvp.activities;

import java.io.File;

import com.chattynoteslite.R;
import com.chattynotes.application.App;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MsgID;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.customviews.VideoSeekBarView;
import com.chattynotes.customviews.VideoTimelineView;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.services.ServiceVideo;
import com.chattynotes.util.AndroidUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.ConversionUtil;
import com.chattynotes.util.ffmpeg.FfmpegUtil;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.NetworkUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.VideoUtil;
import com.chattynotes.util.VideoUtil.VideoMetaData;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import com.chattynotes.emojicon.EmojiconGridView;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.emojicon.emoji.Emojicon;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

import android.support.annotation.NonNull;

//FFMPEG LIBRARY
//http://stackoverflow.com/questions/8620127/maven-in-eclipse-step-by-step-installation

//Input Formats
//https://play.google.com/store/apps/details?id=com.intee.videocutter&hl=en
//https://play.google.com/store/apps/details?id=com.goseet.VidTrim&hl=en

//Texture View : API level 14
//Surface View : API level 1
//Video View   : API level 1

public class SendVideo extends AppCompatActivity implements TextureView.SurfaceTextureListener {
	//Coming From Activity (2)
		// 1 - Conversation  [3] (3rd Party Video App {Photos}, 3rd Party Camera App{Google}, Sharing Video From 3rd Party{Facebook})
		// 2 - GalleryVideoGrid
	
	String msgId;
	//Activity Requirements
	String chatName	= "";
	long chatID;
	//Video
	String 	videoName = "";
	String 	videoPath = "";
	float  	videoSize;
	float  	videoDuration;
	int 	videoWidth = 0;
	int 	videoHeight = 0;
	//video_rotation is taking time as we are fetching it via FFMPEG using AsyncTask
	//but we need video_rotation in order get correct video_rotation 
	//therefore use this boolean isVideoRotationVariableUpdated to ensure we get right rotation 
	Boolean isVideoRotationVariableUpdated = false;
	int 	videoRotation = 0;
	
	
	//Animations
	Animation fade_out;
	
	private MediaPlayer videoPlayer = null;
	private VideoTimelineView videoTimelineView = null;
	private View videoContainerView = null;
	private ImageView playButton = null;
	private VideoSeekBarView videoSeekBarView = null;
	private TextureView textureView = null;
	private View controlView = null;
	private boolean playerPrepared = false;

	private float lastProgress = 0;
	private boolean needSeek = false;
  
	private final Object sync = new Object();
	private Thread __thread = null;
	
	private int resultWidth = 0;
	private int resultHeight = 0;
	private long startTime = 0;
	private long estimatedDuration = 0;
	
	TextView txtViewSize;
	TextView txtViewDuration;
	TextView txtViewTrimInfo;

	EmojiconsPopup popup;
	FrameLayout frameMediaCaption;
	View rootView;
	ConversationTextEntry editText;
	ImageView btn_emoji;
  
//_____________________________________________________________________________________________________________________________
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_video);

		//LogUtil.e(getClass().getSimpleName(), "onCreate");

		//if permission go on, else finish the activity not here but at method onRequestPermissionsResult
		boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_VIDEO);
		if (checkPermission)
			onCreate___();
	}

	void onCreate___() {
		Bundle bundleVideo 	= getIntent().getBundleExtra(IntentKeys.BUNDLE_VIDEO);
		chatName	= bundleVideo.getString(IntentKeys.CHAT_NAME);
		chatID		= bundleVideo.getLong(IntentKeys.CHAT_ID);
		videoName	= bundleVideo.getString(IntentKeys.MEDIA_NAME);
		Uri videoUri= Uri.parse(bundleVideo.getString(IntentKeys.MEDIA_VIDEO_URI));
		
		VideoMetaData videoMetaData = VideoUtil.getVideoMetaDataViaMediaMetadataRetriever(this, videoUri);
		if(videoMetaData != null && (videoMetaData.videoDuration!=0 || videoMetaData.videoSize!=0 || !videoMetaData.videoPath.equals(""))) {
			//video attributes
			videoPath 		=  videoMetaData.videoPath;
			videoSize		=  videoMetaData.videoSize;
			videoDuration	=  videoMetaData.videoDuration;
			//if width and height == null, then giving resolution as 600x600
			//and resolution has no use in New FFMPEG Library
			videoWidth   	=  videoMetaData.videoWidth;
			videoHeight  	=  videoMetaData.videoHeight;
			if(videoPath != null && processOpenVideo())
				init();
			else {
				Toast.makeText(this, "Video is corrupt", Toast.LENGTH_SHORT).show();
				finishCancelled();
			}
		} else {
			Toast.makeText(this, "Video is corrupt", Toast.LENGTH_SHORT).show();
			finishCancelled();
		}
  	}
	
	void init() {
		//Animations
		fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_send_video);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(R.layout.toolbar__2_titles);
			((TextView) findViewById(R.id.toolbar_title_1)).setText(String.format("%s", getString(R.string.send_video)));
			((TextView) findViewById(R.id.toolbar_title_2)).setText(String.format("%s", chatName));
		}
    	  
		txtViewSize = (TextView)findViewById(R.id.size);
		txtViewDuration = (TextView)findViewById(R.id.duration);
		txtViewTrimInfo = (TextView)findViewById(R.id.trim_info);

		videoPlayer = new MediaPlayer();
		videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				Runnable __runnable = new Runnable() {
					@Override
					public void run() {
						onPlayComplete();
					}
				};
				// run on another thread
				App.applicationHandler.post(__runnable);
			}
		});
		videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				playerPrepared = true;
				if (videoTimelineView != null && videoPlayer != null) {
					videoPlayer.seekTo((int) (videoTimelineView.getLeftProgress() * videoDuration));
				}
			}
		});
		try {
			videoPlayer.setDataSource(videoPath);
			videoPlayer.prepareAsync();
		} catch (Exception e) {
			//LogUtil.eException(getClass().getSimpleName(), "Exception", "onCreate -> setDataSource", e.toString());
		}

		videoContainerView = findViewById(R.id.video_holder);
		controlView = findViewById(R.id.video_controls);

		videoTimelineView = (VideoTimelineView) findViewById(R.id.timeline_holder);
		videoTimelineView.setVideoPath(videoPath);
		videoTimelineView.setDelegate(new VideoTimelineView.VideoTimelineViewDelegate() {
			@Override
			public void onLeftProgressChanged(float progress) {
				if (videoPlayer == null || !playerPrepared) {
					return;
				}
				try {
					if (videoPlayer.isPlaying()) {
						videoPlayer.pause();
						playButton.setImageResource(R.drawable.mviewer_videoplay);
					}
					videoPlayer.setOnSeekCompleteListener(null);
					videoPlayer.seekTo((int) (videoDuration * progress));
				} catch (Exception e) {
					//LogUtil.eException(getClass().getSimpleName(), "Exception", "onCreate -> onLeftProgressChanged", e.toString());
				}
				needSeek = true;
				videoSeekBarView.setProgress(videoTimelineView.getLeftProgress());
				updateVideoEditedInfo();
			}

			@Override
			public void onRifhtProgressChanged(float progress) {
				if (videoPlayer == null || !playerPrepared) {
					return;
				}
				try {
					if (videoPlayer.isPlaying()) {
						videoPlayer.pause();
						playButton.setImageResource(R.drawable.mviewer_videoplay);
					}
					videoPlayer.setOnSeekCompleteListener(null);
					videoPlayer.seekTo((int) (videoDuration * progress));
				} catch (Exception e) {
					//LogUtil.eException(getClass().getSimpleName(), "Exception", "onCreate -> onRifhtProgressChanged", e.toString());
				}
				needSeek = true;
				videoSeekBarView.setProgress(videoTimelineView.getLeftProgress());
				updateVideoEditedInfo();
			}
		});


		//LIMIT/STEP SIZE
		//1) check if Video Size is Greater than 16MB
		if(ConversionUtil.bitsToMb(videoSize) > AppConst.MAX_VIDEO_SIZE) {
			//Calculate in %, that Total Videos Size's 16MB
			videoTimelineView.setMaxStepSize(ConversionUtil.calculatePercentageForMaxStepSize(videoSize));//w.r.t 16MB
		}
		videoTimelineView.setMinStepSize(ConversionUtil.calculatePercentageForMinStepSize(videoDuration));//w.r.t 1second
		//This Minimum Step Size will cause problem for videos whose 1 second is > 16MB

		videoSeekBarView = (VideoSeekBarView) findViewById(R.id.seekbar);
		videoSeekBarView.delegate = new VideoSeekBarView.SeekBarDelegate() {
			@Override
			public void onSeekBarDrag(float progress) {
				if (progress < videoTimelineView.getLeftProgress()) {
					progress = videoTimelineView.getLeftProgress();
					videoSeekBarView.setProgress(progress);
				} else if (progress > videoTimelineView.getRightProgress()) {
					progress = videoTimelineView.getRightProgress();
					videoSeekBarView.setProgress(progress);
				}
				if (videoPlayer == null || !playerPrepared) {
					return;
				}
				if (videoPlayer.isPlaying()) {
					try {
						videoPlayer.seekTo((int) (videoDuration * progress));
						lastProgress = progress;
					} catch (Exception e) {
						//LogUtil.eException(getClass().getSimpleName(), "Exception", "onCreate -> onSeekBarDrag", e.toString());
					}
				} else {
					lastProgress = progress;
					needSeek = true;
				}
			}
		};

		playButton = (ImageView) findViewById(R.id.video_control);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				play();
			}
		});

		textureView = (TextureView) findViewById(R.id.video);
		textureView.setSurfaceTextureListener(this);

		updateVideoEditedInfo();

		checkVideoRotation();

		//media_caption
		frameMediaCaption= (FrameLayout) findViewById(R.id.layoutMediaCaption);
		//makes it visible only if permission is granted
		frameMediaCaption.setVisibility(View.VISIBLE);
		rootView 		= findViewById(R.id.rootView);
		editText 		= (ConversationTextEntry)findViewById(R.id.editText);
		btn_emoji 		= (ImageButton) findViewById(R.id.btnEmoji);
		initializeEmojiPopup();
	}
  
//_____________________________________________________________________________________________________________
//It is required because some videos have meta data rotation, example camera videos
  	void checkVideoRotation() {
  		new AsyncTask<Void, Void, Void>() {
  			@Override
  			protected Void doInBackground(Void... params) {
  				/* [bug_: FFMPEG_| not supported in new version]
  				try {
					final File videoFile = new File(videoPath);
					FfmpegController fc = new FfmpegController(SendVideo.this, videoFile);
					Clip clip = fc.getInfo(new Clip(videoPath));
					videoRotation = clip.rotate;
				} catch(Exception ignored) {
  				}*/
				videoRotation = 0;
  				return null;
  			}
  			protected void onPostExecute(Void result) {
  				isVideoRotationVariableUpdated = true;
  				AndroidUtil.checkDisplaySize();
  				fixLayoutInternal();
  			}
  		}.execute();
  	}
  	
//______________________________________________________________________________________________________________ Button Clicks
	public void onClickBtnSend(View view) {
		if(isVideoRotationVariableUpdated)
			btnSendFunctionality(view);
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
  	
  	public void btnSendFunctionality(View view) {
		view.setVisibility(View.GONE);
		File file = new File(videoPath);
		if(file.exists()) {
			try {
				final File outputThumbnail = PathUtil.createInternalMediaThumbFile(chatID, videoName);
				FFmpeg ffmpeg = FfmpegUtil.loadFfmpegBinary(SendVideo.this);
				String[] command = FfmpegUtil.generateThumbnail(videoPath, outputThumbnail.getAbsolutePath(), startTime/1000);
				ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
					@Override
					public void onSuccess(String s) {
					}
					@Override
					public void onFailure(String s) {
						onFailureGenerateThumbnail();
					}
					@Override
					public void onProgress(String s) {
					}
					@Override
					public void onStart() {
					}
					@Override
					public void onFinish() {
					}
				});
			} catch (Exception e) {
				onFailureGenerateThumbnail();
			}
			msgId = MsgConstant.DEFAULT_MSG_ID();
			lbmNewNoteMedia(videoName, editText.getText().toString(), msgId);
			if(ServiceVideo.runningInstanceOfVideoService == null) {
				Intent in = new Intent(this, ServiceVideo.class);
				in.putExtra(IntentKeys.MSG_ID				, msgId);
				in.putExtra(IntentKeys.MEDIA_NAME			, videoName);
				in.putExtra(IntentKeys.MEDIA_VIDEO_PATH		, videoPath);
				in.putExtra(IntentKeys.MEDIA_VIDEO_START_TIME, ConversionUtil.formatDurationForFFMPEG(startTime));// 00:00:00 format
				in.putExtra(IntentKeys.MEDIA_VIDEO_DURATION	, Math.round(Math.floor(estimatedDuration/1000.0))); // seconds format
				in.putExtra(IntentKeys.MEDIA_VIDEO_IS_UPLOAD_ONLY	, false);
				startService(in);
			}
			finishSend();
		} else {
			NetworkUtil.fileNotFoundToast();
			finishCancelled();
		}
	}

	void onFailureGenerateThumbnail() {
		Bitmap bitmap = textureView.getBitmap(MediaThumbUtil.MEDIA_THUMB_WIDTH, MediaThumbUtil.MEDIA_THUMB_HEIGHT);
		MediaThumbUtil.saveThumbMediaVideo(bitmap, videoName, chatID);
	}

	//NOTIFYING_ACTIVITY_FROM_ACTIVITY
  	//Activities to notify : Conversation
  	void lbmNewNoteMedia(String videoName, String mediaCaption, String msgID) {
		//[jugaar_: CHECKED_BEFORE_BROADCAST__]
		if(AppUtil.isUserOrGroupActiveMessageRead(chatID)) {
			Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_MEDIA);
			//bundle
			Bundle sendMediaBundle = new Bundle();
			sendMediaBundle.putString(IntentKeys.MEDIA_NAME, videoName);
			sendMediaBundle.putString(IntentKeys.MEDIA_CAPTION, mediaCaption);
			sendMediaBundle.putInt(IntentKeys.MSG_KIND, MsgKind.M3_VIDEO);
			sendMediaBundle.putInt(IntentKeys.MEDIA_STATUS, MediaStatus.COMPRESSING_MEDIA);
			sendMediaBundle.putString(IntentKeys.MSG_ID, msgID);
			in.putExtra(IntentKeys.BUNDLE_SEND_MEDIA, sendMediaBundle);
			LocalBroadcastManager.getInstance(this).sendBroadcast(in);
		}
  	}
  
//______________________________________________________________________________________________________________
  	@Override
  	public void onConfigurationChanged(Configuration newConfig) {
  		super.onConfigurationChanged(newConfig);
  		AndroidUtil.checkDisplaySize();
  		fixLayoutInternal();
  	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(PermissionUtil.checkPermissionSilent(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
			fixLayoutInternal();
	}
	
//_____________________________________________________________________________________________________________________________	TEXTURE VIEW
	private void setPlayerSurface() {
    	if (textureView == null || !textureView.isAvailable() || videoPlayer == null) {
        	return;
      	}
      	try {
        	Surface s = new Surface(textureView.getSurfaceTexture());
          	videoPlayer.setSurface(s);
          	if (playerPrepared) {
            	videoPlayer.seekTo((int) (videoTimelineView.getLeftProgress() * videoDuration));
        	}
    		} catch (Exception e) {
    	  //LogUtil.eException(getClass().getSimpleName(), "Exception", "setPlayerSurface", e.toString());
    	}
    }
  
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		setPlayerSurface();
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
	  if (videoPlayer == null) {
		  return true;
	  }
	  videoPlayer.setDisplay(null);
	  return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {

	}
	
//________________________________________________________________________________________________________________________
	private void onPlayComplete() {
    	if (playButton != null) {
        	playButton.setImageResource(R.drawable.mviewer_videoplay);
      	}
      	if (videoSeekBarView != null && videoTimelineView != null) {
        	videoSeekBarView.setProgress(videoTimelineView.getLeftProgress());
      	}
      	try {
        	if (videoPlayer != null) {
            	if (videoTimelineView != null) {
                	videoPlayer.seekTo((int) (videoTimelineView.getLeftProgress() * videoDuration));
            }
        }
      	} catch (Exception e) {
    	  //LogUtil.eException(getClass().getSimpleName(), "Exception", "onPlayComplete", e.toString());
    	}
  	}

	private void updateVideoEditedInfo() {
		if (txtViewSize == null) {
			return;
		}

		estimatedDuration = (long)Math.ceil((videoTimelineView.getRightProgress() - videoTimelineView.getLeftProgress()) * videoDuration);
		int estimatedSize = (int) (videoSize * ((float) estimatedDuration / videoDuration));

		if (videoTimelineView.getLeftProgress() == 0) {
			startTime = 0;
	  	} else {
			//startTime = (long) (videoTimelineView.getLeftProgress() * video_duration) * 1000;
			startTime = (long) (videoTimelineView.getLeftProgress() * videoDuration);
	  	}
	  	long endTime;
	  	if (videoTimelineView.getRightProgress() == 1) {
			//endTime = -1;
			endTime = (long) videoDuration;//[jugaar_: VIDEO]
	  	} else {
			//endTime = (long) (videoTimelineView.getRightProgress() * video_duration) * 1000;
		  	endTime = (long) (videoTimelineView.getRightProgress() * videoDuration); //[jugaar_: VIDEO]
	  	}

	  	txtViewSize.setText(String.format("%s", ConversionUtil.formatFileSize(estimatedSize)));//convert bits to MB
	  	txtViewDuration.setText(String.format("%s", ConversionUtil.formatDuration(estimatedDuration)));
		String text = String.format(getString(R.string.trim_duration), ConversionUtil.formatDuration(startTime), ConversionUtil.formatDuration(endTime));
		txtViewTrimInfo.setText(text);

		AndroidUtil.runOnUIThread(new Runnable() {
		  @Override
		  public void run() {
			  txtViewTrimInfo.startAnimation(fade_out);
		  }
	  }, 1);
	}

//____________________________________________________________________________________________________________________________
  	private void fixVideoSize() {
  		int viewHeight = AndroidUtil.displaySize.y - AndroidUtil.statusBarHeight - AndroidUtil.getCurrentActionBarHeight();
  		int width;
  		int height;

  		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
  			width = AndroidUtil.displaySize.x / 3 - AndroidUtil.dp(24);
  			height = viewHeight - AndroidUtil.dp(32);
  		} else {
  			width = AndroidUtil.displaySize.x;
  			height = viewHeight - AndroidUtil.dp(276);
  		}

  		//used to rotate the video
  		int vwidth 	= videoRotation == 90 || videoRotation == 270 ? videoHeight : videoWidth;
  		int vheight = videoRotation == 90 || videoRotation == 270 ? videoWidth  : videoHeight;
  		float wr = (float) width / (float) vwidth;
  		float hr = (float) height / (float) vheight;
  		float ar = (float) vwidth / (float) vheight;

  		if (wr > hr) {
  			width = (int) (height * ar);
  		} else {
  			height = (int) (width / ar);
  		}

  		if (textureView != null) {
  			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textureView.getLayoutParams();
  			layoutParams.width = width;
  			layoutParams.height = height;
  			layoutParams.leftMargin = 0;
  			layoutParams.topMargin = 0;
  			textureView.setLayoutParams(layoutParams);
  		}
  	}

  	private void fixLayoutInternal() {
  		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
  			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoContainerView.getLayoutParams();
  			layoutParams.topMargin = AndroidUtil.dp(16);
  			layoutParams.bottomMargin = AndroidUtil.dp(16);
  			layoutParams.width = AndroidUtil.displaySize.x / 3 - AndroidUtil.dp(24);
  			layoutParams.leftMargin = AndroidUtil.dp(16);
  			videoContainerView.setLayoutParams(layoutParams);

  			layoutParams = (FrameLayout.LayoutParams) controlView.getLayoutParams();
  			layoutParams.topMargin = AndroidUtil.dp(16);
  			layoutParams.bottomMargin = 0;
  			layoutParams.width = AndroidUtil.displaySize.x / 3 * 2 - AndroidUtil.dp(32);
  			layoutParams.leftMargin = AndroidUtil.displaySize.x / 3 + AndroidUtil.dp(16);
  			layoutParams.gravity = Gravity.TOP;
  			controlView.setLayoutParams(layoutParams);

  		} else {
        
  			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoContainerView.getLayoutParams();
  			layoutParams.topMargin = AndroidUtil.dp(16);
  			layoutParams.bottomMargin = AndroidUtil.dp(260);
  			layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
  			layoutParams.leftMargin = 0;
  			videoContainerView.setLayoutParams(layoutParams);

  			layoutParams = (FrameLayout.LayoutParams) controlView.getLayoutParams();
  			layoutParams.topMargin = 0;
  			layoutParams.leftMargin = 0;
  			layoutParams.bottomMargin = AndroidUtil.dp(150);
  			layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
  			layoutParams.gravity = Gravity.BOTTOM;
  			controlView.setLayoutParams(layoutParams);

  		}
  		fixVideoSize();
  		videoTimelineView.clearFrames();
  	}

  	private void play() {
  		if (videoPlayer == null || !playerPrepared) {
  			return;
  		}
  		if (videoPlayer.isPlaying()) {
  			videoPlayer.pause();
  			playButton.setImageResource(R.drawable.mviewer_videoplay);
  		} else {
  			try {
  				playButton.setImageDrawable(null);
  				lastProgress = 0;
  				if (needSeek) {
  					videoPlayer.seekTo((int) (videoDuration * videoSeekBarView.getProgress()));
  					needSeek = false;
  				}
  				videoPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
  					@Override
  					public void onSeekComplete(MediaPlayer mp) {
  						float startTime = videoTimelineView.getLeftProgress() * videoDuration;
  						float endTime = videoTimelineView.getRightProgress() * videoDuration;
  						if (startTime == endTime) {
  							startTime = endTime - 0.01f;
  						}
  						lastProgress = (videoPlayer.getCurrentPosition() - startTime) / (endTime - startTime);
  						float lrdiff = videoTimelineView.getRightProgress() - videoTimelineView.getLeftProgress();
  						lastProgress = videoTimelineView.getLeftProgress() + lrdiff * lastProgress;
  						videoSeekBarView.setProgress(lastProgress);
  					}
  				});
  				videoPlayer.start();
  				synchronized (sync) {
  					if (__thread == null) {
  						__thread = new Thread(__runnable);
  						__thread.start();
  					}
  				}
  			} catch (Exception e) {
  				//LogUtil.eException(getClass().getSimpleName(), "Exception", "play", e.toString());
  			}
  		}
  	}

  	private boolean processOpenVideo() {
  		if (resultWidth > 640 || resultHeight > 640) {
  			float scale = resultWidth > resultHeight ? 640.0f / resultWidth : 640.0f / resultHeight;
  			resultWidth *= scale;
  			resultHeight *= scale;
  		}
  		updateVideoEditedInfo();
  		return true;
  	}
  
  
//_____________________________________________________________________________________________________________________________	
  	private Runnable __runnable = new Runnable() {
  		@Override
  		public void run() {
          boolean playerCheck;

          while (true) {
              synchronized (sync) {
                  try {
                      playerCheck = videoPlayer != null && videoPlayer.isPlaying();
                  } catch (Exception e) {
                      playerCheck = false;
                      //LogUtil.eException(getClass().getSimpleName(), "Exception", "progressRunnable -1", e.toString());
                  }
              }
              if (!playerCheck) {
                  break;
              }
              
              Runnable __runnable = new Runnable() {
                  @Override
                  public void run() {
                      if (videoPlayer != null && videoPlayer.isPlaying()) {
                          float startTime = videoTimelineView.getLeftProgress() * videoDuration;
                          float endTime = videoTimelineView.getRightProgress() * videoDuration;
                          if (startTime == endTime) {
                              startTime = endTime - 0.01f;
                          }
                          float progress = (videoPlayer.getCurrentPosition() - startTime) / (endTime - startTime);
                          float lrdiff = videoTimelineView.getRightProgress() - videoTimelineView.getLeftProgress();
                          progress = videoTimelineView.getLeftProgress() + lrdiff * progress;
                          if (progress > lastProgress) {
                              videoSeekBarView.setProgress(progress);
                              lastProgress = progress;
                          }
                          if (videoPlayer.getCurrentPosition() >= endTime) {
                              try {
                                  videoPlayer.pause();
                                  onPlayComplete();
                              } catch (Exception e) {
                            	//LogUtil.eException(getClass().getSimpleName(), "Exception", "progressRunnable -2", e.toString());
                              }
                          }
                      }
                  }
              };
              // run on another thread
	          App.applicationHandler.post(__runnable);

              try {
                  Thread.sleep(50);
              } catch (Exception e) {
            	  //LogUtil.eException(getClass().getSimpleName(), "Exception", "progressRunnable -3", e.toString());
              }
          }
          synchronized (sync) {
        	  __thread = null;
          }
      }
  };

//_____________________________________________________________________________________________________________________________
	@Override
	public void onDestroy() {
		__thread = null;
		
		if (videoTimelineView != null) {
			videoTimelineView.destroy();
		}
		if (videoPlayer != null) {
			try {
				videoPlayer.stop();
				videoPlayer.release();
				videoPlayer = null;
			} catch (Exception e) {
				//LogUtil.eException(getClass().getSimpleName(), "Exception", "onDestroy", e.toString());
            }
		}
		super.onDestroy();
	}
	
//____________________________________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish(); //*just go to back activity
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//____________________________________________________________________________________________________________________ FINISH
	void finishSend() {
		setResult(RESULT_OK, new Intent());
		finish();
	}

	void finishCancelled() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case PermissionUtil.PERMISSION_GALLERY_VIDEO:
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
