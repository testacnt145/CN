package com.chattynotes.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;
import com.chattynotes.R;
import com.chattynotes.mvp.activities.GalleryVideoGrid.ThumbnailCache;
import com.chattynotes.constant.ThreadName;

public class VideoThumbnailGallery extends AsyncTask<Long, Void, Bitmap> {
	//Used in 	: Adapter galleryVideo
	//Used For 	: Load ThumbNails from Video Id
	
	private final ThumbnailCache mCache;
	private final Context ctx;
	private final ImageView mTarget;
	private long thumbnailID;

	public VideoThumbnailGallery(Context _ctx, ImageView _target, ThumbnailCache _mCache) {
		ctx = _ctx;
		mTarget = _target;
		mCache = _mCache;
	}

	@Override
	protected void onPreExecute() {
		mTarget.setTag(this);
	}

	@Override
	protected Bitmap doInBackground(Long... params) {
		Thread.currentThread().setName(ThreadName.GALLERY_VIDEO_THUMBNAIL);
		thumbnailID = params[0];
		return MediaStore.Video.Thumbnails.getThumbnail(ctx.getContentResolver(), thumbnailID, MediaStore.Video.Thumbnails.MINI_KIND, null);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (mTarget.getTag() == this) {
			if (result != null) {
				mTarget.setImageBitmap(result);
				mCache.put(String.valueOf(thumbnailID), result);
			} else {
				mTarget.setImageResource(R.drawable.media_thumbnail_missing); // Thumb not available
			}
			mTarget.setTag(null);
		}
	}
}