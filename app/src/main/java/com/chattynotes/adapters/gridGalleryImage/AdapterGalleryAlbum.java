package com.chattynotes.adapters.gridGalleryImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import com.chattynotes.application.App;
import com.chattynotes.R;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.annotation.NonNull;

public class AdapterGalleryAlbum extends ArrayAdapter<AlbumItem> {

	private LayoutInflater inflater;
	private ArrayList<AlbumItem> data;
	private ImageLoaderPath imageLoader;

	public AdapterGalleryAlbum(Context ctx, ArrayList<AlbumItem> items) {
		super(ctx, 0, items);
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = items;
		imageLoader = new ImageLoaderPath(ctx);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public AlbumItem getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grid_gallery_album_item, parent, false);
			holder = new ViewHolder();
			holder.albumIcon = (ImageView) convertView.findViewById(R.id.album_icon);
			holder.albumName = (TextView) convertView.findViewById(R.id.album_name);
			holder.albumCount = (TextView) convertView.findViewById(R.id.album_count);
			holder.albumCover = (ImageView) convertView.findViewById(R.id.album_cover);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.albumCover.setTag(position);
		AlbumItem albumEntry = data.get(position);
		switch(albumEntry.icon){
			case 0://camera
				holder.albumIcon.setImageResource(R.drawable.frame_overlay_gallery_camera);
				break;
			case 1://folder
				holder.albumIcon.setImageResource(R.drawable.frame_overlay_gallery_folder);
				break;
			case 2://Chatty Notes
				holder.albumIcon.setImageResource(R.drawable.frame_overlay_gallery_chatty_notes);
				break;
			case 4://video camera
				holder.albumIcon.setImageResource(R.drawable.frame_overlay_gallery_video);
				break;
		}
		holder.albumName.setText(String.format("%s", albumEntry.bucketName));
		holder.albumCount.setText(String.format(Locale.getDefault(), "%d", albumEntry.photos.size()));
		//get path of android resources
		String thumbPath = "android.resource://" + App.PACKAGE_NAME + File.separator + R.drawable.media_empty;
		if(albumEntry.coverPhoto.path != null)
			imageLoader.displayImageElseThumb(albumEntry.coverPhoto.path, 0, holder.albumCover, thumbPath, 200, 200);
		else
			imageLoader.displayImageElseThumb(thumbPath, 0, holder.albumCover, thumbPath, 200, 200);
		return convertView;
	}

	private static class ViewHolder {
		ImageView albumIcon;
		TextView albumName;
		TextView albumCount;
		ImageView albumCover;
	}
}
