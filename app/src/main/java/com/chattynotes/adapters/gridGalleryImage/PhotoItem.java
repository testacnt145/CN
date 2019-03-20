package com.chattynotes.adapters.gridGalleryImage;

public class PhotoItem implements InterfaceGallery {

	public String path;

	public PhotoItem(String path) {
		this.path = path;
	}
	
	@Override
	public String itemType() {
		return "PhotoItem";
	}
}