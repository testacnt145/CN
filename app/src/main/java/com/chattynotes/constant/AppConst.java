package com.chattynotes.constant;

public class AppConst {

    public final static int GARBAGE_REQUEST = 1111;

    //LIMIT VARIABLES
    public final static int MAX_FORWARD_MESSAGES        = 100;
    public final static int NO_OF_MESSAGES_TO_FETCH     = 50;
    public final static int MAX_IMAGE_AT_ONCE           = 10;
    public final static int MAX_CHAT_NAME_LENGTH        = 30;
    public final static int MIN_CHAT_NAME_LENGTH        = 1;
    public final static int PASSWORD_LENGTH             = 3;

    //media
    public final static int MIN_VIDEO_TIME = 1; //second
    public final static int MAX_VIDEO_SIZE = 30; //MB
    public final static int MAX_CAM_VIDEO_FILE_SIZE = 30000000; //byte (30MB)
    public final static int MAX_CAM_VID_ENCODING_BIT_RATE = 1380000; // bits/second or bps = Data Speed
    public final static int MAX_CAM_VID_TIME = 3600000; //ms (1hour)


    //Conversation
    public final static int MIN_LINES       = 2;
    public final static int MAX_LINES       = 6;


    public final static int UNIQUE_NOTIFICATION_FOREGROUND_SERVICE_ID = 1;

    //Conversation Window
    //used for Handling Sound
    public static String ACTIVE_WINDOW = IntentKeys.CONVERSATION_STATE_PAUSED;
}
