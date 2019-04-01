package com.chattynotes.camera;

import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MimeType;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MsgID;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.ShareUtil;
import com.chattynotes.util.ffmpeg.FfmpegUtil;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.NetworkUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.StorageUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import com.chattynoteslite.R;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.chattynotes.emojicon.EmojiconGridView;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.emojicon.emoji.Emojicon;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import android.annotation.SuppressLint;

public class VideoPreviewActivity extends AppCompatActivity {
    //PURPOSE_OF_THE_ACTIVITY__
	//this activity is used to send video via Custom Camera
	//Coming From Activity (1)
		// 1 - CameraActivity

    /* not trimming the camera video */

	VideoView videoView;
    ImageButton btn_play;
    ProgressBar progressBar;
    TextView txtViewCurrentTime, txtViewEndTime;
    
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    
    boolean	isPlaying = false;
    //Activity Requirements
	String chatName = "";
	long chatID;
	//Video
	String 	videoName = "";
	Uri	videoUri = null;
	
	Thread __thread;
	
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
        setContentView(R.layout.activity_camera_video_preview);

		if (checkMultiplePermissionsSilent())
			onCreate___();
		else {
			Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	//RELEASE_BUILD__
	@SuppressLint("WrongViewCast")
	void onCreate___() {

		Bundle bundleVideo = getIntent().getBundleExtra(IntentKeys.BUNDLE_VIDEO);
		videoName = bundleVideo.getString(IntentKeys.MEDIA_NAME);
		videoUri = Uri.parse(bundleVideo.getString(IntentKeys.MEDIA_VIDEO_URI));

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_send_video);
		setSupportActionBar(toolbar);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(R.layout.toolbar__2_titles);

			TextView t1 = (TextView) findViewById(R.id.toolbar_title_1);
			TextView t2 = (TextView) findViewById(R.id.toolbar_title_2);
			t1.setText(getString(R.string.send_video));

			if(bundleVideo.getBoolean(IntentKeys.IS_CAMERA_ACCESS_VIA_HOME_SCREEN)) {
				__via_HOME_SCREEN = true;
				t2.setVisibility(View.GONE);
			} else {
				chatName = bundleVideo.getString(IntentKeys.CHAT_NAME);
				chatID = bundleVideo.getLong(IntentKeys.CHAT_ID);
				t2.setText(chatName);
			}
		}

		videoView = (VideoView) findViewById(R.id.videoView);
		txtViewEndTime = (TextView) findViewById(R.id.txtViewEndTime);
		txtViewCurrentTime = (TextView) findViewById(R.id.txtViewCurrentTime);
		progressBar = (ProgressBar) findViewById(R.id.seekBar); //RELEASE_BUILD__
		btn_play = (ImageButton) findViewById(R.id.btnPlay);

		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

		try {
			videoView.setVideoURI(videoUri);
			videoView.requestFocus();
			if (progressBar != null)
				if (progressBar instanceof SeekBar) {
					SeekBar seeker = (SeekBar) progressBar;
					seeker.setOnSeekBarChangeListener(mSeekListener);
					videoView.setOnCompletionListener(compListener);
				}
		} catch (Exception e) {
			setResult(RESULT_CANCELED, new Intent());
			finish();
		}


		//media_caption
		frameMediaCaption= (FrameLayout) findViewById(R.id.layoutMediaCaption);
		//makes it visible only if permission is granted
		frameMediaCaption.setVisibility(View.VISIBLE);
		rootView 		= findViewById(R.id.rootView);
		editText 		= (ConversationTextEntry)findViewById(R.id.editText);
		btn_emoji 		= (ImageButton) findViewById(R.id.btnEmoji);
		initializeEmojiPopup();
	}

//______________________________________________________________________________________________________________ Button Clicks
  	public void onClickBtnSend(View view) {
  		if(!__via_HOME_SCREEN) {
            view.setVisibility(View.GONE);
            File videoFile = PathUtil.createExternalMediaVideoFile_Sent(videoName);
			String videoPath = videoFile.getAbsolutePath();
            if(videoFile.exists()) {
                try {
                    final File outputThumbnail = PathUtil.createInternalMediaThumbFile(chatID, videoName);
					FFmpeg ffmpeg = FfmpegUtil.loadFfmpegBinary(VideoPreviewActivity.this);
					String[] command = FfmpegUtil.generateThumbnail(videoPath, outputThumbnail.getAbsolutePath(), 1);
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
                lbmNewNoteMedia(videoName, MsgConstant.DEFAULT_MSG_ID(), editText.getText().toString());
                setResult(RESULT_CANCELED, new Intent());
                finish();
            } else {
                NetworkUtil.fileNotFoundToast();
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
  		} else {
			//deal this video as a Shared Video and call the Forward Activity
			setResult(RESULT_CANCELED, new Intent());
			finish();
			ShareUtil.shareMediaCustomCamera(this, videoUri, MimeType.MIME_TYPE_VIDEO);
		}
  	}

  	void onFailureGenerateThumbnail() {
		Bitmap bitmap = null;
		try {
			File videoFile = PathUtil.createExternalMediaVideoFile_Sent(videoName);
			MediaMetadataRetriever media = new MediaMetadataRetriever();
			ParcelFileDescriptor parcel = ParcelFileDescriptor.open(videoFile, ParcelFileDescriptor.MODE_READ_ONLY);
			media.setDataSource(parcel.getFileDescriptor());
			//http://stackoverflow.com/questions/12772547/mediametadataretriever-getframeattime-returns-only-first-frame
			bitmap = media.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST); //it takes microseconds instead of milliseconds
		} catch (Exception ignored) {
		}
		MediaThumbUtil.saveThumbMediaVideo(bitmap, videoName, chatID);
	}

    public void btnPlay(View view) {
    	btn_play.setVisibility(View.INVISIBLE);
    	videoView.start();
    	setProgress();
    	isPlaying = true;
    	progressBar.setMax(videoView.getDuration());
    	startThread();
    }


    void startThread() {
    	Runnable __runnable = new Runnable() {
			@Override
			public void run() {
				//LogUtil.e(getClass().getSimpleName(), "__runnable : " + this.hashCode());
	        	int currentPosition = videoView.getCurrentPosition();
				int total = videoView.getDuration();
				while (videoView != null && currentPosition < total) {
					try {
						Thread.sleep(100);
						currentPosition = videoView.getCurrentPosition();
					} catch (Exception ignored) {
					}
					progressBar.setProgress(currentPosition);
				}
			}
		};
		if(__thread == null) {
    		__thread = new Thread(__runnable);
    		__thread.start();
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

    @Override
    protected void onDestroy() {
    	__thread = null;
    	super.onDestroy();
    }

//___________________________________________________________________________________________________________________
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(isPlaying) {
    		videoView.pause();
    		btn_play.setVisibility(View.VISIBLE);
    		isPlaying = false;
    	}
    	return true;
    }

    private OnCompletionListener compListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			btn_play.setVisibility(View.VISIBLE);
			isPlaying = false;
		}
	};

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
        	videoView.pause();
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
        	try {
        		if (videoView!=null && videoView.isPlaying()) {
        			if (fromuser)
        				videoView.seekTo(progress);
     			   	txtViewCurrentTime.setText(stringForTime(progress));
        		} else
        			progressBar.setProgress(0);
        	} catch (Exception e) {
        		progressBar.setEnabled(false);
        	}
        }

        public void onStopTrackingTouch(SeekBar bar) {
        	if(isPlaying)
        		videoView.start();
        }
    };

//___________________________________________________________________________________________________________________
     private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
    	if (videoView == null) {
            return 0;
        }
        int position = videoView.getCurrentPosition();
        int duration = videoView.getDuration();
        if (progressBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                progressBar.setProgress( (int) pos);
            }
            int percent = videoView.getBufferPercentage();
            progressBar.setSecondaryProgress(percent * 10);
        }
       	txtViewEndTime.setText(stringForTime(duration));
       	txtViewCurrentTime.setText(stringForTime(position));
        return position;
    }

//________________________________________________________________________________________________________________
	//NOTIFYING_ACTIVITY_FROM_ACTIVITY
	// Activities to notify : Conversation
  	void lbmNewNoteMedia(String videoName, String msgID, String mediaCaption) {
		//[jugaar_: CHECKED_BEFORE_BROADCAST__]
		if(AppUtil.isUserOrGroupActiveMessageRead(chatID)) {
			Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_MEDIA);
			//bundle
			Bundle sendMediaBundle = new Bundle();
			sendMediaBundle.putString(IntentKeys.MEDIA_NAME, videoName);
			sendMediaBundle.putString(IntentKeys.MEDIA_CAPTION, mediaCaption);
			sendMediaBundle.putInt(IntentKeys.MSG_KIND, MsgKind.M3_VIDEO);
			sendMediaBundle.putInt(IntentKeys.MEDIA_STATUS, MediaStatus.COMPLETED);
			sendMediaBundle.putString(IntentKeys.MSG_ID, msgID);
			in.putExtra(IntentKeys.BUNDLE_SEND_MEDIA, sendMediaBundle);
			LocalBroadcastManager.getInstance(this).sendBroadcast(in);
		}
  	}

  	@Override
  	public void onBackPressed() {
  		StorageUtil.deleteMediaVideoExtStg(videoName);
  		super.onBackPressed();
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
 
