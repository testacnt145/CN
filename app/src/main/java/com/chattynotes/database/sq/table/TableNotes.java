package com.chattynotes.database.sq.table;

public final class TableNotes {

    //Naming convention guidelines
    //http://leshazlewood.com/software-engineering/sql-style-guide/#comment_count

    public static final String NOTES = "notes";

    public static final class Column {

        public static final String ID = "id";                                  //1
        public static final String CHAT_ID = "chat_id";                        //2
        public static final String MSG = "msg";                                //3
        public static final String MSG_ID = "msg_id";                          //4
        public static final String MSG_FLOW = "msg_flow";                      //5
        public static final String MSG_STATUS = "msg_status";                  //6
        public static final String MSG_KIND = "msg_kind";                      //7
        public static final String MSG_TIMESTAMP = "msg_timestamp";            //8
        public static final String MSG_TIMESTAMP_DONE = "msg_timestamp_done";  //9
        public static final String MSG_STAR = "msg_star";                      //10
        public static final String MEDIA_STATUS = "media_status";              //11
        public static final String MEDIA_CAPTION = "media_caption";            //12
        public static final String MEDIA_SIZE = "media_size";                  //13
        public static final String MEDIA_DURATION = "media_duration";          //14
        public static final String LINK_MESSAGE = "link_message";              //15

        //Column Numbers
        public static final int ID_INDEX                   = 0;
        public static final int CHAT_ID_INDEX              = 1;
        public static final int MSG_INDEX                  = 2;
        public static final int MSG_ID_INDEX               = 3;
        public static final int MSG_FLOW_INDEX             = 4;
        public static final int MSG_STATUS_INDEX           = 5;
        public static final int MSG_KIND_INDEX             = 6;
        public static final int MSG_TIMESTAMP_INDEX        = 7;
        public static final int MSG_TIMESTAMP_DONE_INDEX   = 8;
        public static final int MSG_STAR_INDEX             = 9;
        public static final int MEDIA_STATUS_INDEX         = 10;
        public static final int MEDIA_CAPTION_INDEX        = 11;
        public static final int MEDIA_SIZE_INDEX           = 12;
        public static final int MEDIA_DURATION_INDEX       = 13;
        public static final int LINK_MESSAGE_INDEX         = 14;
    }

}
