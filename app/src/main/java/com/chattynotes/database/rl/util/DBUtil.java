package com.chattynotes.database.rl.util;

import com.chattynotes.database.rl.constant.DBConstant;
import com.chattynotes.database.rl.module.SchemaNotesDB;
import com.chattynotes.util.LogUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.StorageUtil;
import java.io.File;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DBUtil {

    //[release_: make sure of DB_SCHEME_VERSION when restoring | backing-up notes]
    public static RealmConfiguration getRealmConfiguration() {
        return new RealmConfiguration
                .Builder()
                .name(DBConstant.DB_NAME)
                .schemaVersion(DBConstant.DB_SCHEME_VERSION)
                .modules(new SchemaNotesDB())
                .build();
    }


    public static void backupDB() {
        RealmConfiguration configuration = DBUtil.getRealmConfiguration();
        Realm realm = Realm.getInstance(configuration);
        File f = PathUtil.createExternalDatabaseFile();
        StorageUtil.delete(f); //[mandatory_: java.lang.IllegalArgumentException: The destination file must not exist]
        realm.writeCopyTo(f);
        //LogUtil.e("DBUtil -> backupDB", Realm.getGlobalInstanceCount(configuration) + " <------");
        realm.close();

    }

    public static void backupDBInfo(String json) {
        File file = PathUtil.createExternalDatabaseInfoFile();
        StorageUtil.writeFile(file, json);
    }

    public static boolean restoreDB() {
        //realm.getPath()  ->   /data/data/com.package/files/notes.db
        boolean success = false;
        RealmConfiguration configuration = DBUtil.getRealmConfiguration();
        Realm realm = Realm.getInstance(configuration);
        String internalPath = realm.getPath();
        realm.close();
        //https://stackoverflow.com/a/38387137
        //https://github.com/realm/realm-java/issues/2552#issuecomment-206099837
        Realm.deleteRealm(configuration);
        //https://github.com/realm/realm-java/issues/5500
        //LogUtil.e("DBUtil -> restoreDB", Realm.getGlobalInstanceCount(configuration) + " <------");
        File fromFile = PathUtil.getExternalDatabaseFile();
        File toFile = new File(internalPath);
        if (fromFile.exists()) {
            success = true;
            StorageUtil.copy(fromFile, toFile);
        }
        return success;
    }
}
