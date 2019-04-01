package com.chattynotes.adapters.listForward;

import com.chattynoteslite.R;
import com.chattynotes.adapters.listForward.model.ForwardStringItem;
import com.chattynotes.adapters.listForward.model.InterfaceForward;
import com.chattynotes.constant.ItemType;
import com.chattynotes.adapters.listChat.AdapterChat;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.textformatter.TextFormatter;
import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

class AdapterForwardTabs extends ArrayAdapter<InterfaceForward> implements Filterable {

	private Typeface fontMessage = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
	private Context context;
	private ArrayList<InterfaceForward> items;
	private ArrayList<InterfaceForward> originalList;
	private LayoutInflater inflater;

	AdapterForwardTabs(Activity a, ArrayList<InterfaceForward> items) {
		super(a, 0, items);
		context = a;
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
	public InterfaceForward getItem(int position) {
	    return items.get(position);
	}
	
	 @Override
	 public long getItemId(int position) {
		 return position;
	 }

//---------------------------------------------------------------------------------------------
//{@link http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter}
	@Override
	public int getItemViewType(int position) {
		final InterfaceForward i = items.get(position);
		if (i.itemType().equals(ItemType.CHAT))
			return 1;
		else
			return 0;//string
	}

	@Override
	public int getViewTypeCount() {
		//Beware! getItemViewType() must return an int between 0 and getViewTypeCount() - 1
		return 100; //(-1 to 12 is equal to 14), keeping it 100 on the safe side
	}

//___________________________________________________________________________________________________________
	@NonNull
	@SuppressLint("CutPasteId")
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {

		int viewType = this.getItemViewType(position);
		InterfaceForward i = items.get(position);

		switch(viewType) {
			//---------------------------------------------------------------------------------------------------->>>>   string
			case 0:
				ViewHolderString holderString;
				if (convertView == null) {
					holderString =  new ViewHolderString();
					convertView = inflater.inflate(R.layout.list_chat_item_string, parent, false);
					convertView.setOnClickListener(null);
					convertView.setOnLongClickListener(null);
					convertView.setLongClickable(false);
					holderString.txtViewString 	= (TextView) convertView.findViewById(R.id.id_txtView);
					//Typeface fontString 		= Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
					//holderString.txtViewString.setTypeface(fontString, Typeface.NORMAL); //do not need in bold
					convertView.setTag(holderString);
				} else {
					holderString = (ViewHolderString)convertView.getTag();
				}

				ForwardStringItem stringItem = (ForwardStringItem) i;
				holderString.txtViewString.setText(stringItem.title);
				return convertView;
			//---------------------------------------------------------------------------------------------------->>>>   forward_recent
			case 1:
				ViewHolder holderRecent;
				if (convertView == null) {
					holderRecent = new ViewHolder();
					convertView = inflater.inflate(R.layout.list_forward_item, parent, false);
					holderRecent.txtViewName 	= (TextView)  	convertView.findViewById(R.id.forward_row_chat_name);
					holderRecent.txtViewStatus 	= (TextView)  	convertView.findViewById(R.id.forward_row_msg);
					holderRecent.imgView 		= (ImageView) 	convertView.findViewById(R.id.forward_row_chat_image);
					convertView.setTag(holderRecent);
				} else {
					holderRecent = (ViewHolder)convertView.getTag();
				}
				//font
				holderRecent.txtViewStatus.setTypeface(fontMessage, Typeface.BOLD);
				//item
				Chats chatItem = (Chats) i;
				holderRecent.txtViewName.setText(chatItem.getChatName());
				String _msg = AdapterChat.setMessage(chatItem.getNote().getMsg().length() <= 100 ? chatItem.getNote().getMsg() : chatItem.getNote().getMsg().substring(0, 100), chatItem.getNote().getMsgKind(), chatItem.getNote().getMediaCaption());//only 100 letters of message
				TextFormatter.applyFormatting(holderRecent.txtViewStatus, _msg);
				//__ ChatImage
				if(AppUtil.isChatWithChattyNotes(chatItem.getChatId()))
					holderRecent.imgView.setImageResource(R.mipmap.ic_launcher_round);
				else {
					if((PathUtil.getInternalChatImageFile(String.valueOf(chatItem.getChatId())).exists())) {
						holderRecent.imgView.setImageDrawable(null); // <--- added to force redraw of ImageView
						holderRecent.imgView.setImageURI(PathUtil.getInternalChatImageUri(String.valueOf(chatItem.getChatId())));
					} else {
						holderRecent.imgView.setImageResource(R.drawable.avatar);
					}
				}
				return convertView;

			//---------------------------------------------------------------------------------------------------->>>>   STRING
			default:
				return convertView;
		}
	}
	
//___________________________________________________________________________________________________________
	@NonNull
	@Override
    public Filter getFilter() {
		return new Filter() {
			//this warning is intentional
			//{@link http://stackoverflow.com/questions/14642985/type-safety-unchecked-cast-from-object-to-listmyobject}
	        @SuppressWarnings("unchecked")
			@Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
          		 items = (ArrayList<InterfaceForward>)results.values;
            	 AdapterForwardTabs.this.notifyDataSetChanged();  	   
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase(Locale.getDefault());
   		    FilterResults result = new FilterResults();
   		    
	   		    if(constraint.toString().length() > 0) {
	   		    	 ArrayList<InterfaceForward> filteredItems = new ArrayList<>();
		   		     for(int i=0; i<originalList.size(); i++) {
		   		    	InterfaceForward tempList = originalList.get(i);
						 switch (tempList.itemType()) {
							 case ItemType.CHAT:
								 if(((Chats)tempList).getChatName().toLowerCase(Locale.getDefault()).contains(constraint))
									 filteredItems.add(tempList);
								 break;
						 }
		   		     }
		   		     result.count = filteredItems.size();
		   		     if(result.count == 0) {
		   		    	 filteredItems.add(new ForwardStringItem(context.getString(R.string.no_match_found)));
		   		     }
		   		     result.values = filteredItems;
		   		     //LogUtil.e(getClass().getSimpleName(), "filteredItems.size() ->" + filteredItems.size());
		   		     //LogUtil.e(getClass().getSimpleName(), "originalList.size()  ->" + originalList.size());
		   		     //LogUtil.e(getClass().getSimpleName(), "items.size()         ->" + items.size());
		   		     //LogUtil.e(getClass().getSimpleName(), "adapter.size()       ->" + AdapterForwardTabs.this.getCount());
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
	private static class ViewHolderString {
		TextView txtViewString;
	}

	private static class ViewHolder {
		TextView txtViewName;
		TextView txtViewStatus;
		ImageView imgView;
	}
}