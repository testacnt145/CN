package com.chattynotes.adapters.listConversation.model;

import com.chattynotes.constant.ItemType;

public class ConversationLoadEarlierItem implements InterfaceConversation {

	public ConversationLoadEarlierItem() {
	}

	@Override
	public String itemType() {
		return ItemType.CONVERSATION_LOAD;
	}
	
}
