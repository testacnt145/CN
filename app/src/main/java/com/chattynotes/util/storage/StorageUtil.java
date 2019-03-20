package com.chattynotes.util.storage;

import android.content.Context;
import android.net.Uri;

import com.chattynotes.constant.MsgFlow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
//import android.content.Context;
//import android.os.Environment;
//import com.chattynotes.application.App;

public final class StorageUtil {
    //(replace, copy, delete)

//__________________________________________________________________________________________________ DELETE
    public static void deleteChatThumbTempIntStg() {
        File f = new File(PathUtil.getInternalChatImageTempUri().getPath());
        if(f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteChatThumbTempIntStg");
        }
    }

    public static void deleteChatThumbIntStg(String imageName) {
        File f = PathUtil.createInternalChatImageFile(imageName);
        if (f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteChatThumbIntStg");
        }
    }

    //go into user directory and delete specified file
    public static void deleteMediaThumbIntStg(long chatID, String msg) {
        File f = PathUtil.createInternalMediaThumbFile(chatID, msg);
        if (f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteMediaThumbIntStg");
        }
    }

    //delete the complete user directory
    public static void deleteChatMediaThumbIntStg(long chatID) {
        File subDirectory = new File(PathUtil.INTERNAL_MAIN_DIRECTORY, Long.toString(chatID));
        if(subDirectory.exists())//if any image or video notes
            if (subDirectory.isDirectory()) {
                String[] children = subDirectory.list();
                for (String aChildren : children) {
                    File f = new File(subDirectory, aChildren);
                    delete(f);
                    //LogUtil.e("StorageUtil", "deleteChatMediaThumbIntStg");
                }
                delete(subDirectory);//also delete the folder
            }
    }

    //delete every folder excluding chat image
    public static void deleteAllMediaThumbIntStg() {
        File[] subDirectory = PathUtil.INTERNAL_MAIN_DIRECTORY.listFiles();
        for (File aSubDirectory : subDirectory) { //looping sub folders
            String[] files = aSubDirectory.list();
            //if (!aSubDirectory.getName().equals(PathUtil.INTERNAL_SUB_FOLDER_CHAT_IMAGE)) { //except chat image folder
                for (String file : files) {
                    File f = new File(aSubDirectory, file);
                    delete(f);
                    //LogUtil.e("StorageUtil", "deleteAllMediaThumbIntStg");
                }
                delete(aSubDirectory);
            //}
        }
    }

    public static void deleteLinkThumbIntStg() {
        File f = new File(PathUtil.getInternalMediaLinkThumbPath());
        if(f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteLinkThumbIntStg");
        }
    }

    static void deleteChatImageExtStg(String imageName) {
        File f = PathUtil.getExternalChatImageFile(imageName);
        if(f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteChatImageExtStg");
        }
    }

    public static void deleteMediaAudioExtStg(String audioName) {
        File f = PathUtil.getExternalMediaAudioFile(MsgFlow.SEND, audioName);
        if (f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteMediaAudioExtStg");
        }
    }

    public static void deleteMediaVideoExtStg(String videoName) {
        File f = PathUtil.getExternalMediaVideoFile(MsgFlow.SEND, videoName);
        if (f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteMediaVideoExtStg");
        }
    }

    public static void deleteEmailFileExtStg() {
        File f = PathUtil.getExternalEmailFile();
        if (f.exists()) {
            delete(f);
            //LogUtil.e("StorageUtil", "deleteEmailFileExtStg");
        }
    }

    //______________________________________________________________________________________________ COPY
    public static void copyMediaThumbIntStg_Forward(long toID, long fromID, String imageName) {
        File toFile = PathUtil.createInternalMediaThumbFile(toID, imageName);
        if(!toFile.exists()) {
            File fromFile = PathUtil.createInternalMediaThumbFile(fromID, imageName);
            copy(fromFile, toFile);
            //LogUtil.e("StorageUtil", "copyMediaThumbIntStg_Forward");
        }
    }

    public static void copyMediaExtStg_Forward(File fromFile, String mediaName, int msgKind) {
        File toFile = PathUtil.createExternalMediaFile_Sent(msgKind, mediaName);
        if(toFile!=null) {
            copy(fromFile, toFile);
            //LogUtil.e("StorageUtil", "copyMediaExtStg_Forward");
        }
    }

    public static void copyChatImageExtStg(String imageName) {
        //http://stackoverflow.com/questions/2975197/convert-file-uri-to-file-in-android
        //url.toString() return a String in the following format: "file:///mnt/sdcard/myPicture.jpg",
        //url.getPath() returns a String in the following format: "/mnt/sdcard/myPicture.jpg", i.e. without the scheme type pre-fixed
        File fromFile = new File(PathUtil.getInternalChatImageTempUri().getPath());
        if(fromFile.exists()) {
            File toFile = PathUtil.createExternalChatImageFile(imageName);
            copy(fromFile, toFile);
            //LogUtil.e("StorageUtil", "copyChatImageExtStg");
        }
    }

//__________________________________________________________________________________________________ FOR TESTING PURPOSE
//    public static void copyDatabaseExtStg() {
//        //Chatty Notes  :  /data/data/com.chattynotes/databases/Notes.db
//        //WhatsApp      :  /data/data/com.whatsapp/databases/msgstore.db
//        File fromFile = PathUtil.getInternalDatabaseFile();
//        File toFile = PathUtil.createExternalDatabaseFile();
//        if (fromFile.exists())
//            copy(fromFile, toFile);
//	}
//
//    //testing purpose
//    public static void copyAllIntStgPicsToExtStgDatabaseFolder() {
//        File externalDirectory = new File(Environment.getExternalStorageDirectory() + "/Chatty Notes/test");
//        if ((externalDirectory.mkdirs() || externalDirectory.exists()) && externalDirectory.canWrite()) {
//            File mainDirectory = App.applicationContext.getDir("ChattyNotes", Context.MODE_PRIVATE);
//            File[] subDirectory = mainDirectory.listFiles();
//            for (File aSubDirectory : subDirectory) { //looping sub folders
//                //create folder in external storage
//                File subDirectoryExt = new File(externalDirectory, aSubDirectory.getName());
//                mkdirs(subDirectoryExt);
//                String[] files = aSubDirectory.list();
//                for (String file : files) {
//                    File fromFile = new File(aSubDirectory, file);
//                    File toFile = new File(subDirectoryExt, file);
//                    copy(fromFile, toFile);
//                    //LogUtil.e("StorageUtil", "copyAllIntStgPicsToExtStgDatabaseFolder" + fromFile.getPath());
//                }
//            }
//        }
//    }



//__________________________________________________________________________________________________
    public static void copy(File fromFile, File toFile) {
        try {
            //LogUtil.e("fromFile", fromFile.getAbsolutePath());
            //LogUtil.e("toFile"  , toFile.getAbsolutePath());
            FileInputStream is = new FileInputStream(fromFile);
            FileChannel src = is.getChannel();
            FileOutputStream os = new FileOutputStream(toFile);
            FileChannel dst = os.getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();	is.close();
            dst.close();	os.close();
        } catch (Exception ignored) {
        }
    }

    public static void copy(Context context, Uri fromUri, File dstFile) {
        try {
            InputStream in = context.getContentResolver().openInputStream(fromUri);
            if (in == null) return;
            OutputStream out = new FileOutputStream(dstFile);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean writeFile(File file, String content) {
        try {
            FileOutputStream fOut = new FileOutputStream(file, false); //false = do not append
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.append(content);
            osw.flush();
            osw.close();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    //just to avoid warnings
    static void mkdirs(File f) {
        f.mkdirs();
    }

    public static void delete(File f) {
        f.delete();
    }
}
