package com.chattynotes.adapters.listConversation.model;

import com.chattynotes.constant.ItemType;

public class ConversationDateSeparatorItem implements InterfaceConversation {

	private String date;
	
	public ConversationDateSeparatorItem(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}



	@Override
	public String itemType() {
		return ItemType.CONVERSATION_DATE;
	}


}
