package com.chattynotes.adapters.listChatSetings;

import com.chattynotes.constant.ItemType;

public class ChatSetTextItem implements InterfaceChatSettings {

	public final String name;
	
	public ChatSetTextItem(String _name) {
		this.name = _name;
	}

	@Override
	public String itemType() {
		return ItemType.CHAT_SETTING_CHECK_TEXT;
	}

	@Override
	public void setHeading(String _heading) {
	}

}
