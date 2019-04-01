package com.chattynotes.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.chattynotes.mvp.activities.Forward;
import com.chattynoteslite.R;

public class ShareUtil {
    //https://developer.android.com/training/sharing/send.html

    public static void shareText(Context ctx, String msg) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
        ctx.startActivity(Intent.createChooser(sharingIntent, ctx.getString(R.string.share_using)));
    }

    public static void shareMedia(Context ctx, Uri uri, String mimeType) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType(mimeType);
        ctx.startActivity(Intent.createChooser(shareIntent, ctx.getString(R.string.share_using)));
    }

    public static void shareMediaCustomCamera(Context ctx, Uri uri, String mimeType) {
        //https://developer.android.com/training/sharing/send.html
        Intent shareIntent = new Intent(ctx, Forward.class);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType(mimeType);
        ctx.startActivity(shareIntent);
    }
}
