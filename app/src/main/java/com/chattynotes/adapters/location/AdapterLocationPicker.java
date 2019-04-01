package com.chattynotes.adapters.location;

import java.util.ArrayList;
import com.chattynoteslite.R;
import com.chattynotes.constant.ItemType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterLocationPicker extends ArrayAdapter<InterfaceLocation> {

	private ArrayList<InterfaceLocation> fourSquareList;
	private LayoutInflater inflater;

	public AdapterLocationPicker(Context context, ArrayList<InterfaceLocation> _fourSqaureList) {
		super(context, 0, _fourSqaureList);
		fourSquareList = _fourSqaureList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
//---------------------------------------------------------------------------------------------
//http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter
	@Override
	public int getItemViewType(int position) {
		final InterfaceLocation i = fourSquareList.get(position);
		if (i.itemType().equals(ItemType.LOCATION))
			return 1;
		else 
			return 0;
	}
	
	@Override
	public int getViewTypeCount() {
		//Beware! getItemViewType() must return an int between 0 and getViewTypeCount() - 1
	    return 100; //(-1 to 12 is equal to 14), keeping it 100 on the safe side
	}
	
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		int viewType = this.getItemViewType(position);
		InterfaceLocation i = fourSquareList.get(position);
		
		switch(viewType)
	    {
		//---------------------------------------------------------------------------------------------------->>>>   STRING
		   case 0:
			   ViewHolderString holderString;
	           if (convertView == null) { 
	        	   holderString =  new ViewHolderString();
	               convertView = inflater.inflate(R.layout.list_chat_item_string, parent, false);
	               
	               convertView.setOnClickListener(null);
				   convertView.setOnLongClickListener(null);
				   convertView.setLongClickable(false);
					
				   holderString.txtViewString 	= (TextView) convertView.findViewById(R.id.id_txtView);
				  
				   convertView.setTag(holderString);
	           } else { 
	        	   holderString = (ViewHolderString)convertView.getTag(); 
	           } 
	           
	           StringLocationItem stringLocItem = (StringLocationItem) i;
	           holderString.txtViewString.setText(String.format("%s", stringLocItem.getTitle()));
	           
	           return convertView;
	           
		   case 1:
				ViewHolderFourSquare holder4square;
				if (convertView == null) { 
					holder4square =  new ViewHolderFourSquare();
					convertView = inflater.inflate(R.layout.list_location_picker_row, parent, false);
		           
					holder4square.tv_name 		= (TextView) convertView.findViewById(R.id.tv_name);
					holder4square.tv_address 	= (TextView) convertView.findViewById(R.id.tv_address);
					holder4square.imgView_icon 	= (ImageView) convertView.findViewById(R.id.imgView_icon);
				   
					convertView.setTag(holder4square);
				} else { 
					holder4square = (ViewHolderFourSquare)convertView.getTag(); 
				} 
				FoursquareItem fourSquareItem = (FoursquareItem) fourSquareList.get(position);
				holder4square.tv_name.setText(String.format("%s", fourSquareItem.name));
				holder4square.tv_address.setText(String.format("%s", fourSquareItem.loc_address));
				setImage(fourSquareItem.cat_name, holder4square.imgView_icon);
				return convertView;
	    }
		return parent;
	}
	
//____________________________________________________________________________________________________ VIEW HOLDER
	private static class ViewHolderFourSquare {
		TextView tv_name;
		TextView tv_address;
		ImageView imgView_icon;
	}
	
	private static class ViewHolderString {
		TextView txtViewString;
	}

	
//_________________________________________________________________________________________________
	private void setImage(String category, ImageView imgView) {
		if (category.contains("Bank")) {
			imgView.setImageResource(R.drawable.fs_bank_2x);
		} else if (category.contains("Bulding")) {
			imgView.setImageResource(R.drawable.fs_building_2x);
		} else if (category.contains("Capitol")) {
			imgView.setImageResource(R.drawable.fs_capitolbuilding_2x);
		} else if (category.contains("Cafeteria")) {
			imgView.setImageResource(R.drawable.fs_collegecafeteria_2x);
		} else if (category.contains("Coworking")) {
			imgView.setImageResource(R.drawable.fs_coworkingspace_2x);
		} else if (category.contains("Store")) {
			imgView.setImageResource(R.drawable.fs_departmentstore_2x);
		} else if (category.contains("Furniture")) {
			imgView.setImageResource(R.drawable.fs_furniture_2x);
		} else if (category.contains("Gas")) {
			imgView.setImageResource(R.drawable.fs_gas_station_2x);
		} else if (category.contains("Travel")) {
			imgView.setImageResource(R.drawable.fs_general_travel_2x);
		} else if (category.contains("Government")) {
			imgView.setImageResource(R.drawable.fs_government_building_2x);
		} else if (category.contains("Market")) {
			imgView.setImageResource(R.drawable.fs_market_2x);
		} else if (category.contains("Medical")) {
			imgView.setImageResource(R.drawable.fs_medicalschool2x);
		} else if (category.contains("Pizza")) {
			imgView.setImageResource(R.drawable.fs_pizzaplace2x);
		} else if (category.contains("Sandwich")) {
			imgView.setImageResource(R.drawable.fs_sandwichplace_2x);
		} else if (category.contains("Student")) {
			imgView.setImageResource(R.drawable.fs_student_center_2x);
		} else if (category.contains("Tea")) {
			imgView.setImageResource(R.drawable.fs_tea_rooms_2x);
		} else if (category.contains("Tech")) {
			imgView.setImageResource(R.drawable.fs_tech_startup_2x);
		} else if (category.contains("University")) {
			imgView.setImageResource(R.drawable.fs_university2x);
		} else {
			imgView.setImageResource(R.drawable.fs_default_2x);
		}
	}
}
