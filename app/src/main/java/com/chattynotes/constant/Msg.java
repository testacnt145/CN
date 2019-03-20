package com.chattynotes.constant;

public final class Msg {

    public final static String YOU = "You";
    public final static String _CHAT_CREATED = "created chat";
    public static String _CHAT_NAME_CHANGE(String from, String to) {
        return "Chat name changed from \'" + from + "\' to \'" + to + "\'";
    }
    public final static String _CHAT_IMAGE_CHANGE = "Chat image changed";
    public static String FEATURE_NOT_SUPPORTED(int msgKind) {
        return msgKind + " : feature not supported in this version";
    }

    //for Chat List
    public final static String CHAT_IMAGE           = "Image";
    public final static String CHAT_AUDIO           = "Audio";
    public final static String CHAT_VIDEO           = "Video";
    public final static String CHAT_LOCATION        = "Location";
    public final static String CHAT_ADDRESS         = "Address";
    public final static String CHAT_CONTACT         = "Contact";
    public final static String CHAT_LONG_TEXT       = "Text File";
}
