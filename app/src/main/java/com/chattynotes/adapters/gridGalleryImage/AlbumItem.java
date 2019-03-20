package com.chattynotes.adapters.gridGalleryImage;

import java.util.ArrayList;

public class AlbumItem {
	
	public int bucketID;
	public String bucketName;
	PhotoItem coverPhoto;
	public int icon;
	ArrayList<PhotoItem> photos = new ArrayList<>();

	public AlbumItem(int _bucketID, String _bucketName, PhotoItem _coverPhoto, int _icon) {
		this.bucketID 	= _bucketID;
		this.bucketName = _bucketName;
		this.coverPhoto = _coverPhoto;
		this.icon 		= _icon;
	}

	public void addPhoto(PhotoItem photoEntry) {
		photos.add(photoEntry);
	}
}
