package com.chattynotes.adapters.listForward.model;

import com.chattynotes.constant.ItemType;

public class ForwardStringItem implements InterfaceForward{

	public final String title;
	
	public ForwardStringItem(String _title) {
		this.title = _title;
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
