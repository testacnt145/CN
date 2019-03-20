package com.chattynotes.adapters.listNotications;

import com.chattynotes.constant.ItemType;

public class NotificationHeadingItem implements InterfaceNotifications {

	public final String name;
	
	public NotificationHeadingItem(String _name) {
		this.name = _name;
	}

	@Override
	public String itemType() {
		return ItemType.NOTIFICATION_HEADING;
	}
}
