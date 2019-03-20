package com.chattynotes.adapters.listNotications;


import com.chattynotes.constant.ItemType;

public class NotificationCheckBoxItem implements InterfaceNotifications {

	public Boolean isChecked;
	
	public NotificationCheckBoxItem(Boolean _isChecked) {
		this.isChecked = _isChecked;
	}

	@Override
	public String itemType() {
		return ItemType.NOTIFICATION_CHECK_BOX;
	}
}
