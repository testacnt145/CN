package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MimeType;
import com.chattynotes.memory.LruCache;
import com.chattynotes.adapters.gridGalleryVideo.AdapterGalleryVideo;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import com.chattynotes.util.VideoUtil;

import android.Manifest;
import android.app.ActivityManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.annotation.NonNull;

public class GalleryVideoGrid extends AppCompatActivity implements OnItemClickListener {

    //Conversation Activity Requirements
  	String chatName;
  	long chatID;
  	//Album info
  	String bucketID;
  	String bucketName;
  	//used in next activity
  	String mediaName;

    static final int LOADER_CURSOR = 1;
    ThumbnailCache mCache;
    AdapterGalleryVideo adapter;
    GridView gridview;
    LinearLayout ll_NoMedia;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_video_grid);

        mediaName = PathUtil.generateFileNameUnix(MimeType.MEDIA_VIDEO);

        Bundle bundleVideo 	= getIntent().getBundleExtra(IntentKeys.BUNDLE_VIDEO);
        chatName         	= bundleVideo.getString(IntentKeys.CHAT_NAME);
        chatID           	= bundleVideo.getLong(IntentKeys.CHAT_ID);
        bucketID 			= bundleVideo.getString(IntentKeys.GALLERY_BUCKET_ID);
        bucketName			= bundleVideo.getString(IntentKeys.GALLERY_BUCKET_NAME);

        //Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery_video_grid);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(bucketName);
        }

		// Pick cache size based on memory class of device
        final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final int memoryClassBytes = am.getMemoryClass() * 1024 * 1024;
        mCache = new ThumbnailCache(memoryClassBytes / 2);

        gridview = (GridView) findViewById(R.id.grid_Videos);
        gridview.setFastScrollEnabled(true);

        adapter = new AdapterGalleryVideo(this, mCache);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        ll_NoMedia = (LinearLayout) findViewById(R.id.ll_NoMedia);

        boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_VIDEO);
        if(checkPermission)
            populateGrid();
    }

    void populateGrid() {
        ll_NoMedia.setVisibility(View.GONE);
        gridview.setRecyclerListener(new RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
                // Release strong reference when a view is recycled
                final ImageView imageView = (ImageView) view.findViewById(R.id.video);
                imageView.setImageBitmap(null);
                imageView.setImageResource(R.drawable.ic_gallery_picker);
            }
        });
        // Kick off loader for Cursor with list of photos
        getLoaderManager().initLoader(LOADER_CURSOR, null, mCursorCallbacks);
    }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		callSendVideoActivity(id);
	}
	
	void callSendVideoActivity(long id) {
		Uri videoUri = VideoUtil.getVideoContentUri(id);
		Intent in = new Intent(this, SendVideo.class);
        Bundle bundleVideo = new Bundle();
        bundleVideo.putString	(IntentKeys.CHAT_NAME, chatName);
        bundleVideo.putLong	    (IntentKeys.CHAT_ID, chatID);
        bundleVideo.putString	(IntentKeys.MEDIA_NAME, mediaName);
        bundleVideo.putString	(IntentKeys.MEDIA_VIDEO_URI, videoUri.toString());
        in.putExtra(IntentKeys.BUNDLE_VIDEO, bundleVideo);
		startActivityForResult(in, AppConst.GARBAGE_REQUEST);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent != null)
			//doing same work on Send and Cancel, the main reason for Cancel is to delete Camera Video @CameraActivity
			if(resultCode == RESULT_CANCELED) { //cancel is pressed
				finishCancelled();
			} else if(resultCode == RESULT_OK) { //send is pressed
				finishCancelled();
			}
	}
	
	
//_____________________________________________________________________________________________________
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // Memory we can release here will help overall system performance, and
        // make us a smaller target as the system looks for memory

        if (level >= TRIM_MEMORY_MODERATE) { // 60
            // Nearing middle of list of cached background applications; evict our entire thumbnail cache
        	// LogUtil.e(getClass().getSimpleName(), "evicting entire thumbnail cache");
            mCache.evictAll();
        } else if (level >= TRIM_MEMORY_BACKGROUND) { // 40
            // Entering list of cached background applications; evict oldest half of our thumbnail cache
        	// LogUtil.e(getClass().getSimpleName(), "evicting oldest half of thumbnail cache");
            mCache.trimToSize(mCache.size() / 2);
        }
    }
  
//_____________________________________________________________________________________________________
    private final LoaderCallbacks<Cursor> mCursorCallbacks = new LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final String[] columns = { BaseColumns._ID };
            if(bucketID.equals("0"))// means ALL videos
                return new CursorLoader(GalleryVideoGrid.this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC");
            else
            	return new CursorLoader(GalleryVideoGrid.this,  MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, MediaStore.Video.Media.BUCKET_ID + " = ?", new String[] { bucketID }, MediaStore.Video.Media.DATE_TAKEN + " DESC");
	    }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        	adapter.swapCursor(null);
        }
    };
    
//_____________________________________________________________________________________________________
    /**
     * Simple extension that uses {@link Bitmap} instances as keys, using their
     * memory footprint in bytes for sizing.
     */
    public static class ThumbnailCache extends LruCache<String, Bitmap> {
        public ThumbnailCache(int maxSizeBytes) {
            super(maxSizeBytes);
        }
        
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    }
    
//________________________________________________________________________________________________________
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish(); //*just go to back activity
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.swapCursor(null);
    }
  	
  	void finishCancelled() {
  		setResult(RESULT_CANCELED, new Intent());
  		finish();
  	}

//__________________________________________________________________________________________________
    //[os_marshmallow_: permission]
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PermissionUtil.PERMISSION_GALLERY_VIDEO:
                    populateGrid();
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
        }
    }
}

