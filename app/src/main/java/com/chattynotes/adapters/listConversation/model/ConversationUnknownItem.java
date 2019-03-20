package com.chattynotes.adapters.listConversation.model;

import com.chattynotes.constant.ItemType;

public class ConversationUnknownItem implements InterfaceConversation {
	//[bug_: not required]

	private String text;

	public ConversationUnknownItem(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	@Override
	public String itemType() {
		return ItemType.CONVERSATION_UNKNOWN;
	}


}
