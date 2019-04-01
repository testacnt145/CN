package com.chattynotes.adapters.listChat;

import com.chattynotes.adapters.listChat.model.InterfaceChat;
import com.chattynotes.adapters.listChat.model.StringChatItem;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.mvp.activities.ChatImageViewer;
import com.chattynotes.mvp.activities.ChatInfo;
import com.chattynoteslite.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.ItemType;
import com.chattynotes.constant.Msg;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.constant.MsgStatus;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.DateUtil;
import com.chattynotes.util.PasswordUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.textformatter.TextFormatter;
import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.annotation.NonNull;

public class AdapterChat extends ArrayAdapter<InterfaceChat> implements Filterable {

	private Context context;
	private ArrayList<InterfaceChat> items;
	private ArrayList<InterfaceChat> originalList;
	private LayoutInflater inflater;

	public AdapterChat(Context ctx, ArrayList<InterfaceChat> items) {
		super(ctx, 0, items);
		context = ctx;
		this.items = items;
		this.originalList = new ArrayList<>();
		originalList = items;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
//___________________________________________________________________________________________________________
	@Override
	public int getCount() {
	    return items.size();
	}
	
	@Override
	public InterfaceChat getItem(int position) {
	    return items.get(position);
	}
	
	 @Override
	 public long getItemId(int position) {
	   return position;
	 }
	
//---------------------------------------------------------------------------------------------
//http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter
	@Override
	public int getItemViewType(int position) {
		final InterfaceChat i = items.get(position);
		if (i.itemType().equals(ItemType.CHAT))
			return 1;
		else 
			return 0;
	}
	
	@Override
	public int getViewTypeCount() {
		//Beware! getItemViewType() must return an int between 0 and getViewTypeCount() - 1
	    return 100; //(-1 to 12 is equal to 14), keeping it 100 on the safe side
	}
	  
//___________________________________________________________________________________________________________
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		
		int viewType = this.getItemViewType(position);
		InterfaceChat i = items.get(position);
		
		switch(viewType) {
		//---------------------------------------------------------------------------------------------------->>>>   STRING
			case 0:
	    	   ViewHolderString holderString;
	           if (convertView == null) { 
	        	   holderString =  new ViewHolderString();
	               convertView = inflater.inflate(R.layout.list_chat_item_string, parent, false);
	               convertView.setOnClickListener(null);
				   convertView.setOnLongClickListener(null);
				   convertView.setLongClickable(false);
				   holderString.txtViewString = (TextView) convertView.findViewById(R.id.id_txtView);
				   //Typeface fontString = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
				   //holderString.txtViewString.setTypeface(fontString, Typeface.NORMAL); //don't need in bold
				   convertView.setTag(holderString);
	           } else { 
	        	   holderString = (ViewHolderString)convertView.getTag(); 
	           }
	           StringChatItem stringChatItem = (StringChatItem) i;
	           holderString.txtViewString.setText(stringChatItem.getTitle());
	           return convertView;
		//---------------------------------------------------------------------------------------------------->>>>   CHAT ITEM
		   	case 1:
	    		ViewHolderChat holderChat;
	            if (convertView == null) { 
	            	holderChat = new ViewHolderChat();
	        	    convertView = inflater.inflate(R.layout.list_chat_item, parent, false);
	        	    holderChat.txtViewChatName = (TextView)convertView.findViewById(R.id.chat_row_chat_name);
	        	    holderChat.txtViewMsgTimestamp = (TextView)convertView.findViewById(R.id.chat_row_msg_timestamp);
	        	    holderChat.txtViewMsg = (TextView)convertView.findViewById(R.id.chat_row_msg);
					holderChat.imgViewMsgStatus = (ImageView)convertView.findViewById(R.id.chat_row_msg_status);
					holderChat.imgViewChatImage = (ImageView)convertView.findViewById(R.id.chat_row_chat_image);
	        	    convertView.setTag(holderChat);
				} else { 
					holderChat = (ViewHolderChat)convertView.getTag();
				} 
	            
	            //Custom Font
			    //Do not require after material design
				//Typeface fontUnread = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
				//Typeface fontRead 	= Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
			    Typeface fontMessage = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
			   	holderChat.txtViewMsg.setTypeface(fontMessage, Typeface.NORMAL);
				Chats chat = (Chats)i;
			   	//_____________________
			   	clickListeners(chat, holderChat);

				//__ ChatName
				holderChat.txtViewChatName.setText(String.format("%s", chat.getChatName()));
				
				//__ Msg
				switch(chat.getNote().getMsgKind()) {
					//_______
					case MsgKind.M1_IMAGE:
						holderChat.txtViewMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.msg_kind_cam, 0, 0, 0);
						break;
					//_______
					case MsgKind.M2_AUDIO:
						holderChat.txtViewMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.msg_kind_mic_grey, 0, 0, 0);
						break;
					//_______
					case MsgKind.M3_VIDEO:
						holderChat.txtViewMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.msg_kind_video, 0, 0, 0);
						break;
					//_______
					case MsgKind.LOCATION:
					case MsgKind.ADDRESS:
						holderChat.txtViewMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.msg_kind_location, 0, 0, 0);
						break;
					//_______
					case MsgKind.CONTACT:
						holderChat.txtViewMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.msg_kind_contact, 0, 0, 0);
						break;
					//_______
					default:
						holderChat.txtViewMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				}
				String _msg = setMessage(chat.getNote().getMsg().length() <= 100 ? chat.getNote().getMsg() : chat.getNote().getMsg().substring(0, 100), chat.getNote().getMsgKind(), chat.getNote().getMediaCaption());//only 100 letters of message
				TextFormatter.applyFormatting(holderChat.txtViewMsg, _msg);
					
				//__ MsgTimestamp
				holderChat.txtViewMsgTimestamp.setText(String.format("%s", DateUtil.getDateInStringForChatList(chat.getNote().getMsgTimestamp())));

				//__ MsgFlow
				holderChat.imgViewMsgStatus.setVisibility(View.VISIBLE);
				switch(chat.getNote().getMsgStatus()) {
					case MsgStatus.CLOCK:
						holderChat.imgViewMsgStatus.setImageResource(R.drawable.msg_status_clock_gray);
						break;
					case MsgStatus.DONE:
						holderChat.imgViewMsgStatus.setImageResource(R.drawable.msg_status_done_gray);
						break;
				}

				//__ ChatImage
				if(AppUtil.isChatWithChattyNotes(chat.getChatId()))
					holderChat.imgViewChatImage.setImageResource(R.mipmap.ic_launcher_round);
				else {
					if ((PathUtil.getInternalChatImageFile(String.valueOf(chat.getChatId())).exists())) {
						holderChat.imgViewChatImage.setImageDrawable(null); // <--- added to force redraw of ImageView
						holderChat.imgViewChatImage.setImageURI(PathUtil.getInternalChatImageUri(String.valueOf(chat.getChatId())));
					} else {
						holderChat.imgViewChatImage.setImageResource(R.drawable.avatar);
					}
				}
				return convertView;
	
		//---------------------------------------------------------------------------------------------------->>>>   STRING
		   	default:
	    	   ViewHolderString holderDefault;
	           if (convertView == null) {
	        	   holderDefault =  new ViewHolderString();
	               convertView = inflater.inflate(R.layout.list_chat_item_string, parent, false);
				   convertView.setTag(holderDefault);
	           }
	           return convertView;
		}
	}

//_________________________________________________________________________________________________
	private void clickListeners(final Chats chat, final ViewHolderChat holderChat) {

		holderChat.imgViewChatImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		    AlertDialog.Builder alert = new AlertDialog.Builder(context);
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			final View view = inflater.inflate(R.layout.dialog_chat_info, null);
			alert.setView(view);
			final AlertDialog dialog = alert.create();
			dialog.show();
            //http://stackoverflow.com/questions/4406804/how-to-control-the-width-and-height-of-default-alert-dialog-in-android
			//dialog.getWindow().setLayout(850, 980); //Controlling width and height.
			//XML__ custom dialog width and height
			//you cant just give values in integer say dialog.getWindow().setLayout(850, 980)
            //http://stackoverflow.com/questions/19133822/custom-dialog-too-small
            int  DialogWidth, DialogHeight;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			int width = metrics.widthPixels;
			int height = metrics.heightPixels;
            if(height > width) {
                DialogWidth = (7*width)/9;
                DialogHeight = (6*height)/11; //takes 3 part out of 5 from screen
            } else {
                DialogWidth = (7*width)/9;
                DialogHeight = (6*height)/11;
            }
            // Set your dialog width and height dynamically as per your screen.
            Window window = dialog.getWindow();
			if (window != null) {
				window.setLayout(DialogWidth, DialogHeight);
				window.setGravity(Gravity.CENTER);
			}

            //__ Name
			final TextView txtView = (TextView)view.findViewById(R.id.dialog_text_view);
			txtView.setText(chat.getChatName());
			//__ Photo
			final ImageView imgView = (ImageView)view.findViewById(R.id.dialog_photo);
			if(AppUtil.isChatWithChattyNotes(chat.getChatId()))
				imgView.setImageResource(R.mipmap.icon_hd);
			else {
				if ((PathUtil.getInternalChatImageFile(String.valueOf(chat.getChatId())).exists())) {
					ImageLoaderPath imageLoader = new ImageLoaderPath(context);
					String external = PathUtil.getExternalChatImageUri(String.valueOf(chat.getChatId())).toString();
					String internal = PathUtil.getInternalChatImageUri(String.valueOf(chat.getChatId())).toString();
					imageLoader.displayImageElseThumb(external, 0, imgView, internal, 300, 300);
				} else {
					imgView.setImageResource(R.drawable.avatar_large);
				}
			}
			imgView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent in = new Intent(context, ChatImageViewer.class);
					Bundle infoBundle =  new Bundle();
					infoBundle.putString(IntentKeys.CHAT_NAME, chat.getChatName());
					infoBundle.putLong(IntentKeys.CHAT_ID, chat.getChatId());
					in.putExtra(IntentKeys.BUNDLE_INFO, infoBundle);
					context.startActivity(in);
					dialog.cancel();
				}
			});
			//__ Buttons
			final ImageView btn_message = (ImageView)view.findViewById(R.id.dialog_btn_message);
			final ImageView btn_info = (ImageView)view.findViewById(R.id.dialog_btn_info);
			btn_message.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					Bundle conversationBundle = new Bundle();
					conversationBundle.putLong(IntentKeys.CHAT_ID, chat.getChatId());
					PasswordUtil.verifyPassword(context, conversationBundle, false);
					dialog.cancel();
				}
			});
			btn_info.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent in = new Intent(context, ChatInfo.class);
					Bundle infoBundle =  new Bundle();
					infoBundle.putString(IntentKeys.CHAT_NAME, chat.getChatName());
					infoBundle.putLong(IntentKeys.CHAT_ID, chat.getChatId());
					in.putExtra(IntentKeys.BUNDLE_INFO, infoBundle);
					context.startActivity(in);
					dialog.cancel();
				}
			});

				//Working 1
				//https://stackoverflow.com/questions/17516784/setting-animation-and-no-frame-for-alertdialog
				//WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
				//lp.windowAnimations = android.R.style.Animation_Translucent;
				//dialog.show();
				//dialog.getWindow().setAttributes(lp);


				//Not Working
				//https://guides.codepath.com/android/Circular-Reveal-Animation
				//https://github.com/ozodrukh/CircularReveal/blob/master/circualreveal/src/main/java/io/codetail/animation/ViewAnimationUtils.java
//				final View dialogView = View.inflate(context, R.layout.dialog_chat_contact_info, null);
//				dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//					@Override
//					public void onShow(DialogInterface dialog) {
//						final View myView = dialogView.findViewById(R.id.dialog_root);
//						// get the center for the clipping circle
//						int cx = myView.getMeasuredWidth() / 2;
//						int cy = myView.getMeasuredHeight() / 2;
//						// get the initial radius for the clipping circle
//						int initialRadius = myView.getWidth() / 2;
//						// create the animation (the final radius is zero)
//						Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
//						// start the animation
//						anim.start();
//
//
//					}
//				});
			}
		});
	}
	
//_____________________________________________________________________________________________________________
	public static String setMessage(String msg, int msgKind, String mediaCaption) {
		switch(msgKind) {
			case MsgKind.M1_IMAGE:
				if(!mediaCaption.equals(MsgConstant.DEFAULT_MEDIA_CAPTION))
					return " " + mediaCaption;
				else
					return " " + Msg.CHAT_IMAGE;
			case MsgKind.M2_AUDIO:
				return " " + Msg.CHAT_AUDIO;
			case MsgKind.M3_VIDEO :
				if(!mediaCaption.equals(MsgConstant.DEFAULT_MEDIA_CAPTION))
					return " " + mediaCaption;
				else
					return " " + Msg.CHAT_VIDEO;
			case MsgKind.M4_LONG_TEXT :
				return " " + mediaCaption;
			case MsgKind.LOCATION:
				return " " + Msg.CHAT_LOCATION;
			case MsgKind.ADDRESS:
				return " " + Msg.CHAT_ADDRESS;
			case MsgKind.CONTACT:
				return " " + Msg.CHAT_CONTACT;
			case MsgKind.TEXT:
			case MsgKind.LINK:
			case MsgKind._CHANGE_NAME:
			case MsgKind._CHANGE_IMAGE:
				return msg;
			default:
				return Msg.FEATURE_NOT_SUPPORTED(msgKind);
		}
	}

	
//___________________________________________________________________________________________________________________	
	@NonNull
	@Override
	public Filter getFilter() {
		return new Filter() {
			//this warning is intentional
			//http://stackoverflow.com/questions/14642985/type-safety-unchecked-cast-from-object-to-listmyobject
	        @SuppressWarnings("unchecked")
			@Override
	        protected void publishResults(CharSequence constraint, FilterResults results) {
	        	items = (ArrayList<InterfaceChat>)results.values;
				AdapterChat.this.notifyDataSetChanged();
			}
	
	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
				constraint = constraint.toString().toLowerCase(Locale.getDefault());
				FilterResults result = new FilterResults();
				if(constraint.toString().length()>0) {
					 ArrayList<InterfaceChat> filteredItems = new ArrayList<>();
				     for(int i=0; i<originalList.size(); i++) {
				    	InterfaceChat tempItem = originalList.get(i);
				    	 if(tempItem.itemType().equals(ItemType.CHAT)) {
							if(((Chats)tempItem).getChatName().toLowerCase(Locale.getDefault()).contains(constraint))
								 filteredItems.add(tempItem);
						 }
				     }	
					 result.count = filteredItems.size();
					 if(result.count == 0) {
						filteredItems.add(new StringChatItem(context.getString(R.string.no_chats_found)));
					 }
					 result.values = filteredItems;
				} else {
	   		    	 synchronized(this) {
	   		    		 result.values = originalList;
	   		    		 result.count = originalList.size();
	   		    	 }
				}
				return result;
	        }
		};
	}

//____________________________________________________________________________________________________ VIEW HOLDER
	private static class ViewHolderChat {
		TextView txtViewChatName;
		TextView txtViewMsgTimestamp;
		TextView txtViewMsg;
		ImageView imgViewMsgStatus;
		ImageView imgViewChatImage;
	}
	
	private static class ViewHolderString {
		TextView txtViewString;
	}
}