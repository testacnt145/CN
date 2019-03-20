package com.chattynotes.lazylistPath;

import com.chattynotes.constant.ThreadName;
import com.chattynotes.util.ExifUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.net.Uri;
import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageLoaderPath {
    
	public static int type = 0;
	private Context context;
    private MemoryCachePath memoryCache=new MemoryCachePath();
    private FileCachePath fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler=new Handler();//handler to display images in UI thread
    private String defaultImagePath;//thumb to display during loading
    
    public ImageLoaderPath(Context context){
    	this.context = context;
    	fileCache = new FileCachePath();
        executorService=Executors.newFixedThreadPool(5);
    }
    
//__________________________________________________________________________________________________
    public final static int DECODE_IMAGE_FROM_FILE = 0;
    private final static int DECODE_IMAGE_FROM_RESOURCE = 1;

    //display image
    //do not show thumb
    public Bitmap displayImage(String _path, int _decodeFrom, ImageView _imageView, int _orgImageWidth, int _orgImageHeight) {
        imageViews.put(_imageView, _path);
        Bitmap bitmap = memoryCache.get(_path);
        if(bitmap!=null) {
            _imageView.setImageBitmap(bitmap);
            return bitmap;
        } else {
            queuePhoto(_path, _decodeFrom, _imageView, _orgImageWidth, _orgImageHeight);
            _imageView.setImageURI(null);
            return null;
        }
    }

    //display image,
    //show thumb during loading of image, or if image not available
    public Bitmap displayImageElseThumb(String _path, int _decodeFrom, ImageView _imageView, String thumbPath, int _orgImageWidth, int _orgImageHeight) {
    	imageViews.put(_imageView, _path);
        Bitmap bitmap = memoryCache.get(_path);
        if(bitmap!=null) {
        	_imageView.setImageBitmap(bitmap);
        	return bitmap;
        } else {
        	defaultImagePath = thumbPath;
        	queuePhoto(_path, _decodeFrom, _imageView, _orgImageWidth, _orgImageHeight);
        	_imageView.setImageURI(Uri.parse(thumbPath));//set image using URI, don't decode
    		return null;
        }
    }
    
    private void queuePhoto(String _path, int _decodeFrom, ImageView _imageView, int _orgImageWidth, int _orgImageHeight) {
        PhotoToLoad p = new PhotoToLoad(_path, _decodeFrom, _imageView, _orgImageWidth, _orgImageHeight);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(String path, int decodeFrom, int _orgImageWidth, int _orgImageHeight) {
    	Bitmap b = null;
        switch (decodeFrom) {
            case DECODE_IMAGE_FROM_FILE:
                File f = fileCache.getFile(path);
                b = decodeFile(f, _orgImageWidth, _orgImageHeight);
                break;
            case DECODE_IMAGE_FROM_RESOURCE:
                b = decodeResource(path, _orgImageWidth, _orgImageHeight);
                break;
        }
       return b;
    }

    //decodes image and scales it to reduce memory consumption
    //http://stackoverflow.com/a/9234895
	private Bitmap decodeFile(File f, int w, int h) {
        try {
            //decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            FileInputStream in = new FileInputStream(f);
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            
            options.inSampleSize = calculateInSampleSize(options, w, h);
    	    options.inJustDecodeBounds = false;
    	    
            //o2.inSampleSize=scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, options);
            stream2.close();
            
            //JIRA SAA-89 : Camera image rotation issue
            bitmap = ExifUtil.rotateBitmap(f.getPath(), bitmap);
            
            return bitmap;
        } catch (FileNotFoundException ignored) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	private Bitmap decodeResource(String path, int w, int h) {
        try {
        	int resId = Integer.valueOf(path);
    	    InputStream in = context.getResources().openRawResource(resId);
        	BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			//in = null;
			options.inSampleSize = calculateInSampleSize(options, w, h);
    	    options.inJustDecodeBounds = false;
    	    
            //o2.inSampleSize=scale;
            InputStream stream2 = context.getResources().openRawResource(resId);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, options);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException ignored) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
	/*public Bitmap decodeResource(String path, int w, int h) {

		int resId = Integer.valueOf(path);
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(context.getResources(), resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, w, h);
	    options.inJustDecodeBounds = false;
	    
	    return BitmapFactory.decodeResource(context.getResources(), resId, options);
	}*/
    
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		//LogUtil.e("ImageLoader.java --> ORG w&h -||- REQ w&h", width + ":" + height + " -||- " + reqWidth + ":" + reqHeight);
		
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
    
    //Task for the queue
    private class PhotoToLoad {
        String path;
        ImageView imgView;
        int orgImageWidth;
        int _orgImageHeight;
        int decodeFrom;
        
        PhotoToLoad(String _p, int _from, ImageView _i, int _w, int _h){
        	path = _p; 
        	decodeFrom = _from;
        	imgView = _i;
            orgImageWidth = _w;
            _orgImageHeight = _h;
        }
    }
    
    private class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            try {
                Thread.currentThread().setName(ThreadName.IMAGELOADERPATH_PHOTO_LOADER);
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.path, photoToLoad.decodeFrom, photoToLoad.orgImageWidth, photoToLoad._orgImageHeight);
                memoryCache.put(photoToLoad.path, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    private boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imgView);
        return tag == null || !tag.equals(photoToLoad.path);
    }
    
    //Used to display bitmap in the UI thread
    private class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run() {
            Thread.currentThread().setName(ThreadName.IMAGELOADERPATH_BITMAP_DISPLAYER);
            if(imageViewReused(photoToLoad))
                return;
            try {
                if (bitmap != null)
                    photoToLoad.imgView.setImageBitmap(bitmap);
                else
                    photoToLoad.imgView.setImageURI(Uri.parse(defaultImagePath));
            } catch (Exception e) {
                Toast.makeText(context, "Error loading image...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clearCache() {
    	//LogUtil.e("--->>> implicitly called...", "clearCache()");
    	try {
    		memoryCache.clear();
    		fileCache.clear();
    	} catch(Exception e) {
    		//LogUtil.eException(getClass().getSimpleName(), "Exception", "clearCache()", e.toString());
    	}
    }

}
