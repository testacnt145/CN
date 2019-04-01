package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.adapters.listConversation.model.ConversationLoadEarlierItem;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.util.DateUtil;
import com.chattynotes.util.LogUtil;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.ItemType;
import com.chattynotes.constant.MimeType;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgFlow;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.constant.MsgStar;
import com.chattynotes.constant.MsgStatus;
import com.chattynotes.customviews.ConversationTextEntry;
import com.chattynotes.linkpreview.SearchUrls;
import com.chattynotes.linkpreview.SourceContent;
import com.chattynotes.linkpreview.TextCrawler;
import com.chattynotes.adapters.listConversation.model.MsgLink;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.swipe.rfit.API;
import com.chattynotes.swipe.rfit.converter.StringConverterFactory;
import com.chattynotes.emojicon.EmojiconsPopup;
import com.chattynotes.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.chattynotes.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import com.chattynotes.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import com.chattynotes.emojicon.emoji.Emojicon;
import com.chattynotes.camera.CameraActivity;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.adapters.listConversation.model.ConversationDateSeparatorItem;
import com.chattynotes.adapters.listConversation.AdapterConversation;
import com.chattynotes.adapters.listConversation.model.InterfaceConversation;
import com.chattynotes.util.AndroidUtil;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.IntentUtil;
import com.chattynotes.util.media.MediaAudioUtil;
import com.chattynotes.util.media.MediaTextUtil;
import com.chattynotes.util.media.MediaThumbUtil;
import com.chattynotes.util.PasswordUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.storage.StorageUtil;
import com.chattynotes.util.WallpaperUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.realm.Realm;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import static com.chattynotes.util.media.MediaThumbUtil.THUMB_NOT_FOUND;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.support.annotation.NonNull;

//From 
//	Chats
//	NewChat
//	Share
//	Forward
//	AdapterConversation
//	Info

public class Conversation extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener, OnTouchListener, View.OnClickListener {
//OnItemClickListener 		for list
//OnItemLongClickListener 	for list
//OnTouchListener 			for Audio Recording Microphone

	final int REQ_GALLERY_IMAGE = 1;
	final int REQ_GALLERY_IMAGE_3RD_PARTY = 2;
	final int REQ_GALLERY_VIDEO = 3;
	final int REQ_GALLERY_VIDEO_3RD_PARTY = 4;
	final int REQ_CUSTOM_CAMERA_IMAGE = 5;
	final int REQ_CAMERA_IMAGE = 20;
	final int REQ_CAMERA_VIDEO = 40;
	final int REQ_CONTACT = 50;
	final int REQ_LOCATION_PICKER = 60;
	final int REQ_WALLPAPER = 200;
	final int REQ_FORWARD = 300; //just using for closing the activity (RESULT_CANCELED)

	//for camera images
	String CameraImageName;
	Uri cameraImageUri;

	//link preview
	//user click cancel button means, user do not want to show link
	private boolean LINK_USER_FORCE_CLOSE = false;
	private boolean LINK_PREVIEW_SUCCESS = false;
	private String LINK_SUCCESSFUL_URL = "";
	private String LINK_URL = "";
	private MsgLink msgLink;

	ArrayList<InterfaceConversation> conversationList = new ArrayList<>();
	ArrayList<String> selectionList = new ArrayList<>(); //For CAB(multiple selection -  delete and forward)

	public static long CONVERSATION_ID;
	public static String CONVERSATION_NAME = "-";
	public static String CONVERSATION_DRAFT = "";


	//Conversation_List_Variable_2
	public static int MESSAGES_LAST_FETCH_ROW = 0;
	public static int MESSAGES_COUNT = 0;

	ImageView actionBarIcon;
	TextView txtViewName = null;
	ImageLoaderPath imageLoaderList;

	ListView listview = null;
	AdapterConversation adapter;
	private QueryNotesDB realmQuery;
	private Realm realm;

	//_________________________________________ variables for scrolling
	ImageView btn_scroll_top;
	ImageView btn_scroll_bottom;

	//_________________________________________ variables for sound module
	View rootView;

	ConversationTextEntry editText;

	ImageButton btn_camera;
	ImageButton btn_send;
	ImageButton btn_emoji;
	ImageButton btn_mic;
	ImageButton audio_red_mic;
	TextView tv_recording_timer;
    LinearLayout ll_conversation_btn;
    LinearLayout audio_layout;
    LinearLayout chat_layout;

	//link preview bar
	RelativeLayout conversation_link_bar;
	TextView conversation_link_bar_title;
	TextView conversation_link_bar_description;
	TextView conversation_link_bar_url;
	ImageView conversation_link_bar_image;
	ImageView conversation_link_bar_cancel;

	Animation anim_send_txt_btn_fade_in;
	Animation anim_send_mic_btn_scale_in;
	Animation anim_send_mic_btn_fade_out;
	Animation anim_send_btn_bg_scale_in;
    Animation anim_send_btn_bg_scale_out;
	Animation anim_audio_slide_left_to_right;
    Animation anim_audio_mic_blink;
	Animation anim_camera_slide_left_to_right;
	Animation anim_camera_slide_right_to_left;
    //Animation anim_fade_out; //used for Up and Down arrow buttons in conversation list

	CountDownTimer recording_timer;
	int screen_width;
	boolean _micLongClick;
	boolean _do_animation_need_to_be_played = false;

	//Conversation Attachment Menu
	private LinearLayout attachmentLayout;
	private boolean isHidden = true;

//__________________________________________________________________________________________________________________________________________________________
	//Remove Search, Clear and Email Conversation Button, if there is no message in the conversation
	//** not necessary that the user is arrived from NewChat screen

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);

		onCreate___();
	}

	public void onCreate___() {
//---------------------------->>> WALLPAPER
        setConversationBackground();

//---------------------------->>> ACTION BAR
		//ActionBar actionBar = getActionBar();
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_conversation);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		//https://stackoverflow.com/questions/31788678/android-toolbar-back-arrow-with-icon-like-whatsapp
		//https://stackoverflow.com/questions/32643198/aligning-items-in-support-actionbar-appcompat
		actionBarIcon = (ImageView) findViewById(R.id.toolbar_image);
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

//---------------------------->>>
		initializeBottomBar();

//---------------------------->>> DATABASE, PREFERENCE, INTENTS 
		realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		realmQuery = new QueryNotesDB(realm);

		Bundle conversationBundle = getIntent().getBundleExtra(IntentKeys.BUNDLE_CONVERSATION);
		if (conversationBundle != null) {
			CONVERSATION_ID = conversationBundle.getLong(IntentKeys.CHAT_ID);
			Chats chat = realmQuery.getSingleChat(CONVERSATION_ID);
			CONVERSATION_NAME = chat.getChatName();
			CONVERSATION_DRAFT = chat.getDraft();
		}

//---------------------------->>> CUSTOM ACTION BAR
		//______________ setting action bar image
		if(AppUtil.isChatWithChattyNotes(CONVERSATION_ID))
			actionBarIcon.setImageResource(R.mipmap.ic_launcher_round);
		else {
			if ((PathUtil.getInternalChatImageFile(String.valueOf(CONVERSATION_ID)).exists())) {
				//actionBarIcon.setImageDrawable(null); // <--- added to force redraw of ImageView
				actionBarIcon.setImageURI(PathUtil.getInternalChatImageUri(String.valueOf(CONVERSATION_ID)));
			} else
				actionBarIcon.setImageResource(R.drawable.avatar);
		}
		//______________ custom Layout
		txtViewName = (TextView) findViewById(R.id.toolbar_title_1);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
		txtViewName.setTypeface(font, Typeface.NORMAL);
		txtViewName.setText(CONVERSATION_NAME);

		toolbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in = new Intent(Conversation.this, ChatInfo.class);
				Bundle bundleInfo = new Bundle();
				bundleInfo.putString(IntentKeys.CHAT_NAME, CONVERSATION_NAME);
				bundleInfo.putLong(IntentKeys.CHAT_ID, CONVERSATION_ID);
				in.putExtra(IntentKeys.BUNDLE_INFO, bundleInfo);
				startActivity(in);
			}
		});

		//Conversation Attachment Menu
		initializeConversationMenu();

//---------------------------->>> EDIT TEXT
		editTextChatStateListeners();

//---------------------------->>> CONVERSATION LIST
		insertionInConversationList();

//---------------------------->>> LISTVIEW
		imageLoaderList = new ImageLoaderPath(this);
		listview = (ListView) findViewById(R.id.list_conversation);
		adapter = new AdapterConversation(this, conversationList, imageLoaderList, realmQuery);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setLongClickable(true);
		listview.setOnItemLongClickListener(this);
		listview.setSelection(conversationList.size()-1);

		//Scroll to Top/Bottom Button
		btn_scroll_top = (ImageView) findViewById(R.id.btn_scroll_top);
		btn_scroll_bottom = (ImageView) findViewById(R.id.btn_scroll_bottom);

		listview.setOnScrollListener(new OnScrollListener() {
			private int mLastFirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, final int scrollState) {
				//disable the buttons if user stopped scrolling
				if (scrollState == 0) {
					AndroidUtil.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							btn_scroll_top.setVisibility(View.GONE);
							btn_scroll_bottom.setVisibility(View.GONE);
						}
					}, 4000);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//https://stackoverflow.com/questions/16791100/detect-scroll-up-scroll-down-in-listview
				if (mLastFirstVisibleItem < firstVisibleItem)
					btn_scroll_bottom.setVisibility(View.VISIBLE);
				else if (mLastFirstVisibleItem > firstVisibleItem)
					btn_scroll_top.setVisibility(View.VISIBLE);

				//if last item, don't show buttons
				if (listview.getLastVisiblePosition() == adapter.getCount() - 1) {
					btn_scroll_top.setVisibility(View.GONE);
					btn_scroll_bottom.setVisibility(View.GONE);
				}

				mLastFirstVisibleItem = firstVisibleItem;
			}
		});

        if(conversationBundle!=null) {
//---------------------------->>> FORWARD MESSAGE
            //after initializing listView (otherwise exceptions)
            if (conversationBundle.containsKey(IntentKeys.FORWARD_MSG_ID_LIST)) {
                selectionList = conversationBundle.getStringArrayList(IntentKeys.FORWARD_MSG_ID_LIST);
                sendForwardMessages(selectionList);
                selectionList.clear(); //make sure to empty the selection List after forwarding
            }

//---------------------------->>> SHARED TEXT/IMAGE/VIDEO MESSAGE FROM OTHER APPS
            if (conversationBundle.containsKey(IntentKeys.SHARE_MSG_TEXT)) {
                String text = conversationBundle.getString(IntentKeys.SHARE_MSG_TEXT);
                textMessage(text);
            } else if (conversationBundle.containsKey(IntentKeys.SHARE_MSG_IMAGE_URI)) {
                Intent in = new Intent(this, SendImage.class);
				Bundle bundleImage = new Bundle();
				bundleImage.putString(IntentKeys.CHAT_NAME, conversationBundle.getString(IntentKeys.CHAT_NAME));
				bundleImage.putLong(IntentKeys.CHAT_ID, conversationBundle.getLong(IntentKeys.CHAT_ID));
				bundleImage.putString(IntentKeys.MEDIA_NAME	, conversationBundle.getString(IntentKeys.MEDIA_NAME));
				//uri -> content://com.whatsapp.provider.media/item/118
				bundleImage.putString(IntentKeys.SHARE_MSG_IMAGE_URI, conversationBundle.getString(IntentKeys.SHARE_MSG_IMAGE_URI));
				in.putExtra(IntentKeys.BUNDLE_IMAGE, bundleImage);
				startActivity(in);
            } else if (conversationBundle.containsKey(IntentKeys.MEDIA_VIDEO_URI)) {
                callSendVideoActivity(conversationBundle);
            }
        }
		//____

		//set Draft & set pointer to end
		editText.setText(CONVERSATION_DRAFT);
		editText.setSelection(editText.length());

//---------------------------->>> LOCAL BROADCAST MANAGER (to receive intents when activity is running)
		//make sure to register at last of onCreate, because you can accidently insert in list, before list is initialize, that will cause an exception
		lbmRegister();
	}


	//__________________________________________________________________________________________________________ SCROLLING
	public void onClickBtnScrollToTop(View view) {
		listview.setSelection(0);
	}

	public void onClickBtnScrollToBottom(View view) {
		listview.setSelection(adapter.getCount() - 1);
	}


	//__________________________________________________________________________________________________________
	void insertionInConversationList() {
		ArrayList<InterfaceConversation> tempList = realmQuery.getNoteList(CONVERSATION_ID, 0);
		//re-initializing as they are static
		MESSAGES_LAST_FETCH_ROW = 0;
		MESSAGES_COUNT = 0;
		MESSAGES_LAST_FETCH_ROW = tempList.size();
		MESSAGES_COUNT = realmQuery.getChatNotesCount(CONVERSATION_ID);
		if (tempList.size() > 0) {
			if (MESSAGES_LAST_FETCH_ROW < MESSAGES_COUNT)
				conversationList.add(new ConversationLoadEarlierItem());
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hide the keyboard...
			int today = new DateTime().getDayOfYear();
			int tempDay = 0;
			for (int i = 0; i < tempList.size(); i++) {
				//---------------------------->>> DATE TIME
				//we are 100% sure here tempList has only ConversationItem
				Notes note = (Notes) tempList.get(i);
				DateTime date = new DateTime(note.getMsgTimestamp());
				int dbDay = date.getDayOfYear();
				if (today >= dbDay) {
					if (tempDay != dbDay) {
						conversationList.add(new ConversationDateSeparatorItem(date.toString("dd MMMM yyyy").toUpperCase(Locale.getDefault())));
						tempDay = dbDay;
					}
				}
				conversationList.add(tempList.get(i));
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration c) {
		switch (c.orientation) {
			case Configuration.ORIENTATION_LANDSCAPE:
				//Mandatory : Display SEND Button in Landscape
				editText.setMaxLines(AppConst.MIN_LINES);
				break;
			case Configuration.ORIENTATION_PORTRAIT:
				editText.setMaxLines(AppConst.MAX_LINES);
				break;
		}
		super.onConfigurationChanged(c);
	}

	void editTextChatStateListeners() {
		//LogUtil.e(getClass().getSimpleName(), "editTextChatStateListeners");
		//setting listeners, once we set all final values of isGroup, CONVERSATION_NUMBER etc

		editText.setFocusableInTouchMode(true);
		editText.requestFocus();

		//changing keyboard Enter Button based on User Setting in Preference
		//http://stackoverflow.com/questions/3205339/android-how-to-make-keyboard-enter-button-say-search-and-handle-its-click
		//in XML 	android:inputType="text" 			is mandatory
		//			android:inputType="textMultiLine"	won't work

		/* CUSTOM EDIT TEXT : Reason -> MultiLine edit text without the enter key (or with Send Button)*/
		//http://stackoverflow.com/questions/5014219/multiline-edittext-with-done-softinput-action-label-on-2-3
//		if(Prefs.getIsEnterKeySend(this)) {
//			//editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//			editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
//		} else
//			editText.setImeActionLabel("Send", EditorInfo.IME_ACTION_SEND);

		//This Listener is mandatory as we are using Send Button in Orientation Landscape
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					//String text = editText.getText().toString().trim();
					//http://stackoverflow.com/a/6652772/4754141
					//left only
					String text = editText.getText().toString().replaceAll("^\\s+", "");
					textMessage(text);
					return true;
				}
				return false;
			}
		});

		editText.addTextChangedListener(new TextWatcher() {
			/**
			 * [12] exist in editText
			 * [345] pasted
			 *
			 * @param s old-string present in editText [12]
			 * @param start old-string length [2]
			 * @param count 0 [0]
			 * @param after length of new-string entered(1) or pasted(pasted length) [3]
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			/**
			 * @param s new-string [12345]
			 * @param start old-string length [2]
			 * @param before 0 [0]
			 * @param count length of new-string entered(1) or pasted(pasted length) [3]
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			/**
			 * @param s new-string [12345]
			 */
			@Override
			public void afterTextChanged(Editable s) {
				//link preview
				/*if(!LINK_USER_FORCE_CLOSE) {
					if (!LINK_PREVIEW_SUCCESS) {
						//if no link found, keep on checking
						searchUrl();
					} else {
						//check whether user erases the valid url
						//not that strict check, that is WeArewww.wwe.comPewDiPie is acceptable
						//not checking _www.wwe.com_ (_=space)
						if (!editText.getText().toString().contains(LINK_SUCCESSFUL_URL)) {
							hideLinkPreviewBar();
							//user can change valid url www.yahoo.com to another valid url www.yaho.com therefore
							searchUrl();
						}
					}
				}*/

				//animations
				if (editText.length() == 0) {
					//if its first time don't play the animation(on first time _do_animation_need_to_be_played = false;)
					if (_do_animation_need_to_be_played) {
						//btn_send.startAnimation(anim_fade_out);
						btn_send.setVisibility(View.GONE);
						btn_send.setClickable(false);
						btn_mic.setVisibility(View.VISIBLE);
						btn_mic.startAnimation(anim_send_mic_btn_scale_in);
						btn_camera.setVisibility(View.VISIBLE);
						btn_camera.startAnimation(anim_camera_slide_right_to_left);
						_do_animation_need_to_be_played = false;
					}
				} else if (!_do_animation_need_to_be_played) {
					btn_send.setVisibility(View.VISIBLE);
					//btn_send.startAnimation(anim_send_fade_in);
					btn_send.setClickable(true);
					btn_mic.setVisibility(View.GONE);
					btn_mic.startAnimation(anim_send_mic_btn_fade_out);
					btn_camera.setVisibility(View.GONE);
					btn_camera.startAnimation(anim_camera_slide_left_to_right);
					//btn_camera.startAnimation(anim_fade_out);
					_do_animation_need_to_be_played = true;
				}
			}
		});
	}

	//______________________________________________________________________________________________ link preview
	void searchUrl() {
		try {
			LINK_URL = SearchUrls.getSingleURL(editText.getText().toString());
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(LINK_URL)
					.addConverterFactory(StringConverterFactory.create())
					.build();
			API api = retrofit.create(API.class);
			Call<String> call = api.crawlLink(LINK_URL);
			call.enqueue(new Callback<String>() {
				@Override
				public void onResponse(Call<String> call, Response<String> response) {
					SourceContent sourceContent = TextCrawler.parseContent(response.body(), LINK_URL);
					//due to delay in async task, check again here
					if (sourceContent != null && editText.getText().toString().contains(sourceContent.getFinalUrl())) {
						showLinkPreviewBar(sourceContent);
					}
				}

				@Override
				public void onFailure(Call<String> call, Throwable t) {

				}
			});
		} catch(Exception ignored) {
			//https://github.com/square/retrofit/issues/1872
			//java.lang.IllegalArgumentException: Illegal URL
			//http:// https:// http://.com + several others are invalid
		}
	}

	void showLinkPreviewBar(final SourceContent sc) {
		//if title successfully fetched then go for UI change
		if(!sc.getTitle().equals("")) {
			conversation_link_bar.setVisibility(View.VISIBLE);
			LINK_PREVIEW_SUCCESS = true;
			LINK_SUCCESSFUL_URL = sc.getFinalUrl();
			//content
			conversation_link_bar_title.setText(sc.getTitle());
			if (conversation_link_bar_title.getLineCount() < 2)
				conversation_link_bar_description.setText(sc.getDescription());
			else
				conversation_link_bar_description.setVisibility(View.GONE);
			conversation_link_bar_url.setText(sc.getCannonicalUrl());
			//image
			String imageUrl = sc.getImages().get(0);
			if (sc.getImages().size()>0 && !imageUrl.equals("")) {

				//1- download image at internal location (1 fixed location)
				//2- generate base64 of image for database
				//3- delete image immediately

				//[bug_: do not use this library UrlImageViewHelper]
				UrlImageViewHelper.setUrlDrawable(conversation_link_bar_image, imageUrl, new UrlImageViewCallback() {
					@Override
					public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
						if (loadedBitmap != null) {
							MediaThumbUtil.saveThumbMsgLink(loadedBitmap);
							//imageView.setImageBitmap(loadedBitmap); //too heavy
							imageView.setVisibility(View.VISIBLE);
							imageView.setImageURI(PathUtil.getInternalMediaLinkThumbUri());
							msgLink = new MsgLink(sc.getTitle(), sc.getDescription(), sc.getUrl(), MediaThumbUtil.getThumbMsgLinkInString());
						}
					}
				});
			}
			msgLink = new MsgLink(sc.getTitle(), sc.getDescription(), sc.getUrl(), THUMB_NOT_FOUND);
		}
	}

	void hideLinkPreviewBar() {
		LINK_PREVIEW_SUCCESS = false;
		LINK_SUCCESSFUL_URL = "";
		StorageUtil.deleteLinkThumbIntStg();
		msgLink = MsgConstant.DEFAULT_LINK_OBJECT;
		//views
		conversation_link_bar_title.setText("");
		conversation_link_bar_description.setText("");
		conversation_link_bar_url.setText("");
		//visibility
		conversation_link_bar_description.setVisibility(View.VISIBLE);
		conversation_link_bar_image.setVisibility(View.GONE);
		//
		conversation_link_bar.setVisibility(View.GONE);

	}


//_____________________________________________________________________________________________________________ LBM
	private BroadcastReceiver lbmNewNoteConversation = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			Notes note = in.getParcelableExtra(IntentKeys.PARCELABLE_NOTE_ITEM);
			conversationList.add(note);
			adapter.notifyDataSetChanged();
			listview.setSelection(adapter.getCount() - 1);
			//Conversation_List_Variable_2
			//this is very imp as not doing this will cause problem in case of Load Earlier
			MESSAGES_LAST_FETCH_ROW++;
			MESSAGES_COUNT++;
		}
	};

	private BroadcastReceiver lbmNewNoteMedia = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle sendMediaBundle = intent.getBundleExtra(IntentKeys.BUNDLE_SEND_MEDIA);
			if (sendMediaBundle != null) {
				String msg 			= sendMediaBundle.getString(IntentKeys.MEDIA_NAME);
				String mediaCaption = sendMediaBundle.getString(IntentKeys.MEDIA_CAPTION);
				int msgKind 		= sendMediaBundle.getInt(IntentKeys.MSG_KIND, MsgKind.TEXT);
				int msgDownload 	= sendMediaBundle.getInt(IntentKeys.MEDIA_STATUS, MsgConstant.DEFAULT_MEDIA_STATUS);
				String msgID 		= sendMediaBundle.getString(IntentKeys.MSG_ID, MsgConstant.DEFAULT_MSG_ID());
				sendMessage(msg, msgID, msgKind, msgDownload, mediaCaption, MsgConstant.DEFAULT_LINK_OBJECT, false);
			}
		}
	};

	private BroadcastReceiver lbmMediaStatus = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			/* CHECKED_BEFORE_BROADCAST__ */
			String msgID = in.getStringExtra(IntentKeys.MSG_ID);
			int mediaStatus = in.getIntExtra(IntentKeys.MEDIA_STATUS, MsgConstant.DEFAULT_MEDIA_STATUS);
			if (!conversationList.isEmpty())
				for (int i = 0; i < conversationList.size(); i++)
					if (conversationList.get(i).itemType().equals(ItemType.CONVERSATION_NOTE)) {
						Notes note = (Notes)conversationList.get(i);
						if (note.getMsgId().equals(msgID)) {
							note.setMediaStatus(mediaStatus);
							adapter.notifyDataSetChanged();
						}
					}
		}
	};

	private BroadcastReceiver lbmChangeName = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			long chatID = in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
			if (chatID == CONVERSATION_ID) {
				CONVERSATION_NAME = in.getStringExtra(IntentKeys.CHAT_NAME);
				txtViewName.setText(CONVERSATION_NAME);
			}
		}
	};

	private BroadcastReceiver lbmChangeImage = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent in) {
			long chatID = in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
			if (chatID == CONVERSATION_ID) {
				if(AppUtil.isChatWithChattyNotes(CONVERSATION_ID))
					actionBarIcon.setImageResource(R.mipmap.ic_launcher_round);
				else {
					if ((PathUtil.getInternalChatImageFile(String.valueOf(CONVERSATION_ID)).exists())) {
						actionBarIcon.setImageDrawable(null); // <--- added to force redraw of ImageView
						actionBarIcon.setImageURI(PathUtil.getInternalChatImageUri(String.valueOf(CONVERSATION_ID)));
					} else
						actionBarIcon.setImageResource(R.drawable.avatar);
				}
			}
		}
	};



	//_____________________________________________________________________________________________________
	public void onClickBtnCamera(View view) {
		//[jugaar_: [os_marshmallow_: permission] ]
		//do not need to check audio permission here, instead check permission when making video using custom camera
		List<String> _permissionList = new ArrayList<>();
		_permissionList.add(Manifest.permission.CAMERA);
		_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		_permissionList.add(Manifest.permission.RECORD_AUDIO);
		boolean checkPermission = PermissionUtil.checkMultiplePermissions(this, _permissionList, getString(R.string.permission_storage_cam_mic_custom_camera_request), getString(R.string.permission_storage_cam_mic_custom_camera), R.drawable.permission_cam, R.drawable.permission_storage, R.drawable.permission_mic, PermissionUtil.PERMISSION_MULTIPLE_CUSTOM_CAMERA);
		if (checkPermission)
			startCamera();
	}

	void startCamera() {
		Intent in = new Intent(this, CameraActivity.class);
		in.putExtra(IntentKeys.CHAT_NAME, CONVERSATION_NAME);
		in.putExtra(IntentKeys.CHAT_ID, CONVERSATION_ID);
		startActivityForResult(in, REQ_CUSTOM_CAMERA_IMAGE);
	}

	//_____________________________________________________________________________________________________
	void textMessage(String text) {
		//reset flag while sending message
		LINK_USER_FORCE_CLOSE = false;

		if(LINK_PREVIEW_SUCCESS)
			sendMessage(text, MsgKind.LINK, MediaStatus.COMPLETED);
		else if (text.length() > MsgConstant.LONG_TEXT_MESSAGE_LENGTH)
			longText(text);
		else
			sendMessage(text, MsgKind.TEXT, MediaStatus.COMPLETED);
	}

	void longText(String text) {
		final String textName = PathUtil.generateFileNameUnix(MimeType.MEDIA_LONG_TEXT);
		File file = PathUtil.createExternalMediaTextFile_Sent(textName);
		try {
			FileOutputStream fOut = new FileOutputStream(file, true);//true = append
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.append(text);
			osw.flush();
			osw.close();
		} catch (Exception ignored) {
		}
		String mediaCaption = text.substring(0, MsgConstant.LONG_TEXT_MESSAGE_LENGTH);
		sendMessageMediaCaption(textName, MsgKind.M4_LONG_TEXT, MediaStatus.COMPLETED, mediaCaption);
	}


	//_______________________________________________________________   XMPP MESSAGE SEND    __________________________________________________________________
	public void onClickBtnSend(View view) {
		//String text = editText.getText().toString().trim();
		//http://stackoverflow.com/a/6652772/4754141
		//left only
		String text = editText.getText().toString().replaceAll("^\\s+", "");
		textMessage(text);
	}

	//thumb for 1,7 = audio and video
	//time for 4 = self destructive timer setting

	//Basic
	//forward : false
	//media caption : default
	void sendMessage(String msg, int msgKind, int msgDownload) {
		sendMessage(msg, MsgConstant.DEFAULT_MSG_ID(), msgKind, msgDownload, MsgConstant.DEFAULT_MEDIA_CAPTION, msgLink, false);
	}

	//Overloading for media caption
	//forward : false
	void sendMessageMediaCaption(String msg, int msgKind, int msgDownload, String mediaCaption) {
		sendMessage(msg, MsgConstant.DEFAULT_MSG_ID(), msgKind, msgDownload, mediaCaption, MsgConstant.DEFAULT_LINK_OBJECT, false);
	}

	//Overloading for forward message
	//forward : true
	void sendMessageForward(String msg, int msgKind, int msgDownload, String mediaCaption, MsgLink msgLink) {
		sendMessage(msg, MsgConstant.DEFAULT_MSG_ID(), msgKind, msgDownload, mediaCaption, msgLink, true);
	}

	//final
	void sendMessage(String msg, String msgID, int msgKind, int mediaStatus, String mediaCaption, MsgLink msgLink, Boolean isForwardMsg) {
		if (!msg.equals("")) {
			long chatID = CONVERSATION_ID;
			long msgTimestamp = MsgConstant.DEFAULT_MSG_TIMESTAMP();
			Notes note = new Notes();
			note.setNoteId(realmQuery.getIncrementedNoteId());
			note.setChatId(chatID);
			note.setMsg(msg);
			note.setMsgId(msgID);
			note.setMsgKind(msgKind);
			note.setMsgTimestamp(msgTimestamp);
			note.setMediaStatus(mediaStatus);
			note.setMediaCaption(mediaCaption);
			conversationList.add(note);
			Chats chat = new Chats();
			chat.setChatId(chatID);
			chat.setChatName(CONVERSATION_NAME);
			chat.setNote(note);
			lbmNewNoteChat(chat);
			String msgLinkString = MsgLink.convertToString(msgLink);
			realmQuery.insertNote(note);

			adapter.notifyDataSetChanged();
			listview.setSelection(adapter.getCount() - 1);
			//Conversation_List_Variable_2
			//this is very imp as not doing this will cause problem in case of Load Earlier
			MESSAGES_LAST_FETCH_ROW++;
			MESSAGES_COUNT++;

			//clear draft message, iff
			if(!isForwardMsg && AppUtil.isMsgTextOrLongTextOrLink(msgKind)) {
				CONVERSATION_DRAFT = "";
				editText.setText(CONVERSATION_DRAFT);
				hideLinkPreviewBar();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_conversation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int id = item.getItemId();
		if (id == android.R.id.home) {
			/* only in case of Toolbar back button */
			//call hardware back button
			onBackPressed();
			return true;
		} else if (id == R.id.menu_attachments) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
				showMenuBelowLollipop();
			else
				showMenu();
			return true;
		} else if (id == R.id.menu_wallpaper) {
			menuWallpaper();
			return true;
		} else if (id == R.id.menu_clear_chat) {
			menuClearChat();
			return true;
		} else if (id == R.id.menu_email_chat) {
			menuEmailChat();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//______________________________________________________________________________________________ ATTACHMENTS
	void attachmentGalleryPhoto() {
		Intent in = new Intent(this, GalleryImageAlbum.class);
		in.putExtra(IntentKeys.CHAT_NAME, CONVERSATION_NAME);
		in.putExtra(IntentKeys.CHAT_ID, CONVERSATION_ID);
		startActivityForResult(in, REQ_GALLERY_IMAGE);
	}

	//____________
	void attachmentGalleryVideo() {
		Intent in = new Intent(this, GalleryVideoAlbum.class);
		in.putExtra(IntentKeys.CHAT_NAME, CONVERSATION_NAME);
		in.putExtra(IntentKeys.CHAT_ID, CONVERSATION_ID);
		startActivityForResult(in, REQ_GALLERY_VIDEO);
	}

	//____________
	void attachmentLocation() {
		Intent in = new Intent(this, SendLocation.class);
		startActivityForResult(in, REQ_LOCATION_PICKER);
	}

	//____________
	void attachmentContact() {
		Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		//error data1
		//http://stackoverflow.com/questions/27727178/trying-to-get-contact-info-invalid-column-data1
		in.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		startActivityForResult(in, REQ_CONTACT);
	}

	//____________ CAMERA PHOTO
	void attachmentCameraPhoto() {
		//**** for external storage
		//Save image here instead of camera default folder
		CameraImageName = PathUtil.generateFileNameUnix(MimeType.MEDIA_IMAGE);
		cameraImageUri = PathUtil.createCameraMediaImageUri(this, CameraImageName);
		File f = PathUtil.createCameraMediaImageFile(CameraImageName);
		Intent intent = IntentUtil.cameraImagePickerList(this, f);
		if (intent != null)
			startActivityForResult(intent, REQ_CAMERA_IMAGE);
		else
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
	}

	//____________ CAMERA VIDEO
	void attachmentCameraVideo() {
		//**** for external storage
		//Save image here instead of camera default folder
//			Camera_Video_Name 	= StorageUtil.generateFileNameUnix(PHONE_NUMBER);
//			cameraVideoUri 		= PathUtil.createCameraMediaVideoUri(Camera_Video_Name);
		//LogUtil.e(getClass().getSimpleName(), "cameraVideoUri:" + cameraVideoUri.toString());

		Intent intent = IntentUtil.cameraVideoPickerList(this);
		if (intent != null)
			startActivityForResult(intent, REQ_CAMERA_VIDEO);
		else
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
	}

	//________________________________
	//XML__ Remember, you need to declare <intent-filter> in AndroidManifest otherwise it wont work in API-15
	void menuWallpaper() {
		final ArrayList<Intent> intentList = new ArrayList<>();
		//picker
		//Intent p = new Intent(WallpaperUtil.WALLPAPER_PICKER_ACTION);
		//p.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_PICKER_PACKAGE_NAME));
		//intentList.add(p);
		//default
		Intent d = new Intent(WallpaperUtil.WALLPAPER_DEFAULT_ACTION);
		d.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_DEFAULT_PACKAGE_NAME));
		intentList.add(d);
		//none
		Intent n = new Intent(WallpaperUtil.WALLPAPER_NONE_ACTION);
		n.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_NONE_PACKAGE_NAME));
		intentList.add(n);
		//color
		Intent c = new Intent(WallpaperUtil.WALLPAPER_COLOR_ACTION);
		c.setComponent(new ComponentName(this, WallpaperUtil.WALLPAPER_COLOR_PACKAGE_NAME));
		intentList.add(c);
		//....
		if(intentList.isEmpty())
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
		else {
			final Intent chooserIntent = Intent.createChooser(intentList.remove(intentList.size()-1), "Wallpaper");//this removes 'Always' & 'Only once' buttons
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
			startActivityForResult(chooserIntent, REQ_WALLPAPER);
		}
	}

	void dialogWallpaperColorChooser() {
		//https://github.com/kristiyanP/colorpicker
		final ColorPicker colorPicker = new ColorPicker(this);
		colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
			@Override
			public void setOnFastChooseColorListener(int position, int color) {
				Prefs.putWallpaperNumber(Conversation.this, position);
				setConversationBackground();
			}
			@Override
			public void onCancel(){
			}
		})
		.setDefaultColorButton(1)
		.setColumns(5)
		.setRoundColorButton(true)
		.setTitle("Choose color")
		.show();
	}

// ----------------------------------------------------------------------------------------------------------------------------------------------
	//WorkFlow
	//Gallery Images 	- GALLERY_IMAGE_REQUEST (if overflow is pressed will return)
	//Gallery 3rd Party	- GALLERY_IMAGE_REQUEST_3RD_PARTY --> CONFIRM_SEND_IMAGE
	//Camera Images 	- CAMERA_IMAGE_REQUEST --> CONFIRM_SEND_IMAGE

	//Gallery Videos 	- GALLERY_VIDEO_REQUEST (if overflow is pressed will return)
	//Gallery 3rd Party	- GALLERY_VIDEO_REQUEST_3RD_PARTY --> CONFIRM_SEND_IMAGE
	//Camera Images 	- CAMERA_IMAGE_REQUEST

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent in) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQ_WALLPAPER) {
				//set the wallpaper directly in case of Default and None otherwise display color dialog
				if(in.hasExtra(IntentKeys.WALLPAPER_NUMBER)) {
					int wallpaperNumber = in.getIntExtra(IntentKeys.WALLPAPER_NUMBER, WallpaperUtil.DEFAULT_WALLPAPER_NUMBER);
					Prefs.putWallpaperNumber(this, wallpaperNumber);
					setConversationBackground();
				} else
					dialogWallpaperColorChooser();
			} else if (requestCode == REQ_GALLERY_IMAGE) {
                //means overflow is pressed in GalleryImageAlbum
				boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE_3RD_PARTY);
				if(checkPermission)
					thirdPartyGalleryImagePickerMenu();
			} else if (requestCode == REQ_GALLERY_VIDEO) {
                //means overflow is pressed in GalleryVideoAlbum
				boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_VIDEO_3RD_PARTY);
                if(checkPermission)
                    thirdPartyGalleryVideoPickerMenu();
			}
			//-----------------------------------------------SENDING
			else if (requestCode == REQ_GALLERY_IMAGE_3RD_PARTY) {
				if (in != null && in.getData() != null)
					send3rdPartyImage(in.getData());
				else
					Toast.makeText(this, "Camera failed", Toast.LENGTH_SHORT).show();
			} else if (requestCode == REQ_GALLERY_VIDEO_3RD_PARTY) {
				if (in != null && in.getData() != null)
					callSendVideoActivity(in.getData());
				else
					Toast.makeText(this, "Error opening video", Toast.LENGTH_SHORT).show();
			} else if (requestCode == REQ_CAMERA_IMAGE) {
				//[bug_: SendImage cannot recognise image from our content:// uri]
				//uri -> content://com.chattynotes.util.androidos.GenericFileProvider/external_files/Chatty%20Notes/Media/Images/Sent/IMG-1509629784.jpg
				sendCameraImage(cameraImageUri);
			} else if (requestCode == REQ_CAMERA_VIDEO) {
				//intent.getData() contains the 'CONTENT URI' where Video actually exists that is Camera Folder DCIM
				//we are using it because FFMPEG wants different input and output path
				if (in != null && in.getData() != null)
					callSendVideoActivity(in.getData());
				else
					Toast.makeText(this, "Camera failed", Toast.LENGTH_SHORT).show();

//					//old method when video is stored in Chatty Notes Video Folder
//		    		Uri uri = VideoUtil.getVideoContentUri(PathUtil.getExternalMediaVideoFile(Camera_Video_Name));
//		    		 if(uri!=null)
//		    			 callSendVideoActivity(uri);
//		    		 else
//			        	Toast.makeText(this, "Camera Failed", Toast.LENGTH_SHORT).show();
//		    		 //cameraVideoUri : not using i guess
			}
			//--------------------------------------------------LOCATION
			else if(requestCode==REQ_LOCATION_PICKER) {
				String msg = in.getStringExtra(IntentKeys.MSG);
				int msgKind = in.getIntExtra(IntentKeys.MSG_KIND, MsgKind.LOCATION);
				sendMessage(msg, msgKind, MediaStatus.COMPLETED);
			}
			//--------------------------------------------------CONTACT
			else if(requestCode==REQ_CONTACT) {
				if (in != null && in.getData() != null)
                	sendContact(in.getData());
				else
					Toast.makeText(this, "Contact sending failed", Toast.LENGTH_SHORT).show();
            }
		}
		//Will Come Here From Activities(3) : Forward, GalleryImageAlbum, InfoGroup
		else if (resultCode==RESULT_CANCELED) {
			//LogUtil.e(getClass().getSimpleName(), "RESULT_CANCELED : reqCode " +  requestCode);
			switch (requestCode) {
				case REQ_GALLERY_IMAGE:
					//LogUtil.e(getClass().getSimpleName(), "RESULT_CANCELED -> GalleryImageAlbum");
					break;

				case REQ_CUSTOM_CAMERA_IMAGE:
					//LogUtil.e(getClass().getSimpleName(), "RESULT_CANCELED -> CameraActivity");
					break;

				case REQ_FORWARD:
					//in case of back button is pressed, intent == null so Exception
					if (in!=null && in.getExtras()!=null) {
						String chatName = in.getStringExtra(IntentKeys.CHAT_NAME);
						long chatID = in.getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
						Bundle conversationBundle = new Bundle();
						conversationBundle.putLong(IntentKeys.CHAT_ID, chatID);
						conversationBundle.putStringArrayList(IntentKeys.FORWARD_MSG_ID_LIST, selectionList);//Only put from here
						PasswordUtil.verifyPassword(this, conversationBundle, true);
					}
					break;
			}
		}
	}

	void thirdPartyGalleryImagePickerMenu() {
		Intent intent = IntentUtil.thirdPartyImageVideoPickerList(this, MimeType.MIME_TYPE_IMAGE, "Choose Image");
		if (intent != null)
			startActivityForResult(intent, REQ_GALLERY_IMAGE_3RD_PARTY);
		else
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
	}

	void thirdPartyGalleryVideoPickerMenu() {
		Intent intent = IntentUtil.thirdPartyImageVideoPickerList(this, MimeType.MIME_TYPE_VIDEO, "Choose Video");
		if (intent != null)
			startActivityForResult(intent, REQ_GALLERY_VIDEO_3RD_PARTY);
		else
			Toast.makeText(this, "No apps can perform this action", Toast.LENGTH_LONG).show();
	}

	//________________________________________________________________________________________________
	void send3rdPartyImage(Uri _uri) {
		//_uri.toString() content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F35007/ORIGINAL/NONE/1742303171
		Intent in = new Intent(Conversation.this, SendImage.class);
		Bundle bundleImage = new Bundle();
		bundleImage.putString(IntentKeys.CHAT_NAME, CONVERSATION_NAME);
		bundleImage.putLong(IntentKeys.CHAT_ID, CONVERSATION_ID);
		bundleImage.putString(IntentKeys.MEDIA_NAME	, PathUtil.generateFileNameUnix(MimeType.MEDIA_IMAGE));
		bundleImage.putString(IntentKeys.SHARE_MSG_IMAGE_URI, _uri.toString());
		in.putExtra(IntentKeys.BUNDLE_IMAGE, bundleImage);
		startActivity(in);
	}

	void callSendVideoActivity(Uri _uri) {
		Intent in = new Intent(this, SendVideo.class);
		Bundle bundleVideo = new Bundle();
		bundleVideo.putString(IntentKeys.CHAT_NAME, CONVERSATION_NAME);
		bundleVideo.putLong(IntentKeys.CHAT_ID, CONVERSATION_ID);
		bundleVideo.putString(IntentKeys.MEDIA_NAME, PathUtil.generateFileNameUnix(MimeType.MEDIA_VIDEO));
		bundleVideo.putString(IntentKeys.MEDIA_VIDEO_URI, _uri.toString());
		in.putExtra(IntentKeys.BUNDLE_VIDEO, bundleVideo);
		startActivityForResult(in, AppConst.GARBAGE_REQUEST);
//		in.putExtra(getString(R.string.video_size)		, (long)new File(_uri.getPath()).length()); //size using File
//		in.putExtra(getString(R.string.video_duration)	, (long)MediaPlayer.create(this, _uri).getDuration()); //duration using MediaPlayer
	}

	void callSendVideoActivity(Bundle _conversationBundle) {
		Intent in = new Intent(this, SendVideo.class);
		Bundle bundleVideo = new Bundle();
		bundleVideo.putString(IntentKeys.CHAT_NAME, _conversationBundle.getString(IntentKeys.CHAT_NAME));
		bundleVideo.putLong(IntentKeys.CHAT_ID, _conversationBundle.getLong(IntentKeys.CHAT_ID));
		bundleVideo.putString(IntentKeys.MEDIA_NAME, _conversationBundle.getString(IntentKeys.MEDIA_NAME));
		bundleVideo.putString(IntentKeys.MEDIA_VIDEO_URI, _conversationBundle.getString(IntentKeys.MEDIA_VIDEO_URI));
		in.putExtra(IntentKeys.BUNDLE_VIDEO, bundleVideo);
		startActivityForResult(in, AppConst.GARBAGE_REQUEST);
	}

	//________________________________________________________________________________________________
	void sendCameraImage(Uri _uri) {
		Intent in = new Intent(Conversation.this, SendImage.class);
		Bundle bundleImage = new Bundle();
		bundleImage.putString(IntentKeys.CHAT_NAME, CONVERSATION_NAME);
		bundleImage.putLong(IntentKeys.CHAT_ID, CONVERSATION_ID);
		bundleImage.putString(IntentKeys.MEDIA_NAME, CameraImageName);
		bundleImage.putString(IntentKeys.SHARE_MSG_IMAGE_URI, _uri.toString()); //_uri.getPath()
		in.putExtra(IntentKeys.BUNDLE_IMAGE, bundleImage);
		startActivity(in);
	}

//________________________________________________________________________________________________
    private void sendContact(Uri contactUri) {
        String msg;
        String name = "";
        String number = "";
        //Help from
        //  http://code.tutsplus.com/tutorials/android-essentials-using-the-contact-picker--mobile-2017
        //  UpdateContact.class
        //  Azfar Contact Module
        //String id = contactUri.getLastPathSegment();

        // SAME_CODE_IN__ RegFinal, InfoContact, UpdateContactList
		//name
		Cursor nameCursor = getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (nameCursor != null) {
                int nameColumn = nameCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                if (nameCursor.moveToFirst())
                    name = nameCursor.getString(nameColumn);
            }
        } finally {
            if (nameCursor != null)
                nameCursor.close();
            msg = name + ",";
        }

        //number
        Cursor phoneCursor = getContentResolver().query(contactUri, null, null, null, null);
        try {
            if(phoneCursor!=null) {
                int contactColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if (phoneCursor.moveToFirst())
                    number = phoneCursor.getString(contactColumn);
            }
        } finally {
            if (phoneCursor != null)
                phoneCursor.close();
            msg += number;
            if(!number.equals(""))
                sendMessage(msg, MsgKind.CONTACT, MediaStatus.COMPLETED);
            else
                Toast.makeText(this, "Error getting contact details", Toast.LENGTH_SHORT).show();
        }
     }

//________________________________________________________________________________________________
	final static int FIRST_PERMISSION = 0;
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PermissionUtil.PERMISSION_GALLERY_IMAGE_3RD_PARTY:
				if(grantResults[FIRST_PERMISSION] == PackageManager.PERMISSION_GRANTED)
					thirdPartyGalleryImagePickerMenu();
				else
					permissionNotGranted();
				break;
			case PermissionUtil.PERMISSION_GALLERY_VIDEO_3RD_PARTY:
				if(grantResults[FIRST_PERMISSION] == PackageManager.PERMISSION_GRANTED)
					thirdPartyGalleryVideoPickerMenu();
				else
					permissionNotGranted();
				break;
			case PermissionUtil.PERMISSION_CAMERA_IMAGE:
				if(grantResults[FIRST_PERMISSION] == PackageManager.PERMISSION_GRANTED)
					attachmentCameraPhoto();
				else
					permissionNotGranted();
				break;
			case PermissionUtil.PERMISSION_CAMERA_VIDEO:
				if(grantResults[FIRST_PERMISSION] == PackageManager.PERMISSION_GRANTED)
					attachmentCameraVideo();
				else
					permissionNotGranted();
				break;
			case PermissionUtil.PERMISSION_MULTIPLE_AUDIO_DIALOG:
				Map<String, Integer> permAudioDialog = new HashMap<>();
				// important step
				//initial put all permission you requested in HashMap, because if PERMISSION_GRANTED = true
				//permissions[i] array will not contain that value
				permAudioDialog.put(Manifest.permission.WRITE_EXTERNAL_STORAGE	, PackageManager.PERMISSION_GRANTED);
				permAudioDialog.put(Manifest.permission.RECORD_AUDIO			, PackageManager.PERMISSION_GRANTED);
				//replace the permission with appropriate result
				for (int i=0; i<permissions.length; i++)
					permAudioDialog.put(permissions[i], grantResults[i]);
				//check and display appropriate message
				if(permAudioDialog.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
						&& permAudioDialog.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					//all permissions not granted
					permissionNotGranted();
				} else if(permAudioDialog.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_storage, Toast.LENGTH_SHORT).show();
				} else if(permAudioDialog.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_mic, Toast.LENGTH_SHORT).show();
				} else {//all permissions granted
					attachmentAudioRecorder();
				}
				/* old technique
				if(grantResults[FIRST_PERMISSION] == PackageManager.PERMISSION_GRANTED) {
					LogUtil.e(getClass().getSimpleName(), "onRequestPermissionsResult: PERMISSION_MULTIPLE_AUDIO_DIALOG");
					List<String> _permissionListAudio = new ArrayList<>();
					_permissionListAudio.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
					_permissionListAudio.add(Manifest.permission.RECORD_AUDIO);
					if(PermissionUtil.checkMultiplePermissionsSilent(this, _permissionListAudio))
						attachmentAudioRecorder();
				} else
					permissionNotGranted();
				*/
				break;
			case PermissionUtil.PERMISSION_MULTIPLE_AUDIO:
				Map<String, Integer> permAudioBar = new HashMap<>();
				permAudioBar.put(Manifest.permission.WRITE_EXTERNAL_STORAGE	, PackageManager.PERMISSION_GRANTED);
				permAudioBar.put(Manifest.permission.RECORD_AUDIO			, PackageManager.PERMISSION_GRANTED);
				for (int i=0; i<permissions.length; i++)
					permAudioBar.put(permissions[i], grantResults[i]);
				if(permAudioBar.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
						&& permAudioBar.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					permissionNotGranted(); //both permissions not granted
				} else if(permAudioBar.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_storage, Toast.LENGTH_SHORT).show();
				} else if(permAudioBar.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_mic, Toast.LENGTH_SHORT).show();
				}
				//do not call attachmentAudioRecorder() as for bottom bar animation
				//button needs to be pressed
				/* else {//both permissions were granted
					attachmentAudioRecorder();
				}*/
				break;
			case PermissionUtil.PERMISSION_MULTIPLE_CUSTOM_CAMERA:
				Map<String, Integer> permCamera = new HashMap<>();
				permCamera.put(Manifest.permission.CAMERA					, PackageManager.PERMISSION_GRANTED);
				permCamera.put(Manifest.permission.WRITE_EXTERNAL_STORAGE	, PackageManager.PERMISSION_GRANTED);
				permCamera.put(Manifest.permission.RECORD_AUDIO				, PackageManager.PERMISSION_GRANTED);
				for (int i=0; i<permissions.length; i++)
					permCamera.put(permissions[i], grantResults[i]);
				if(permCamera.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
						&& permCamera.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
							&& permCamera.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					permissionNotGranted(); //all permissions not granted
				} else if(permCamera.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_camera, Toast.LENGTH_SHORT).show();
				} else if(permCamera.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_storage, Toast.LENGTH_SHORT).show();
				} else if(permCamera.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, PermissionUtil.permission_not_granted_mic, Toast.LENGTH_SHORT).show();
				} else {//all permissions were granted
					startCamera();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				//imp : do not call permissionNotGranted() here because, PERMISSION_DO_NOTHING will come in this case
		}
	}

	void permissionNotGranted() {
		//LogUtil.e(getClass().getSimpleName(), "onRequestPermissionsResult: Permission was not granted");
		Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
	}

//_________________________________________________________________________________________________________ CLEAR
	private void menuClearChat() {
		new AlertDialog.Builder(this)
		.setMessage("Are you sure you want to clear ALL notes in this chat?")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//Things to do
				//0- take care of conversation list variables
				//1- delete notes from DB
				//2- delete thumbs if present
				//3- remove from conversation list and update
				//4- remove from chat list and update

				//0-  take care of conversation list variables
				//Conversation_List_Variable_2
				//this is very imp as not doing this will cause problem in case of Load Earlier
				MESSAGES_LAST_FETCH_ROW=0;
				MESSAGES_COUNT=0;

				//1- delete notes from DB
				realmQuery.clearChat(CONVERSATION_ID); //don't delete group identifier message

				//2- delete thumbs if present
				StorageUtil.deleteChatMediaThumbIntStg(CONVERSATION_ID);

				//3- remove from conversation list and update
				conversationList.clear();
				adapter.notifyDataSetChanged();

				//4- remove from chat list and update
				updateChatScreenEmpty();
			}
		})
		 .setNegativeButton(android.R.string.no, null).show();
	}

//_________________________________________________________________________________________________________ DONE
	private void doneSelectedItems(final ArrayList<String> _selectionList, final ActionMode _mode) {
		for (int i = 0; i < _selectionList.size(); i++) {
			//another loop, pick 1 msgID and loop through other list, repeat
			for (int conIndex=0; conIndex<conversationList.size(); conIndex++) {
				if (conversationList.get(conIndex).itemType().equals(ItemType.CONVERSATION_NOTE)) {
					Notes note = (Notes) conversationList.get(conIndex);
					String msgId = _selectionList.get(i);
					if (note.getMsgId().equals(msgId)) {
						//only update MSG_TIMESTAMP_DONE of those that have 0 default
						//otherwise it will overwrite with latest time in multiple selection
						if(note.getMsgStatus()== MsgStatus.CLOCK) {
							long msgTimestampDone = MsgConstant.DEFAULT_MSG_TIMESTAMP();
							int msgStatus = MsgStatus.DONE;
							realmQuery.updateDoneTimestamp(msgId, msgStatus, msgTimestampDone);
							note.setMsgStatus(msgStatus);
							note.setMsgTimestampDone(msgTimestampDone);
							lbmMsgStatus(note.getChatId(), msgId, msgStatus);
						}
						long completionTime = note.getMsgTimestampDone() - note.getMsgTimestamp();
						String text = String.format(getString(R.string.dialog_msg_timestamp_completion_detail), DateUtil.getDateInStringForMsgTimestampDialogCompletion(completionTime));
						MediaAudioUtil.playNoteDoneSound(this, Prefs.getSound(this), Prefs.getLight(this), Prefs.getVibrate(this), CONVERSATION_NAME, text);
					}
				}
			}
		}
		adapter.notifyDataSetChanged();
		_mode.finish();
	}

	//_________________________________________________________________________________________________________ STAR
	private void starSelectedItems(final ArrayList<String> _selectionList, final ActionMode _mode){
		for (int i = 0; i < _selectionList.size(); i++) {
			//1- delete notes from DB
			int msgStar = MsgStar.NO;
			if(_isStarButtonEnabled)
				msgStar = MsgStar.YES;
			realmQuery.starSelectedMessages(_selectionList.get(i), msgStar);

			//another loop, pick 1 msgID and loop through other list, repeat
			for (int conIndex = 0; conIndex < conversationList.size(); conIndex++) {
				if (conversationList.get(conIndex).itemType().equals(ItemType.CONVERSATION_NOTE)) {
					Notes note = (Notes) conversationList.get(conIndex);
					if (note.getMsgId().equals(_selectionList.get(i))) { //this item is selected for star
						//2- set list item star/un-star
						note.setMsgStar(msgStar);
					}
				}
			}
		}
		adapter.notifyDataSetChanged();
		_mode.finish();
	}
//_________________________________________________________________________________________________________ DELETE
	private void deleteSelectedItems(final ArrayList<String> _selectionList, final ActionMode _mode) {
		String message = "Delete " + _selectionList.size() + (_selectionList.size() == 1 ? " note" : " notes");
		new AlertDialog.Builder(this)
		.setMessage(message)
		.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
				//Things to do
                //0- take care of conversation list variables
                //1- delete notes from DB
                //2- delete thumbs if present
                //3- remove from conversation list and update
                //4- remove from chat list and update

                for (int i = 0; i < _selectionList.size(); i++) {

                    //0-  take care of conversation list variables
					//Conversation_List_Variable_2
					//this is very imp as not doing this will cause problem in case of Load Earlier
					MESSAGES_LAST_FETCH_ROW--;
                    MESSAGES_COUNT--;

                    //1- delete notes from DB
					realmQuery.deleteSelectedNote(_selectionList.get(i), CONVERSATION_ID);

                    //another loop, pick 1 msgID and loop through other list, repeat
                    for (int conIndex = 0; conIndex < conversationList.size(); conIndex++) {
                        if (conversationList.get(conIndex).itemType().equals(ItemType.CONVERSATION_NOTE)) {
                            Notes note = (Notes) conversationList.get(conIndex);
                            if (note.getMsgId().equals(_selectionList.get(i))) { //this item is selected for deletion

                                //all selected notes are deleted (as mentioned above),
                                //because of forward message, multiple notes can have same media_name(message) to access single thumb in internal
                                //check now, if count of message(media name) of this user == 0, delete the internal thumb,
                                //otherwise if count > 0, means thumb is accessed by any other user

                                //2- delete thumbs if present
								if(AppUtil.isMsgHasThumb(note.getMsgKind())) {
                                    int count = realmQuery.getMediaCountOfSpecificMessage(note.getChatId(), note.getMsg()); //getting count
                                    if (count == 0)//item not use for forwarding purpose
                                        StorageUtil.deleteMediaThumbIntStg(CONVERSATION_ID, note.getMsg());//delete internal media thumb
                                }

                                //3- remove from conversation list and update
                                conversationList.remove(conIndex);
                            }
                        }
                    }
                }

                //4- remove from chat list and update
                //check if list not empty,
                //start a loop from End to 0
                //find the last 'ConversationItem', get its msgID, if not found set msg = ""
                //update chat list accordingly
                if (!conversationList.isEmpty())
                    for (int i = conversationList.size() - 1; i >= 0; i--) {
                        if (conversationList.get(i).itemType().equals(ItemType.CONVERSATION_NOTE)) {
                            Notes note = (Notes) conversationList.get(i); //****** ClassCastException
							Chats chat = new Chats();
							chat.setChatId(CONVERSATION_ID);
							chat.setChatName(CONVERSATION_NAME);
							chat.setNote(note);
                            lbmNewNoteChat(chat);
                            break;
                        }

                        if (i == 0)
                            //blank entry in chat list
							updateChatScreenEmpty();
            }


            //there is a problem, while deleting single notes
            //if we delete all notes, still Date Item will stay there, therefore
            //we can't delete chat list on the basis of => if(conversationList.isEmpty())
            //if(conversationList.isEmpty())
            //lbmGroupDeleted(CONVERSATION_NUMBER);//remove chat list item

            adapter.notifyDataSetChanged();
            _mode.finish();
		    }})
		 .setNegativeButton(android.R.string.cancel, null).show();
	}
	
//_________________________________________________________________________________________________________ FORWARD
	private void forwardSelectedItems() {
		Intent i = new Intent(this, Forward.class);
		startActivityForResult(i, REQ_FORWARD);
	}
	
	private void sendForwardMessages(final ArrayList<String> _selectionList) {
		for(int i=0; i < _selectionList.size(); i++) {

			Notes note = realmQuery.getSingleNote(_selectionList.get(i));
			//LogUtil.e(getClass().getSimpleName(), "sendForwardMessages -> " + singleMessage.msgKind + ":" + singleMessage.msgFlow);
			
			if(note!=null) {
				//MEDIA MESSAGE(1,2,7,22)
				if(AppUtil.isMsgMedia(note.getMsgKind())) {
					//since forward message always save media in SENT folder, therefore
					//we just need to chk if not present, so copy there

					//here if media is present in case of MsgFlow.RECEIVE, i.e not in sent folder
					//to whom we are forwarding the message this message will become msgFlow=MsgFlow.SEND,
					//therefore the listView will access SENT Folder to show image and won't find any media therefore will show no media present error
					//therefore in case of MsgFlow.RECEIVE, we additionally need to copy media from RECEIVE Folder to SENT Folder
					File mediaFile = PathUtil.getExternalMediaFile(note.getMsgKind(), note.getMsgFlow(), note.getMsg());
					if(mediaFile!=null && mediaFile.exists()) {
						if(note.getMsgFlow() == MsgFlow.RECEIVE)
							StorageUtil.copyMediaExtStg_Forward(mediaFile, note.getMsg(), note.getMsgKind());

						if(AppUtil.isMsgHasThumb(note.getMsgKind())) {
							//if you access 'getThumbInStringFromIntStg' method, it will access thumb of folder of the user you are forwarding message from
							//therefore we also need to 
							// 1- copy the thumb in this current active user folder, if not forwarding again to the same user
							//********
							//i am talking to usa1 (CONVERSATION_NUMBER=12345678901)
							//i forward image to usa1 (singleMessage.number=12345678901)
							//so do not need to copy thumb
							if(note.getChatId() != CONVERSATION_ID)
								StorageUtil.copyMediaThumbIntStg_Forward(CONVERSATION_ID, note.getChatId(), note.getMsg());
							
							
							// 2- don't upload media again
							// 3- don't set sdTime
						} 
						
						//everything done, sendMessage
						sendMessageForward(note.getMsg(), note.getMsgKind(), MediaStatus.COMPLETED, note.getMediaCaption(), null);
						
					} else {
						Toast.makeText(this, "Forwarding failed, media doesn't exists", Toast.LENGTH_SHORT).show();
					}
				} else //Non Media Message , Simply Forward
					sendMessageForward(note.getMsg(), note.getMsgKind(), MediaStatus.COMPLETED, note.getMediaCaption(), null);
			} 
		}
	}

	
//_________________________________________________________________________________________________________ AUDIO
	//Global Fields
	private CountDownTimer cDownTimer_audioPlay = null;// because we need to cancel it, once dialog is dismissed (either Send/Cancel)
	final int MAXIMUM_VOICE_MESSAGE_LENGTH = 120000;//2 minutes = 120 seconds = 120000 ms

	private void attachmentAudioRecorder() {
		//dialog activity :   http://developer.android.com/guide/topics/ui/dialogs.html
		
		final String audioName = PathUtil.generateFileNameUnix(MimeType.MEDIA_AUDIO);
		final File file = PathUtil.createExternalMediaAudioFile_Sent(audioName);
		final MediaRecorder mRecorder = createMediaRecorder(file);
		 
		//custom layout
		LayoutInflater inflater = this.getLayoutInflater();
		//****** instead of passing null, you can find parent from this code
		//http://stackoverflow.com/questions/4486034/get-root-view-from-current-activity
		View custom_view = inflater.inflate(R.layout.dialog_audio_recorder, (ViewGroup) findViewById(android.R.id.content), false);
		final ProgressBar progressBarAudio = (ProgressBar) custom_view.findViewById(R.id.pBar_audio);
		final Button btnPlay = (Button) custom_view.findViewById(R.id.btn_play);
		final SeekBar seekBarAudio = (SeekBar) custom_view.findViewById(R.id.sBar_audio);
		final TextView txtViewLeft = (TextView) custom_view.findViewById(R.id.tv_timer_left);
		final TextView txtViewRight = (TextView) custom_view.findViewById(R.id.tv_timer_right);
		//one and only media player and 
		final MediaPlayer mPlayer = new MediaPlayer();
		
		seekBarAudio.setVisibility(View.GONE);
		btnPlay.setVisibility(View.GONE);
		//alert dialog
	    final AlertDialog alertDialog = new AlertDialog.Builder(this)
		.setView(custom_view)
        .setTitle(getString(R.string.audio_player_title))
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				StorageUtil.deleteMediaAudioExtStg(audioName);
				closeRecorder(mPlayer);
			}
		})
        .setPositiveButton("Record", null) //Set to null. We override the onClick
        .create();

	    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
	    	@Override
		    public void onShow(DialogInterface dialog) {
	    		final CountDownTimer recordTimer = initializeTimer(progressBarAudio, txtViewLeft);
    			final Button btn_cancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		    	final Button btn_record = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		        btn_record.setOnClickListener(new View.OnClickListener() {
		        	int recording_stage;//0 = start record(RECORD), 0 = recording(STOP), 1 = done recording(SEND), 2 = send the audio? 
		    		@Override
		            public void onClick(View view) {
		            	switch(recording_stage) {
		            		case 0 :
		            			btn_record.setText(R.string.audio_player_stop);
		            			recording_stage = 1;
		            			btn_cancel.setVisibility(View.GONE);//don't show cancel button
		            			//start recording timer
		            			recordTimer.start();
		            			//start recording
		            			startRecording(mRecorder);
		            			break;
		            		case 1 :
		            			btn_record.setText(R.string.audio_player_send);
		            			recording_stage = 2;
		            			btn_cancel.setVisibility(View.VISIBLE);
		            			progressBarAudio.setVisibility(View.GONE);
		            			txtViewLeft.setVisibility(View.GONE);
		            			btnPlay.setVisibility(View.VISIBLE);
		            			seekBarAudio.setVisibility(View.VISIBLE);
		            			txtViewRight.setText(R.string.audio_player_start_time);
		            			btnPlay.setOnClickListener(new View.OnClickListener() {
		        		            @Override
		        		            public void onClick(View view) {
		        		            	//play recording
		        		            	playRecording(mPlayer, file.getAbsolutePath(), seekBarAudio, btnPlay, txtViewRight);
		        		            }
		        		         });
		            			//stop recording timer
		            			recordTimer.cancel();
		            			//stop the Recording
		            			if(stopRecording(mRecorder)) { //exception
		            				recording_stage = 0;
			            			closeRecorder(mPlayer);
			            			alertDialog.dismiss();
		            			}
		            			break;
		            		case 2 :
		            			recording_stage = 0;
		            			//send the audio
		            			sendMessage(audioName, MsgKind.M2_AUDIO, MediaStatus.COMPLETED);
		            			//close
		        				closeRecorder(mPlayer);
		            			//dismiss
		            			alertDialog.dismiss();
		            			break;
		            	}
		            }
		        });
		    }
		});
	    alertDialog.setCancelable(false);//so that user can't cancel it via back button
	    alertDialog.show();
	}
	
	public MediaRecorder createMediaRecorder(File file) {
		
		final MediaRecorder mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		mRecorder.setMaxDuration(120000);//2 minutes
		mRecorder.setOutputFile(file.getAbsolutePath());
		
		return mRecorder;
	}
	
	public void startRecording(MediaRecorder mRecorder) {
		try {
			mRecorder.prepare();
			mRecorder.start();
		} catch(Exception ignored) {
		}/* catch (IllegalStateException e) {
		} catch (IOException e) {
		}*/
	}
	
	public Boolean stopRecording(MediaRecorder mRecorder) {
		try {
			mRecorder.stop();
			mRecorder.release();
			return false;//no exception
		} catch (Exception ignored) {
			return true;
		} 	
	}
	
	public MediaPlayer playRecording(final MediaPlayer _mp, String _path, SeekBar sb, final Button btn, final TextView txtViewLength) {
		//not stopping the media player because WhatsApp don't
		//if(mediaPlayer != null && mediaPlayer.isPlaying()) {
		//	stopConversationMediaPlayer(playingAudioItem);
		//}
		
		try {
			//mp will never be null, un-till the message is send or cancel
			_mp.setDataSource(_path);
			_mp.prepare();
			_mp.start();
			btn.setBackgroundResource(R.drawable.inline_audio_pause_normal);//always draw pause
			_mp.setOnCompletionListener(new OnCompletionListener() {
		            @Override
		            public void onCompletion(MediaPlayer _mp) {
		           	_mp.pause();//just pause the media player on complete sound, don't do the null
		           }
	            });
				sb.setMax(_mp.getDuration());
				playingTimer(_mp.getDuration(), sb, 0);
				//sb.setProgress(_mp.getCurrentPosition());
				sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					int progress = 0;
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						if(_mp != null)//check because, when we cancel dialog during play, mp is null but seek bar was accessing mp
							_mp.pause();
					}
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						_mp.seekTo(progress);
						_mp.start();
						playingTimer(_mp.getDuration() - progress, seekBar, progress);
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int _progress, boolean fromUser) {
						progress = _progress;
						final int HOUR = 60*60*1000;
						final int MINUTE = 60*1000;
						final int SECOND = 1000;
						int durationInMillis = _mp.getDuration() - _progress;
						txtViewLength.setText(String.format(Locale.getDefault(), "%02d:%02d",(durationInMillis%HOUR)/MINUTE, (durationInMillis%MINUTE)/SECOND));
					}
				});
		} catch(Exception ignored) {
		}
		return _mp;
	}
	
	private void playingTimer(final int remainingLength, final SeekBar sb, final int currentProgress) {
		if(cDownTimer_audioPlay != null)
			cDownTimer_audioPlay.cancel();
		
		cDownTimer_audioPlay = new CountDownTimer(remainingLength, 100) {
			@Override
			public void onTick(long tick) {
				int progress = (int) (remainingLength - tick);
				sb.setProgress(currentProgress + progress);
			}
			@Override
			public void onFinish() {
				cDownTimer_audioPlay.cancel();
			}
		}.start();
	}
	
	private CountDownTimer initializeTimer(final ProgressBar pb, final TextView tv) {
		pb.setMax(240);//double of 2mins(120secs) = 240 seconds
		return new CountDownTimer(MAXIMUM_VOICE_MESSAGE_LENGTH, 500) {
					double minutes,seconds;
					@Override
					public void onTick(long tickSeconds) {//like 120000, 119000, 118000
						int progress = (int) ((MAXIMUM_VOICE_MESSAGE_LENGTH - tickSeconds) / 500);// this will be done every 1000 milliseconds ( 1 seconds )
						tickSeconds = MAXIMUM_VOICE_MESSAGE_LENGTH - tickSeconds;//this line will make it increasing
						minutes = TimeUnit.MILLISECONDS.toMinutes(tickSeconds);
						seconds = TimeUnit.MILLISECONDS.toSeconds(tickSeconds)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tickSeconds));
						tv.setText(new DecimalFormat("00").format(minutes) + ":" + new DecimalFormat("00").format(seconds));
						pb.setProgress(progress);
						//we don't need to take tension of this timer, because on stop button, this timer will auto cancel
					}
					@Override
					public void onFinish() {
					}
				};
	}
	
	void closeRecorder(MediaPlayer _mp) {
		if(_mp != null && _mp.isPlaying()) {
			_mp.stop();
			_mp.release();
		}
		if(cDownTimer_audioPlay != null)
			cDownTimer_audioPlay.cancel();
	}
	
	private void menuEmailChat() {
		if(PermissionUtil.checkPermissionSilent(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			String content = realmQuery.getEmailContent(CONVERSATION_ID);
			if(!MediaTextUtil.writeTextFile(this, CONVERSATION_NAME, content))
				Toast.makeText(this, "Error mailing conversation", Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
	}

	//____________________________________________________________________________________________________________________________ CAB
	//after day spent on CAB

	//[bug_: UI_List_| 1- sometimes onItemClick does not work - http://stackoverflow.com/a/5568006/4754141]
	//[bug_: UI_List_| 2- select 2 unstar item then star then deselect 1 star and unstar item, it will show the star on already starred item]

	//adapter.setNewSelection(position, true); [trigger_: onItemClick]
	//but sometimes onItemClick does not work
	//1- http://stackoverflow.com/a/5568006/4754141
	//2- http://stackoverflow.com/questions/5551042/onitemclicklistener-not-working-in-listview

	//mActionMode = startActionMode(mActionModeCallback); [triggers_: CAB]

	//Rules
	//CAB must be started from onItemLongClick because if you want to go with the below strategy
		//STRATEGY WRONG
		//1- [trigger_: onItemClick] from onItemLongClick
		//2- [trigger_: CAB] from onItemClick
		//this will fail, because onItemClick sometimes not called from onItemLongClick
		//CODED AND TESTED
	//Therefore
	//STRATEGY RIGHT
		//[trigger_: CAB and onItemClick] both from onItemLongClick

	// Sequence of calling
		//1- onItemLongClick ([trigger_: 2 and 4 using code])
		//2- onCreateActionMode
		//3- onPrepareActionMode
		//4- onItemClick
		//5- onDestroyActionMode
		//*- onActionItemClicked

	private ActionMode mActionMode;
	private Boolean isActionModeFirstTime = false;
	private Boolean _isDoneButtonEnabled = true;
	private Boolean _isCopyButtonEnabled = false;
	private Boolean _isStarButtonEnabled = true;
	private String copiedText = "";
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		//called 1 only (when cab is started)
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			//LogUtil.e("CAB", "2 - onCreateActionMode : ~~~~~~(`.`)~~~~~~" + selectionList.size());
			//LogUtil.e("CAB -> onCreateActionMode", "_isCopyButtonEnabled : " + _isCopyButtonEnabled + ":" + selectionList.size());

			isActionModeFirstTime = true;

			// Inflate the menu for the CAB
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.cab_conversation, menu);
	        
	        return true;
	    }
		
		@Override
		//call everytime (when you select/deselect item)
		//will be called when you Select/UnSelect item, best in case when you select 2 items then UnSelect 1 and you want to show Copy/Info Button 
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			//LogUtil.e("CAB", "3 - onPrepareActionMode : " + selectionList.size());
			//LogUtil.e("CAB -> onPrepareActionMode", "_isCopyButtonEnabled : " + _isCopyButtonEnabled + ":" + selectionList.size());

			//update the CAB title
	    	mode.setTitle(String.format(Locale.getDefault(), "%d", selectionList.size()));

	    	// Here you can perform updates to the CAB due to an invalidate() request
			if(selectionList.size() == 1) {
				//if one item is selected and it is the TEXT item, enable copy button
				if (_isCopyButtonEnabled)
					menu.findItem(R.id.cab_copy).setVisible(true);
				else
					menu.findItem(R.id.cab_copy).setVisible(false);
			} else {
				menu.findItem(R.id.cab_copy).setVisible(false);
			}
			if(_isDoneButtonEnabled)
				menu.findItem(R.id.cab_done).setVisible(true);
			else
				menu.findItem(R.id.cab_done).setVisible(false);

			/*
			if(_isStarButtonEnabled)
				menu.findItem(R.id.cab_star).setIcon(R.drawable.ic_action_star);
			else
				menu.findItem(R.id.cab_star).setIcon(R.drawable.ic_action_unstar);
			*/
	    	return true;
	    }

	    @Override
		//call everytime(when you tap cab item on toolbar)
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			//LogUtil.e("CAB", "* - onActionItemClicked : " + selectionList.size());
	    	//LogUtil.e("CAB -> onActionItemClicked", "_isCopyButtonEnabled : " + _isCopyButtonEnabled + ":" + selectionList.size());

	    	// Respond to clicks on the actions in the CAB
	        switch (item.getItemId()) {
				case R.id.cab_done:
					doneSelectedItems(selectionList, mode);
					return true;
				/*case R.id.cab_star:
					starSelectedItems(selectionList, mode);
					return true;*/
	            case R.id.cab_delete:
	            	deleteSelectedItems(selectionList, mode);
	            	//adapter.clearSelection();
                    //mode.finish(); // Action picked, so close the CAB
	                return true;
	            case R.id.cab_copy:
					ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	            	ClipData clip = ClipData.newPlainText("Message Copied", copiedText);
	            	clipboard.setPrimaryClip(clip);
	            	Toast.makeText(Conversation.this, "Message Copied", Toast.LENGTH_SHORT).show();
	            	mode.finish(); // Action picked, so close the CAB
					return true;
	            case R.id.cab_forward:
	            	forwardSelectedItems();
	                //adapter.clearSelection();
                    //mode.finish(); // Action picked, so close the CAB
	                return true;
	            default:
	                return false;
	        }
	    }

	    @Override
		//called 1 only (when cab is destroyed)
	    public void onDestroyActionMode(ActionMode mode) {
			//LogUtil.e("CAB", "5 - onDestroyActionMode : " + selectionList.size());

	    	selectionList.clear();
			_isDoneButtonEnabled = true;
	    	_isCopyButtonEnabled = false;
			_isStarButtonEnabled = true;
	    	copiedText = "";
	    	
	    	adapter.clearSelection();
	    	adapter.notifyDataSetChanged();
	    	//Here you can make any necessary updates to the activity when
	        //the CAB is removed. By default, selected items are not-selected/unchecked.
	    	
	    	//very important 
	    	mActionMode = null;
			isActionModeFirstTime = false;
		}
	};


	/**
	 * @param position ConversationItem _item = (ConversationItem) adapter.getItem(position);
	 *                 this item is the one what is clicked
	 */
	@Override
	//call everytime (when you select/deselect item)
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		//LogUtil.e("CAB", "4- onItemClick : " + selectionList.size());

		//Do when
		//1- if CAB is not null
		//2- not isActionModeFirstTime
		if (mActionMode != null && !isActionModeFirstTime) {
			//contextual action mode
			//http://developer.android.com/guide/topics/ui/menus.html

			Notes note = (Notes)adapter.getItem(position);
			//[bug_: *WARNING* should be placed first]
			//Avoid Downloading/Uploading/Compressing Media From Delete
			if(AppUtil.isMsgMedia(note.getMsgKind())) {
				if (note.getMediaStatus() == MediaStatus.COMPRESSING_MEDIA) {
					Toast.makeText(this, "Cannot select media item at the moment.", Toast.LENGTH_SHORT).show();
					return;
				}
			}

			//adding or removing item from selection list
			if (adapter.isPositionChecked(position)) {
				selectionList.remove(selectionList.indexOf(note.getMsgId())); //casting to Integer, very important
				adapter.removeSelection(position);
				//if its 0, close
				if (selectionList.size() == 0) {
					mActionMode.finish();
					return;
				}
			} else {
				if (selectionList.size() < AppConst.MAX_FORWARD_MESSAGES) {
					selectionList.add(note.getMsgId());
					adapter.setNewSelection(position, true);
				} else
					Toast.makeText(this, "Maximum forward notes limit reached", Toast.LENGTH_SHORT).show();
			}

			//LogUtil.e("CAB", "is return statement taking me out of function? YES");

			if (selectionList.size() == 1) {
				cabDoWorkForSingleItem(note);
			} else {
				_isCopyButtonEnabled = false;
				copiedText = "";
				//for more than 1 selected items
				//draw DONE on basis of last item clicked,
				//not problem as database will only be updated for items that do not have old value of msg_timestamp_done
				if(note.getMsgStatus() == MsgStatus.CLOCK)
					_isDoneButtonEnabled = true;
				else
					_isDoneButtonEnabled = false;
				//for more than 1 selected items
				//if ANY one of the selected item is not starred, draw star for all notes, otherwise draw un-star
				if (note.getMsgStar() == MsgStar.NO)
					_isStarButtonEnabled = true;
			}

//			  //[bug_: UI_List_| *EFFICIENT CODE* I was using multiple loops which was not required]
//            //if the first item in the List is TEXT, then we can enable Copy Button
//            if (!conversationList.isEmpty() && !selectionList.isEmpty())
//                for (int i = 0; i < conversationList.size(); i++)
//                    if (conversationList.get(i).itemType().equals(ItemType.CONVERSATION_NOTE)) {
//                        ConversationItem item = (ConversationItem) conversationList.get(i);
//                        if (item.msgID.equals(selectionList.get(0))) {//****** ClassCastException Error
//                            if (item.msgFlow == MsgFlow.SEND)
//                                _isInfoButtonEnabled = true;
//                            if (item.msgKind == MsgKind.TEXT) {
//                                _isCopyButtonEnabled = true;
//                                copiedText = item.msg; //this is perfectly fine
//                                break;
//                            } else if (!item.mediaCaption.equals(MsgConstant.DEFAULT_MEDIA_CAPTION) && AppUtil.isMsgHasMediaCaption(item.msgKind)) {
//                                _isCopyButtonEnabled = true;
//                                copiedText = item.mediaCaption; //this is perfectly fine
//                                break;
//                            }
//                        }
//                    }

			//LogUtil.e("CAB -> onItemCheckedStateChanged", "_isCopyButtonEnabled : " + _isCopyButtonEnabled + ":" + selectionList.size());

			//invalidate() will call onPrepareActionMode so that Info/Copy/etc button can be displayed
			mActionMode.invalidate();

			adapter.notifyDataSetChanged();
		}

		//just to disable the single click first time
		if (isActionModeFirstTime)
			isActionModeFirstTime = false;
    }
	
	@Override
	//[os_lollipop_: material_design] Steps to make ListView like WhatsApp, (onItemClick, onItemLongClick, ActionMode)
	//Set android:longClickable="true" in Parent (Father) RelativeLayout of all items (text, image, video etc ..)

	//any Long Click in any inner View(TextView, RelativeLayout) of the Item of the ListView will take us here,
	//and from here we will launch CAB
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		//LogUtil.e("CAB", "1 - onItemLongClick : " + selectionList.size());
		//LogUtil.e("CAB", "1 - onItemLongClick : " + position);

		//http://stackoverflow.com/questions/9268219/selected-item-on-custom-listview-with-contextual-action-bar
		//The call to setItemChecked will result in StackOverflowError because calling setItemChecked in onItemCheckedStateChanged will be just calling each other infinitely
		//listview.setItemChecked(position, !adapter.isPositionChecked(position)); //[trigger_: CAB] on Long Press

		//avoid using MultiChoiceModeListener rather use Action Mode also for ListView
		//http://stackoverflow.com/questions/14128558/activate-cab-menu-when-onclickevent-happens-in-android
		//because you can start Action Mode whenever you want, and don't need any hack
		//http://stackoverflow.com/questions/14128558/activate-cab-menu-when-onclickevent-happens-in-android
		//http://developer.android.com/guide/topics/ui/menus.html

		//do nothing if CAB is ON
		Notes note = (Notes) adapter.getItem(position);

		//Do not do anything
		//1- if CAB is not null OR
		//2- user selected media progress item
		if (mActionMode != null || cabIsMediaItemInMiddleOfSomething(note)) {
            return false;
        }

		cabDoWorkForSingleItem(note);
		selectionList.add(note.getMsgId());
		mActionMode = startActionMode(mActionModeCallback);
		//[bug_: UI_List_| still present - http://stackoverflow.com/a/5568006/4754141]
		//sometimes onItemClick does not work //http://stackoverflow.com/questions/5551042/onitemclicklistener-not-working-in-listview
		adapter.setNewSelection(position, true);

		return true;

    }

	boolean cabIsMediaItemInMiddleOfSomething(Notes note) {
		//avoid Downloading/Uploading/Compressing Media from delete/forward/etc
		if(AppUtil.isMsgMedia(note.getMsgKind())) {
			if (note.getMediaStatus() == MediaStatus.COMPRESSING_MEDIA) {
				Toast.makeText(this, "Cannot select media item at the moment.", Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		return false;
	}

	void cabDoWorkForSingleItem(Notes note) {
		//done
		if(note.getMsgStatus() == MsgStatus.CLOCK)
			_isDoneButtonEnabled = true;
		else
			_isDoneButtonEnabled = false;
		//copy
		if (note.getMsgKind()==MsgKind.TEXT || note.getMsgKind()==MsgKind.LINK) {
			_isCopyButtonEnabled = true;
			copiedText = note.getMsg();
		} else if(AppUtil.isMsgHasMediaCaption(note.getMsgKind()) && !note.getMediaCaption().equals(MsgConstant.DEFAULT_MEDIA_CAPTION)) {
			_isCopyButtonEnabled = true;
			copiedText = note.getMediaCaption();
		}
		//star
		if(note.getMsgStar() == MsgStar.YES)
			_isStarButtonEnabled = false;
	}


//____________________________________________________________________________________________________________________________ ACTIVITY


	@Override
	protected void onResume() {
		super.onResume();
		AppConst.ACTIVE_WINDOW = IntentKeys.CONVERSATION_STATE_RESUMED;
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppConst.ACTIVE_WINDOW = IntentKeys.CONVERSATION_STATE_PAUSED;
		clearCache();
	}

	@Override
    public void onDestroy() {
		super.onDestroy();
		//Don't Do CONVERSATION_ID = "-" if Conversation is in Resume State (This will only happen in Case of Forward)
		if(AppConst.ACTIVE_WINDOW.equals(IntentKeys.CONVERSATION_STATE_PAUSED))
			CONVERSATION_ID = MsgConstant.DEFAULT_CHAT_ID;
		realm.close();
        lbmUnregister();
		//stop the Audio Player
		AdapterConversation.stopAudioPlayer();
	}

	@Override
	/* only in case of Hardware back button */
	public void onBackPressed() {
		super.onBackPressed();
		CONVERSATION_DRAFT = editText.getText().toString();
		realmQuery.updateChatDraft(CONVERSATION_ID, CONVERSATION_DRAFT);
		//this.finish(); , not required, automatically done by super.onBackPressed();
	}

	//_________________________________________________________________________________________________________________________________
	void clearCache() {
		imageLoaderList.clearCache();
		//imageLoaderList = null;
	}
	
//________________________________________________________________________________________________________________________ REGISTER LBM
	void lbmRegister() {
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmChangeName, 		new IntentFilter(IntentKeys.LBM_CHANGE_NAME));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmChangeImage, 		new IntentFilter(IntentKeys.LBM_CHANGE_IMAGE));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmNewNoteConversation,new IntentFilter(IntentKeys.LBM_NEW_NOTE_CONVERSATION));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmNewNoteMedia,		new IntentFilter(IntentKeys.LBM_NEW_NOTE_MEDIA));
		LocalBroadcastManager.getInstance(this).registerReceiver(lbmMediaStatus,		new IntentFilter(IntentKeys.LBM_MEDIA_STATUS));
	}
	
//________________________________________________________________________________________________________________________ UNREGISTER LBM
	void lbmUnregister() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmChangeName);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmChangeImage);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmNewNoteConversation);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmNewNoteMedia);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(lbmMediaStatus);
	}



//...
//..
//.
//__________________________________________________________________________________________________________ AUDIO ANIMATION
	void initializeBottomBar() {
		rootView 			= findViewById(R.id.rootView);
		editText 			= (ConversationTextEntry)   findViewById(R.id.conversation_editText);
        btn_send 			= (ImageButton)             findViewById(R.id.conversation_btn_send_text);
        btn_emoji 			= (ImageButton)             findViewById(R.id.conversation_btn_send_emoji);
		btn_camera 			= (ImageButton)             findViewById(R.id.conversation_btn_send_camera);
		btn_mic 			= (ImageButton)             findViewById(R.id.conversation_btn_send_audio);
        audio_red_mic 		= (ImageButton)             findViewById(R.id.conversation_bar_audio_red_mic);
        ll_conversation_btn = (LinearLayout)            findViewById(R.id.conversation_btn_ll);
		audio_layout 		= (LinearLayout)            findViewById(R.id.conversation_bar_ll_audio);
		chat_layout 		= (LinearLayout)            findViewById(R.id.conversation_bar_ll_chat);
        tv_recording_timer	= (TextView)                findViewById(R.id.conversation_bar_audio_timer);

		conversation_link_bar = (RelativeLayout) findViewById(R.id.conversation_link_bar);
		conversation_link_bar_title = (TextView) findViewById(R.id.conversation_link_bar_title);
		conversation_link_bar_description = (TextView) findViewById(R.id.conversation_link_bar_description);
		conversation_link_bar_url = (TextView) findViewById(R.id.conversation_link_bar_url);
		conversation_link_bar_image = (ImageView) findViewById(R.id.conversation_link_bar_image);
		conversation_link_bar_cancel = (ImageView) findViewById(R.id.conversation_link_bar_cancel);
		conversation_link_bar_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LINK_USER_FORCE_CLOSE = true;
				hideLinkPreviewBar();
			}
		});

		//bottom bar
        anim_send_txt_btn_fade_in 		= AnimationUtils.loadAnimation(this, R.anim.send_text_btn_fade_in);
		anim_send_mic_btn_fade_out 		= AnimationUtils.loadAnimation(this, R.anim.send_mic_btn_fade_out);
		anim_send_mic_btn_scale_in		= AnimationUtils.loadAnimation(this, R.anim.send_mic_btn_scale_in);
		anim_send_btn_bg_scale_in		= AnimationUtils.loadAnimation(this, R.anim.send_btn_bg_scale_in);
        anim_send_btn_bg_scale_out      = AnimationUtils.loadAnimation(this, R.anim.send_btn_bg_scale_out);
        anim_camera_slide_left_to_right	= AnimationUtils.loadAnimation(this, R.anim.camera_slide_left_to_right);
		anim_camera_slide_right_to_left	= AnimationUtils.loadAnimation(this, R.anim.camera_slide_right_to_left);
		anim_audio_slide_left_to_right 	= AnimationUtils.loadAnimation(this, R.anim.audio_slide_left_to_right);
		anim_audio_mic_blink 			= AnimationUtils.loadAnimation(this, R.anim.audio_mic_blink);
        //list
        //anim_fade_out 				= AnimationUtils.loadAnimation(this, R.anim.fade_out);

        //Calculating Screen Width, It will use in OnTouchListener
		Display _display = getWindowManager().getDefaultDisplay();
		Point _size = new Point();
		_display.getSize(_size);
		screen_width = _size.x;
		btn_mic.setOnTouchListener(this);
		
		initializeEmojiPopup();
	}
	
	
//______________________________________________________________________________________________________ ANIMATIONS
	private CountDownTimer initializeRecordingTimer(final TextView tv) {
		CountDownTimer __cdTimer;
		__cdTimer =  new CountDownTimer(MAXIMUM_VOICE_MESSAGE_LENGTH, 1000) {
				double seconds, minutes;
				@Override
				public void onTick(long tickSeconds) {//like 120000, 119000, 118000
					tickSeconds = MAXIMUM_VOICE_MESSAGE_LENGTH - tickSeconds;//this line will make it increasing
					minutes = TimeUnit.MILLISECONDS.toMinutes(tickSeconds);
					seconds = TimeUnit.MILLISECONDS.toSeconds(tickSeconds)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tickSeconds));
					
					//create audio file and start recording only if 1 second is passed
					if(seconds == 1.0) {
						AR_stage = 1;
						AR_audio_name = PathUtil.generateFileNameUnix(MimeType.MEDIA_AUDIO);
						final File file = PathUtil.createExternalMediaAudioFile_Sent(AR_audio_name);
						AR_mRecorder = createMediaRecorder(file);
						//start recording
						startRecording(AR_mRecorder);
					}
					
					tv.setText(new DecimalFormat("00").format(minutes) + ":" + new DecimalFormat("00").format(seconds));
					//we don't need to take tension of this timer, because on stop button, this timer will auto cancel
				}
				@Override
				public void onFinish() {
					AR_send_Message(AR_audio_name, AR_mRecorder);
				}
			};
		return __cdTimer;
	}
	
	
	//Global Media Recorder for Recording Audio 
	MediaRecorder AR_mRecorder;
	String AR_audio_name;
	int AR_stage = 0;
	
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
//________________________
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			List<String> _permissionList = new ArrayList<>();
			_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			_permissionList.add(Manifest.permission.RECORD_AUDIO);
			//PermissionUtil.PERMISSION_MULTIPLE_AUDIO because you need to press and hold the button to record
			boolean checkPermission = PermissionUtil.checkMultiplePermissions(this, _permissionList, getString(R.string.permission_storage_mic_send_request), getString(R.string.permission_storage_mic_send), R.drawable.permission_storage, R.drawable.permission_mic, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_MULTIPLE_AUDIO);
			if(checkPermission)
				recordVoiceMessage();
		}
		
//________________________
		if (event.getAction() == MotionEvent.ACTION_MOVE && _micLongClick && event.getRawX() < (screen_width/2)) //event.getRawX() = x
			stopVoiceMessage(event, AR_mRecorder, AR_audio_name);
		
//________________________
		if (event.getAction() == MotionEvent.ACTION_UP && _micLongClick)
			stopVoiceMessage(event, AR_mRecorder, AR_audio_name);

		// if left to right sweep event on screen x1 < x2
		// if UP to Down sweep event on screen y1 < y2
		// if Down to UP sweep event on screen y1 > y2
		// if right to left sweep event on screen x1 > x2

		return false;
	}

	void recordVoiceMessage() {
		//LogUtil.e(getClass().getSimpleName(), "recordVoiceMessage");
		//Fix Orientation, so that it can not be changed during recording
		int orientation = getResources().getConfiguration().orientation;
		switch(orientation) {
			case Configuration.ORIENTATION_LANDSCAPE:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			case Configuration.ORIENTATION_PORTRAIT:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
		}

		listview.setEnabled(false);

		MediaAudioUtil.playSound(R.raw.audio_start);

		btn_mic.getParent().requestDisallowInterceptTouchEvent(true);
		chat_layout.getParent().requestDisallowInterceptTouchEvent(true);
		audio_layout.getParent().requestDisallowInterceptTouchEvent(true);

		audio_red_mic.startAnimation(anim_audio_mic_blink);
		_micLongClick = true;
		ll_conversation_btn.startAnimation(anim_send_btn_bg_scale_in);
		chat_layout.setVisibility(View.GONE);
		audio_layout.startAnimation(anim_audio_slide_left_to_right);
		audio_layout.setVisibility(View.VISIBLE);

		//Start Recording Timer
		//Create Recording File .aac in initializeRecordingTimer if seconds > 1
		recording_timer = initializeRecordingTimer(tv_recording_timer);
		recording_timer.start();
	}
	
	void stopVoiceMessage(MotionEvent _event, MediaRecorder m, String audio_name) {
		//LogUtil.e(getClass().getSimpleName(), "stopVoiceMessage");
		
		float x_up = _event.getX();
		if(AR_stage == 0)//Recording cancel (Fast)
			AR_do_nothing();
		else if(Math.abs(x_up) > (screen_width / 4)) //Recording cancel (Bin Animation)
			AR_dustbin_animation(m);
		else
			AR_send_Message(audio_name, m);
	}
	
	void finish_Stuff() {
        ll_conversation_btn.startAnimation(anim_send_btn_bg_scale_out); //it will scale down from 200% to 100%
		editText.requestFocus(); //don't lose focus
		listview.setEnabled(true);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		_micLongClick = false;
		tv_recording_timer.setText("");
		recording_timer.cancel();
		AR_stage = 0;
	}
	
//____________________________________________________
	void AR_do_nothing() {
		//************************
		audio_layout.setVisibility(View.GONE);
		chat_layout.setVisibility(View.VISIBLE);
        MediaAudioUtil.playSound(R.raw.audio_error);
		//************************
		finish_Stuff();
	}

    void AR_dustbin_animation(final MediaRecorder m) {

        //************************
        audio_layout.setVisibility(View.GONE);
        chat_layout.setVisibility(View.VISIBLE);
        MediaAudioUtil.playSound(R.raw.audio_error);
        AndroidUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                AndroidUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //************************ placed here because [time consuming operation]
                        stopRecording(m);    //stop the Recording [time consuming operation]
                    }
                }, 50);
            }
        }, 1000);
        //************************
        StorageUtil.deleteMediaAudioExtStg(AR_audio_name); //delete External File
        finish_Stuff();
    }

	void AR_send_Message(String audio_name, MediaRecorder m) {
		//Message Sending Condition
		audio_layout.setVisibility(View.GONE);
		chat_layout.setVisibility(View.VISIBLE);
		MediaAudioUtil.playSound(R.raw.audio_stop);
		
		//************************
		stopRecording(m);	//stop the Recording [time consuming operation]
		finish_Stuff();
		
		//send the audio
		sendMessage(audio_name, MsgKind.M2_AUDIO, MediaStatus.COMPLETED);
	}


//...
//..
//.
//__________________________________________________________________________________________________________ EMOJICON
	public void onClickBtnEmoji(View view) {
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
	
	EmojiconsPopup popup;
	
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

		//On emoji clicked, add it to editText
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
    void setConversationBackground() {
        //Setting background Wall Paper
        //getWindow().setBackgroundDrawableResource(WallpaperUtil.getWallpaperDrawable(Prefs.getWallpaperNumber(this)));
        if(Prefs.getWallpaperNumber(this) == WallpaperUtil.NONE_WALLPAPER_NUMBER)
            ((ImageView) findViewById(R.id.imgView_Conversation)).setImageDrawable(null);
        else
            ((ImageView) findViewById(R.id.imgView_Conversation)).setImageResource(WallpaperUtil.getWallpaperDrawable(Prefs.getWallpaperNumber(this)));

    }

//__________________________________________________________________________________________________ CONVERSATION ATTACHMENT MENU
	void initializeConversationMenu() {
		attachmentLayout = (LinearLayout) findViewById(R.id.conversation_menu_parent);

		ImageButton btnImage = (ImageButton) findViewById(R.id.menu_attachment_image);
		ImageButton btnVideo = (ImageButton) findViewById(R.id.menu_attachment_video);
		ImageButton btnCamera = (ImageButton) findViewById(R.id.menu_attachment_camera);
		ImageButton btnAudio = (ImageButton) findViewById(R.id.menu_attachment_audio);
		ImageButton btnLocation = (ImageButton) findViewById(R.id.menu_attachment_location);
		ImageButton btnContact = (ImageButton) findViewById(R.id.menu_attachment_contact);

		btnImage.setOnClickListener(this);
		btnVideo.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
		btnAudio.setOnClickListener(this);
		btnLocation.setOnClickListener(this);
		btnContact.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		hideMenu();

		switch (v.getId()) {
			case R.id.menu_attachment_image:
				attachmentGalleryPhoto();
				break;
			case R.id.menu_attachment_video:
				attachmentGalleryVideo();
				break;
			case R.id.menu_attachment_camera:
				String[] menuItems = {"Image", "Video"};
				AlertDialog.Builder builder = new AlertDialog.Builder(Conversation.this);
				builder.setItems(menuItems, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int position) {
						switch (position) {
							case 0:
								boolean checkPermissionCamImg = PermissionUtil.checkPermission(Conversation.this, Manifest.permission.CAMERA, getString(R.string.permission_cam_send_request), getString(R.string.permission_cam_send), R.drawable.permission_cam, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_CAMERA_IMAGE);
								if(checkPermissionCamImg)
									attachmentCameraPhoto();
								break;
							case 1:
								boolean checkPermissionCamVid = PermissionUtil.checkPermission(Conversation.this, Manifest.permission.CAMERA, getString(R.string.permission_cam_send_request), getString(R.string.permission_cam_send), R.drawable.permission_cam, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_CAMERA_VIDEO);
								if(checkPermissionCamVid)
									attachmentCameraVideo();
								break;
						}
					}
				});
				builder.create();
				builder.show();
				break;
			case R.id.menu_attachment_audio:
				List<String> _permissionList = new ArrayList<>();
				_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
				_permissionList.add(Manifest.permission.RECORD_AUDIO);
				boolean checkPermissionAudio = PermissionUtil.checkMultiplePermissions(this, _permissionList, getString(R.string.permission_storage_mic_send_request), getString(R.string.permission_storage_mic_send), R.drawable.permission_storage, R.drawable.permission_mic, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_MULTIPLE_AUDIO_DIALOG);
				if(checkPermissionAudio)
					attachmentAudioRecorder();
				break;
			case R.id.menu_attachment_location:
				attachmentLocation();
				break;
			case R.id.menu_attachment_contact:
				attachmentContact();
				break;
		}
	}

	void showMenuBelowLollipop() {
		int cx = (attachmentLayout.getLeft() + attachmentLayout.getRight());
		int cy = attachmentLayout.getTop();
		int radius = Math.max(attachmentLayout.getWidth(), attachmentLayout.getHeight());

		try {
			SupportAnimator animator = ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, 0, radius);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.setDuration(300);

			if (isHidden) {
				//LogUtil.e(getClass().getSimpleName(), "showMenuBelowLollipop");
				attachmentLayout.setVisibility(View.VISIBLE);
				animator.start();
				isHidden = false;
			} else {
				SupportAnimator animatorReverse = animator.reverse();
				animatorReverse.start();
				animatorReverse.addListener(new SupportAnimator.AnimatorListener() {
					@Override
					public void onAnimationStart() {
					}

					@Override
					public void onAnimationEnd() {
						//LogUtil.e("MainActivity", "onAnimationEnd");
						isHidden = true;
						attachmentLayout.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onAnimationCancel() {
					}

					@Override
					public void onAnimationRepeat() {
					}
				});
			}
		} catch (Exception e) {
			//LogUtil.e(getClass().getSimpleName(), "try catch");
			isHidden = true;
			attachmentLayout.setVisibility(View.INVISIBLE);
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	void showMenu() {
		int cx = (attachmentLayout.getLeft() + attachmentLayout.getRight());
		int cy = attachmentLayout.getTop();
		int radius = Math.max(attachmentLayout.getWidth(), attachmentLayout.getHeight());

		if (isHidden) {
			Animator anim = android.view.ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, 0, radius);
			attachmentLayout.setVisibility(View.VISIBLE);
			anim.start();
			isHidden = false;
		} else {
			Animator anim = android.view.ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, radius, 0);
			anim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					attachmentLayout.setVisibility(View.INVISIBLE);
					isHidden = true;
				}
			});
			anim.start();
		}
	}

	private void hideMenu() {
		attachmentLayout.setVisibility(View.GONE);
		isHidden = true;
	}

//_____________________________________________________________________________ NOTIFYING_ACTIVITY_FROM_ACTIVITY
	void lbmMsgStatus(long chatID, String msgID, int msgStatus) {
		Intent in = new Intent(IntentKeys.LBM_MSG_STATUS);
		in.putExtra(IntentKeys.CHAT_ID, chatID);
		in.putExtra(IntentKeys.MSG_ID, msgID);
		in.putExtra(IntentKeys.MSG_STATUS, msgStatus);
		LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	}

	void lbmNewNoteChat(Chats chat) {
        Intent in = new Intent(IntentKeys.LBM_NEW_NOTE_CHAT);
        in.putExtra(IntentKeys.PARCELABLE_CHAT_ITEM, chat);
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }

	public void updateChatScreenEmpty() {
		Chats chat = new Chats();
		chat.setChatId(CONVERSATION_ID);
		chat.setChatName(CONVERSATION_NAME);
		Notes note = realmQuery.getSingleNoteChatCreated(CONVERSATION_ID);
		chat.setNote(note);
		lbmNewNoteChat(chat);
	}
}



//____________
/*
 conversationBundle can have following values
 String : CHAT_NAME
 String : CHAT_ID

 //Forward
 IntegerArrayList : forward_mid_list
 
 //Share
 String : video_uri
 String : media_name
 String : shared_image_uri
 String : shared_text_msg
 */




