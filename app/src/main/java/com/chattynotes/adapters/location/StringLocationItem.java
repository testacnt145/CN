package com.chattynotes.adapters.location;

public class StringLocationItem implements InterfaceLocation {

	private final String title;
	
	public StringLocationItem(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public String itemType() {
		return "String";
	}
}
