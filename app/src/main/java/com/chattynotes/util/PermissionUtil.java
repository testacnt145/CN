package com.chattynotes.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.chattynoteslite.R;
import com.chattynotes.application.App;

import java.util.ArrayList;
import java.util.List;
import android.annotation.TargetApi;

public class PermissionUtil {

    //APP CRASHING WHEN PERMISSION CHANGED FROM SYSTEM_SETTINGS__
        //Activity restarts when permission granted (NORMAL BEHAVIOUR OF ANDROID)
        //http://developer.android.com/training/basics/activity-lifecycle/recreating.html
        //http://developer.android.com/guide/topics/resources/runtime-changes.html
        //http://stackoverflow.com/questions/34865719/android-6-permission-crashes-when-toggling-permission-in-setting-and-go-back-t

    //[link-video_: https://www.youtube.com/watch?v=f17qe9vZ8RM]
    //http://stackoverflow.com/questions/32307786/application-is-getting-killed-after-enable-disable-permissions-from-settings-on
    //@04:38 the other part is SYSTEM_SETTINGS__
    //@10:58 you do not have to worry about Callbacks at runtime. I am middle of operation and user turn off permission from Settings, NOT TO WORRY WE KILL PROCESS (Same as low memory conditions)

    //general
    public final static int PERMISSION_NO_PHOTO = 0;

    private final static int PERMISSION_DO_NOTHING                   = 0;
    //Contacts
    //public final static int PERMISSION_CONTACT                      = 1;
    //Storage and Camera
    public final static int PERMISSION_MULTIPLE_CHAT_IMAGE          = 100;
    public final static int PERMISSION_CAMERA_IMAGE                 = 101;
    public final static int PERMISSION_CAMERA_VIDEO                 = 102;
    public final static int PERMISSION_MULTIPLE_CUSTOM_CAMERA       = 103;
    public final static int PERMISSION_GALLERY_IMAGE                = 104;
    public final static int PERMISSION_GALLERY_VIDEO                = 105;
    public final static int PERMISSION_GALLERY_IMAGE_3RD_PARTY      = 106;
    public final static int PERMISSION_GALLERY_VIDEO_3RD_PARTY      = 107;
    public final static int PERMISSION_STORAGE_TEXT                 = 108;
    //Microphone
    public final static int PERMISSION_MULTIPLE_AUDIO               = 200; //conversation bottom bar
    public final static int PERMISSION_MULTIPLE_AUDIO_DIALOG        = 201; //conversation dialog
    //Location
    public final static int PERMISSION_LOCATION                     = 300;

    //Toasts
    public final static String permission_not_granted           = "Permission not granted";
    public final static String permission_not_granted_camera    = "Camera permission not granted";
    public final static String permission_not_granted_storage   = "Storage permission not granted";
    public final static String permission_not_granted_mic       = "Mic permission not granted";


    //on the very first screen of application
    //ask for all the permissions, so that FIRST_TIME_PERMISSION_ISSUE  is resolved
    //FIRST_TIME_PERMISSION_ISSUE(when app install)
    //Important question regarding using shouldShowRequestPermissionRationale()
    //http://stackoverflow.com/questions/32347532/android-m-permissions-confused-on-the-usage-of-shouldshowrequestpermissionrati
    public static void alertPermissionFirstTime(final Activity ctx) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        final View      view       = View.inflate(ctx, R.layout.dialog_request_permission, null);
        final TextView  txtView    = (TextView) view.findViewById(R.id.permission_message);
        final ImageView imgView    = (ImageView)view.findViewById(R.id.permission_image);
        final Button    btnCancel  = (Button)   view.findViewById(R.id.cancel);
        btnCancel.setVisibility(View.GONE);
        final Button    btnSubmit  = (Button)   view.findViewById(R.id.submit);
        alert.setView(view);
        alert.setCancelable(false); //set cancel false
        final AlertDialog dialog = alert.show();
        //work
        txtView.setText(ctx.getString(R.string.permission_first_time));
        imgView.setImageResource(R.mipmap.ic_launcher_round);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //permissions
                List<String> permissionList = new ArrayList<>();
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissionList.add(Manifest.permission.CAMERA);
                permissionList.add(Manifest.permission.RECORD_AUDIO);
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                ctx.requestPermissions(permissionList.toArray(new String[permissionList.size()]), PERMISSION_DO_NOTHING);
            }
        });
    }

    //[link-video_: https://www.youtube.com/watch?v=C8lUdPVSzDk]
    //[link-video_: https://www.youtube.com/watch?v=iZqDdvhTZj0]
    //http://developer.android.com/training/permissions/requesting.html
    //http://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
    //Activities() :
    // NewChat, ChatSettings, Conversation, AdapterConversation,
    // GalleryImageAlbum, GalleryImageGrid, GalleryImageSend, SendImage, GalleryVideoAlbum, GalleryVideoGrid, SendVideo,
    // InfoContact, InfoGroup,  ImageViewer,  LocationPicker, RegFinal, RegUpdateProfile, RegVerifyNumber,
    public static Boolean checkPermission(Activity ctx, String permission, String msgGuideUser, String msgSettings, int image, int image1, int image2, int REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //LogUtil.e("PermissionUtil", "checkPermission - [os_marshmallow_: ]");
            //_______________________________________________
            //[os_marshmallow_: permission]
            //[link-video_: https://www.youtube.com/watch?v=C8lUdPVSzDk @1:13]
            //Check if Camera permission is already available
            if (ctx.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                //Camera permission is already available, show the camera preview
                return true;
            } else {
                //LogUtil.e("PermissionUtil", "checkPermission - PERMISSION_NOT_GRANTED");

                List<String> _permissionList = new ArrayList<>();
                _permissionList.add(permission);

                //Camera Permission has not been granted
                //Provide the additional rationale(reasons) to the user if permission was not granted
                //and the user would benefit from additional context for the use of permission
                //FIRST_TIME_PERMISSION_ISSUE(when app install)
                //Important question regarding using shouldShowRequestPermissionRationale()
                //http://stackoverflow.com/questions/32347532/android-m-permissions-confused-on-the-usage-of-shouldshowrequestpermissionrati
                //for now on - when app install i.e first time (alertSettings method is called)
                if (ctx.shouldShowRequestPermissionRationale(permission))
                    alertGuideUser(ctx, _permissionList, msgGuideUser, image, image1, image2, REQUEST_CODE);
                else
                    alertSettings(ctx, msgSettings, image, image1, image2);

                return false;
            }
            //_______________________________________________
        } else {
            //LogUtil.e("PermissionUtil", "checkPermission - [os_marshmallow_: pre-marshmallow]");
            return true;
        }
    }

    //for method onRequestPermissionsResult (handling multiple permissions),
    //take help from http://developer.android.com/training/permissions/requesting.html
    //List<String> _permissionList = new ArrayList<>();
    //_permissionList.add(Manifest.permission.CAMERA);
    //_permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    public static Boolean checkMultiplePermissions(Activity ctx, List<String> permissionsList, String msgGuideUser, String msgSettings, int image, int image1, int image2, int REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //LogUtil.e("PermissionUtil", "checkMultiplePermissions - [os_marshmallow_: ]" + permissionsList.size());

            //check if all permissions are granted or not
            for (int i=0; i<permissionsList.size(); i++)
                if (ctx.checkSelfPermission(permissionsList.get(i)) == PackageManager.PERMISSION_GRANTED) {
                    //LogUtil.e("PermissionUtil", "granted -> " + permissionsList.get(i) + ":" + i);
                    permissionsList.remove(i);
                    i--;
                }

            if (permissionsList.isEmpty()) { //all permissions are already granted
                return true;
            } else {

                //now on these permissionsList which are not granted,
                //check if any even ONE permission shouldShowRequestPermissionRationale() is false
                //display settings dialog
                for (int i=0; i<permissionsList.size(); i++)
                    if (!ctx.shouldShowRequestPermissionRationale(permissionsList.get(i))) {
                        alertSettings(ctx, msgSettings, image, image1, image2);
                        return false; //means do not run the below code
                    }

                alertGuideUser(ctx, permissionsList, msgGuideUser, image, image1, image2, REQUEST_CODE);
                return false;
            }
        } else {
            //LogUtil.e("PermissionUtil", "checkMultiplePermissions - [os_marshmallow_: pre-marshmallow]");
            return true;
        }
    }

    public static Boolean checkPermissionSilent(Context ctx, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return ctx.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        return true;
    }

    //Activities(3) : Camera
    public static Boolean checkMultiplePermissionsSilent(Activity ctx, List<String> permissionsList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i=0; i<permissionsList.size(); i++)
                if (ctx.checkSelfPermission(permissionsList.get(i)) == PackageManager.PERMISSION_GRANTED) {
                    permissionsList.remove(i);
                    i--;
                }
            return permissionsList.isEmpty();
        }
        return true;
    }

    //<editor-fold desc="ALERTS">
//__________________________________________________________________________________________________
    private static void alertGuideUser(final Context _ctx, final List<String> _permissionsList, String _msgGuideUser, int _image, int _image1, int _image2, final int _REQUEST_CODE) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(_ctx);
        final View      view       = View.inflate(_ctx, R.layout.dialog_request_permission, null);
        final TextView  txtView    = (TextView) view.findViewById(R.id.permission_message);
        final ImageView imgView    = (ImageView)view.findViewById(R.id.permission_image);
        final Button    btnCancel  = (Button)   view.findViewById(R.id.cancel);
        final Button    btnSubmit  = (Button)   view.findViewById(R.id.submit);
        alert.setView(view);
        final AlertDialog dialog = alert.show();
        dialog.setCancelable(false);
        //work
        txtView.setText(_msgGuideUser);
        imgView.setImageResource(_image);
        if(_image1 != PermissionUtil.PERMISSION_NO_PHOTO) {
            ImageView imgView1 = (ImageView)view.findViewById(R.id.permission_image_1);
            imgView1.setVisibility(View.VISIBLE);
            imgView1.setImageResource(_image1);
            ImageView imgViewPlus1 = (ImageView)view.findViewById(R.id.permission_image_plus_1);
            imgViewPlus1.setVisibility(View.VISIBLE);
        }
        if(_image2 != PermissionUtil.PERMISSION_NO_PHOTO) {
            ImageView imgView2 = (ImageView)view.findViewById(R.id.permission_image_2);
            imgView2.setVisibility(View.VISIBLE);
            imgView2.setImageResource(_image2);
            ImageView imgViewPlus2 = (ImageView)view.findViewById(R.id.permission_image_plus_2);
            imgViewPlus2.setVisibility(View.VISIBLE);
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //http://stackoverflow.com/questions/31878501/android-m-permission-dialog-not-showing
                //dialog not will show, if you requestPermissions for the permission that you have not added in your manifest
                ((Activity)_ctx).requestPermissions(_permissionsList.toArray(new String[_permissionsList.size()]), _REQUEST_CODE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
                dialog.dismiss();
            }
        });
    }

    private static void alertSettings(final Context _ctx, String _msgSettings, int _image, int _image1, int _image2) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(_ctx);
        final View      view       = View.inflate(_ctx, R.layout.dialog_request_permission, null);
        final TextView  txtView    = (TextView)view.findViewById(R.id.permission_message);
        final ImageView imgView    = (ImageView)view.findViewById(R.id.permission_image);
        final Button    btnCancel  = (Button)view.findViewById(R.id.cancel);
        final Button    btnSubmit  = (Button)view.findViewById(R.id.submit);
        //change submit text to settings
        btnSubmit.setText(_ctx.getString(R.string.permission_settings_open));
        alert.setView(view);
        final AlertDialog dialog = alert.show();
        dialog.setCancelable(false);
        //work
        txtView.setText(_msgSettings);
        imgView.setImageResource(_image);
        if(_image1 != PermissionUtil.PERMISSION_NO_PHOTO) {
            ImageView imgView1 = (ImageView)view.findViewById(R.id.permission_image_1);
            imgView1.setVisibility(View.VISIBLE);
            imgView1.setImageResource(_image1);
            ImageView imgViewPlus1 = (ImageView)view.findViewById(R.id.permission_image_plus_1);
            imgViewPlus1.setVisibility(View.VISIBLE);
        }
        if(_image2 != PermissionUtil.PERMISSION_NO_PHOTO) {
            ImageView imgView2 = (ImageView)view.findViewById(R.id.permission_image_2);
            imgView2.setVisibility(View.VISIBLE);
            imgView2.setImageResource(_image2);
            ImageView imgViewPlus2 = (ImageView)view.findViewById(R.id.permission_image_plus_2);
            imgViewPlus2.setVisibility(View.VISIBLE);
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //move to settings page
                //http://stackoverflow.com/questions/32822101/how-to-programmatically-open-the-permission-screen-for-a-specific-app-on-android
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + App.PACKAGE_NAME));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                _ctx.startActivity(i);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
                dialog.dismiss();
            }
        });

    }
    //</editor-fold>
}
