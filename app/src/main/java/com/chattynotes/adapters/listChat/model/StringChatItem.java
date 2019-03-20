package com.chattynotes.adapters.listChat.model;

import com.chattynotes.constant.ItemType;

public class StringChatItem implements InterfaceChat{

	private final String title;
	
	public StringChatItem(String title) {
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}

	@Override
	public String itemType() {
		return ItemType.STRING;
	}

	@Override
	public long getLastNoteTimestamp() {
		return 0;
	}
}
