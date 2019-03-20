/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chattynotes.customviews.crop_biokys;

import com.chattynotes.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.util.BiokysUtil;
import com.chattynotes.util.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * The activity can crop specific region of interest from an image.
 * This activity is cropping the image, and on OK click, it is saving the crop portion to imageSaveUri (MediaUtil.internal_temporary_profile_path)
 */

public class BiokysCropImage extends MonitoredActivity {

    private Uri 	image_uri = null;
    private Uri 	image_save_uri = null;
    private int 	image_maximum_bound;
    private int		mAspectX;
    private int     mAspectY;
    private int     mOutputX;
    private int     mOutputY;
    private boolean mScale;
    private boolean mCircleCrop = false;
    
    private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
    private boolean mDoFaceDetection = true;
    private final Handler mHandler = new Handler();
    
    private Bitmap          mBitmap;
    private BiokysCropImageView mImageView;
    private ContentResolver mContentResolver;
    
    boolean       mWaitingToPick; 	// Whether we are wait the user to pick a face.
    boolean       mSaving;  		// Whether the "save" button is already clicked.
    HighlightView mCrop;

    // These options specify the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private boolean mScaleUp = true;

    private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContentResolver = getContentResolver();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cropimage);
        mImageView = (BiokysCropImageView) findViewById(R.id.image);
        
        HighlightView.image_minimum_bound 	= getIntent().getExtras().getFloat  (IntentKeys.MEDIA_IMAGE_MIN_BOUND);
        image_maximum_bound 	            = getIntent().getExtras().getInt    (IntentKeys.MEDIA_IMAGE_MAX_BOUND);
        //____________________
        //these are the value that should be passed from the previous activity,
        //but we are using default values for now on
        //mAspectX 	= getIntent().getExtras().getInt(IntentKeys.MEDIA_IMAGE_ASPECT_X);
        mAspectX = 1;
		//mAspectY 	= getIntent().getExtras().getInt(IntentKeys.MEDIA_IMAGE_ASPECT_Y);
        mAspectY = 1;
		//mScaleUp 	= getIntent().getExtras().getBoolean(IntentKeys.MEDIA_IMAGE_IS_SCALE_UP_REQUIRED, true);
        mScaleUp = true;
		//mScale 	= getIntent().getExtras().getBoolean(IntentKeys.MEDIA_IMAGE_IS_SCALE_REQUIRED, true);
        mScale = true;
        mOutputX 	= getIntent().getExtras().getInt(IntentKeys.MEDIA_IMAGE_OUTPUT_X);
		mOutputY 	= getIntent().getExtras().getInt(IntentKeys.MEDIA_IMAGE_OUTPUT_Y);
        //____________________
        mCircleCrop = getIntent().getExtras().getBoolean(IntentKeys.MEDIA_IMAGE_IS_CIRCLE_CROP);
		//URI was passed as Uri.toString()
		image_uri 		= Uri.parse(getIntent().getStringExtra(IntentKeys.MEDIA_IMAGE_URI));
        image_save_uri  = Uri.parse(getIntent().getStringExtra(IntentKeys.MEDIA_IMAGE_TEMP_FILE));
		mBitmap 		= decodeBitmap(image_uri);
		
		if(mCircleCrop) {
			 mAspectX = 1;
             mAspectY = 1;
		}
		
        if (mBitmap == null) {
        	setResult(RESULT_CANCELED);
            finish();
            return;
        }

        // Make UI full screen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.discard).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                    	setResult(RESULT_CANCELED);
                        finish();
                    }
                });

        findViewById(R.id.save).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                    	      onSaveClicked();
                        } catch (Exception e) {
                        	setResult(RESULT_CANCELED);
                            finish();
                        }
                    }
                });
        findViewById(R.id.rotateLeft).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        mBitmap = BiokysUtil.rotateImage(mBitmap, -90);
                        RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
                        mImageView.setImageRotateBitmapResetBase(rotateBitmap, true);
                        mRunFaceDetection.run();
                    }
                });

        findViewById(R.id.rotateRight).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        mBitmap = BiokysUtil.rotateImage(mBitmap, 90);
                        RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
                        mImageView.setImageRotateBitmapResetBase(rotateBitmap, true);
                        mRunFaceDetection.run();
                    }
                });
        
        mImageView.setImageBitmapResetBase(mBitmap, true);

        startFaceDetection();
    }

    private void startFaceDetection() {

        if (isFinishing()) {
            return;
        }

        mImageView.setImageBitmapResetBase(mBitmap, true);

        BiokysUtil.startBackgroundJob(this, null,
                "Please wait\u2026",
                new Runnable() {
                    public void run() {

                        final CountDownLatch latch = new CountDownLatch(1);
                        final Bitmap b = mBitmap;
                        mHandler.post(new Runnable() {
                            public void run() {

                                if (b != mBitmap && b != null) {
                                    mImageView.setImageBitmapResetBase(b, true);
                                    mBitmap.recycle();
                                    mBitmap = b;
                                }
                                if (mImageView.getScale() == 1F) {
                                    mImageView.center(true, true);
                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        mRunFaceDetection.run();
                    }
                }, mHandler);
    }


    private void onSaveClicked() throws Exception {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving) return;

        if (mCrop == null)
            return;

        mSaving = true;

        Rect r = mCrop.getCropRect();

        int width = r.width();
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the third parameter here.
        Bitmap croppedImage;
        try {
            croppedImage = Bitmap.createBitmap(width, height, mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } catch (Exception e) {
            throw e;
        }
        if (croppedImage == null)
            return;

        {
            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(mBitmap, r, dstRect, null);
        }

        if (mCircleCrop) {

            // OK, so what's all this about?
            // Bitmaps are inherently rectangular but we want to return
            // something that's basically a circle.  So we fill in the
            // area around the circle with alpha.  Note the all important
            // PortDuff.Mode.CLEAR.
            Canvas c = new Canvas(croppedImage);
            Path p = new Path();
            p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
            c.clipPath(p, Region.Op.DIFFERENCE);
            c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
        }

		/* If the output is required to a specific size then scale or fill */
        if (mOutputX != 0 && mOutputY != 0) {
            if (mScale) {
                /* Scale the image to the required dimensions */
                Bitmap old = croppedImage;
                croppedImage = BiokysUtil.transform(new Matrix(), croppedImage, mOutputX, mOutputY, mScaleUp);
                if (old != croppedImage) {
                    old.recycle();
                }
            } else {

            	/* Don't scale the image crop it to the size requested.
                 * Create an new image with the cropped image in the center and
				 * the extra space filled.
				 */

                // Don't scale the image but instead fill it so it's the
                // required dimension
                Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(b);

                Rect srcRect = mCrop.getCropRect();
                Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

                int dx = (srcRect.width() - dstRect.width()) / 2;
                int dy = (srcRect.height() - dstRect.height()) / 2;

				/* If the srcRect is too big, use the center part of it. */
                srcRect.inset(Math.max(0, dx), Math.max(0, dy));

				/* If the dstRect is too big, use the center part of it. */
                dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

				/* Draw the cropped bitmap in the center */
                canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

				/* Set the cropped bitmap as the new bitmap */
                croppedImage.recycle();
                croppedImage = b;
            }
        }

        final Bitmap b = croppedImage;
        BiokysUtil.startBackgroundJob(this, null, "Saving Image ...", new Runnable() {
                    public void run() {
                        saveOutput(b);
                    }
                }, mHandler);
    }

    
//_______________________________________________________________________________________________
    @Override
    protected void onPause() {
        super.onPause();
        BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null)
            mBitmap.recycle();
    }

//_______________________________________________________________________________________________
    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {

            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right,
                        faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom,
                        faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);

            mImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY)
                    cropHeight = cropWidth * mAspectY / mAspectX;
                else
                    cropWidth = cropHeight * mAspectX / mAspectY;
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);

            mImageView.mHighlightViews.clear(); // Thong added for rotate

            mImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {

            if (mBitmap == null)
                return null;

            if (mBitmap.getWidth() > 256) // 256 pixels wide is enough.
                mScale = 256.0F / mBitmap.getWidth();
            
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        }

        public void run() {

            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null && mDoFaceDetection) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
                        faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {

                    mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    } else {
                        makeDefault();
                    }
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() == 1) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }

                    if (mNumFaces > 1) {
                        Toast.makeText(BiokysCropImage.this,"Multi face crop help", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    
//_________________________________________________________________________________________________
    private Bitmap decodeBitmap(Uri uri) {
        InputStream in;
        try {
            in = mContentResolver.openInputStream(uri);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();
            int scale = 1;
            if (o.outHeight > image_maximum_bound || o.outWidth > image_maximum_bound) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(image_maximum_bound / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();
            return b;
        } catch (Exception ignored) {
        }
        return null;
    }
    
//_______________________________________________________________________________________________
    private void saveOutput(Bitmap croppedImage) {
        if (image_save_uri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(image_save_uri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 90, outputStream);
                }
            } catch (IOException ignored) {
                //java.io.FileNotFoundException: No such file or directory
                //LogUtil.e("BiokysCropImage", ignored + ":");
                setResult(RESULT_CANCELED);
                finish();
                return;
            } finally {
                BiokysUtil.closeSilently(outputStream);
            }
        } /* else {
        }*/
        croppedImage.recycle();
        setResult(RESULT_OK);
        finish();
    }
}
