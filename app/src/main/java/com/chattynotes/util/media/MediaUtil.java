package com.chattynotes.util.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import com.chattynotes.application.App;
import com.chattynotes.util.ExifUtil;
import com.chattynotes.util.storage.PathUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class MediaUtil {

	public 	final static int CHAT_IMAGE_MAX_BOUND = 600;
	public 	final static float CHAT_IMAGE_MIN_BOUND = 192F;
	public final static int CHAT_IMAGE_MIN_WIDTH = 192;
	public final static int CHAT_IMAGE_MIN_HEIGHT = 192;

	private final static int MEDIA_IMAGE_WIDTH 	= 2000;
	private final static int MEDIA_IMAGE_HEIGHT = 2000;

	public final static int MEDIA_IMAGE_TO_LOAD_WIDTH = 250;
	public final static int MEDIA_IMAGE_TO_LOAD_HEIGHT = 250;

	private final static int MEDIA_IMAGE_QUALITY_SENDING = 100;
	private final static int MEDIA_IMAGE_CAMERA_QUALITY_SENDING = 100;

	final static float MEDIA_THUMB_BLUR = 7f; //old 16f

//________________________________________________________________________________________________________________
	private static boolean saveImageToExtStg(Bitmap bmp, File file) {
		try {

			//old method
			//ByteArrayOutputStream bos = new ByteArrayOutputStream();
			//bmp.compress(Bitmap.CompressFormat.JPEG, THUMB_QUALITY_MEDIA_SENDING, bos);
			//FileOutputStream fos = new FileOutputStream(file);
			//byte[] bytes = bos.toByteArray();
			//fos.write(bytes);
			//fos.close();

			FileOutputStream fos = new FileOutputStream(file);
			if (bmp != null)
				bmp.compress(Bitmap.CompressFormat.JPEG, MEDIA_IMAGE_QUALITY_SENDING, fos);
			fos.close();
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

//______________________________________________________________________________________________________________________
	//MEDIA_IMAGE_SIZE = 800x600
	//Activities (3) : GalleryImageSend, SendImage, ImagePreviewActivity
	public static boolean saveMediaImageToExtStg_Sent(Uri uri, String imageName, int orgWidth, int orgHeight) {
		File f = PathUtil.createExternalMediaImageFile_Sent(imageName);
		Bitmap bmp = createOptimizedScaleBitmap(uri, MEDIA_IMAGE_WIDTH, MEDIA_IMAGE_HEIGHT, orgWidth, orgHeight);
		return saveImageToExtStg(bmp, f);
	}

	static Bitmap createOptimizedScaleBitmap(Uri uri, int reqWidth, int reqHeight, int orgWidth, int orgHeight) {
		try {
			//LogUtil.e("MediaUtil", "createOptimizedScaleBitmap-A|" + reqWidth + ":" + reqHeight + "|" + orgWidth + ":" + orgHeight);

			// calculate rough re-size (this is no exact resize)
			InputStream in = App.applicationContext.getContentResolver().openInputStream(uri);
			BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inSampleSize = Math.max(orgWidth/reqWidth, orgHeight/reqHeight);
		    // decode full image
		    Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);
			if (in != null)
				in.close();

		    //rotation of image, https://gist.github.com/9re/1990019
		    roughBitmap = ExifUtil.rotateBitmap(uri.getPath(), roughBitmap);

		    //calculate exact destination size
		    Matrix m = new Matrix();

		    RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
		    RectF outRect = new RectF(0, 0, reqWidth, reqHeight);
		    m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
		    float[] values = new float[9];
		    m.getValues(values);
		    orgWidth = (int) (roughBitmap.getWidth() * values[0]);
		    orgHeight = (int) (roughBitmap.getHeight() * values[4]);

		    //LogUtil.e("MediaUtil", "createOptimizedScaleBitmap-B|" + reqWidth + ":" + reqHeight + "|" + orgWidth + ":" + orgHeight);

		    //return Bitmap.createBitmap(roughBitmap, 0, 0, orgWidth, orgHeight, m, true);
			return Bitmap.createScaledBitmap(roughBitmap, orgWidth, orgHeight, true);
		}
		catch (Exception ignored) {
		}
		return null;
	}

	static Bitmap createOptimizedScaleBitmap(Bitmap roughBitmap, int reqWidth, int reqHeight) {
		try {
			//calculate exact destination size
			Matrix m = new Matrix();

			RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
			RectF outRect = new RectF(0, 0, reqWidth, reqHeight);
			m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
			float[] values = new float[9];
			m.getValues(values);
			int orgWidth = (int) (roughBitmap.getWidth() * values[0]);
			int orgHeight = (int) (roughBitmap.getHeight() * values[4]);

			//LogUtil.e("MediaUtil", "createOptimizedScaleBitmap(after)-> O w:h -||- R w:h " + orgWidth + ":" + orgHeight + " -||- " + reqWidth + ":" + reqHeight);

			//return Bitmap.createBitmap(roughBitmap, 0, 0, orgWidth, orgHeight, m, true);
			return Bitmap.createScaledBitmap(roughBitmap, orgWidth, orgHeight, true);
		}
		catch (Exception ignored) {
		}
		return null;
	}
	
	
	
//__ROTATION
	//http://stackoverflow.com/questions/14066038/why-image-captured-using-camera-intent-gets-rotated-on-some-devices-in-android
	/*public static void checkIfOriginalImageIsRotated(String _image_path) {
		try {
			ExifInterface ei = new ExifInterface(_image_path);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			LogUtil.e("MediaUtil", "Image orientation : " + orientation);//6 = ORIENTATION_ROTATE_90 , 1 = No Rotation
			
			//if orientation occurs
			switch(orientation) {
			    case ExifInterface.ORIENTATION_ROTATE_90:
			        rotateAndSaveImage(90);
			        break;
			    case ExifInterface.ORIENTATION_ROTATE_180:
			    	rotateAndSaveImage(180);
			        break;
				}
		} catch (Exception e) {
			LogUtil.eException("MediaUtil", "Exception", "checkIfOriginalImageIsRotated", e.toString());
		}	
	}*/
		
//	//http://stackoverflow.com/questions/4166917/android-how-to-rotate-a-bitmap-on-a-center-point
//	//http://stackoverflow.com/questions/3647993/android-bitmaps-loaded-from-gallery-are-rotated-in-imageview
//	//http://stackoverflow.com/questions/10530165/android-camera-orientation-issue
//	public static void rotateAndSaveImage(int orientation) {
//	}

	public static Boolean saveCustomCameraImageWithCorrectRotation(byte[] data, Uri uri, Boolean isCameraFront) {
		//LogUtil.e("MediaUtil", "saveCustomCameraImageWithCorrectRotation");
		try {
			File file = new File(uri.getPath());
			
			//1- write original image (not optimized)
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();

			//http://stackoverflow.com/questions/18321554/how-to-correct-this-error-java-lang-outofmemoryerror
			//2- after writing, decode image(optimized) again for rotation fixing
			//Calculating each image original width and height
			BitmapFactory.Options options = MediaUtil.calculateOrgWidthHeightMetaData(uri);
			int orgWidth = options.outWidth;
			int orgHeight = options.outHeight;
			Bitmap image = createOptimizedScaleBitmap(uri, MEDIA_IMAGE_WIDTH, MEDIA_IMAGE_HEIGHT, orgWidth, orgHeight);

			//3- fix its rotation and re-write image with 100% quality as it is already optimized
			Matrix matrix = new Matrix();
			matrix = MediaUtil.fixRotation(matrix, isCameraFront);
			image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
			FileOutputStream fout = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.JPEG, MEDIA_IMAGE_CAMERA_QUALITY_SENDING, fout);
			fout.flush();
			fout.close();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
		
	private static Matrix fixRotation(Matrix matrix, Boolean _isFrontCamera) {
		Display display = ((WindowManager) App.applicationContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		if (_isFrontCamera) {
			//if (android.os.Build.VERSION.SDK_INT > 13) {
				float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
				matrix = new Matrix();
				Matrix matrixMirrorY = new Matrix();
				matrixMirrorY.setValues(mirrorY);
				matrix.postConcat(matrixMirrorY);
			//}
		}
		if (display.getRotation() == Surface.ROTATION_0) {
			if (_isFrontCamera)
				matrix.preRotate(270);
			else
				matrix.postRotate(90);
		}
		if (display.getRotation() == Surface.ROTATION_90) {
			if (_isFrontCamera)
				matrix.postRotate(0);
			else
				matrix.postRotate(0);
		}
		if (display.getRotation() == Surface.ROTATION_270) {
			if (_isFrontCamera)
				matrix.postRotate(180);
			else
				matrix.postRotate(180);
		}
		return matrix;
	}
	
//	//Activities (3) : GalleryImageSend, SendImage, CropProfilePhoto
//	public static BitmapFactory.Options calculateOrgWidthHeightMetaDataFromPath(String path) {
//		InputStream in;
//		BitmapFactory.Options options = null;
//		try {
//			in = new FileInputStream(path);
//			// decode image size (decode meta-data only, not the whole image)
//		    options = new BitmapFactory.Options();
//		    options.inJustDecodeBounds = true;
//		    BitmapFactory.decodeStream(in, null, options);
//		    in.close();
//		    in = null;
//		    //LogUtil.e("MediaUtil", "createOptimizedScaleBitmap-> O w:h " + options.outWidth + ":" + options.outHeight);
//		} catch (Exception e) {
//			LogUtil.eException("MediaUtil", "Exception", "calculateOrgWidthHeightMetaDataFromPath", e.toString());
//		}
//		return options;
//	}
	
	//Activities (3) : GalleryImageSend, SendImage, CropProfilePhoto
	public static BitmapFactory.Options calculateOrgWidthHeightMetaData(Uri uri) {
		InputStream in;
		BitmapFactory.Options options = null;
		try {
//			//Sharing Image from SKYPE Exception
//			//java.lang.securityexception permission denial opening provider from processrecord
//			//http://www.andreamaglie.com/blog/2015/01/09/access-storage-framework-uri-permission/
//			//(workarround) https://stackoverflow.com/questions/13558177/security-exception-when-trying-to-access-a-picasa-image-on-device-running-4-2
//			//https://thinkandroid.wordpress.com/2012/08/07/granting-content-provider-uri-permissions/
//			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//				App.applicationContext.grantUriPermission(App.PACKAGE_NAME, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//				final int takeFlags =  (Intent.FLAG_GRANT_READ_URI_PERMISSION);
//				App.applicationContext.getContentResolver().takePersistableUriPermission(uri, takeFlags);
//			}
			
			in = App.applicationContext.getContentResolver().openInputStream(uri);
			// decode image size (decode meta-data only, not the whole image)
		    options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, options);
			if (in != null)
				in.close();

		    //LogUtil.e("MediaUtil", "createOptimizedScaleBitmap-> O w:h " + options.outWidth + ":" + options.outHeight);
		} catch (Exception ignored) {
			//java.io.FileNotFoundException: No content provider: /storage/emulated/0/Chatty Notes/Media/Images/Sent/IMG-1509722986.jpg
		}
		return options;
	}
	
//______________________________________________________________________________________________________________________
	static void blurImage(Bitmap b, float blurRadius) {
		//define this only once if blurring multiple times
		RenderScript rs = RenderScript.create(App.applicationContext);
		final Allocation input = Allocation.createFromBitmap( rs, b, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT );
		final Allocation output = Allocation.createTyped( rs, input.getType() );
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs, Element.U8_4( rs ) );
		script.setRadius( blurRadius /* e.g. 3.f */ );
		script.setInput( input );
		script.forEach( output );
		output.copyTo( b );
	}
}
