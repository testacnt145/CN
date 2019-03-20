package com.chattynotes.util.storage.json;

import com.chattynotes.util.LogUtil;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.json.model.BackupModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileReader;

public final class JsonUtil {

    //http://www.studytrails.com/java/json/java-google-json-parse-json-to-java/
    public static String convertJavaToJson(BackupModel backupModel) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(backupModel);
    }
        /* http://jsoneditoronline.org/
            {
              "type":0,
              "versionCode":2000,
              "backupTimestamp":14567834523,
              "chatCount":2,
              "chatDetails": [
                {
                  "chatName":"USA1",
                  "msgCount":25

                },
                {
                  "chatName":"USA2",
                  "msgCount":50
                }
              ],
              "lastBackupNote": {
                "chatName":"USA2",
                "text":"Hi hello this is a text message",
                "timestamp":1224335677

              },
              "msgCount":3000,
              "type":0
            }
         */

    public static BackupModel convertJsonToJava() {
        try {
            Gson gson = new Gson();
            File file = PathUtil.getExternalDatabaseInfoFile();
            JsonReader reader = new JsonReader(new FileReader(file));
            return gson.fromJson(reader, BackupModel.class);
        } catch (Exception ignored) {
            LogUtil.exception("JsonUtil", "convertJsonToJava", ignored);
        }
        return null;
    }

}
