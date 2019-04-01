package com.chattynotes.util.media;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.chattynoteslite.R;
import com.chattynotes.application.App;
import com.chattynotes.constant.MimeType;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.util.androidos.GenericFileProvider;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.storage.StorageUtil;
import java.io.File;

public class MediaTextUtil {

    public static Boolean writeTextFile(Context _ctx, String chatName, String content) {
        //The Problem with saving in internal storage is that, the text file won't attached with emailIntent
        //File file = new File(App.applicationContext.getFilesDir(), "Chatty Notes-Conversation.txt");

        //WhatsApp save the email text File in WhatsApp/.shared Folder
        File f = PathUtil.createExternalEmailFile();
        boolean success = StorageUtil.writeFile(f, content);
        if (success) {
            Uri contentUri;
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                contentUri = FileProvider.getUriForFile(_ctx, App.PACKAGE_NAME + GenericFileProvider.PATH, f);
                emailIntent.setDataAndType(contentUri, MimeType.MIME_TYPE_EMAIL);
            } else {
                contentUri = Uri.fromFile(f);
                emailIntent.setType(MimeType.MIME_TYPE_EMAIL);
            }
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, String.format(_ctx.getString(R.string.email_subject), chatName));
            emailIntent.putExtra(Intent.EXTRA_TEXT, _ctx.getString(R.string.email_text));
            emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            _ctx.startActivity(Intent.createChooser(emailIntent, _ctx.getString(R.string.email_chooser)));
            return true;
        }
        return false;
    }
}
