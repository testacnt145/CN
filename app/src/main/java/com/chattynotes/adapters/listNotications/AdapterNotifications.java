package com.chattynotes.adapters.listNotications;

import com.chattynoteslite.R;
import com.chattynotes.constant.ItemType;
import java.util.ArrayList;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AdapterNotifications extends ArrayAdapter<InterfaceNotifications>  {

	private ArrayList<InterfaceNotifications> items;
	private LayoutInflater inflater;
	
	public AdapterNotifications(Context ctx, ArrayList<InterfaceNotifications> items) {
		super(ctx, 0, items);
		this.items = items;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	//---------------------------------------------------------------------------------------------
	//http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter
	@Override
	public int getItemViewType(int position) {
		final InterfaceNotifications i = items.get(position);
		if (i.itemType().equals(ItemType.NOTIFICATION_HEADING))
			return 0;
		else if (i.itemType().equals(ItemType.NOTIFICATION_CHECK_BOX))
			return 1;
		else if (i.itemType().equals(ItemType.NOTIFICATION_DOUBLE_LINE))
			return 2;
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
        InterfaceNotifications i = items.get(position);

        switch (viewType) {
            //---------------------------------------------------------------------------------------------------->>>>   notification_heading
            case 0:
                ViewHolderHeading holderHeading;
                if (convertView == null) {
                    holderHeading = new ViewHolderHeading();
                    convertView = inflater.inflate(R.layout.list_notification_heading_item, parent, false);
                    convertView.setOnClickListener(null);
                    convertView.setOnLongClickListener(null);
                    convertView.setLongClickable(false);
                    holderHeading.txtViewHeading = (TextView) convertView.findViewById(R.id.list_notification_textView_text);
                    convertView.setTag(holderHeading);
                } else {
                    holderHeading = (ViewHolderHeading) convertView.getTag();
                }
                NotificationHeadingItem headingItem = (NotificationHeadingItem) i;
                holderHeading.txtViewHeading.setText(headingItem.name);
                return convertView;
            //---------------------------------------------------------------------------------------------------->>>>   notification_check_box
            case 1:
                ViewHolderCheckBox holderCheckBox;
                if (convertView == null) {
                    holderCheckBox = new ViewHolderCheckBox();
                    convertView = inflater.inflate(R.layout.list_notification_checkbox_item, parent, false);
                    convertView.setOnClickListener(null);
                    convertView.setOnLongClickListener(null);
                    convertView.setLongClickable(false);
                    holderCheckBox.checkBox = (CheckBox) convertView.findViewById(R.id.list_notification_checkbox);
                    convertView.setTag(holderCheckBox);
                } else {
                    holderCheckBox = (ViewHolderCheckBox) convertView.getTag();
                }
                final NotificationCheckBoxItem checkBoxItem = (NotificationCheckBoxItem) i;
                holderCheckBox.checkBox.setChecked(checkBoxItem.isChecked);
                return convertView;
            //---------------------------------------------------------------------------------------------------->>>>   notification_double_line
            case 2:
                ViewHolderDoubleLine holderDoubleLine;
                if (convertView == null) {
                    holderDoubleLine = new ViewHolderDoubleLine();
                    convertView = inflater.inflate(R.layout.list_notification_double_line_item, parent, false);
                    holderDoubleLine.txtViewFirstLine = (TextView) convertView.findViewById(R.id.list_notification_textView_heading);
                    holderDoubleLine.txtViewSecondLine = (TextView) convertView.findViewById(R.id.list_notification_textView_description);
                    convertView.setTag(holderDoubleLine);
                } else {
                    holderDoubleLine = (ViewHolderDoubleLine) convertView.getTag();
                }
                final NotificationDoubleLineItem doubleItem = (NotificationDoubleLineItem) i;
                holderDoubleLine.txtViewFirstLine.setText(doubleItem.firstLine);
                holderDoubleLine.txtViewSecondLine.setText(doubleItem.secondLine);
                return convertView;
            //---------------------------------------------------------------------------------------------------->>>>
            default:
                return convertView;
        }
    }

//__________________________________________________________________________________________________
    private static class ViewHolderHeading {
        TextView txtViewHeading;
    }
    private static class ViewHolderCheckBox {
        CheckBox checkBox ;
    }
    private static class ViewHolderDoubleLine {
        TextView txtViewFirstLine;
        TextView txtViewSecondLine;
    }
}