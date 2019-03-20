package com.chattynotes.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.StorageUtil;
import java.io.File;

public class PrefsDraft {
	//saved in internal location : com.chattynotes/shared_prefs/p_dm.xml

	//used in versionCode 2000 and so on
	public static void deletePrefFile(final Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PDefaultValue.PREFERENCE_FILENAME_DRAFT, Context.MODE_PRIVATE);
		prefs.edit().clear().apply();
		//https://stackoverflow.com/a/6125371
		File f = new File(PathUtil.PREFERENCE_PATH_DRAFT);
		//LogUtil.e("PrefsDraft", f.getPath() + ":");
		if(f.exists())
			StorageUtil.delete(f);
	}
}
