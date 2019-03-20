package com.chattynotes.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.chattynotes.application.App;
import com.chattynotes.constant.MimeType;
import com.chattynotes.util.androidos.GenericFileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class IntentUtil {

    //______________________________________________________________________________________________ CAMERA IMAGE
    public static Intent cameraImagePickerList(Context ctx, File f) {
        final PackageManager packageManager = ctx.getPackageManager();
        //Camera Image
        final ArrayList<Intent> intentList = new ArrayList<>();
        final Intent cameraImageIntent = new Intent();
        //setting intent properties
        cameraImageIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        final List<ResolveInfo> listCamImage = packageManager.queryIntentActivities(cameraImageIntent, 0);
        for (ResolveInfo res : listCamImage) {
            final String packageName = res.activityInfo.packageName;
            //LogUtil.e("cameraImagePickerList",  "^^^^^ : " + packageName);
            //allow all -> do not filter out package
            if (packageName != null) {
                final Intent cameraIntent = new Intent(cameraImageIntent);
                cameraIntent.setComponent(new ComponentName(packageName, res.activityInfo.name));
                cameraIntent.setPackage(packageName);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(ctx, App.PACKAGE_NAME + GenericFileProvider.PATH, f);
                    //ctx.grantUriPermission(packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //cameraIntent.setDataAndType(uri, MimeType.MIME_TYPE_IMAGE);
                    //https://stackoverflow.com/a/42717997
                    //cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else
                    uri = Uri.parse("file://" + f.toString());
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //**** for external storage
                intentList.add(cameraIntent);
            }
        }
        if (intentList.isEmpty())
            return null;
        else {
            final Intent chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "Choose Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
            return chooserIntent;
        }
    }

    //______________________________________________________________________________________________ CAMERA VIDEO
    //____________________
    //LG Nexus 5 (Nexus 5)	6.0       (23)	MARSHMELLOW
        //com.google.android.GoogleCamera       Camcorder
        //photo.camera.hdcameras                Camcorder
    public static Intent cameraVideoPickerList(Context ctx) {
        final PackageManager packageManager = ctx.getPackageManager();
        //Camera Video
        final ArrayList<Intent> intentList = new ArrayList<>();
        final Intent cameraVideoIntent = new Intent();
        //setting intent properties
        cameraVideoIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);

        final List<ResolveInfo> listCamVideo = packageManager.queryIntentActivities(cameraVideoIntent, 0);
        for (ResolveInfo res : listCamVideo) {
            final String packageName = res.activityInfo.packageName;
            //LogUtil.e("cameraVideoPickerList",  "^^^^^ : " + packageName);
            //allow all -> do not filter out package
            if (packageName != null) {
                final Intent intent = new Intent(cameraVideoIntent);
                intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intentList.add(intent);
            }
        }
        if (intentList.isEmpty())
            return null;
        else {
            final Intent chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "Choose Video");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
            return chooserIntent;
        }
    }


    //______________________________________________________________________________________________ 3RD PARTY IMAGE & VIDEO
    //only allow these apps
    private static final String THIRD_PARTY_PACKAGE_GOOGLE_PHOTOS = "com.google.android.apps.photos";
    private static final String THIRD_PARTY_PACKAGE_ANDROID_PLUS = "android.apps.plus";
    private static final String THIRD_PARTY_PACKAGE_ANDROID_GALLERY3D = "android.gallery3d";
    private static boolean isValidThirdPartyImagePackage(String packageName) {
        return packageName.contains(THIRD_PARTY_PACKAGE_GOOGLE_PHOTOS) ||
                    packageName.contains(THIRD_PARTY_PACKAGE_ANDROID_PLUS) ||
                        packageName.contains(THIRD_PARTY_PACKAGE_ANDROID_GALLERY3D);
    }

    //______________________________________________________________________________________________
    //GALAXY S4
        //com.sec.android.gallery3d         SELECTED
        //com.sec.pcw
        //com.google.android.apps.plus      SELECTED
        //com.dropbox.android
    //____________________
    //LG Nexus 5 (Nexus 5 Br)	5.1.1    (22)	LOLLIPOP
        //com.android.documentsui
        //com.google.android.apps.photos        SELECTED
        //com.joeware.android.gpulumera
        //com.metago.astro
        //com.aviary.android.feather
        //com.rhmsoft.fm
        //com.rhmsoft.fm
        //com.estrongs.android.pop
        //com.dropbox.android
        //com.chattynotes
    //____________________
    //LG Nexus 5 (Nexus 5)	6.0       (23)	MARSHMELLOW
        //com.android.documentsui
        //com.google.android.apps.photos        SELECTED
        //com.metago.astro
        //com.aviary.android.feather
        //com.joeware.android.gpulumera
        //com.myapp1
        //com.myapp
        //com.rhmsoft.fm
        //com.rhmsoft.fm
        //com.estrongs.android.pop
        //com.dropbox.android
        //com.chattynotes

    public static Intent thirdPartyImageVideoPickerList(Context ctx, String action, String msg) {
        final PackageManager packageManager = ctx.getPackageManager();
        //Third Party
        final ArrayList<Intent> intentList = new ArrayList<>();
        final Intent thirdPartyIntent = new Intent();
        //setting intent properties
        thirdPartyIntent.setAction(Intent.ACTION_GET_CONTENT);
        thirdPartyIntent.setType(action);

        final List<ResolveInfo> listThirdParty = packageManager.queryIntentActivities(thirdPartyIntent, 0);
        for (ResolveInfo res : listThirdParty) {
            final String packageName = res.activityInfo.packageName;
            //LogUtil.e("thirdPartyImageVideoPickerList",  "^^^^^ : " + packageName);
            //filter out package using -> isValidThirdPartyImagePackage
            if (packageName != null && isValidThirdPartyImagePackage(packageName)) {
                final Intent intent = new Intent(thirdPartyIntent);
                intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intentList.add(intent);
            }
        }
        if (intentList.isEmpty())
            return null;
        else {
            final Intent chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), msg);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
            return chooserIntent;
        }
    }


    //______________________________________________________________________________________________ PROFILE IMAGE
    public static Intent profileImagePickerList(Context ctx, Uri uri) {

        final PackageManager packageManager = ctx.getPackageManager();
        final List<Intent> intentList = new ArrayList<>();

        //Camera
        final Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(cameraIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(cameraIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //**** for external storage
            intentList.add(intent);
        }

        //Gallery Image
        final Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType(MimeType.MIME_TYPE_IMAGE);
        final List<ResolveInfo> listGalleries = packageManager.queryIntentActivities(galleryIntent, 0);
        for(ResolveInfo res : listGalleries) {
            final String packageName = res.activityInfo.packageName;
            //LogUtil.e("profileImagePickerList",  "^^^^^ : " + packageName);
            //filter out package using -> isValidThirdPartyImagePackage
            if (packageName != null && isValidThirdPartyImagePackage(packageName)) {
                final Intent intent = new Intent(galleryIntent);
                intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intentList.add(intent);
            }
        }

        if (intentList.isEmpty())
            return null;
        else {
            final Intent chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "Choose Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
            return chooserIntent;
        }
    }
}
