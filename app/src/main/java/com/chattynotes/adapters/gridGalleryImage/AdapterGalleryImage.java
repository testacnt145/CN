package com.chattynotes.adapters.gridGalleryImage;

import java.io.File;
import java.util.ArrayList;
import com.chattynotes.application.App;
import com.chattynotes.R;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.support.annotation.NonNull;

public class AdapterGalleryImage extends ArrayAdapter<InterfaceGallery> {

	private LayoutInflater inflater;
	private Context context;
	private ArrayList<InterfaceGallery> data;
	private ImageLoaderPath imageLoader;

	public AdapterGalleryImage(Context ctx, ArrayList<InterfaceGallery> items, ImageLoaderPath _imageLoader) {
		super(ctx, 0, items);
		context = ctx;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = _imageLoader;
		data = items;
	}

	//------------------------------------------------------ METHODS FOR MULTIPLE SELECTION
	private SparseBooleanArray mSelection = new SparseBooleanArray();
	  
	public boolean isPositionChecked(int position) {
		return mSelection.get(position);
    }
	
	public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

	public void removeSelection(int position) {
        mSelection.delete(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection.clear();
        notifyDataSetChanged();
    }

    /*
    public Set<Integer> getCurrentCheckedPosition() {
        return this.getCurrentCheckedPosition();
    }
    */
    
//__________________________________________________________________________________________________
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public InterfaceGallery getItem(int position) {
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
			holder = new ViewHolder();
			//if (i.itemType() == "PhotoItem") {
				convertView = inflater.inflate(R.layout.grid_gallery_image_item, parent, false);
				holder.imgView = (ImageView) convertView.findViewById(R.id.image);
				
				/*
				//PUTTING IT HERE, AVOID THE LOADING AGAIN AND AGAIN
				PhotoItem photoItem = (PhotoItem)data.get(position);
				try {
					imageLoader.displayImage("file://" + photoItem.path, holder.imgView, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.imgView.setImageResource(R.drawable.nophotos);
							super.onLoadingStarted(imageUri, view);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				//PROBLEM : not showing actual bottom images, but the repeated top one
				*/
				
				
				convertView.setTag(holder);
			//}
			/*else if(i.itemType() == "GalleryDateSeperatorItem") {
				convertView = infalter.inflate(R.layout.grid_gallery_item_date_seperator, parent, false);
				//GridLayout.LayoutParams itemLP = (GridLayout.LayoutParams)convertView.getLayoutParams();
				//itemLP.columnSpec = GridLayout.spec(0, 3);
				//convertView.setLayoutParams(itemLP);
				holder.txtView = (TextView) convertView.findViewById(R.id.id_txtViewDateSeparator);
				convertView.setTag(holder);
			}*/
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		//Place those work, which you want to update every time
		//since we want multiple selection to update every time
		multipleSelection(convertView,position);
		
		//PUTTING IT HERE, LOAD IT AGAIN AND AGAIN
		final PhotoItem photoItem = (PhotoItem)data.get(position);
		//get path of android resources
		String thumbPath = "android.resource://" + App.PACKAGE_NAME + File.separator + R.drawable.media_empty;
		imageLoader.displayImageElseThumb(photoItem.path, 0, holder.imgView, thumbPath, 100, 100);
		
		return convertView;
	}
	
	private void multipleSelection(View view, int position) {
		//Multiple Selection
		//view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.background_light)); //default color
        if (mSelection.get(position)) {
        	view.setPadding(15, 15, 15, 15);
        	view.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));// this is a selected position so make it red
        } else {
        	view.setPadding(0, 0, 0, 0);
        	view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.background_light));
        }	
		
	}

	private static class ViewHolder {
		ImageView imgView;
	}
}
