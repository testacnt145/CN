package com.chattynotes.adapters.gridGalleryImage;

import java.io.File;
import java.util.ArrayList;
import com.chattynotes.application.App;
import com.chattynotes.R;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.support.annotation.NonNull;

public class AdapterGalleryImageSend extends ArrayAdapter<String> {

	private LayoutInflater inflater;
	private ArrayList<String> imagePathList;
	private ImageLoaderPath imageLoader;

	public AdapterGalleryImageSend(Context _ctx, ArrayList<String> items, ImageLoaderPath _imageLoader) {
		super(_ctx, 0, items);
		inflater = (LayoutInflater) _ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imagePathList = items;
		imageLoader = _imageLoader;
	}

	//--------------------------------------------------------------------------------------------------------------------
	@Override
	public int getCount() {
		return imagePathList.size();
	}

	@Override
	public String getItem(int position) {
		return imagePathList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
    @NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		//final ItemGalleryInterface i = data.get(position);
		
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.grid_gallery_image_item, parent, false);
			holder.imgView = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		//for the first time only
//		if(position == 0) {
//			convertView.setPadding(15, 15, 15, 15);
//			convertView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.accent));// this is a selected position so make it red
//		}

		//PUTTING IT HERE, LOAD IT AGAIN AND AGAIN
		final String path = imagePathList.get(position);

		if(path.equals("R.drawable.ic_add_large")) {
			holder.imgView.setImageResource(R.drawable.ic_add_large);
			holder.imgView.setPadding(40, 40, 40, 40);
		} else {
			//get path of android resources
			String thumbPath = "android.resource://" + App.PACKAGE_NAME + File.separator + R.drawable.media_empty;
			imageLoader.displayImageElseThumb(path, 0, holder.imgView, thumbPath, 60, 60);
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView imgView;
	}
}
