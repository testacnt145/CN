package com.chattynotes.adapters.mediaViewer;

import java.util.ArrayList;
import com.chattynoteslite.R;
import com.chattynotes.adapters.mediaViewer.model.Media;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.DateUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.customviews.photoview.PhotoView;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class AdapterMediaViewerPage extends PagerAdapter {

	private ArrayList<Media> data;
	private ImageLoaderPath imageLoader;
	private LayoutInflater inflater;
	
	public AdapterMediaViewerPage(Context ctx, ArrayList<Media> items, ImageLoaderPath _imageLoader) {
		data = items;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = _imageLoader;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	//----------------------------------------------------------------------------------
	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View convertView = inflater.inflate(R.layout.pager_image_item, container, false);
		PhotoView photoView = (PhotoView)convertView.findViewById(R.id.id_photoViewPager);
		TextView txtView = (TextView)convertView.findViewById(R.id.id_txtViewPager);
		Media media = data.get(position);
		String _imagePath = PathUtil.getExternalMediaImagePath(media.getMsgFlow(), media.getMsg());
		String _thumbPath = PathUtil.getInternalMediaThumbPath(media.getChatId(), media.getMsg());
		imageLoader.displayImageElseThumb(_imagePath, 0, photoView, _thumbPath, 300, 300);
		txtView.setText(String.format("%s", DateUtil.getDateInStringComplete(media.getMsgTimestamp())));
		
		// Now just add View to ViewPager and return it
		container.addView(convertView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return convertView;
	}
}
