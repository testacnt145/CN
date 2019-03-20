package com.chattynotes.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.StorageUtil;

import java.io.File;

public final class AndUriUtil {

//    //depreciated because
//    // 1) get filename from uri is WRONG (https://stackoverflow.com/a/37420320)
//    // 2) It works for very few Uri values (https://stackoverflow.com/a/42508921)
//    // used only for videos
//    //https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore
//    static String getRealPathFromURI(Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = App.getContext().getContentResolver().query(contentUri,  proj, null, null, null);
//            assert cursor != null;
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(columnIndex);
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return null;
//    }


    //[os_: nougat]
    //https://stackoverflow.com/a/46102318
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = PathUtil.createExternalMediaShareFile(fileName);
            StorageUtil.copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    private static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }
}
