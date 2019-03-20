package com.chattynotes.mvp.activities;

import java.io.File;
import java.util.ArrayList;

import com.chattynotes.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.customviews.HackyViewPager;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.adapters.mediaViewer.model.Media;
import com.chattynotes.adapters.mediaViewer.AdapterMediaViewerPage;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.PermissionUtil;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBar;
import android.widget.Toast;
import android.support.annotation.NonNull;

import io.realm.Realm;

public class MediaViewer extends AppCompatActivity {

	ImageLoaderPath imageLoader;
	ArrayList<Media> mediaList = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);

        imageLoader = new ImageLoaderPath(this);

		//if permission go on, else finish the activity not here but at method onRequestPermissionsResult
        boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage_media_view_request), getString(R.string.permission_storage_media_view), R.drawable.permission_storage, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_GALLERY_IMAGE);
		if(checkPermission)
            onCreate___();
	}

	//__________________________________
	void onCreate___() {
        Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
        QueryNotesDB realmQuery = new QueryNotesDB(realm);
        long chatID = getIntent().getLongExtra(IntentKeys.CHAT_ID, MsgConstant.DEFAULT_CHAT_ID);
        String msgID =  getIntent().getStringExtra(IntentKeys.MSG_ID);
        mediaList.addAll(realmQuery.getMediaList(chatID));
        realm.close();

		int selectedIndex = 0;
		for(int i=0; i < mediaList.size(); i++) {
			Media media = mediaList.get(i);
			//finding index of selected image
			if(media.getMsgId().equals(msgID)) {
				selectedIndex = i;
				//break;, not required as below task needs complete loop to execute
			}
			//detecting deleted images
			File f = PathUtil.getExternalMediaImageFile(media.getMsgFlow(), media.getMsg());
			if(!f.exists()) {
				mediaList.remove(i);
				i--;
			}
		}
		final int totalCount = mediaList.size();

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_media_viewer);
		setSupportActionBar(toolbar);
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(selectedIndex + 1 + " of " + totalCount);
		}
		ViewPager viewPager = (HackyViewPager) findViewById(R.id.view_pager);
        AdapterMediaViewerPage adapter = new AdapterMediaViewerPage(this, mediaList, imageLoader);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(selectedIndex);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int position) {
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageSelected(int position) {
                if (actionBar != null)
                    actionBar.setTitle(position + 1 + " of " + totalCount);
            }
		});
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_media_viewer, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
        clearCache();
	}
	
//________________________________________________ OUT OF MEMORY
	void clearCache() {
		imageLoader.clearCache();
		imageLoader = null;
	}

//__________________________________________________________________________________________________
    //[os_marshmallow_: permission]
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PermissionUtil.PERMISSION_GALLERY_IMAGE:
                    onCreate___();
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
