package com.chattynotes.constant;


import com.chattynotes.util.DateUtil;

public class ChatConstant {

    public static long DEFAULT_CHAT_TIMESTAMP() { //we want fresh value everytime, can not be done using variable
        return DateUtil.getDateInMilliseconds();
    }
    public static final String DEFAULT_DRAFT = "";
    public static final int DEFAULT_PASSWORD_STATUS = 0;
    public static final String DEFAULT_PASSWORD = "";
    public static final String DEFAULT_PASSWORD_HINT = "";
    public static final long DEFAULT_PASSWORD_CODE = 0;
}
