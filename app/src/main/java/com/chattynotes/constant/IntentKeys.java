package com.chattynotes.constant;

public final class IntentKeys {

    public static final String IS_CAMERA_ACCESS_VIA_HOME_SCREEN    = "IS_CAMERA_ACCESS_VIA_HOME_SCREEN";
    //______________________________________________________________________________________________
    public static final String BUNDLE_CONVERSATION          = "BUNDLE_CONVERSATION";
    public static final String BUNDLE_INFO                  = "BUNDLE_INFO";
    public static final String BUNDLE_IMAGE                 = "BUNDLE_IMAGE";
    public static final String BUNDLE_VIDEO                 = "BUNDLE_VIDEO";
    public static final String BUNDLE_SEND_MEDIA            = "BUNDLE_SEND_MEDIA";
    public static final String BUNDLE_SHARED                = "BUNDLE_SHARED";
    public static final String SHARE_MSG_TEXT               = "SHARE_MSG_TEXT";
    public static final String SHARE_MSG_IMAGE_URI          = "SHARE_MSG_IMAGE_URI";
    //______________________________________________________________________________________________
    public static final String PARCELABLE_CHAT_ITEM         = "PARCELABLE_CHAT_ITEM";
    public static final String PARCELABLE_NOTE_ITEM         = "PARCELABLE_NOTE_ITEM";
    //______________________________________________________________________________________________
    public static final String LBM_NEW_NOTE_CHAT            = "LBM_NEW_NOTE_CHAT";          //(1) Chats
    public static final String LBM_NEW_NOTE_CONVERSATION    = "LBM_NEW_NOTE_CONVERSATION";  //(1) Conversation
    public static final String LBM_NEW_NOTE_MEDIA           = "LBM_NEW_NOTE_MEDIA";         //(6) Conversation, GalleryImageSend, SendImage, SendVideo, ImagePreviewActivity, VideoPreviewActivity CHECKED_BEFORE_BROADCAST__
    public static final String LBM_MSG_STATUS               = "LBM_MSG_STATUS";             //(2) Chats, Conversation
    public static final String LBM_MEDIA_STATUS             = "LBM_MEDIA_STATUS";           //(1) Conversation CHECKED_BEFORE_BROADCAST__
    //info
    public static final String LBM_CHANGE_NAME              = "LBM_CHANGE_NAME";            //(3) Chats, Conversation, InfoGroup
    public static final String LBM_CHANGE_IMAGE             = "LBM_CHANGE_IMAGE";           //(6) Chat, Conversation, InfoContact, InfoGroup, NewChat, Profile
    //others
    public static final String LBM_DELETE_ALL_CHAT          = "LBM_DELETE_ALL_CHAT";        //(1) Chats
    public static final String LBM_RESTORE_CHAT             = "LBM_RESTORE_CHAT";           //(1) Chats
    //______________________________________________________________________________________________
    public static final String CHAT_NAME                     = "CHAT_NAME";
    public static final String CHAT_ID                       = "CHAT_ID";
    //message
    public static final String MSG                           = "MSG";
    public static final String MSG_ID                        = "MSG_ID";
    public static final String MSG_FLOW                      = "MSG_FLOW";
    public static final String MSG_STATUS                    = "MSG_STATUS";
    public static final String MSG_KIND                      = "MSG_KIND";
    //public static final String MSG_TIMESTAMP                 = "MSG_TIMESTAMP";
    public static final String MEDIA_STATUS                  = "MEDIA_STATUS";
    public static final String MEDIA_CAPTION                 = "MEDIA_CAPTION";
    public static final String MEDIA_NAME                    = "MEDIA_NAME";
    //image
    public static final String MEDIA_IMAGE_URI               = "MEDIA_IMAGE_URI";
    public static final String MEDIA_IMAGE_TEMP_FILE         = "MEDIA_IMAGE_TEMP_FILE";
    public static final String MEDIA_IMAGE_URI_LIST          = "MEDIA_IMAGE_URI_LIST";
    //crop image
    //public static final String MEDIA_IMAGE_IS_SCALE_REQUIRED      = "MEDIA_IMAGE_IS_SCALE_REQUIRED";
    //public static final String MEDIA_IMAGE_IS_SCALE_UP_REQUIRED   = "MEDIA_IMAGE_IS_SCALE_UP_REQUIRED";
    //public static final String MEDIA_IMAGE_ASPECT_X               = "MEDIA_IMAGE_ASPECT_X";
    //public static final String MEDIA_IMAGE_ASPECT_Y               = "MEDIA_IMAGE_ASPECT_Y";
    public static final String MEDIA_IMAGE_OUTPUT_X          = "MEDIA_IMAGE_OUTPUT_X";
    public static final String MEDIA_IMAGE_OUTPUT_Y          = "MEDIA_IMAGE_OUTPUT_Y";
    public static final String MEDIA_IMAGE_IS_CIRCLE_CROP    = "MEDIA_IMAGE_IS_CIRCLE_CROP";
    public static final String MEDIA_IMAGE_MIN_BOUND         = "MEDIA_IMAGE_MIN_BOUND";
    public static final String MEDIA_IMAGE_MAX_BOUND         = "MEDIA_IMAGE_MAX_BOUND";
    //video
    public static final String MEDIA_VIDEO_URI               = "MEDIA_VIDEO_URI";
    //crop video
    public static final String MEDIA_VIDEO_PATH              = "MEDIA_VIDEO_PATH";
    public static final String MEDIA_VIDEO_IS_UPLOAD_ONLY    = "MEDIA_VIDEO_IS_UPLOAD_ONLY";
    //public static final String MEDIA_VIDEO_SIZE            = "MEDIA_VIDEO_SIZE";
    public static final String MEDIA_VIDEO_START_TIME        = "MEDIA_VIDEO_START_TIME";
    public static final String MEDIA_VIDEO_DURATION          = "MEDIA_VIDEO_DURATION";
    //gallery
    public static final String GALLERY_BUCKET_ID             = "GALLERY_BUCKET_ID";
    public static final String GALLERY_BUCKET_NAME           = "GALLERY_BUCKET_NAME";
    //Forward
    public static final String FORWARD_MSG_ID_LIST           = "FORWARD_MSG_ID_LIST";
    //Notifications
    public static final String CONVERSATION_STATE_PAUSED     = "CONVERSATION_STATE_PAUSED";
    public static final String CONVERSATION_STATE_RESUMED    = "CONVERSATION_STATE_RESUMED";
    //Wallpaper
    public static final String WALLPAPER_NUMBER              = "WALLPAPER_NUMBER";
    //WebView
    public static final String URL                           = "URL";

}
