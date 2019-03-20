package com.chattynotes.adapters.location;

import com.chattynotes.constant.ItemType;

public class FoursquareItem implements InterfaceLocation {
	
	public String id = "";
	public String name = "";
	public String url = "";
	public String loc_lat = "";
	public String loc_lng = "";
	public String loc_address = "";
	public String cat_name = "";
	public String type; //0=location, 1=address
	
	public FoursquareItem(){}

	//_________________________________________________________________________________________
	@Override
	public String itemType() {
		return ItemType.LOCATION;
	}

}
