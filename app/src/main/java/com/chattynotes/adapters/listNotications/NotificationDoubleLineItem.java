package com.chattynotes.adapters.listNotications;


import com.chattynotes.constant.ItemType;

public class NotificationDoubleLineItem implements InterfaceNotifications {

	public String firstLine;
	String secondLine;
	
	public NotificationDoubleLineItem(String _firstLine, String _secondLine) {
		this.firstLine = _firstLine;
		this.secondLine = _secondLine;
	}

	@Override
	public String itemType() {
		return ItemType.NOTIFICATION_DOUBLE_LINE;
	}


}
