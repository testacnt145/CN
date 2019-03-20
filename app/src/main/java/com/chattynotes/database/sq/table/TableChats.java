package com.chattynotes.database.sq.table;

public final class TableChats {

    //Naming convention guidelines
    //http://leshazlewood.com/software-engineering/sql-style-guide/#comment_count

    public static final String CHATS = "chats";

    public static final class Column {

//        public static final String ID = "id"; //1
//        public static final String NAME = "name"; //2
//        public static final String TIMESTAMP = "timestamp"; //3

        //Column Numbers
        public static final int ID_INDEX           = 0;
        public static final int NAME_INDEX         = 1;
        public static final int TIMESTAMP_INDEX    = 2;
    }

}
