package com.chattynotes.adapters.gridGalleryVideo;

import com.chattynotes.async.VideoThumbnailGallery;
import com.chattynotes.mvp.activities.GalleryVideoGrid.ThumbnailCache;
import com.chattynotes.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

public class AdapterGalleryVideo extends CursorAdapter {
	
	private Context ctx;
	private ThumbnailCache mCache;
	
	public AdapterGalleryVideo(Context context, ThumbnailCache _mCache) {
        super(context, null, false);
        ctx = context;
        mCache = _mCache;
    }
    
//_____________________________________________________________________________________________________
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_gallery_video_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final long thumbnailID = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        final ImageView imageView = (ImageView) view.findViewById(R.id.video);
        // Cancel any pending thumbnail task, since this view is now bound to new thumbnail
        final VideoThumbnailGallery oldTask = (VideoThumbnailGallery) imageView.getTag();
        if (oldTask != null) {
            oldTask.cancel(false);
        }
        final Bitmap cachedResult = mCache.get(String.valueOf(thumbnailID));
        if (cachedResult != null) {
            imageView.setImageBitmap(cachedResult);
            return;
        }
        // If we arrived here, either cache is disabled or cache miss, so we
        // need to kick task to load manually
        final VideoThumbnailGallery task = new VideoThumbnailGallery(ctx, imageView, mCache);
        imageView.setImageBitmap(null);
        imageView.setImageResource(R.drawable.media_empty);
        imageView.setTag(task);
        task.execute(thumbnailID);
    } 
}
