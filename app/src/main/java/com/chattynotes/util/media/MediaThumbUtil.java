package com.chattynotes.util.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.chattynotes.util.storage.PathUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public final class MediaThumbUtil {

    private final static int PROFILE_THUMB_WIDTH = 150;
    private final static int PROFILE_THUMB_HEIGHT = 150;
    public final static int MEDIA_THUMB_WIDTH = 150;
    public final static int MEDIA_THUMB_HEIGHT = 150;
    private final static int LINK_THUMB_WIDTH = 100;
    private final static int LINK_THUMB_HEIGHT = 100;
    private final static int THUMB_QUALITY_RECEIVED = 100;
    private final static int THUMB_QUALITY_CHAT_IMAGE = 100;
    private final static int THUMB_QUALITY_MEDIA_SENT = 50;
    public final static String THUMB_NOT_FOUND = "";

    private static boolean saveThumbToIntStg(Bitmap bmp, File file, int quality) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //______________________________________________________________________________________________
    // --> SAVING THUMB WHEN MESSAGE IS SENT
    //______________________________________________________________________________________________
    //size: 150x150
    public static boolean saveThumbChatImage(Uri imageUri, String imageName, int orgWidth, int orgHeight) {
        File file = PathUtil.createInternalChatImageFile(imageName);
        Bitmap resizedThumb = MediaUtil.createOptimizedScaleBitmap(imageUri, PROFILE_THUMB_WIDTH, PROFILE_THUMB_HEIGHT, orgWidth, orgHeight);
        // not using ---> Bitmap.createScaledBitmap(image, MEDIA_THUMB_WIDTH, MEDIA_THUMB_HEIGHT, false);
        return saveThumbToIntStg(resizedThumb, file, THUMB_QUALITY_CHAT_IMAGE);
    }


    //USED FOR all 3 (image, video, pdf)
    //size: 200x180
    private static boolean saveThumbMediaAll_Sent(Bitmap bmp, String mediaName, long chatID) {
        File file = PathUtil.createInternalMediaThumbFile(chatID, mediaName);
        return saveThumbToIntStg(bmp, file, THUMB_QUALITY_MEDIA_SENT);
    }

    //______________________________________________________________________________________________ THUMB GENERATION
    //resize the bitmap into small thumb size
    //apply the blur effect
    public static void saveThumbMediaImage(Uri imageUri, String mediaName, long chatID, int orgWidth, int orgHeight) {
        Bitmap resizedThumb = MediaUtil.createOptimizedScaleBitmap(imageUri, MEDIA_THUMB_WIDTH, MEDIA_THUMB_HEIGHT, orgWidth, orgHeight);
        // not using ---> Bitmap.createScaledBitmap(image, MEDIA_THUMB_WIDTH, MEDIA_THUMB_HEIGHT, false);
        MediaUtil.blurImage(resizedThumb, MediaUtil.MEDIA_THUMB_BLUR);
        saveThumbMediaAll_Sent(resizedThumb, mediaName, chatID);
    }


    //resize the bitmap into small thumb size
    //first Save Image using Bitmap
    //then replace it by blurring it
    public static void saveThumbMediaVideo(Bitmap thumb, String mediaName, long chatID) {
        //Bitmap resizedThumb = Bitmap.createScaledBitmap(thumb, MEDIA_THUMB_WIDTH, MEDIA_THUMB_HEIGHT, true);
        Bitmap resizedThumb = MediaUtil.createOptimizedScaleBitmap(thumb, MEDIA_THUMB_WIDTH, MEDIA_THUMB_HEIGHT);
        saveThumbMediaAll_Sent(resizedThumb, mediaName, chatID);
        //do blurring on the saved thumb
        //Bitmap savedThumb = BitmapFactory.decodeFile(PathUtil.getInternalMediaThumbPath(number, mediaName));
        //MediaUtil.blurImage(savedThumb, MediaUtil.BLUR_VIDEO_THUMB);
        //again replacing the thumb
        //saveMediaThumbToIntStg_Sent(resizedThumb, mediaName, number);
    }

    //resize the bitmap into small thumb size
    public static boolean saveThumbMsgLink(Bitmap thumb) {
        Bitmap resizedThumb = MediaUtil.createOptimizedScaleBitmap(thumb, LINK_THUMB_WIDTH, LINK_THUMB_HEIGHT);
        File file = PathUtil.createInternalMediaLinkThumbFile();
        return saveThumbToIntStg(resizedThumb, file, THUMB_QUALITY_RECEIVED);
    }

    //______________________________________________________________________________________________ THUMB
//    public static String getThumbMediaAllInString(long chatID, String imageName) {
//        try {
//            File file = PathUtil.createInternalMediaThumbFile(chatID, imageName);
//            Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
//            return convertBitmapToThumb(bmp);
//        } catch (Exception ignored) {
//        }
//        return THUMB_NOT_FOUND;
//    }

    public static String getThumbMsgLinkInString() {
        try {
            File file = PathUtil.createInternalMediaLinkThumbFile();
            Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
            return convertBitmapToThumb(bmp);
        } catch (Exception ignored) {
        }
        return THUMB_NOT_FOUND;
    }

    //saving image as blob is preferred over base64
    //http://stackoverflow.com/questions/9722603/storing-image-in-database-directly-or-as-base64-data
    //http://stackoverflow.com/a/32573024/4754141
    private static String convertBitmapToThumb(Bitmap bmp) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, THUMB_QUALITY_RECEIVED, bos);
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap convertThumbToBitmap(String thumb) {
        byte[] bytes = Base64.decode(thumb.getBytes(), 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
