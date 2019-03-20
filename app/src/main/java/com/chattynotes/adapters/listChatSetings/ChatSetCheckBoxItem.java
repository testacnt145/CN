package com.chattynotes.adapters.listChatSetings;


import com.chattynotes.constant.ItemType;

class ChatSetCheckBoxItem implements InterfaceChatSettings {

	public final String name;
	Boolean isChecked;
	
	public ChatSetCheckBoxItem(String _name, Boolean _isChecked) {
		this.name = _name;
		this.isChecked = _isChecked;
	}

	@Override
	public String itemType() {
		return ItemType.CHAT_SETTING_CHECK_BOX;
	}

	@Override
	public void setHeading(String _heading) {
	}

}
