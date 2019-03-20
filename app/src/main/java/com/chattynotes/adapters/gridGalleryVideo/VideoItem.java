package com.chattynotes.adapters.gridGalleryVideo;

public class VideoItem {
	
	public long id;
	public String videopath;
	public long size;
	public long duration;
	public String thumbpath;
	
	public VideoItem(long _id, String _videopath, long _size, long _duration, String _thumbpath) {
		id = _id;
		videopath = _videopath;
		size = _size;
		duration = _duration;
		thumbpath = _thumbpath;
	}
}