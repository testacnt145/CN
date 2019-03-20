package com.chattynotes.constant;

public final class MediaStatus {

    public static final int DEFAULT                 = 0;


    public static final int COMPLETED               = 1; //default for non-media notes + LongText,Image,Audio (not for Video as it required compression)
    public static final int PLAYING_MEDIA           = 2; //audio
    public static final int COMPRESSING_MEDIA       = 3; //video
    public static final int COMPRESSING_MEDIA_FAILED= 4; //video compression failed
}
