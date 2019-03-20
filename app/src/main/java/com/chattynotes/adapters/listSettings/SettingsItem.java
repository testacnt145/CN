package com.chattynotes.adapters.listSettings;

public class SettingsItem implements InterfaceSettings {

	public final String name;
	public final int id;
	
	public SettingsItem(String _name, int _id) {
		this.name = _name;
		this.id = _id;
	}

	@Override
	public String itemType() {
		return "SettingsItem";
	}

}
