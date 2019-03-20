package com.chattynotes.async;

import java.io.File;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import com.chattynotes.mvp.activities.GalleryVideoGrid.ThumbnailCache;
import com.chattynotes.constant.ThreadName;

public class VideoThumbnailForConversationList extends AsyncTask<File, Void, Bitmap> {
	//Used in 	: AdapterConversation
	
	private final ThumbnailCache mCache;
	private final ImageView mTarget;
	private String msgID;
	private final Uri blurredUri;

	public VideoThumbnailForConversationList(ImageView _target, ThumbnailCache _mCache, String _msgID, Uri _blurredUri) {
		mTarget = _target;
		mCache = _mCache;
		msgID = _msgID;
		blurredUri = _blurredUri;
	}

	@Override
	protected void onPreExecute() {
		mTarget.setTag(this);
	}

	@Override
	protected Bitmap doInBackground(File... file) {
		Thread.currentThread().setName(ThreadName.CONVERSATION_VIDEO_THUMBNAIL);
		Bitmap bitmap = null;
		try {
			File f = file[0];
			MediaMetadataRetriever media = new MediaMetadataRetriever();
			ParcelFileDescriptor parcel = ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
			media.setDataSource(parcel.getFileDescriptor());
			//@link http://stackoverflow.com/questions/12772547/mediametadataretriever-getframeattime-returns-only-first-frame
			bitmap = media.getFrameAtTime(2 * 1000, MediaMetadataRetriever.OPTION_CLOSEST); //it takes microseconds instead of milliseconds
		} catch (Exception ignored) {
		}
	    return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (mTarget.getTag() == this) {
			if (result != null) {
				mTarget.setImageBitmap(result);
				mCache.put(msgID, result);
			} else {
				mTarget.setImageURI(blurredUri); // Thumb not available
			}
			mTarget.setTag(null);
		}
	}
}