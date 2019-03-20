package com.chattynotes.adapters.listConversation;

import com.chattynotes.adapters.listConversation.model.ConversationDateSeparatorItem;
import com.chattynotes.adapters.listConversation.model.ConversationLoadEarlierItem;
import com.chattynotes.adapters.listConversation.model.ConversationUnknownItem;
import com.chattynotes.adapters.listConversation.model.InterfaceConversation;
import com.chattynotes.application.App;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.mvp.activities.Conversation;
import com.chattynotes.async.VideoThumbnailForConversationList;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.ItemType;
import com.chattynotes.constant.MimeType;
import com.chattynotes.constant.Msg;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.MsgFlow;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.constant.MsgStar;
import com.chattynotes.constant.MsgStatus;
import com.chattynotes.constant.keyboard.KeyboardVar;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.adapters.listConversation.utility.InitializeUtil;
import com.chattynotes.adapters.listConversation.utility.MsgAddressUtil;
import com.chattynotes.adapters.listConversation.viewholders.ViewHolder;
import com.chattynotes.adapters.location.FoursquareItem;
import com.chattynotes.preferences.Prefs;
import com.chattynotes.R;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.DateUtil;
import com.chattynotes.util.ShareUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.mvp.activities.MediaViewer;
import com.chattynotes.mvp.activities.TextViewer;
import com.chattynotes.mvp.activities.GalleryVideoGrid.ThumbnailCache;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.joda.time.DateTime;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.content.FileProvider;
import android.text.method.LinkMovementMethod;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.chattynotes.emojicon.EmojiconTextView;
import com.chattynotes.util.androidos.GenericFileProvider;
import com.chattynotes.util.textformatter.TextFormatter;
import com.chattynotes.adapters.listConversation.viewholders.ViewHolder.*;
import com.squareup.picasso.Picasso;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

//import static com.chattynotes.util.media.MediaThumbUtil.THUMB_NOT_FOUND;

public class AdapterConversation extends ArrayAdapter<InterfaceConversation> {

	private Context context;
	private ArrayList<InterfaceConversation> conversationList = null;
	private LayoutInflater inflater;
	private QueryNotesDB realmQuery;
	//image
	public ImageLoaderPath imageLoader;
	private ThumbnailCache  mCache; //for Video Thumbs
	
	public AdapterConversation(Context ctx, ArrayList<InterfaceConversation> _conversationList, ImageLoaderPath _imageLoader, QueryNotesDB _realmQuery) {
		super(ctx, 0, _conversationList);
		context = ctx;
		realmQuery = _realmQuery;
		conversationList = _conversationList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = _imageLoader;
		
		//LRU Cache for Video Thumbnail
		// Pick cache size based on memory class of device
	    final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    final int memoryClassBytes = am.getMemoryClass() * 1024 * 1024;
	    mCache = new ThumbnailCache(memoryClassBytes / 2);
	}
	
	//------------------------------------------------------ METHODS FOR MULTIPLE SELECTION
	private SparseBooleanArray mSelection = new SparseBooleanArray();

	public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }
    public boolean isPositionChecked(int position) {
		return mSelection.get(position);
    }
    public void removeSelection(int position) {
        mSelection.delete(position);
        notifyDataSetChanged();
    }
    public void clearSelection() {
        mSelection.clear();
    }

	
//--------------------------------------------------------------------------------------
	@Override
	public int getCount() {
		return conversationList.size();
	}

	@Override
	public InterfaceConversation getItem(int position) {
		return conversationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
//---------------------------------------------------------------------------------------------
//http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter
	@Override
	public int getItemViewType(int position) {
		final InterfaceConversation i = conversationList.get(position);
		switch(i.itemType()) {
			case ItemType.CONVERSATION_NOTE:
				Notes note = (Notes) i;
				return note.getMsgKind(); //0 - 22
			case ItemType.CONVERSATION_DATE:
				return MsgKind.date;
			case ItemType.CONVERSATION_LOAD:
				return MsgKind.load_earlier;
			default:
				return MsgKind.unknown;
		}
		//return super.getItemViewType(position);
	}
	
	@Override
	public int getViewTypeCount() {
		//Beware! getItemViewType() must return an int between 0 and getViewTypeCount() - 1
	    return MsgKind.unknown + 10; //(-1 to 12 is equal to 14), keeping it 109 on the safe side
	}

//___________________________________________________________________________________________________________
	@NonNull
	@SuppressLint("CutPasteId")
	@Override
	public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
		
		int viewType = this.getItemViewType(position);
		InterfaceConversation i = conversationList.get(position);
		final Notes note;
		final ListView listview = (ListView)parent;
		switch (viewType) {
//---------------------------------------------------------------------------------------------------->>>>   DATE
			case MsgKind.date:
	    	   Date holderDate;
	           if (convertView == null) { 
	        	    holderDate =  new Date();
	                convertView = inflater.inflate(R.layout.list_conversation_item_date_seperator, parent, false);
	                convertView.setOnClickListener(null);
				    convertView.setOnLongClickListener(null);
				    convertView.setLongClickable(false);
					InitializeUtil.date(holderDate, convertView);
				    holderDate.txtViewDateSeparator = dateLayouting(holderDate.txtViewDateSeparator);
				    convertView.setTag(holderDate);
	           } else { 
	        	   holderDate = (Date)convertView.getTag();
	           }
	           ConversationDateSeparatorItem dateSeparatorItem = (ConversationDateSeparatorItem) i;
	           holderDate.txtViewDateSeparator.setText(String.format("%s", dateSeparatorItem.getDate()));
	           return convertView;
//---------------------------------------------------------------------------------------------------->>>>   LOAD EARLIER
			case MsgKind.load_earlier:
				LoadEarlier holderLoadEarlier;
				if (convertView == null) {
					holderLoadEarlier =  new LoadEarlier();
					convertView = inflater.inflate(R.layout.list_conversation_item_load_earlier, parent, false);
					InitializeUtil.loadEarlier(holderLoadEarlier, convertView);
					convertView.setTag(holderLoadEarlier);
				} else {
					holderLoadEarlier = (LoadEarlier)convertView.getTag();
				}

				holderLoadEarlier.txtViewLoadEarlier.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						int NO_OF_MESSAGES_FETCHED = 0;
						int DATE_ITEMS_IN_PREVIOUS_LIST = 0;
						int DATE_ITEMS_IN_CURRENT_LIST = 0;

						//______ CAB
						ArrayList<String> _selectionList = new ArrayList<>();
						if(mSelection.size() > 0) {
							//since Lists positions will change after LoadEarlierButton is pressed therefore,
							//store MIDs of selected items so that we can reselect the positions again
							for(int i=0; i<mSelection.size(); i++) {
								Notes note = (Notes) conversationList.get(mSelection.keyAt(i));
								_selectionList.add(note.getMsgId());
								//LogUtil.e("AdapterConversation", "CAB - MID: " + item.msgID);
							}
						}

						//______ LIST
						//temporary lists to fetch old message from DB
						ArrayList<InterfaceConversation> _tempList = realmQuery.getNoteList(Conversation.CONVERSATION_ID, Conversation.MESSAGES_LAST_FETCH_ROW);
						Conversation.MESSAGES_LAST_FETCH_ROW += _tempList.size();

						//add all the notes + load earlier (if require) to tempList
						if(_tempList.size() > 0) {

							//---------------------------->>> LOAD EARLIER
							if(Conversation.MESSAGES_LAST_FETCH_ROW < Conversation.MESSAGES_COUNT)
								_tempList.add(0, new ConversationLoadEarlierItem());

							NO_OF_MESSAGES_FETCHED = _tempList.size();

							//append new notes to tempList, (not the date, load earlier, unread)
							for(int i=0; i<conversationList.size(); i++)
								if(conversationList.get(i).itemType().equals(ItemType.CONVERSATION_NOTE))
									_tempList.add(conversationList.get(i));
								else if(conversationList.get(i).itemType().equals(ItemType.CONVERSATION_DATE))
									DATE_ITEMS_IN_PREVIOUS_LIST++;

							//empty the conversationList so that we can add notes + date
							conversationList.clear();

							//now adding notes + date in conversationList
							int today = new DateTime().getDayOfYear();
							int tempDay = 0;
							for(int i = 0; i < _tempList.size(); i++) {
								//here tempList may have both ConversationItem + LoadEarlierItem
								if(_tempList.get(i).itemType().equals(ItemType.CONVERSATION_NOTE)) {
									Notes note = (Notes) _tempList.get(i);

									//---------------------------->>> DATE TIME
									DateTime date =  new DateTime(note.getMsgTimestamp());
									int dbDay = date.getDayOfYear();
									if(today >= dbDay) {
										if(tempDay != dbDay) {
											conversationList.add(new ConversationDateSeparatorItem(date.toString("dd MMMM yyyy").toUpperCase(Locale.getDefault())));
											tempDay = dbDay;
											DATE_ITEMS_IN_CURRENT_LIST++;
										}
									}
								}
								//append old notes
								conversationList.add(_tempList.get(i));
							}
						}

						//______ CAB
						//here conversationList is updated, so select the CAB items
						if(mSelection.size() > 0) {
							//remove all old selections
							mSelection.clear();
							//check notes msgID, (not the date, load earlier, unread)
							for(int i=0; i<conversationList.size(); i++)
								if(conversationList.get(i).itemType().equals(ItemType.CONVERSATION_NOTE)) {
									Notes note = (Notes) conversationList.get(i);
									//another loop, pick 1 msgID and loop through other list, repeat
									for(int j=0; j<_selectionList.size(); j++) {
										if(note.getMsgId().equals(_selectionList.get(j))) {
											mSelection.put(i, true);
											_selectionList.remove(j); //for efficiency in loop
										}
									}
								}
						}

						//fetching+sorting of old notes done,
						//selections of CAB items done,
						//refresh the list view
						notifyDataSetChanged();
						//listview.setSelection(Utilities.NO_OF_MESSAGES_TO_FETCH + (conversationList.size() - _tempList.size()));
						listview.setSelection(NO_OF_MESSAGES_FETCHED + (DATE_ITEMS_IN_CURRENT_LIST - DATE_ITEMS_IN_PREVIOUS_LIST));
					}
				});
				return convertView;

//---------------------------------------------------------------------------------------------------->>>>   TEXT
	    	case MsgKind.TEXT:
	    		final Text holderText;
	            if (convertView == null) { 
	        	    holderText = new Text();
	        		convertView = inflater.inflate(R.layout.list_conversation_item_text, parent, false);
					InitializeUtil.text(context, holderText, convertView);
				    convertView.setTag(holderText);
				} else { 
					holderText = (Text)convertView.getTag();
				}
				note = (Notes) i;
				formatText(holderText.txtViewMessage_T, note.getMsg());
				//_____________________
				bubbleAlignment(note, holderText);
				bubbleTimeBar(note, holderText);
				text_clickListeners(holderText);

				//_________________
				multipleSelection(convertView, position);
				return convertView;

//---------------------------------------------------------------------------------------------------->>>>   1-IMAGE
	       	case MsgKind.M1_IMAGE:
	    		final Image holderImage;
	           	if (convertView == null) {
	        		holderImage = new Image();
	        	   	convertView = inflater.inflate(R.layout.list_conversation_item_image, parent, false);
					InitializeUtil.image(context, holderImage, convertView);
	               	convertView.setTag(holderImage);
	           	} else {
	        		holderImage = (Image)convertView.getTag();
	           	}
	           	note = (Notes) i;
	           	//_____________________
				bubbleAlignment(note, holderImage);
				bubbleTimeBar(note, holderImage);
				image_clickListeners(note, holderImage);
				image_mediaCaption(note, holderImage);
				image_status(note, holderImage);

				//_________________
   			   	multipleSelection(convertView, position);
	           	return convertView;
	           
//---------------------------------------------------------------------------------------------------->>>>   2-AUDIO
		   	case MsgKind.M2_AUDIO:
				Audio holderAudio;
		       	if (convertView == null) {
		       		holderAudio = new Audio();
				   	convertView = inflater.inflate(R.layout.list_conversation_item_audio, parent, false);
				   	InitializeUtil.audio(holderAudio, convertView);
		           	convertView.setTag(holderAudio);
			   	} else {
					holderAudio = (Audio)convertView.getTag();
			   	}
				note = (Notes) i;
			   	//________________
				bubbleAlignment(note, holderAudio);
				bubbleTimeBar(note, holderAudio);
				audio_clickListeners(note, holderAudio);
				audio_status(note, holderAudio);
			   //_________________
			   multipleSelection(convertView, position);
			   return convertView;

//---------------------------------------------------------------------------------------------------->>>>   7-VIDEO
		   	case MsgKind.M3_VIDEO:
				Video holderVideo;
				if (convertView == null) {
					holderVideo = new Video();
					convertView = inflater.inflate(R.layout.list_conversation_item_video, parent, false);
					InitializeUtil.video(context, holderVideo, convertView);
					convertView.setTag(holderVideo);
				} else {
					holderVideo = (Video)convertView.getTag();
				}
				note = (Notes) i;
				//_____________________
				bubbleAlignment(note, holderVideo);
				bubbleTimeBar(note, holderVideo);
				video_clickListeners(note, holderVideo);
				video_mediaCaption(note, holderVideo);
				video_status(note, holderVideo);
				//_________________
				multipleSelection(convertView, position);
				return convertView;

//---------------------------------------------------------------------------------------------------->>>>   22-LONG_TEXT
		   	case MsgKind.M4_LONG_TEXT:
				final LongText holderLongText;
				if (convertView == null) {
					holderLongText = new LongText();
					convertView = inflater.inflate(R.layout.list_conversation_item_longtext, parent, false);
					InitializeUtil.longtext(context, holderLongText, convertView);
					convertView.setTag(holderLongText);
				} else {
					holderLongText = (LongText)convertView.getTag();
				}
				note = (Notes) i;
				formatText(holderLongText.txtViewMessage_LT, note.getMediaCaption());
			   	//_____________________
				bubbleAlignment(note, holderLongText);
				bubbleTimeBar(note, holderLongText);
				longtext_clickListeners(note, holderLongText);
				longtext_status(note, holderLongText);
				//_________________
				multipleSelection(convertView, position);
				return convertView;
//---------------------------------------------------------------------------------------------------->>>>   LOCATION
		   	case MsgKind.LOCATION:
				final Location holderLocation;
				if (convertView == null) {
					holderLocation = new Location();
					convertView = inflater.inflate(R.layout.list_conversation_item_location, parent, false);
					InitializeUtil.location(holderLocation, convertView);
					convertView.setTag(holderLocation);
				} else {
					holderLocation = (Location)convertView.getTag();
				}
				note = (Notes) i;
				//_____________________
				bubbleAlignment(note, holderLocation);
				bubbleTimeBar(note, holderLocation);
				location_clickListeners(note, holderLocation);
				location_status(note, holderLocation);
				//_________________
				multipleSelection(convertView, position);
				return convertView;

//---------------------------------------------------------------------------------------------------->>>>   ADDRESS
		   	case MsgKind.ADDRESS:
				Address holderAddress;
				if (convertView == null) {
					holderAddress = new Address();
					convertView = inflater.inflate(R.layout.list_conversation_item_address, parent, false);
					InitializeUtil.address(context, holderAddress, convertView);
					convertView.setTag(holderAddress);
				} else {
					holderAddress = (Address)convertView.getTag();
				}
				note = (Notes) i;
				//_____________________
				FoursquareItem model = MsgAddressUtil.decodeModel(note.getMsg());
				//http://stackoverflow.com/questions/9290651/make-a-hyperlink-textview-in-android
				//holderAddress.txtViewAddName_Ad.setMovementMethod(LinkMovementMethod.getInstance());
				//String text = "<a href='" + model.url + "'> " + model.name + " </a>";
			   //[os_lollipop_: material_design] Href was causing the bubble non clickable in CAB
				holderAddress.txtViewAddName_Ad.setText(String.format("%s", model.name));
				holderAddress.txtViewAddFull_Ad.setText(String.format("%s", model.loc_address));
				//_____________________
				bubbleAlignment(note, holderAddress);
				bubbleTimeBar(note, holderAddress);
				address_clickListeners(note, holderAddress, model);
				address_status(note, holderAddress, model);
				//_________________
				multipleSelection(convertView, position);
				return convertView;

//---------------------------------------------------------------------------------------------------->>>>   CONTACT
		   	case MsgKind.CONTACT:
				Contact holderContact;
				if (convertView == null) {
					holderContact = new Contact();
					convertView = inflater.inflate(R.layout.list_conversation_item_contact, parent, false);
					InitializeUtil.contact(context, holderContact, convertView);
					convertView.setTag(holderContact);
				} else {
					holderContact = (Contact)convertView.getTag();
				}
				note = (Notes) i;
			   	String[] split = note.getMsg().split(",");
				String name = split[0];
				String number = split[1];
				holderContact.txtViewContactName_C.setText(String.format("%s", name));
			    //_____________________
				bubbleAlignment(note, holderContact);
				bubbleTimeBar(note, holderContact);
				contact_clickListeners(holderContact, number);
				//_________________
				multipleSelection(convertView, position);
				return convertView;

//---------------------------------------------------------------------------------------------------->>>>   LINK
			case MsgKind.LINK:
				//[link_: comment]
				/*
				Link holderLink;
				if (convertView == null) {
					holderLink = new Link();
					convertView = inflater.inflate(R.layout.list_conversation_item_link, parent, false);
					InitializeUtil.link(holderLink, convertView);
					convertView.setTag(holderLink);
				} else {
					holderLink = (Link) convertView.getTag();
				}
				note = (Notes) i;
				formatText(holderLink.txtViewMessage_L, note.getMsg());
				//_____________________
				holderLink.txtViewTitle_L.setText(note.msgLink.lTitle);
				if (holderLink.txtViewTitle_L.getLineCount() < 2) {
					holderLink.txtViewDesc_L.setText(note.msgLink.lDes);
					holderLink.txtViewDesc_L.setVisibility(View.VISIBLE);
				} else
					holderLink.txtViewDesc_L.setVisibility(View.GONE);
				holderLink.txtViewUrl_L.setText(item.msgLink.lUrl);
				if(!note.msgLink.lThumb.equals(THUMB_NOT_FOUND)) {
					holderLink.imgView_L.setVisibility(View.VISIBLE);
					holderLink.imgView_L.setImageBitmap(MediaThumbUtil.convertThumbToBitmap(item.msgLink.lThumb));
				} else
					holderLink.imgView_L.setVisibility(View.GONE);
				//_____________________
				bubbleAlignment(note, holderLink);
				bubbleTimeBar(note, holderLink);
				link_clickListeners(note, holderLink);
				//_________________
				multipleSelection(convertView, position);*/
				return convertView;

//---------------------------------------------------------------------------------------------------->>>> OTHERS
			case MsgKind._CHANGE_NAME:
	       	case MsgKind._CHANGE_IMAGE:
	       		Date holderOthers;
				if (convertView == null) {
					holderOthers =  new Date();
					convertView = inflater.inflate(R.layout.list_conversation_item_date_seperator, parent, false);
			        convertView.setOnClickListener(null);
					convertView.setOnLongClickListener(null);
					convertView.setLongClickable(false);
					holderOthers.txtViewDateSeparator = (EmojiconTextView) convertView.findViewById(R.id.id_txtViewDateSeparator);
					holderOthers.txtViewDateSeparator = dateLayouting(holderOthers.txtViewDateSeparator);
					convertView.setTag(holderOthers);
			    } else {
					holderOthers = (Date)convertView.getTag();
			    }
				note = (Notes) i;
				setMessage(holderOthers.txtViewDateSeparator, note.getMsg());
			    return convertView;
						
//------------------------------------------------------------------------------------------------------->>>>   default
		   	default :
				Date holderUnknownType;
				//[bug_: used for | Starred notes | notes with unknown type]
		        if (convertView == null) {
		        	holderUnknownType =  new Date();
		            convertView = inflater.inflate(R.layout.list_conversation_item_unknown, parent, false);
		            convertView.setOnClickListener(null);
					convertView.setOnLongClickListener(null);
					convertView.setLongClickable(false);
					holderUnknownType.txtViewDateSeparator = (EmojiconTextView) convertView.findViewById(R.id.id_txtViewDateSeparator);
					holderUnknownType.txtViewDateSeparator = dateLayouting(holderUnknownType.txtViewDateSeparator);
					convertView.setTag(holderUnknownType);
		        } else {
		        	holderUnknownType = (Date)convertView.getTag();
		        }

				if(i.itemType().equals(ItemType.CONVERSATION_NOTE)) {
					//handle message with anonymous types
					note = (Notes) i;
					holderUnknownType.txtViewDateSeparator.setText(Msg.FEATURE_NOT_SUPPORTED(note.getMsgKind()));
				} else if(i.itemType().equals(ItemType.CONVERSATION_UNKNOWN)) {
					//handle anonymous type conversation items
					ConversationUnknownItem itemUnknown = (ConversationUnknownItem) i;
					holderUnknownType.txtViewDateSeparator.setText(itemUnknown.getText());
				}
	           	return convertView;
		}
	}

//_________________________________________________________________________________________________ 0 - TYPE_TEXT
	private void text_clickListeners(Text holderText) {
		
		if(mSelection.size() == 0) {
			//_______ on clicking
			holderText.rlFather.setLongClickable(true);
			//XML__ : http://stackoverflow.com/questions/8558732/listview-textview-with-linkmovementmethod-makes-list-item-unclickable
			//holderText.txtViewMessage_T.setLinksClickable(true);
			holderText.txtViewMessage_T.setMovementMethod(LinkMovementMethod.getInstance());
			//----------
		} else {
			holderText.rlFather.setLongClickable(false);
     	   	//message
			holderText.txtViewMessage_T.setLinksClickable(false);
			holderText.txtViewMessage_T.setMovementMethod(null);
		}		
	}

//_________________________________________________________________________________________________ MEDIA_MESSAGES
	//media notes (images, video) use
	// their bottom_bar margins are different | android:layout_alignParentBottom="true" in Bottom Bar
	// tick_on_media

//_________________________________________________________________________________________________ 1 - IMAGE
	private void image_clickListeners(final Notes note, Image holderImage) {
		
		if(mSelection.size() == 0) {
			//_______ on clicking
     	   	holderImage.rlFather.setLongClickable(true);
			
			//if you put SingleClickListener, you will also have to put LongClickListener,
			//if you want to select that item onLongClick,
			//For Example : We Don't want to select item on name LongClick so we are not putting LongClickListener on Name
			//_______ because setting single click listener won't allow to select on Long Click
			holderImage.imgViewMessage_I.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return false;
				}
			});
			
			//_______
			holderImage.imgViewMessage_I.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (note.getMediaStatus()) {
						case MediaStatus.COMPLETED:
							File f = PathUtil.getExternalMediaImageFile(note.getMsgFlow(), note.getMsg());
							if (f.exists()) {
								Intent intentImage = new Intent(context, MediaViewer.class);
								intentImage.putExtra(IntentKeys.CHAT_ID, note.getChatId());
								intentImage.putExtra(IntentKeys.MSG_ID, note.getMsgId());
								context.startActivity(intentImage);
							} else
								showMediaNotExistDialog();
							break;
					}
				}
			});
		} else {
     	   holderImage.rlFather.setLongClickable(false);
     	   holderImage.imgViewMessage_I.setClickable(false);
     	   holderImage.imgViewMessage_I.setLongClickable(false);
		}		
	}
	private void image_mediaCaption(final Notes note, Image holderImage) {
		if(note.getMediaCaption().equals(MsgConstant.DEFAULT_MEDIA_CAPTION))
			holderImage.txtViewMedCap_I.setVisibility(View.GONE);
		else {
			holderImage.txtViewMedCap_I.setVisibility(View.VISIBLE);
			formatText(holderImage.txtViewMedCap_I, note.getMediaCaption());
		}
	}

	private void image_status(final Notes note, Image holderImage) {
		switch(note.getMediaStatus()) {
			case MediaStatus.COMPLETED:
				image_displayThumb(note, holderImage);
				break;
		}
	}

	private void image_displayThumb(Notes note, Image holderImage) {
		File f = PathUtil.getExternalMediaImageFile(note.getMsgFlow(), note.getMsg());
		String imageThumbPath = PathUtil.getInternalMediaThumbPath(note.getChatId(), note.getMsg());
		if(f.exists()) {
			String imageExternalPath = PathUtil.getExternalMediaImagePath(note.getMsgFlow(), note.getMsg());
			imageLoader.displayImageElseThumb(imageExternalPath, 0, holderImage.imgViewMessage_I, imageThumbPath, 70, 280);
		} else
			holderImage.imgViewMessage_I.setImageURI(Uri.parse(imageThumbPath));
	}
	//_____________________________________________________________________________________________ 7 - VIDEO
	private void video_clickListeners(final Notes note, final Video holderVideo) {

		if(mSelection.size() == 0) {
			//_______ on clicking
			holderVideo.rlFather.setLongClickable(true);

			//_______
			holderVideo.imgViewMessage_V.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return false;
				}
			});

			//_______
			holderVideo.imgViewMessage_V.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (note.getMediaStatus()) {
						case MediaStatus.COMPLETED:
							File f = PathUtil.getExternalMediaVideoFile(note.getMsgFlow(), note.getMsg());
							if (f.exists()) {
								Intent intentVideo = new Intent(Intent.ACTION_VIEW);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
									intentVideo.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
									Uri contentUri = FileProvider.getUriForFile(context, App.PACKAGE_NAME + GenericFileProvider.PATH, f);
									intentVideo.setDataAndType(contentUri, MimeType.MIME_TYPE_VIDEO);
								} else
									intentVideo.setDataAndType(Uri.fromFile(f), MimeType.MIME_TYPE_VIDEO);
								context.startActivity(intentVideo);
							} else
								showMediaNotExistDialog();
							break;
					}
				}
			});

		} else {
			holderVideo.rlFather.setLongClickable(false);
			//image (2)
			holderVideo.imgViewMessage_V.setClickable(false);
			holderVideo.imgViewMessage_V.setLongClickable(false);
		}
	}
	private void video_mediaCaption(final Notes note, Video holderVideo) {
		if(note.getMediaCaption().equals(MsgConstant.DEFAULT_MEDIA_CAPTION))
			holderVideo.txtViewMedCap_V.setVisibility(View.GONE);
		else {
			holderVideo.txtViewMedCap_V.setVisibility(View.VISIBLE);
			formatText(holderVideo.txtViewMedCap_V, note.getMediaCaption());
		}
	}

	private void video_status(final Notes note, Video holderVideo) {
		switch(note.getMediaStatus()) {
			case MediaStatus.COMPRESSING_MEDIA_FAILED:
				holderVideo.rl_Compressing_V.setVisibility(View.GONE);
				holderVideo.imgViewMessage_V.setImageResource(R.drawable.media_thumbnail_missing);
				break;
			case MediaStatus.COMPRESSING_MEDIA:
				holderVideo.rl_Compressing_V.setVisibility(View.VISIBLE);
				video_displayThumb(note, holderVideo);
				break;
			case MediaStatus.COMPLETED:
				holderVideo.rl_Compressing_V.setVisibility(View.GONE);
				video_displayThumb(note, holderVideo);
				break;
		}
	}

	private void video_displayThumb(final Notes note, final Video holderVideo) {
		String videoThumbPath = PathUtil.getInternalMediaThumbPath(note.getChatId(), note.getMsg());
		File f = PathUtil.getExternalMediaVideoFile(note.getMsgFlow(), note.getMsg());
		if(f.exists()) {
			//_________________________________________________________________________________________________
			// Cancel any pending ThumbNail task, since this view is now bound to new ThumbNail
			final VideoThumbnailForConversationList oldTask = (VideoThumbnailForConversationList) holderVideo.imgViewMessage_V.getTag();
			if (oldTask != null) {
				oldTask.cancel(false);
			}
			final Bitmap cachedResult = mCache.get(note.getMsgId());
			if (cachedResult != null) {
				holderVideo.imgViewMessage_V.setImageBitmap(cachedResult);
				return;
			}
			// If we arrived here, either cache is disabled or cache miss, so we
			// need to kick task to load manually
			final VideoThumbnailForConversationList task = new VideoThumbnailForConversationList(holderVideo.imgViewMessage_V, mCache, note.getMsgId(), Uri.parse(videoThumbPath));
			holderVideo.imgViewMessage_V.setImageBitmap(null);
			holderVideo.imgViewMessage_V.setImageURI(Uri.parse(videoThumbPath));
			holderVideo.imgViewMessage_V.setTag(task);
			task.execute(f);
		} else
			holderVideo.imgViewMessage_V.setImageURI(Uri.parse(videoThumbPath));

	}
//_________________________________________________________________________________________________ 3 - AUDIO
	private void audio_clickListeners(final Notes note, final Audio holderAudio) {

		if(mSelection.size() == 0) {
			//_______ on clicking
			holderAudio.rlFather.setLongClickable(true);

            //[os_lollipop_: material_design] (play btn & seekBar) was causing the bubble non clickable in CAB
            //.setVisibility and .setFocusable are the 2 property that allows click in CAB
            //holderAudio.btnPlay_A.setVisibility(View.VISIBLE);
            holderAudio.btnPlay_A.setFocusable(true);
            //holderAudio.sBar_A.setVisibility(View.VISIBLE);
            holderAudio.sBar_A.setFocusable(true);
             //_______ audio_player
			holderAudio.btnPlay_A.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (note.getMediaStatus()) {
						case MediaStatus.COMPLETED:
							File f = PathUtil.getExternalMediaAudioFile(note.getMsgFlow(), note.getMsg());
							if (f.exists())
								playRecording(note, holderAudio, f);
							else
								showMediaNotExistDialog();
							break;
						case MediaStatus.PLAYING_MEDIA:
							//holderAudio.tvTimerRight_A.setText("" + String.format("%02d:%02d:%02d", durationHour, durationMint, durationSec));
                            stopAudioPlayerWork(note);
							break;
					}
				}
			});
		} else {
			holderAudio.rlFather.setLongClickable(false);
			//we want all these button not to respond when ActionMode is ON
			//audio player
            //holderAudio.btnPlay_A.setVisibility(View.GONE); //not using this
            holderAudio.btnPlay_A.setFocusable(false);
            //holderAudio.sBar_A.setVisibility(View.GONE); //not using this
            holderAudio.sBar_A.setFocusable(false);
            holderAudio.btnPlay_A.setClickable(false);
		}
	}

    private void audio_status(final Notes note, Audio holderAudio) {
		switch (note.getMediaStatus()) {
            case MediaStatus.PLAYING_MEDIA:
                holderAudio.btnPlay_A.setBackgroundResource(R.drawable.media_audio_pause);
                break;
            default://whatever the case is stop the audio player
				holderAudio.btnPlay_A.setBackgroundResource(R.drawable.media_audio_play);
                holderAudio.tvTimerRight_A.setText(context.getString(R.string.media_start_time));
                holderAudio.sBar_A.setProgress(0);
                break;
        }
    }
	private void playRecording(final Notes note, final Audio holderAudio, File f) {
		if(mediaPlayer!=null && mediaPlayer.isPlaying())
            stopAudioPlayerWork(note);

		try {
            lastAudioPlayedItemPosition = getPosition(note);//mark current item as last playing
            //LogUtil.e(getClass().getSimpleName(), "pos:" +lastAudioPlayedItemPosition + ":" + getPosition(item));

            Uri myUri = Uri.fromFile(f);
            mediaPlayer = MediaPlayer.create(context, myUri);
            //mp will never be null, un-till the message is send or cancel
            //mediaPlayer.setDataSource(f.getPath());
            //mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer _mp) {
                    stopAudioPlayerWork(note);
                }
            });

            //seekBar
            holderAudio.sBar_A.setMax(mediaPlayer.getDuration());
			playingTimer(mediaPlayer.getDuration(), holderAudio.sBar_A, 0);
			//holderAudio.sBar_A.setProgress(_mp.getCurrentPosition());
            holderAudio.sBar_A.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;

                @Override
                public void onStartTrackingTouch(final SeekBar seekBar) {
                    if (mediaPlayer != null)//check because, when we cancel dialog during play, mp is null but seek bar was accessing mp
                        mediaPlayer.pause();
                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                        mediaPlayer.start();
                        playingTimer(mediaPlayer.getDuration() - progress, seekBar, progress);
                    }
                }

                @Override
                public void onProgressChanged(final SeekBar seekBar, int _progress, boolean fromUser) {
                    if (mediaPlayer != null) {
                        //LogUtil.e(getClass().getSimpleName(), "onProgressChanged:progress:  " + _progress / 1000);

                        progress = _progress;
                        final int HOUR = 60 * 60 * 1000;
                        final int MINUTE = 60 * 1000;
                        final int SECOND = 1000;
                        //for max_length to 0 approach
                        //int durationInMillis = mediaPlayer.getDuration() - _progress;
                        //for 0 to max_length approach
						//LogUtil.e(getClass().getSimpleName(), durationInMillis + ":" + mediaPlayer.getDuration() + ":" + _progress);
						//int durationInMillis = _progress;
						durationHour = _progress / HOUR;
                        durationMint = (_progress % HOUR) / MINUTE;
                        durationSec = (_progress % MINUTE) / SECOND;
                        if (durationHour > 0)
                            holderAudio.tvTimerRight_A.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", durationHour, durationMint, durationSec));
                        else
                            holderAudio.tvTimerRight_A.setText(String.format(Locale.getDefault(), "%02d:%02d", durationMint, durationSec));

                        notifyDataSetChanged();
                    }
                }
            });

            //set media item is playing
			note.setMediaStatus(MediaStatus.PLAYING_MEDIA);//for clicked item only

            notifyDataSetChanged();
        } catch (Exception ignored) {
		}
	}
	private void playingTimer(final int remainingLength, final SeekBar sBar_a, final int currentProgress) {
		if(cDownTimer_audioPlay != null)
			cDownTimer_audioPlay.cancel();

		cDownTimer_audioPlay = new CountDownTimer(remainingLength, 100) {
			@Override
			public void onTick(long tick) {
                //LogUtil.e(getClass().getSimpleName(), "CountDownTimer:pos:" + lastAudioPlayedItemPosition);
                int progress = (int) (remainingLength - tick);
                sBar_a.setProgress(currentProgress + progress);

                //this step is very very important
                //if you do not do notifyDataSetChanged(), then if new item is added to the list, the new bubble seekBar will update instead of real one
                notifyDataSetChanged();

                //LogUtil.e(getClass().getSimpleName(), tick + ":" + progress + ":" + currentProgress);
                //currentProgress is used when the user moves the seekBar using touch
			}
			@Override
			public void onFinish() {
				cDownTimer_audioPlay.cancel();
			}
		}.start();
	}
    private void stopAudioPlayerWork(final Notes note) {
		//LogUtil.e(getClass().getSimpleName(), "stopAudioPlayerWork");
		stopAudioPlayer();
        //set media item to completed
        if(lastAudioPlayedItemPosition!=-1)
			((Notes)(conversationList.get(lastAudioPlayedItemPosition))).setMediaStatus(MediaStatus.COMPLETED);
        note.setMediaStatus(MediaStatus.COMPLETED);
        notifyDataSetChanged();
    }
	public static void stopAudioPlayer() {
		//stop the cDownTimer
        if(cDownTimer_audioPlay!=null)
		    cDownTimer_audioPlay.cancel();
		//stop the media player
		if(mediaPlayer!=null) {
			mediaPlayer.stop();
			mediaPlayer.release();
            mediaPlayer=null;
		}
	}
	//Global Fields
	private int lastAudioPlayedItemPosition = -1; // to reset the last bubble, -1 because list can not have -1 item
	private static CountDownTimer cDownTimer_audioPlay = null;
    private static MediaPlayer mediaPlayer = null;
    private static int durationHour;
    private static int durationMint;
    private static int durationSec;


//_________________________________________________________________________________________________ 3 - TYPE_LOCATION
//_________________________________________________________________________________________________ 18 - ADDRESS
	private void location_clickListeners(final Notes note, Location holderLocation) {
		if(mSelection.size() == 0) {
			//_______ on clicking
			holderLocation.rlFather.setLongClickable(true);
			holderLocation.imgViewMessage_L.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return false;
				}
			});
			holderLocation.imgViewMessage_L.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (note.getMediaStatus()) {
						case MediaStatus.COMPLETED://only allow when message is sent/receive, else show green
							context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + note.getMsg())), "Choose Application"));//createChooser removes 'Always' & 'Only once' buttons
							break;
					}
				}
			});
		} else {
			holderLocation.rlFather.setLongClickable(false);
			//image (2)
			holderLocation.imgViewMessage_L.setClickable(false);
			holderLocation.imgViewMessage_L.setLongClickable(false);
		}
	}
	private void address_clickListeners(final Notes note, Address holderAddress, final FoursquareItem model) {
		if(mSelection.size() == 0) {
			//_______ on clicking
			holderAddress.rlFather.setLongClickable(true);
			holderAddress.imgViewMessage_Ad.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return false;
				}
			});
			holderAddress.imgViewMessage_Ad.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (note.getMediaStatus()) {
						case MediaStatus.COMPLETED://only allow when message is sent/receive, else show green
							context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + note.getMsg())), "Choose Application"));//createChooser removes 'Always' & 'Only once' buttons
							break;
					}
				}
			});
			holderAddress.txtViewAddName_Ad.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.url));
					context.startActivity(browserIntent);
				}
			});
		} else {
			holderAddress.rlFather.setLongClickable(false);
			//image (2)
			holderAddress.imgViewMessage_Ad.setClickable(false);
			holderAddress.imgViewMessage_Ad.setLongClickable(false);
			//address
			holderAddress.txtViewAddName_Ad.setClickable(false);

			//holderAddress.rlFather.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			//holderAddress.rlFather.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
			//holderAddress.txtViewView.setBackgroundResource(R.drawable.button_incoming_normal);
			//holderAddress.txtViewName.setBackgroundResource(Color.TRANSPARENT); //to avoid focus affect during CAB
		}		
	}

	private void location_status(final Notes note, Location holderLocation) {
		switch(note.getMediaStatus()) {
			//only show Map when message is sent/receive, else show green
			case MediaStatus.COMPLETED:
				Picasso.with(context)
						.load(KeyboardVar.locationUrl(note.getMsg()))
						.placeholder(R.drawable.media_location)
						.into(holderLocation.imgViewMessage_L);
				break;
		}
	}
	private void address_status(final Notes note, Address holderAddress, FoursquareItem fs) {
		switch(note.getMediaStatus()) {
			//only show Map when message is sent/receive, else show green
			case MediaStatus.COMPLETED:
				Picasso.with(context)
						.load(KeyboardVar.locationUrl(fs.loc_lat + "," + fs.loc_lng))
						.placeholder(R.drawable.media_location)
						.into(holderAddress.imgViewMessage_Ad);
				break;
		}
	}
	
//_________________________________________________________________________________________________ 12 - TYPE_CONTACT
	private void contact_clickListeners(Contact holderContact, final String number) {
		if(mSelection.size() == 0) {
			//_______ on clicking
			holderContact.rlFather.setLongClickable(true);
			//_______
			holderContact.txtViewContactName_C.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return false;
				}
			});
			//_______
			holderContact.txtViewContactName_C.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String uri = "tel:" + number;
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse(uri));
					context.startActivity(intent);
				}
			});
			//----------
		} else {
			holderContact.rlFather.setLongClickable(false);
			//we want all these button not to respond when ActionMode is ON
     	   	//view button
			holderContact.txtViewContactName_C.setClickable(false);
			holderContact.txtViewContactName_C.setLongClickable(false);
		}		
	}

//_________________________________________________________________________________________________ 22 - TYPE_LONG_TEXT
	private void longtext_clickListeners(final Notes note, LongText holderLongText) {

		if(mSelection.size() == 0) {
			//_______ on clicking
			holderLongText.rlFather.setLongClickable(true);
			//FIX : http://stackoverflow.com/questions/8558732/listview-textview-with-linkmovementmethod-makes-list-item-unclickable
			//holderText.txtViewMessage_T.setLinksClickable(true);
			holderLongText.txtViewMessage_LT.setMovementMethod(LinkMovementMethod.getInstance());
			//_______
			holderLongText.btnViewMore_LT.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch(note.getMediaStatus()) {
						case MediaStatus.COMPLETED:
							File f = PathUtil.getExternalMediaTextFile(note.getMsgFlow(), note.getMsg());
							if(f.exists()) {
								Intent intentText = new Intent(context, TextViewer.class);
								intentText.putExtra(IntentKeys.MSG, note.getMsg());
								intentText.putExtra(IntentKeys.MSG_FLOW, note.getMsgFlow());
								context.startActivity(intentText);
							} else
								showMediaNotExistDialog();
							break;

						default:
							showMediaNotExistDialog();
							break;
					}
				}
			});
			//----------
		} else {
			holderLongText.rlFather.setLongClickable(false);
			//message
			holderLongText.txtViewMessage_LT.setLinksClickable(false);
			holderLongText.txtViewMessage_LT.setMovementMethod(null);
			//view more
			holderLongText.btnViewMore_LT.setClickable(false);
		}
	}

	private void longtext_status(final Notes note, LongText holderLongText) {
		switch(note.getMediaStatus()) {
			case MediaStatus.COMPLETED:
				holderLongText.btnViewMore_LT.setVisibility(View.VISIBLE);
				break;
		}
	}

//_________________________________________________________________________________________________ 25 - LINK
	/*//[link_: comment]
	private void link_clickListeners(final Notes note, Link holderLink) {
		if(mSelection.size() == 0) {
			holderLink.rlFather.setLongClickable(true);
			holderLink.txtViewTitle_L.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return false;
				}
			});
			holderLink.txtViewTitle_L.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//[link_: comment]
					//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(note.msgLink.lUrl));
					//context.startActivity(browserIntent);
				}
			});
		} else {
			holderLink.rlFather.setLongClickable(false);
			holderLink.txtViewTitle_L.setClickable(false);
			holderLink.txtViewTitle_L.setLongClickable(false);
		}
	}*/
	
//_____________________________________________________________________________________________________________  CLICK LISTENERS
	private void showMediaNotExistDialog() {
		new AlertDialog.Builder(context)
		.setMessage("Sorry, this media file doesn't exist on your SD card.")
		.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
			}
		}).show();
	}

//_____________________________________________________________________________________________________________
	private void setMessage(TextView txtView, String msg) {
		txtView.setText(msg);
	}

//__________________________________________________________________________________________________  MULTIPLE SELECTION
	private void multipleSelection(View view, int position) {
		if (mSelection.get(position)) {
			view.setBackgroundResource(R.drawable.list_selected_holo_light);
			//[os_lollipop_: material_design] For WhatsApp overlay behaviour , set Foreground
			//https://dzone.com/articles/adding-foreground-selector

			//if(msgFlow==MsgFlow.RECEIVE)
			//	r.setBackgroundResource(R.drawable.balloon_receive);
			//else
			//	r.setBackgroundResource(R.drawable.balloon_send);
		} else
			view.setBackgroundColor(Color.TRANSPARENT);
	}

	private EmojiconTextView dateLayouting(EmojiconTextView tv) {
		//Typeface fontString = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
		//tv.setTypeface(fontString, Typeface.BOLD); //don't need in bold
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getDateBubbleFontSize(context));
		tv.setEmojiconSize(Prefs.getFontSize(context)); //using font size as emoji size for date bubble
		return tv;
	}

//__________________________________________________________________________________________________  LAYOUTING
	private static final int MARGIN = 120;
	private void bubbleAlignment(final Notes note, ViewHolder.Parent holderParent) {
		holderParent.rlParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (note.getMsgFlow() == MsgFlow.SEND) {
			holderParent.rlParam.setMargins(MARGIN, 0, 0, 0);
			holderParent.rlParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
			holderParent.rlMother.setBackgroundResource(R.drawable.balloon_send);
		} else {
			holderParent.rlParam.setMargins(0, 0, MARGIN, 0);
			holderParent.rlParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
			holderParent.rlMother.setBackgroundResource(R.drawable.balloon_receive);
		}
		holderParent.rlMother.setLayoutParams(holderParent.rlParam);
	}

	private void bubbleTimeBar(final Notes note, Parent holderTimeBar) {
		//text
		/*if(AppUtil.isMsgHasThumb(note.getMsgKind()))
			holderTimeBar.txtViewTime.setTextColor(Color.WHITE);
		else
			holderTimeBar.txtViewTime.setTextColor(Color.BLACK);*/
		holderTimeBar.txtViewTime.setText(DateUtil.getDateInStringForConversationList(note.getMsgTimestamp()));
		//star
		if(note.getMsgStar() == MsgStar.YES) {
			holderTimeBar.imgViewStar.setVisibility(View.VISIBLE);
			if(AppUtil.isMsgHasThumb(note.getMsgKind()))
				holderTimeBar.imgViewStar.setImageResource(R.drawable.msg_star_media);
			else
				holderTimeBar.imgViewStar.setImageResource(R.drawable.msg_star);
		} else
			holderTimeBar.imgViewStar.setVisibility(View.GONE);
		//msg status
		holderTimeBar.imgViewMsgStatus.setVisibility(View.VISIBLE);
		switch (note.getMsgStatus()) {
			case MsgStatus.CLOCK:
				holderTimeBar.imgViewMsgStatus.setImageResource(R.drawable.msg_status_clock);
				break;
			case MsgStatus.DONE:
				holderTimeBar.imgViewMsgStatus.setImageResource(R.drawable.msg_status_done);
				break;
		}
		//click listeners
		if(mSelection.size() == 0) {
			if(note.getMsgStatus() == MsgStatus.DONE) {
				holderTimeBar.llTimeBar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						final View viewDialog = View.inflate(context, R.layout.dialog_msg_timestamp, null);
						final TextView tvMsgTimestamp = (TextView) viewDialog.findViewById(R.id.dialog_msg_timestamp);
						final TextView tvMsgTimestampDone = (TextView) viewDialog.findViewById(R.id.dialog_msg_timestamp_done);
						final TextView tvMsgTimestampCompletionDetail = (TextView) viewDialog.findViewById(R.id.dialog_msg_timestamp_completion_detail);
						tvMsgTimestamp.setText(DateUtil.getDateInStringForMsgTimestampDialog(note.getMsgTimestamp()));
						tvMsgTimestampDone.setText(DateUtil.getDateInStringForMsgTimestampDialog(note.getMsgTimestampDone()));
						long completionTime = note.getMsgTimestampDone() - note.getMsgTimestamp();
						tvMsgTimestampCompletionDetail.setText(String.format(context.getString(R.string.dialog_msg_timestamp_completion_detail), DateUtil.getDateInStringForMsgTimestampDialogCompletion(completionTime)));
						alert.setView(viewDialog);
						alert.setTitle("Note detail");
						alert.show();
					}
				});
			} else
				holderTimeBar.llTimeBar.setClickable(false);
			//share
			holderTimeBar.imgViewShare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					switch (note.getMsgKind()) {
						case MsgKind._CHANGE_NAME:
						case MsgKind._CHANGE_IMAGE:
						case MsgKind.TEXT:
						case MsgKind.LINK:
							ShareUtil.shareText(context, note.getMsg());
							break;
						case MsgKind.CONTACT:
							ShareUtil.shareText(context, Msg.CHAT_CONTACT + ": " + note.getMsg());
							break;
						case MsgKind.M1_IMAGE:
							File imageFile = PathUtil.getExternalMediaImageFile(note.getMsgFlow(), note.getMsg());
							if(imageFile.exists())
								ShareUtil.shareMedia(context, PathUtil.getExternalMediaImageUri(context, note.getMsgFlow(), note.getMsg()), MimeType.MIME_TYPE_IMAGE_JPEG);
							else
								Toast.makeText(context, "Image does not exist", Toast.LENGTH_SHORT).show();
							break;
						case MsgKind.M2_AUDIO:
							File audioFile = PathUtil.getExternalMediaAudioFile(note.getMsgFlow(), note.getMsg());
							if(audioFile.exists())
								ShareUtil.shareMedia(context, PathUtil.getExternalMediaAudioUri(context, note.getMsgFlow(), note.getMsg()), MimeType.MIME_TYPE_AUDIO);
							else
								Toast.makeText(context, "Audio does not exist", Toast.LENGTH_SHORT).show();
							break;
						case MsgKind.M3_VIDEO:
							File videoFile = PathUtil.getExternalMediaVideoFile(note.getMsgFlow(), note.getMsg());
							if(videoFile.exists())
								ShareUtil.shareMedia(context, PathUtil.getExternalMediaVideoUri(context, note.getMsgFlow(), note.getMsg()), MimeType.MIME_TYPE_VIDEO);
							else
								Toast.makeText(context, "Video does not exist", Toast.LENGTH_SHORT).show();
							break;
						case MsgKind.M4_LONG_TEXT:
							File textFile = PathUtil.getExternalMediaTextFile(note.getMsgFlow(), note.getMsg());
							if(textFile.exists())
								ShareUtil.shareMedia(context, PathUtil.getExternalMediaTextUri(context, note.getMsgFlow(), note.getMsg()), MimeType.MIME_TYPE_TEXT);
							else
								ShareUtil.shareText(context, note.getMediaCaption()); //share short text instead
							break;
						case MsgKind.LOCATION:
							ShareUtil.shareText(context, Msg.CHAT_LOCATION + ": " + note.getMsg());
							break;
						case MsgKind.ADDRESS:
							FoursquareItem model = MsgAddressUtil.decodeModel(note.getMsg());
							ShareUtil.shareText(context, Msg.CHAT_ADDRESS + ": " + "\n" + model.name + "\n" + model.loc_address + "\n" + "Location: " + model.loc_lat + "," + model.loc_lng);
							break;
					}
				}
			});
		} else {
			holderTimeBar.llTimeBar.setClickable(false);
			holderTimeBar.imgViewShare.setClickable(false);
		}
	}

	//______________________________________________________________________________________________ TEXT FORMAT
	private void formatText(TextView txtView, String msg) {
		TextFormatter.applyFormatting(txtView, msg);
	}
}