package com.chattynotes.mvp.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.chattynotes.R;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.adapters.gridGalleryImage.AlbumItem;
import com.chattynotes.adapters.gridGalleryImage.AdapterGalleryAlbum;
import com.chattynotes.adapters.gridGalleryImage.PhotoItem;
import com.chattynotes.util.PermissionUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

public class GalleryImageAlbum extends AppCompatActivity implements OnItemClickListener {

	//Conversation Activity Requirements
	String chatName;
	long chatID;

    ArrayList<AlbumItem> albumList = new ArrayList<>();
	GridView gridview;
	LinearLayout ll_NoMedia;
	AdapterGalleryAlbum adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_image_album);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery_image_album);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		chatName = getIntent().getStringExtra(IntentKeys.CHAT_NAME);
		chatID = getIntent().getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
		
		gridview = (GridView) findViewById(R.id.gridView_Album_Image);
		gridview.setFastScrollEnabled(true);

		adapter = new AdapterGalleryAlbum(this, albumList);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		
		ll_NoMedia = (LinearLayout) findViewById(R.id.ll_NoMedia);

		populateGrid();

		//just for showing gallery image albums
		PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
	}

	void populateGrid() {
		//Get All Albums
		albumList.addAll(getGalleryImageAlbums());
		adapter.notifyDataSetChanged();

		if (albumList.isEmpty())
			ll_NoMedia.setVisibility(View.VISIBLE);
		else
			ll_NoMedia.setVisibility(View.GONE);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
		AlbumItem item = albumList.get(position);
		Intent in = new Intent(this, GalleryImageGrid.class);
		Bundle bundleImage = new Bundle();
		bundleImage.putString(IntentKeys.CHAT_NAME, chatName);
		bundleImage.putLong(IntentKeys.CHAT_ID, chatID);
		bundleImage.putString(IntentKeys.GALLERY_BUCKET_ID	, String.valueOf(item.bucketID));
		bundleImage.putString(IntentKeys.GALLERY_BUCKET_NAME, item.bucketName);
		in.putExtra(IntentKeys.BUNDLE_IMAGE, bundleImage);
		startActivityForResult(in, AppConst.GARBAGE_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent != null) //means,  either Send or Cancel is pressed
			if(resultCode == RESULT_CANCELED) {
				setResult(RESULT_CANCELED, new Intent());
				finish();
			}
	}
	
//-------------------------------------------------------------------------------------------------
    static final String[] projectionPhotos = {
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.ORIENTATION
    };
	
	private ArrayList<AlbumItem> getGalleryImageAlbums() {
		final ArrayList<AlbumItem> albumsSorted = new ArrayList<>();
		Map<Integer, AlbumItem> albumDictionary = new HashMap<>();
	    AlbumItem allPhotosAlbum = null;
	    String cameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "Camera/";
	    Integer cameraAlbumId = null;
	
	    Cursor cursor = null;
	    try {
	        cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
	        if (cursor != null) {
	            //int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
	            int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
	            int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
	            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
	            int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
	            //int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
	
	            while (cursor.moveToNext()) {
	                //int imageId = cursor.getInt(imageIdColumn);
	                int bucketId = cursor.getInt(bucketIdColumn);
	                String bucketName = cursor.getString(bucketNameColumn);
	                String path = cursor.getString(dataColumn);
	                long dateTaken = cursor.getLong(dateColumn);
	                //int orientation = cursor.getInt(orientationColumn);
	                if (path == null || path.length() == 0) {
	                    continue;
	                }
	                PhotoItem photoEntry = new PhotoItem(path);
	                //if no album, make single album name "All pictures"
	                if (allPhotosAlbum == null) {
	                    allPhotosAlbum = new AlbumItem(0, "All pictures", photoEntry, 0);
	                    albumsSorted.add(0, allPhotosAlbum);
	                } else
	                    allPhotosAlbum.addPhoto(photoEntry);
	                //ALBUM WORK
	                AlbumItem albumEntry = albumDictionary.get(bucketId);//get bucketId
	                //LogUtil.e(getClass().getSimpleName(), "check albumEntry " + albumEntry);
	                //albumEntry will be null when dictionary don't contain the key,
	                //albumEntry null meaning new Album
	                if (albumEntry == null) {
	                	if(bucketName.contains(getString(R.string.app_name)))
	                		albumEntry = new AlbumItem(bucketId, bucketName, photoEntry, 2);
	                	else if(bucketName.contains("Camera"))
	                		albumEntry = new AlbumItem(bucketId, bucketName, photoEntry, 0);
	                	else
	                		albumEntry = new AlbumItem(bucketId, bucketName, photoEntry, 1);
						albumDictionary.put(bucketId, albumEntry);//put bucketId in dictionary
	
	                    if (cameraAlbumId == null && path.startsWith(cameraFolder)) {
	                        albumsSorted.add(0, albumEntry);
	                        cameraAlbumId = bucketId;
	                    } else {
	                        albumsSorted.add(albumEntry);
	                    }
	                }
	                albumEntry.addPhoto(photoEntry);
	            }
	        }
	    } catch (Exception ignored) {
	    } finally {
	        if (cursor != null) {
	            try {
	                cursor.close();
	            } catch (Exception ignored) {
	            }
	        }
	    }
	    
	    return albumsSorted;
			
	}
//___________________________________________________________________________________________________________
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu__overflow, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} else if (id == R.id.menu_overflow) {
			setResult(RESULT_OK, new Intent());
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case PermissionUtil.PERMISSION_GALLERY_IMAGE:
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
