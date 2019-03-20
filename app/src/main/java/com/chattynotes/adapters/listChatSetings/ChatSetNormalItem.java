package com.chattynotes.adapters.listChatSetings;


import com.chattynotes.constant.ItemType;

public class ChatSetNormalItem implements InterfaceChatSettings {

	String heading;
	final String description;
	
	public ChatSetNormalItem(String _heading, String _description) {
		this.heading = _heading;
		this.description = _description;
	}

	@Override
	public String itemType() {
		return ItemType.CHAT_SETTING_CHECK_NORMAL;
	}

	@Override
	public void setHeading(String _heading) {
		this.heading = _heading;
	}
}
