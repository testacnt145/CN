package com.chattynotes.util.storage;

import java.io.File;
import com.chattynotes.application.App;
import com.chattynotes.constant.MimeType;
import com.chattynotes.constant.MsgFlow;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.database.rl.constant.DBConstant;
import com.chattynotes.preferences.PDefaultValue;
import com.chattynotes.util.DateUtil;
import com.chattynotes.util.androidos.GenericFileProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

public final class PathUtil {

	private final static String INFO_FILE_DATABASE = "info"; //do not use .txt
	private final static String INFO_FILE_DATABASE_ENCRYPT = "e-info";
	private final static String INFO_FILE_DATABASE_GDRIVE = "gd-info";

	private final static String EXTERNAL_FOLDER = "Chatty Notes Lite";
	private final static String INTERNAL_FOLDER = "ChattyNotesLite";
	final static String INTERNAL_SUB_FOLDER_CHAT_IMAGE = "folder_chat_image";
	//not required for Picasso
	//[bug_: Picasso_| store images in Internal cache]
	// therefore when clear cache from settings, images will disappear,
	// until internet is not connected again
	//public final static String INTERNAL_SUB_FOLDER_LOCATION = "folder_location";
	private final static String INTERNAL_SUB_FOLDER_LINK = "folder_link";

	private final static String TEMP_CHAT_IMAGE = "temp_chat_image";
	private final static String TEMP_LINK_IMAGE = "temp_link_image";
	private final static String TEMP_CAMERA_IMAGE = "temp_camera_image";
	private final static String EMAIL_TEXT_FILE = "Chatty Notes Lite | Chat.txt";

	//		/data/data/com.chattynotes/app_ChattyNotes
	final static File INTERNAL_MAIN_DIRECTORY = App.applicationContext.getDir(INTERNAL_FOLDER, Context.MODE_PRIVATE);
	private final static String INTERNAL_PATH = INTERNAL_MAIN_DIRECTORY.getPath();

	//PATHS : EXTERNAL
	private final static String FOLDER_PATH_CHAT_IMAGE 		= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Chat Images";
	private final static String FOLDER_PATH_MEDIA_IMAGE 	= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Images";
	private final static String FOLDER_PATH_MEDIA_IMAGE_SENT= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Images/Sent";
	private final static String FOLDER_PATH_MEDIA_AUDIO 	= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Audio";
	private final static String FOLDER_PATH_MEDIA_AUDIO_SENT= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Audio/Sent";
	private final static String FOLDER_PATH_MEDIA_VIDEO 	= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Video";
	private final static String FOLDER_PATH_MEDIA_VIDEO_SENT= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Video/Sent";
	private final static String FOLDER_PATH_MEDIA_TEXT 		= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Texts";
	private final static String FOLDER_PATH_MEDIA_TEXT_SENT	= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Media/Texts/Sent";
	//(hidden)
	private final static String FOLDER_PATH_EMAIL			= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + ".Email";
	//for 3rd Party, Share and Camera
	private final static String FOLDER_PATH_SHARE			= Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Shared Media";
	//database
	private final static String FOLDER_PATH_DATABASE                = Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Database";
	private final static String FOLDER_PATH_DATABASE_INFO           = Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Database/.info";
	private final static String FOLDER_PATH_DATABASE_ENCRYPT        = Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Database/Encrypt";
	private final static String FOLDER_PATH_DATABASE_ENCRYPT_INFO   = Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Database/Encrypt/.info";
	private final static String FOLDER_PATH_DATABASE_GDRIVE         = Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Database/Google Drive";
	private final static String FOLDER_PATH_DATABASE_GDRIVE_INFO    = Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_FOLDER + File.separator + "Database/Google Drive/.info";

	public final static String PREFERENCE_PATH_DRAFT = App.applicationContext.getFilesDir().getParent() + "/shared_prefs/" + PDefaultValue.PREFERENCE_FILENAME_DRAFT + ".xml";

//__________________________________________________________________________________________________ INTERNAL
//	Environment.getDataDirectory()
//	    /data
//
//	Environment.getDataDirectory() + "//data//" + App.PACKAGE_NAME + "//databases//" + DATABASE_NAME
//	    /data//data//com.chattynotes//databases//Notes.db
//
//	App.applicationContext.getDatabasePath(DATABASE_NAME)
//	    /data/user/0/com.chattynotes/databases/Notes.db

//	getDir(INTERNAL_FOLDER, Context.MODE_PRIVATE).getPath()
//		/data/data/com.chattynotes/app_ChattyNotes

// 	Environment.getDataDirectory() + "//data//" + App.PACKAGE_NAME + "//app_ChattyNotes// + temp_chat_image.jpg
//	    /data//data//com.chattynotes//app_ChattyNotes//temp_chat_image.jpg

//__________________________________________________________________________________________________ EXTERNAL
//		LogUtil.e(getClass().getSimpleName(), getExternalCacheDir().getPath());
//		/storage/emulated/0/Android/data/com.chattynotes/cache

//		LogUtil.e(getClass().getSimpleName(), Environment.getExternalStorageDirectory().getPath());
//		/storage/emulated/0

//		LogUtil.e(getClass().getSimpleName(), Environment.getDataDirectory().getPath());
//		/data
		
//		LogUtil.e(getClass().getSimpleName(), Environment.getRootDirectory().getPath());
//		/system

//		LogUtil.e(getClass().getSimpleName(), Environment.getExternalStorageDirectory().getPath());
//		/storage/emulated/0

	public static void createAppFolder() {
		//File folder1 = new File(FOLDER_PATH_DATABASES);
		File folder2 = new File(FOLDER_PATH_CHAT_IMAGE);
		File folder3 = new File(FOLDER_PATH_MEDIA_IMAGE_SENT);
		File folder4 = new File(FOLDER_PATH_MEDIA_AUDIO_SENT);
		File folder5 = new File(FOLDER_PATH_MEDIA_VIDEO_SENT);
		File folder6 = new File(FOLDER_PATH_MEDIA_TEXT_SENT);
		//if(!folder1.exists())
			//StorageUtil.mkdirs(folder1);
		if(!folder2.exists())
			StorageUtil.mkdirs(folder2);
		if(!folder3.exists())
			StorageUtil.mkdirs(folder3);
		if(!folder4.exists())
			StorageUtil.mkdirs(folder5);
		if(!folder5.exists())
			StorageUtil.mkdirs(folder6);
		if(!folder6.exists())
			StorageUtil.mkdirs(folder6);
	}


	//********************************************************************************************//
	//******************************* 2 METHODS - GET, CREATE ************************************//
	//get = check only
	//create = check and create if not exist
	//********************************************************************************************//

	private static String getExternalDatabasePath() {
		return FOLDER_PATH_DATABASE + File.separator + DBConstant.DB_NAME;
	}
	public static File getExternalDatabaseFile() {
		return new File(getExternalDatabasePath());
	}
	public static File createExternalDatabaseFile() {
		File directory = new File(FOLDER_PATH_DATABASE);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, DBConstant.DB_NAME);
	}

	private static String getExternalDatabaseInfoPath() {
		return FOLDER_PATH_DATABASE_INFO + File.separator + INFO_FILE_DATABASE;

	}
	public static File getExternalDatabaseInfoFile() {
		return new File(getExternalDatabaseInfoPath());
	}
	public static File createExternalDatabaseInfoFile() {
		File directory = new File(FOLDER_PATH_DATABASE_INFO);
		if (!directory.exists())//must do this
			StorageUtil.mkdirs(directory);
		return new File(directory, INFO_FILE_DATABASE);
	}




	private static String getExternalEmailPath() {
		return FOLDER_PATH_EMAIL + File.separator + EMAIL_TEXT_FILE;
	}
	static File getExternalEmailFile() {
		return new File(getExternalEmailPath());
	}
	public static File createExternalEmailFile() {
		File directory = new File(FOLDER_PATH_EMAIL);
		if (!directory.exists())//must do this
			StorageUtil.mkdirs(directory);
		return new File(directory, EMAIL_TEXT_FILE);
	}

//___________________________________________________________________________________________________ CHAT IMAGE

	//______________________________ CHAT_IMAGE_INTERNAL_TEMP
	private static String getInternalChatImageTempPath() {
		return Environment.getDataDirectory() + "//data//" + App.PACKAGE_NAME + "//app_ChattyNotes" + File.separator + INTERNAL_SUB_FOLDER_CHAT_IMAGE + File.separator + TEMP_CHAT_IMAGE + MimeType.FORMAT_IMAGE;
	}
	public static Uri getInternalChatImageTempUri() {
		// file:///data/data/com.chattynotes/app_ChattyNotes/chat_image/temp_chat_image.jpg
		return Uri.fromFile(new File(getInternalChatImageTempPath()));
		//return Uri.parse(getInternalChatImageTempPath());
		// /data//data//com.chattynotes//app_ChattyNotes/chat_image/temp_chat_image.jpg
	}
	public static Uri createInternalChatImageTempFile() {
		File directory = new File(INTERNAL_PATH, INTERNAL_SUB_FOLDER_CHAT_IMAGE);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		File file = new File(directory, TEMP_CHAT_IMAGE + MimeType.FORMAT_IMAGE);
		return Uri.fromFile(file);
	}


	//______________________________ CHAT_IMAGE_INTERNAL
	private static String getInternalChatImagePath(String imageName) {
		// /data//data//com.chattynotes//app_ChattyNotes/chat_image/3.jpg
		return INTERNAL_PATH + File.separator + INTERNAL_SUB_FOLDER_CHAT_IMAGE + File.separator + imageName + MimeType.FORMAT_IMAGE;
	}
	public static Uri getInternalChatImageUri(String imageName) {
		return Uri.parse(getInternalChatImagePath(imageName));
	}
	public static File getInternalChatImageFile(String imageName) {
		return new File(getInternalChatImagePath(imageName));
	}
	public static File createInternalChatImageFile(String imageName) {
		File directory = new File(INTERNAL_PATH, INTERNAL_SUB_FOLDER_CHAT_IMAGE);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, imageName + MimeType.FORMAT_IMAGE);
	}

	//______________________________ CHAT_IMAGE_EXTERNAL
	private static String getExternalChatImagePath(String imageName) {
		return FOLDER_PATH_CHAT_IMAGE + File.separator + imageName + MimeType.FORMAT_IMAGE;
	}
	public static Uri getExternalChatImageUri(String imageName) {
		return Uri.parse(getExternalChatImagePath(imageName));
	}
	static File getExternalChatImageFile(String imageName) {
		return new File(getExternalChatImagePath(imageName));
	}
	static File createExternalChatImageFile(String imageName) {
		File directory = new File(FOLDER_PATH_CHAT_IMAGE);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, imageName + MimeType.FORMAT_IMAGE);
	}

//___________________________________________________________________________________________________ MEDIA ALL
	public static String getInternalMediaThumbPath(long chatID, String mediaName) {
		// /data/data/com.chattynotes/app_ChattyNotes/1/IMG-1428901016.jpg
		return INTERNAL_PATH + File.separator + chatID + File.separator +
				mediaName + MimeType.FORMAT_IMAGE;
	}
	//this method was created because if folder don't exist in internal storage, it will give error(av_interleaved_write_frame()).
	//https://stackoverflow.com/questions/2787539/ffmpeg-error-av-interleaved-write-frame
	public static File createInternalMediaThumbFile(long chatID, String mediaName) {
		// /data/data/com.chattynotes/app_ChattyNotes/1/IMG-1428901016.jpg
		File directory = new File(INTERNAL_PATH + File.separator + chatID);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, mediaName + MimeType.FORMAT_IMAGE);
	}


	public static String getExternalMediaImagePath(int msgFlow, String imageName) {
		if (msgFlow == MsgFlow.SEND)
			return FOLDER_PATH_MEDIA_IMAGE_SENT + File.separator + imageName + MimeType.FORMAT_IMAGE;
		else
			return FOLDER_PATH_MEDIA_IMAGE + File.separator + imageName + MimeType.FORMAT_IMAGE;
	}
	public static Uri getExternalMediaImageUri(Context context, int msgFlow, String imageName) {
		File f = getExternalMediaImageFile(msgFlow, imageName);
		return FileProvider.getUriForFile(context, App.PACKAGE_NAME + GenericFileProvider.PATH, f);
		//old
		//return Uri.parse(getExternalMediaImagePath(msgFlow, imageName));
	}
	public static File getExternalMediaImageFile(int msgFlow, String imageName) {
		return new File(getExternalMediaImagePath(msgFlow, imageName));
	}
	public static File createExternalMediaImageFile_Sent(String imageName) {
		File directory = new File(FOLDER_PATH_MEDIA_IMAGE_SENT);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, imageName + MimeType.FORMAT_IMAGE);
	}

	public static File createExternalMediaShareFile(String imageName) {
		File directory = new File(FOLDER_PATH_SHARE);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, imageName);
	}



	private static String getExternalMediaAudioPath(int msgFlow, String audioName) {
		if (msgFlow == MsgFlow.SEND)
			return FOLDER_PATH_MEDIA_AUDIO_SENT + File.separator + audioName + MimeType.FORMAT_AUDIO;
		else
			return FOLDER_PATH_MEDIA_AUDIO + File.separator + audioName + MimeType.FORMAT_AUDIO;
	}
	public static Uri getExternalMediaAudioUri(Context context, int msgFlow, String audioName) {
		File f = getExternalMediaAudioFile(msgFlow, audioName);
		return FileProvider.getUriForFile(context, App.PACKAGE_NAME + GenericFileProvider.PATH, f);
		//old
		//return Uri.parse(getExternalMediaAudioPath(msgFlow, audioName));
	}
	public static File getExternalMediaAudioFile(int msgFlow, String audioName) {
		return new File(getExternalMediaAudioPath(msgFlow, audioName));
	}
	public static File createExternalMediaAudioFile_Sent(String audioName) {
		File directory = new File(FOLDER_PATH_MEDIA_AUDIO_SENT);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, audioName + MimeType.FORMAT_AUDIO);
	}


	private static String getExternalMediaVideoPath(int msgFlow, String videoName) {
		if (msgFlow == MsgFlow.SEND)
			return FOLDER_PATH_MEDIA_VIDEO_SENT + File.separator + videoName + MimeType.FORMAT_VIDEO;
		else
			return FOLDER_PATH_MEDIA_VIDEO + File.separator + videoName + MimeType.FORMAT_VIDEO;
	}
	public static Uri getExternalMediaVideoUri(Context context, int msgFlow, String videoName) {
		File f = getExternalMediaVideoFile(msgFlow, videoName);
		return FileProvider.getUriForFile(context, App.PACKAGE_NAME + GenericFileProvider.PATH, f);
		//old
		//return Uri.parse(getExternalMediaVideoPath(msgFlow, videoName));
	}
	public static File getExternalMediaVideoFile(int msgFlow, String videoName) {
		return new File(getExternalMediaVideoPath(msgFlow, videoName));
	}
	public static File createExternalMediaVideoFile_Sent(String videoName) {
		File directory = new File(FOLDER_PATH_MEDIA_VIDEO_SENT);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, videoName + MimeType.FORMAT_VIDEO);
	}


	private static String getExternalMediaTextPath(int msgFlow, String textName) {
		if (msgFlow == MsgFlow.SEND)
			return FOLDER_PATH_MEDIA_TEXT_SENT + File.separator + textName + MimeType.FORMAT_TEXT;
		else
			return FOLDER_PATH_MEDIA_TEXT + File.separator + textName + MimeType.FORMAT_TEXT;
	}
	public static Uri getExternalMediaTextUri(Context context, int msgFlow, String textName) {
		File f = getExternalMediaTextFile(msgFlow, textName);
		return FileProvider.getUriForFile(context, App.PACKAGE_NAME + GenericFileProvider.PATH, f);
		//old
		//return Uri.parse(getExternalMediaTextPath(msgFlow, textName));
	}
	public static File getExternalMediaTextFile(int msgFlow, String textName) {
		return new File(getExternalMediaTextPath(msgFlow, textName));
	}
	public static File createExternalMediaTextFile_Sent(String textName) {
		File directory = new File(FOLDER_PATH_MEDIA_TEXT_SENT);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, textName + MimeType.FORMAT_TEXT);
	}


	/*private static String getExternalMediaPath(int msgKind, int msgFlow, String mediaName) {
		switch(msgKind) {
			case MsgKind.M1_IMAGE:
				return getExternalMediaImagePath(msgFlow, mediaName);
			case MsgKind.M2_AUDIO:
				return getExternalMediaAudioPath(msgFlow, mediaName);
			case MsgKind.M3_VIDEO:
				return getExternalMediaVideoPath(msgFlow, mediaName);
			case MsgKind.M4_LONG_TEXT:
				return getExternalMediaTextPath(msgFlow, mediaName);
		}
		return null;
	}*/

	public static File getExternalMediaFile(int msgKind, int msgFlow, String mediaName) {
		String mediaPath=null;
		switch(msgKind) {
			case MsgKind.M1_IMAGE:
				mediaPath = getExternalMediaImagePath(msgFlow, mediaName);
				break;
			case MsgKind.M2_AUDIO:
				mediaPath = getExternalMediaAudioPath(msgFlow, mediaName);
				break;
			case MsgKind.M3_VIDEO:
				mediaPath = getExternalMediaVideoPath(msgFlow, mediaName);
				break;
			case MsgKind.M4_LONG_TEXT:
				mediaPath = getExternalMediaTextPath(msgFlow, mediaName);
				break;
		}
		return mediaPath!=null ? new File(mediaPath) : null;
	}
	static File createExternalMediaFile_Sent(int msgKind, String mediaName) {
		switch(msgKind) {
			case MsgKind.M1_IMAGE:
				return createExternalMediaImageFile_Sent(mediaName);
			case MsgKind.M2_AUDIO:
				return createExternalMediaAudioFile_Sent(mediaName);
			case MsgKind.M3_VIDEO:
				return createExternalMediaVideoFile_Sent(mediaName);
			case MsgKind.M4_LONG_TEXT:
				return createExternalMediaTextFile_Sent(mediaName);
		}
		return null;
	}


	static String getInternalMediaLinkThumbPath() {
		return INTERNAL_PATH + File.separator + INTERNAL_SUB_FOLDER_LINK + File.separator +
				TEMP_LINK_IMAGE + MimeType.FORMAT_IMAGE;
	}
	public static Uri getInternalMediaLinkThumbUri() {
		return Uri.parse(getInternalMediaLinkThumbPath());
	}
	public static File createInternalMediaLinkThumbFile() {
		File directory = new File(INTERNAL_PATH + File.separator + INTERNAL_SUB_FOLDER_LINK);
		if(!directory.exists())
			StorageUtil.mkdirs(directory);
		return new File(directory, TEMP_LINK_IMAGE + MimeType.FORMAT_IMAGE);
	}

	


//_______________________________________________________________________________________
	//Save image here instead of camera default folder
	//Save image with name "Camera_Image.jpg", so it may not replace the real file until uploading is completed
	public static Uri createCameraChatImageUri() {
		StorageUtil.deleteChatImageExtStg(TEMP_CAMERA_IMAGE);
		String _pathCamera = FOLDER_PATH_CHAT_IMAGE + File.separator + TEMP_CAMERA_IMAGE + MimeType.FORMAT_IMAGE;
		final File file = new File(_pathCamera);
		return Uri.fromFile(file);
	}
	
	
	public static Uri createCameraMediaImageUri(String image_name) {
		File directory = new File(FOLDER_PATH_MEDIA_IMAGE_SENT);
		if(!directory.exists())//must do this
			StorageUtil.mkdirs(directory);
		final File file = new File(directory, image_name + MimeType.FORMAT_IMAGE);
		return Uri.parse("file://" + file.toString());//append file:// otherwise Directory Error
		/*  String _path = Utilities.FOLDER_PATH_MEDIA_IMAGE_SENT + File.separator + TEMP_CAMERA_IMAGE + MimeType.IMAGE;
			final File file = new File(_path);
			outputFileUri = Uri.fromFile(file); 
		*/
	}

	public static Uri createCameraMediaImageUri(Context ctx, String image_name) {
		File directory = new File(FOLDER_PATH_MEDIA_IMAGE_SENT);
		if(!directory.exists())//must do this
			StorageUtil.mkdirs(directory);
		final File file = new File(directory, image_name + MimeType.FORMAT_IMAGE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
			return FileProvider.getUriForFile(ctx, App.PACKAGE_NAME + GenericFileProvider.PATH, file);
		else
			return Uri.parse("file://" + file.toString());
	}

	public static File createCameraMediaImageFile(String image_name) {
		File directory = new File(FOLDER_PATH_MEDIA_IMAGE_SENT);
		if(!directory.exists())//must do this
			StorageUtil.mkdirs(directory);
		return new File(directory, image_name + MimeType.FORMAT_IMAGE);
	}




	
	public static Uri createCameraMediaVideoUri(String video_name) {
		File directory = new File(FOLDER_PATH_MEDIA_VIDEO_SENT);
		if(!directory.exists())//must do this
			StorageUtil.mkdirs(directory);
		final File file = new File(directory, video_name + MimeType.FORMAT_VIDEO);
		return Uri.parse("file://" + file.toString());//append file:// otherwise Directory Error
	}

//______________________________________________________________________________________________________________ FileName
	public static String generateFileNameUnix(String mediaType) {
        return mediaType + "-" + DateUtil.getDateInUnixTimestamp();
    }
}
