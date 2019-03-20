package com.chattynotes.mvp.activities;

import java.util.ArrayList;

import com.chattynotes.R;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.adapters.gridGalleryImage.AdapterGalleryImage;
import com.chattynotes.adapters.gridGalleryImage.InterfaceGallery;
import com.chattynotes.adapters.gridGalleryImage.PhotoItem;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.PermissionUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

public class GalleryImageGrid extends AppCompatActivity implements OnItemClickListener {

	//multiple selection
	ArrayList<InterfaceGallery> selectionList = new ArrayList<>();//For selected item
		
	//Conversation Activity Requirements
	String chatName;
	long chatID;
	//Album info 
	String bucketID;
	String bucketName;

    private AdapterGalleryImage adapter;
    ArrayList<InterfaceGallery> photoList = new ArrayList<>();
    GridView gridview;
    LinearLayout ll_NoMedia;
    ImageLoaderPath imageLoader;
		
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_image_grid);

		Bundle bundleImage 	= getIntent().getBundleExtra(IntentKeys.BUNDLE_IMAGE);
		chatName 	= bundleImage.getString(IntentKeys.CHAT_NAME);
		chatID 		= bundleImage.getLong(IntentKeys.CHAT_ID);
		bucketID 	= bundleImage.getString(IntentKeys.GALLERY_BUCKET_ID);
		bucketName	= bundleImage.getString(IntentKeys.GALLERY_BUCKET_NAME);

		//Toolbar
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery_image_grid);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(R.layout.toolbar__2_titles);
			((TextView) findViewById(R.id.toolbar_title_1)).setText(String.format("%s", bucketName));
			((TextView) findViewById(R.id.toolbar_title_2)).setText(String.format("%s", getString(R.string.multiple_selection)));
		}

        gridview = (GridView) findViewById(R.id.gridView_Photos);
        gridview.setFastScrollEnabled(true);

        imageLoader = new ImageLoaderPath(this);
        adapter = new AdapterGalleryImage(this,  photoList, imageLoader);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        ll_NoMedia = (LinearLayout) findViewById(R.id.ll_NoMedia);

        initializeCAB();

        populateGrid();

		//just for showing gallery images
		PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_send_request), getString(R.string.permission_storage_media_send), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
	}

    void populateGrid() {
        //Get All Photos
        //photoList.add(new GalleryDateSeparatorItem("January"));
        photoList.addAll(getGalleryPhotos());
        adapter.notifyDataSetChanged();

        if (photoList.isEmpty())
            ll_NoMedia.setVisibility(View.VISIBLE);
        else
            ll_NoMedia.setVisibility(View.GONE);
    }


//____________________________________________________________________________________________________________________________ CAB
		void initializeCAB() {
		//multiple selection
		gridview.setLongClickable(true);
		gridview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		gridview.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			private int selectionCount = 0;
			
			@Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		    	selectionList.clear();
		    	selectionCount = 0;
		        // Inflate the menu for the CAB
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.cab__tick, menu);
		        return true;
		    }
			
			@Override
		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		    	mode.setTitle(String.valueOf(selectionList.size()));
	            // Here you can perform updates to the CAB due to
		        // an invalidate() request
		        return false;
		    }
			
		    @Override
		    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
		        // Here you can do something when items are selected/de-selected,
		        // such as update the title in the CAB
		    	//LogUtil.e("onItemCheckedStateChanged", position + ":");
		    	//if (position == 0)
		        //   return; // so we start the CAB but there aren't any items checked
		       
		    	 if (!adapter.isPositionChecked(position)) {
		    		 if(selectionCount < AppConst.MAX_IMAGE_AT_ONCE) {
		    			 selectionList.add(adapter.getItem(position));
			    		 selectionCount++;
	                     adapter.setNewSelection(position, checked);
	                }
                 } else {
                	 selectionList.remove(adapter.getItem(position));
                	 selectionCount--;
                     adapter.removeSelection(position); 
                     
                     if(selectionCount == 0)
                    	 mode.finish();
		 		 }
                 mode.setTitle(String.valueOf(selectionCount));
                 //LogUtil.e("selectionList.size()", selectionList.size() + ":");
     	    }

		    @Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		    	// Respond to clicks on the actions in the CAB
		        switch (item.getItemId()) {
		            case R.id.menu_tick:
		            	String[] str = new String[selectionList.size()];
                    	for(int i=0; i < selectionList.size(); i++)
                    		str[i] = ((PhotoItem)selectionList.get(i)).path;
                        changeActivity(str);
                        
                        //Don't do these 3 steps on click Tick, because if user returns back to this activity, we want selection to be held as it is
                        //selectionCount = 0;
                        //adapter.clearSelection();
                        //mode.finish(); // Action picked, so close the CAB
		                return true;
		            default:
		                return false;
		        }
		    }

		    @Override
		    public void onDestroyActionMode(ActionMode mode) {
		    	adapter.clearSelection();
		        // Here you can make any necessary updates to the activity when
		        // the CAB is removed. By default, selected items are deselected/unchecked.
		    }

		    
		});
		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
		    @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		    	gridview.setItemChecked(position, !adapter.isPositionChecked(position)); //Trigger CAB on Long Press
                return false;
            }
        });
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		//LogUtil.e(getClass().getSimpleName(), "click: Single -->> " + position);
		
		String[] str = new String[1];
		str[0] = ((PhotoItem)photoList.get(position)).path;
		changeActivity(str);
		
		//Start CAB on single click
		gridview.setItemChecked(position, !adapter.isPositionChecked(position));
        
	}
	
	void changeActivity(String[] _imageUriArray) {
		
		Intent in = new Intent(this, GalleryImageSend.class);
		Bundle bundleImage = new Bundle();
		bundleImage.putString(IntentKeys.CHAT_NAME, chatName);
		bundleImage.putLong(IntentKeys.CHAT_ID, chatID);
		bundleImage.putStringArray(IntentKeys.MEDIA_IMAGE_URI_LIST, _imageUriArray);
		in.putExtra(IntentKeys.BUNDLE_IMAGE, bundleImage);
		
    	imageLoader.clearCache();
		startActivityForResult(in, AppConst.GARBAGE_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//LogUtil.e(getClass().getSimpleName(), "requestCode : " + requestCode);
		//LogUtil.e(getClass().getSimpleName(), "resultCode  : " + resultCode);
		//LogUtil.e(getClass().getSimpleName(), "data/intent : " + intent);
		if(intent != null)//means,  either Send or Cancel is pressed
			if(resultCode == RESULT_CANCELED) {
				setResult(RESULT_CANCELED, new Intent());
				finish();
			}
	}
	
	
//-------------------------------------------------------------------------------------------------
    static final String[] projectionPhotos = {
        //MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        //MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DATE_TAKEN,
        //MediaStore.Images.Media.ORIENTATION
    };
    
    private ArrayList<PhotoItem> getGalleryPhotos() {
		final ArrayList<PhotoItem> photoList = new ArrayList<>();
	    Cursor cursor = null;
	    try {
	    	if(bucketID.equals("0"))// means ALL pictures
	    		cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
	    	else
	    		cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, MediaStore.Images.Media.BUCKET_ID + " = ?", new String[] {bucketID}, MediaStore.Images.Media.DATE_TAKEN + " DESC");
	    	
	    	//second method, (not working)
	    	//String WHERE =  MediaStore.Images.Media.BUCKET_ID + " = " + BUCKET_ID;
	    	//cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, WHERE, MediaStore.Images.Media.DATE_TAKEN + " DESC");
    		
	    	if (cursor != null) {
	            //int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
	            //int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
	            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
	            int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
	            //int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
	            
	            while (cursor.moveToNext()) {
	            	//int bucketId = cursor.getInt(bucketIdColumn);
	                //int imageId = cursor.getInt(imageIdColumn);
	                long dateTaken = cursor.getLong(dateColumn);
	                String path = cursor.getString(dataColumn);
	                //int orientation = cursor.getInt(orientationColumn);
	
	                if (path == null || path.length() == 0) {
	                    continue;
	                }
	                //PhotoItem photoItem = new PhotoItem(bucketId, imageId, dateTaken, path, orientation);
	                PhotoItem photoItem = new PhotoItem(path);
	                photoList.add(photoItem);
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
	    
	    return photoList;
			
	}

//__________________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
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
