package com.chattynotes.adapters.listChatSetings;

import com.chattynoteslite.R;
import com.chattynotes.constant.ItemType;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.support.annotation.NonNull;

public class AdapterChatSettings extends ArrayAdapter<InterfaceChatSettings>  {

	private ArrayList<InterfaceChatSettings> items;
	private LayoutInflater inflater;

	public AdapterChatSettings(Activity ctx, ArrayList<InterfaceChatSettings> items) {
		super(ctx, 0, items);
		this.items = items;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View view=convertView;
        if(convertView==null)
            view = inflater.inflate(R.layout.list_chat_settings_text_item, parent, false);

		final InterfaceChatSettings i = items.get(position);
		if (i != null) {
			if (i.itemType().equals(ItemType.CHAT_SETTING_CHECK_BOX)) {
				final ChatSetCheckBoxItem checkBoxItem = (ChatSetCheckBoxItem) i;
				view = inflater.inflate(R.layout.list_chat_settings_checkbox_item, parent, false);
				//All types of touch Events are disable on Section Item
				view.setOnClickListener(null);
				view.setOnLongClickListener(null);
				view.setLongClickable(false);
				final TextView txtViewName = (TextView) view.findViewById(R.id.list_chat_settings__textView_checkBox);
				final CheckBox checkBox = (CheckBox) view.findViewById(R.id.list_chat_settings_checkbox);
				txtViewName.setText(String.format("%s", checkBoxItem.name));
				checkBox.setChecked(checkBoxItem.isChecked);
			} else if (i.itemType().equals(ItemType.CHAT_SETTING_CHECK_NORMAL)) {
				final ChatSetNormalItem normalItem = (ChatSetNormalItem) i;
				view= inflater.inflate(R.layout.list_notification_double_line_item, parent, false);
				final TextView txtViewHeading = (TextView) view.findViewById(R.id.list_notification_textView_heading);
				final TextView txtViewDescription = (TextView) view.findViewById(R.id.list_notification_textView_description);
				txtViewHeading.setText(String.format("%s", normalItem.heading));
				txtViewDescription.setText(String.format("%s", normalItem.description));
			} else if (i.itemType().equals(ItemType.CHAT_SETTING_CHECK_TEXT)) {
				ChatSetTextItem textItem = (ChatSetTextItem) i;
				view = inflater.inflate(R.layout.list_chat_settings_text_item, parent, false);
				final TextView txtViewName = (TextView) view.findViewById(R.id.list_chat_settings__textView_text);
				txtViewName.setText(String.format("%s", textItem.name));
			}		
		}
		return view;
	}
}