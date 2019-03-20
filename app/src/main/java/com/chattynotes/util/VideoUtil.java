package com.chattynotes.util;

import java.io.File;
import com.chattynotes.application.App;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

public class VideoUtil {
	
//_______________________________________________________________________________________________________________________________________ URI
	//https://stackoverflow.com/questions/3004713/get-content-uri-from-file-path-in-android
	//Uri.parse("content://mnt/sdcard/Movies/landscapes.mp4") is not an Uri for MediaStore. It would try to find a ContentProvider for authority mnt which does not exist
	//MediaStore can handle only content://media/... Uris which you should get exclusively via MediaStore, not by using Uri.parse()
	public static Uri getVideoContentUri(File videoFile) {
         String filePath = videoFile.getAbsolutePath();
         Uri contentUri = null;
         Cursor cursor = App.getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Video.Media._ID }, MediaStore.Video.Media.DATA + "=? ", new String[] { filePath }, null);
         if (cursor != null && cursor.moveToFirst()) {
             int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
             contentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + id);
         } else if (videoFile.exists()) {
             ContentValues values = new ContentValues();
             values.put(MediaStore.Video.Media.DATA, filePath);
             contentUri = App.getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
         }
         
//         if(contentUri!=null) {
//	         LogUtil.e("VideoUtil", "getVideoContentUri:contentUri.P " + contentUri.getPath());
//	         // /external/video/media/17958
//	         LogUtil.e("VideoUtil", "getVideoContentUri:contentUri.S " + contentUri.toString()); //toString returns the scheme
//	         // content://media/external/video/media/17958
//         }
         
         return contentUri;
    }
	
	public static Uri getVideoContentUri(long _video_id) {
		return Uri.parse(MediaStore.Video.Media.EXTERNAL_CONTENT_URI + File.separator + _video_id);
    }
	
//______________________________________________________________________________________________________________________________________ METADATA
	//https://stackoverflow.com/questions/16259552/how-to-get-length-in-milliseconds-of-video-from-url-without-video-view-in-androi
	public static VideoMetaData getVideoMetaDataViaMediaMetadataRetriever(Context ctx, Uri _videoUri) {
		try {
			
			//LogUtil.e("UriUtilities : getVideoMetaDataViaMediaMetadataRetriever|U", _video_uri.toString());
			// _video_uri should be like
			// content://media/external/video/media/16269
	
			//https://stackoverflow.com/questions/2975197/convert-file-uri-to-file-in-android
			//_video_uri.toString() 		=> NOT WORKING	=> content://media/external/video/media/16269
			//_video_uri.getPath()  		=> NOT WORKING	=> external/video/media/16269
			//getRealPathFromURI(_video_uri)=> WORKING 	    => /storage/emulated/0/Video/SummerSlam 2015.mp4


			String videoPath;
			if(_videoUri!=null && _videoUri.getScheme()!=null && _videoUri.getScheme().equals("content"))
				videoPath = AndUriUtil.getRealPathFromURI(ctx, _videoUri);
			else
				videoPath = _videoUri.getPath();

			//LogUtil.e("VideoUtil","getVideoMetaDataViaMediaMetadataRetriever : P: " + videoPath);
			//LogUtil.e("VideoUtil","getVideoMetaDataViaMediaMetadataRetriever : U: " + _videoUri);

			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			retriever.setDataSource(App.getContext(), _videoUri);
			String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			long videoDuration = Long.parseLong(time);
			//long video_duration = timeInMilliSec / 1000;
			//long hours = duration / 3600;
			//long minutes = (duration - hours * 3600) / 60;
			//long seconds = duration - (hours * 3600 + minutes * 60);
			String videoWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
			String videoHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
			File file = new File(videoPath);
			long videoSize = file.length();

			return new VideoMetaData(videoPath, videoSize, videoDuration, Integer.parseInt(videoWidth), Integer.parseInt(videoHeight));
			
		} catch(Exception ignored) {
		}
		return null;
	}
	
//	
//	//https://stackoverflow.com/questions/16259552/how-to-get-length-in-milliseconds-of-video-from-url-without-video-view-in-androi
//	public static VideoMetaData getVideoMetaDataViaMediaMetadataRetriever(String _video_path) {
//	
//		try {
//			//LogUtil.e("UriUtilities : getVideoMetaDataViaMediaMetadataRetriever|P", _video_path);
//			
//			// _video_path should be like
//			// /storage/emulated/0/Video/SummerSlam 2015.mp4
//			
//			File file = new File(_video_path);
//			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//			retriever.setDataSource(_video_path);
//			String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//			long video_duration = Long.parseLong(time);
//			//long video_duration = timeInmillisec / 1000;
//			//long hours = duration / 3600;
//			//long minutes = (duration - hours * 3600) / 60;
//			//long seconds = duration - (hours * 3600 + minutes * 60);
//			String video_width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
//			String video_height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
//			long video_size = file.length();
//			
//			VideoMetaData meta_data = new VideoMetaData(_video_path, video_size, video_duration, Integer.parseInt(video_width), Integer.parseInt(video_height));
//			return meta_data;
//			
//		} catch(Exception e){
//			LogUtil.e("getVideoMetaDataViaMediaMetadataRetriever|P", "Exception : " + e.toString());
//		}
//		
//		return null;
//	}

//______________________________________________________________________________________________________
//	public static VideoMetaData getVideoMetaDataViaMediaStoreID(long id) {
//		
//		//LogUtil.e("UriUtilities : getVideoMetaDataViaMediaStoreID", id + ":");
//		
//		String video_path = "";
//		long video_size = 0;
//		long video_duration = 0;
//		String resolution = "";
//		int video_width = 600;
//		int video_height = 600;
//		
//		
//		final Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
//    	Cursor cursor = null;
//		try { 
//			  String[] proj = { MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.RESOLUTION };
//			  cursor = App.getContext().getContentResolver().query(uri,  proj, null, null, null);
//			  int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//			  int sizeColumn = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
//		      int durationColumn = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
//		      int resolutionColumn = cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION);
//		      
//		      if (cursor != null) {
//				  while (cursor.moveToNext()) {
//					  video_path = cursor.getString(dataColumn);
//					  video_size = cursor.getLong(sizeColumn);
//					  video_duration = cursor.getLong(durationColumn);
//					  resolution = cursor.getString(resolutionColumn);
//		           }
//		      }
//		} finally {
//			if (cursor != null) {
//		    cursor.close();
//			}
//		}
//		
//		if(resolution != null) {
//			String[] _resolution = resolution.split("x");
//			video_width = Integer.parseInt(_resolution[0]);
//			video_height = Integer.parseInt(_resolution[1]);
//		}
//		
//		VideoMetaData meta_data = new VideoMetaData(video_path, video_size, video_duration, video_width, video_height);
//		return meta_data;
//	}
	
//______________________________________________________________________________________________________________________________________
	public static class VideoMetaData {
		
		public String videoPath 	= "";
		public long   videoSize 	= 0;
		public long   videoDuration	= 0;
		public int    videoWidth 	= 0;
		public int    videoHeight 	= 0;
		
		VideoMetaData(String _videoPath, long _videoSize, long _videoDuration, int _videoWidth, int _videoHeight) {
			videoPath 	 = _videoPath;
			videoSize 	 = _videoSize;
			videoDuration= _videoDuration;
			videoWidth   = _videoWidth;
			videoHeight  = _videoHeight;
		}
	}

}
