package com.chattynotes.adapters.listSettings;

import com.chattynotes.R;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.annotation.NonNull;

public class AdapterSettings extends ArrayAdapter<SettingsItem>  {

	private ArrayList<SettingsItem> items;
	private LayoutInflater inflater;

	public AdapterSettings(Context ctx, ArrayList<SettingsItem> items) {
		super(ctx, 0, items);
		this.items = items;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		ViewHolderSettings holderSettings;
		if (convertView == null) { 
			holderSettings =  new ViewHolderSettings();
			convertView = inflater.inflate(R.layout.list_settings_item, parent, false);
			holderSettings.txtViewSettings = (TextView) convertView.findViewById(R.id.settings_row_text);
			holderSettings.imgViewSettings = (ImageButton) convertView.findViewById(R.id.settings_row_image);
			convertView.setTag(holderSettings);
		} else { 
			holderSettings = (ViewHolderSettings)convertView.getTag(); 
		} 
		SettingsItem settingItem = items.get(position);
		holderSettings.txtViewSettings.setText(settingItem.name);
		holderSettings.imgViewSettings.setImageResource(settingItem.id);
		return convertView;
	}
	
//____________________________________________________________________________________________________ VIEW HOLDER
	private static class ViewHolderSettings {
		TextView txtViewSettings;
		ImageButton imgViewSettings;
	}

}