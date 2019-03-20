package com.chattynotes.camera;

import java.io.IOException;
import android.content.Context;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	public static SurfaceHolder mSurfaceHolder;
	public Camera mCamera;

	int w;
	int h;
	Context context;

	public CameraPreview(Context context, Camera camera, SurfaceView preview1) {

		super(context);
		this.context = context;
		mCamera = camera;

		mSurfaceHolder = preview1.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera == null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.setDisplayOrientation(90);
				mCamera.startPreview();
			}
		} catch (IOException e) {
			//LogUtil.e(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
		w = width;
		h = height;
		refreshCamera(mCamera);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

	}

	public void refreshCamera(Camera camera) {
		if (mSurfaceHolder.getSurface() == null) {
			return;
		}
		
		// stop preview before making changes
		try {
			camera.stopPreview();
		} catch (Exception e) {

		}
		
		// set preview size and make any resize, rotate or reformatting changes here start preview with new settings
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		if (display.getRotation() == Surface.ROTATION_0) {
			camera.setDisplayOrientation(90);
		}

		if (display.getRotation() == Surface.ROTATION_90) {
			camera.setDisplayOrientation(0);
		}

		if (display.getRotation() == Surface.ROTATION_270) {
			camera.setDisplayOrientation(180);
		}
		
		/*- XXX start-*/
		//https://stackoverflow.com/questions/11121963/how-can-i-set-camera-preview-size-to-squared-aspect-ratio-in-a-squared-surfacevi
		// set preview size and make any resize, rotate or reformatting changes here
        // Now that the size is known, set up the camera parameters and begin the preview.
        Camera.Parameters parameters = camera.getParameters();
        // You need to choose the most appropriate previewSize for your application
        Camera.Size previewSize = parameters.getSupportedPreviewSizes().get(0);
//        //log test
//        String size0 =  parameters.getSupportedPreviewSizes().get(0).width + "x" +  parameters.getSupportedPreviewSizes().get(0).height;
//        String size1 =  parameters.getSupportedPreviewSizes().get(1).width + "x" +  parameters.getSupportedPreviewSizes().get(1).height;
//        String size2 =  parameters.getSupportedPreviewSizes().get(2).width + "x" +  parameters.getSupportedPreviewSizes().get(2).height;
//        LogUtil.e("CameraPreview", "Sizes :" + size0 + ":" + size1 + ":" + size2);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
         // start preview with new settings
        camera.setParameters(parameters);
         // Set the holder size based on the aspect ratio
         int size = Math.min(display.getWidth(), display.getHeight());
         double ratio = (double) previewSize.width / previewSize.height;
         mSurfaceHolder.setFixedSize((int)(size * ratio), size);
		/*- XXX end-*/
         
         
		setCamera(camera);

		try {
			camera.setPreviewDisplay(mSurfaceHolder);
			camera.startPreview();
		} catch (Exception ignored) {
		}
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
	}

}