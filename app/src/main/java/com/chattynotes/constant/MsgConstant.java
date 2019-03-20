package com.chattynotes.constant;

import com.chattynotes.adapters.listConversation.model.MsgLink;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.util.DateUtil;

public final class MsgConstant {

	//21
	public final static int LONG_TEXT_MESSAGE_LENGTH = 404; //400(android), 404(ios)

	public static final long 	DEFAULT_CHAT_ID = ChatID.CHATTY_NOTES;
	public static final String 	DEFAULT_MSG = "";
	public static String 	    DEFAULT_MSG_ID()  { //we want fresh value everytime, can not be done using variable
        return QueryNotesDB.getMsgId();
    }
	public static final int 	DEFAULT_MSG_FLOW = MsgFlow.SEND;
	public static final int 	DEFAULT_MSG_STATUS = MsgStatus.CLOCK;
	public static final int 	DEFAULT_MSG_KIND = MsgKind.TEXT;
	public static long 			DEFAULT_MSG_TIMESTAMP() { //we want fresh value everytime, can not be done using variable
		return DateUtil.getDateInMilliseconds();
	}
	public static final long 	DEFAULT_MSG_TIMESTAMP_DONE = 0;
	public static final int 	DEFAULT_MSG_STAR = MsgStar.NO;
	public static final int 	DEFAULT_MEDIA_STATUS = MediaStatus.DEFAULT;
	public static final String 	DEFAULT_MEDIA_CAPTION = "";
	public static final int 	DEFAULT_MEDIA_SIZE = 0;
	public static final int 	DEFAULT_MEDIA_DURATION = 0;
	public static final String 	DEFAULT_LINK_MESSAGE = "";
	public static final MsgLink DEFAULT_LINK_OBJECT = null;

}