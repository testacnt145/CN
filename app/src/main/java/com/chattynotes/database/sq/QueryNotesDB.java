package com.chattynotes.database.sq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

//http://stackoverflow.com/questions/29502357/android-api-level-19-and-try-can-use-automatic-resource-management-warning
@SuppressWarnings("TryFinallyCanBeTryWithResources")
public class QueryNotesDB {

//	private static final String TAG = "QueryNotesDB";

	private NotesDatabaseHelper DbHelper;
	private SQLiteDatabase Db;

	public QueryNotesDB(Context ctx) {
		DbHelper = new NotesDatabaseHelper(ctx);
		Db = DbHelper.getWritableDatabase();
	}
//
//	public QueryNotesDB open() throws SQLException {
//		Db = DbHelper.getReadableDatabase();
//		return this;
//	}
//
//	public void close() {
//		Db.close();
//	}
//
////_______________________________________________________________________________________________________________
////_______________________________________________________________________________________________________________
//	//use for Chatty Notes
//	public void insertOrReplaceNewChat(long timestamp) {
//		try {
//			String sql = "INSERT OR REPLACE INTO " + TableChats.CHATS + " ("
//					+ TableChats.Column.ID + ","
//					+ TableChats.Column.NAME + ","
//					+ TableChats.Column.TIMESTAMP + ")" +
//					" values ('"
//					+ ChatID.CHATTY_NOTES + "','"
//					+ ChatName.CHATTY_NOTES + "',COALESCE((SELECT " + TableChats.Column.TIMESTAMP + " FROM " + TableChats.CHATS + " WHERE " + TableChats.Column.ID + "=" + ChatID.CHATTY_NOTES + "),"
//					+ timestamp + "))";
//			Db.execSQL(sql);
//		} catch (SQLException ignored) {
//		}
//	}
//
//	public void insertNewChat(String chatName, long timestamp) {
//		try {
//			/*
//			String sql = "INSERT INTO " + TableChats.CHATS + " ("
//					+ TableChats.Column.NAME + ","
//					+ TableChats.Column.TIMESTAMP + ")"
//					+ " values ('"
//					+ chatName + "','"
//					+ timestamp + "')";
//			LogUtil.e(TAG, "insertNewChat: " + sql);
//			Db.execSQL(sql);*/
//			ContentValues values = new ContentValues();
//			values.put(TableChats.Column.NAME, chatName);
//			values.put(TableChats.Column.TIMESTAMP, timestamp);
//			Db.insert(TableChats.CHATS, null, values);
//		} catch (SQLException ignored) {
//			LogUtil.exception(TAG, "insertNewChat", ignored);
//		}
//	}
//
//	public void insertNewNote(ConversationItem conItem, String msgLinkString) {
//		conItem.msg = DBUtil.sqlEscapeString(conItem.msg);
//		conItem.mediaCaption = DBUtil.sqlEscapeString(conItem.mediaCaption);
//		msgLinkString = DBUtil.sqlEscapeString(msgLinkString);
//		try {
//			String sql = "INSERT INTO " + TableNotes.NOTES + " ("
//					+ TableNotes.Column.CHAT_ID + ","
//					+ TableNotes.Column.MSG + ","
//					+ TableNotes.Column.MSG_ID + ","
//					+ TableNotes.Column.MSG_FLOW + ","
//					+ TableNotes.Column.MSG_STATUS + ","
//					+ TableNotes.Column.MSG_KIND + ","
//					+ TableNotes.Column.MSG_TIMESTAMP + ","
//					+ TableNotes.Column.MSG_TIMESTAMP_DONE + ","
//					+ TableNotes.Column.MSG_STAR + ","
//					+ TableNotes.Column.MEDIA_STATUS + ","
//					+ TableNotes.Column.MEDIA_CAPTION + ","
//					+ TableNotes.Column.MEDIA_SIZE + ","
//					+ TableNotes.Column.MEDIA_DURATION + ","
//					+ TableNotes.Column.LINK_MESSAGE + ")" +
//					" values ('"
//					+ conItem.chatID + "','"
//					+ conItem.msg + "','"
//					+ conItem.msgID + "','"
//					+ conItem.msgFlow + "','"
//					+ conItem.msgStatus  + "','"
//					+ conItem.msgKind + "','"
//					+ conItem.msgTimestamp  + "','"
//					+ conItem.msgTimestampDone  + "','"
//					+ conItem.msgStar + "','"
//					+ conItem.mediaStatus  + "','"
//					+ conItem.mediaCaption + "','"
//					+ conItem.mediaSize + "','"
//					+ conItem.mediaDuration + "','"
//					+ msgLinkString + "')";
//			Db.execSQL(sql);
//		} catch (SQLException ignored) {
//			LogUtil.exception(TAG, "insertNewNote", ignored);
//		}
//	}
//
//	public ConversationItem getSingleNote(String msgID) {
//		String selectQuery = "SELECT * FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.MSG_ID + "='" + msgID + "' AND " + TableNotes.Column.MSG_ID + "!='" + MsgID.DEFAULT + "'";
//		Cursor cur = Db.rawQuery(selectQuery, null);
//		try {
//			if (cur.moveToFirst())
//				return DBUtil.getConversationItem(cur);
//		} catch (SQLiteException ignored) {
//		} finally {
//		    cur.close();
//		}
//        return null;
//	}
//
////______________________________________________________________________________________________________ CHAT LIST
//	//last message Of all chats
//	public ArrayList<ChatItem> getChatList() {
//		String selectQuery = "SELECT no."
//				+ TableNotes.Column.CHAT_ID +
//				", ch." + TableChats.Column.NAME +
//				", no." + TableNotes.Column.MSG +
//				", no." + TableNotes.Column.MSG_ID +
//				", no." + TableNotes.Column.MSG_FLOW +
//				", no." + TableNotes.Column.MSG_STATUS +
//				", no." + TableNotes.Column.MSG_KIND +
//				", no." + TableNotes.Column.MSG_TIMESTAMP +
//				", no." + TableNotes.Column.MEDIA_CAPTION +
//				" FROM " + TableNotes.NOTES + " no LEFT JOIN " + TableChats.CHATS + " ch ON ch." + TableChats.Column.ID + " = no." + TableNotes.Column.CHAT_ID +
//				" GROUP BY " + TableNotes.Column.CHAT_ID +
//				" ORDER BY no." + TableNotes.Column.ID + " DESC";
//		//LogUtil.e(TAG, "getChatList: " + selectQuery);
//		Cursor cur = Db.rawQuery(selectQuery, null);
//		try {
//			ArrayList<ChatItem> itemList = new ArrayList<>();
//			if (cur.moveToFirst()) {
//				do {
//					itemList.add(getChatItem(cur));
//				} while (cur.moveToNext());
//			}
//			return itemList;
//		} catch (SQLiteException ignored) {
//			//LogUtil.exception(TAG, "getChatList", ignored);
//		} finally {
//			cur.close();
//		}
//		return null;
//	}
//
//	private static final int CHAT_ID_INDEX              = 0;
//	private static final int CHAT_NAME_INDEX            = 1;
//	private static final int MSG_INDEX                  = 2;
//	private static final int MSG_ID_INDEX               = 3;
//	private static final int MSG_FLOW_INDEX             = 4;
//	private static final int MSG_STATUS_INDEX           = 5;
//	private static final int MSG_KIND_INDEX             = 6;
//	private static final int MSG_TIMESTAMP_INDEX        = 7;
//	private static final int MEDIA_CAPTION_INDEX        = 8;
//	//private static final int LINK_MESSAGE_INDEX         = 9;
//	private ChatItem getChatItem(Cursor c) {
//		return new ChatItem(
//				c.getLong(CHAT_ID_INDEX),
//				c.getString(CHAT_NAME_INDEX),
//				c.getString(MSG_INDEX),
//				c.getString(MSG_ID_INDEX),
//				c.getInt(MSG_FLOW_INDEX),
//				c.getInt(MSG_STATUS_INDEX),
//				c.getInt(MSG_KIND_INDEX),
//				c.getLong(MSG_TIMESTAMP_INDEX),
//				c.getString(MEDIA_CAPTION_INDEX));
//	}
//
//	public void updateChatName(long chatID, String chatName) {
//		try {
//			//DBUtil.sqlEscapeString(chatName); //not required, Android update method handle itself
//			ContentValues values = new ContentValues();
//			values.put(TableChats.Column.NAME, chatName);
//			Db.update(TableChats.CHATS, values, TableChats.Column.ID + "=?", new String[] {Long.toString(chatID)});
//		} catch (SQLException ignored) {
//		}
//	}
//
//	public void updateMediaStatus(String msgID, int mediaStatus) {
//		try {
//			String sql = "UPDATE " + TableNotes.NOTES + " SET " + TableNotes.Column.MEDIA_STATUS + "='" + mediaStatus + "' WHERE " + TableNotes.Column.MSG_ID + "='" + msgID +"'";
//			Db.execSQL(sql);
//		} catch (SQLiteException ignored) {
//		}
//	}
//
//	//update MSG_STATUS and MSG_TIMESTAMP_DONE
//	public void doneSelectedMessages(String msgID) {
//		try {
//			String sql = "UPDATE " + TableNotes.NOTES + " SET " + TableNotes.Column.MSG_STATUS + "='" + MsgStatus.DONE + "'," + TableNotes.Column.MSG_TIMESTAMP_DONE + "='" + MsgConstant.DEFAULT_MSG_TIMESTAMP() + "' WHERE " + TableNotes.Column.MSG_ID + "='" + msgID +"'";
//			Db.execSQL(sql);
//		} catch (SQLiteException ignored) {
//		}
//	}
//
//	public void starSelectedMessages(String msgID, int msgStar) {
//		try {
//			String sql = "UPDATE " + TableNotes.NOTES + " SET " + TableNotes.Column.MSG_STAR + "='" + msgStar + "' WHERE " + TableNotes.Column.MSG_ID + "='" + msgID +"'";
//			Db.execSQL(sql);
//		} catch (SQLiteException ignored) {
//		}
//	}
//
//	public void unstarAllMessages() {
//		try {
//			String sql = "UPDATE " + TableNotes.NOTES + " SET " + TableNotes.Column.MSG_STAR + "='" + MsgStar.NO + "'";
//			Db.execSQL(sql);
//		} catch (SQLiteException ignored) {
//		}
//	}
//
//	public void deleteSelectedMessage(String msgID) {
//		try {
//			String deleteQuery = "DELETE FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.MSG_ID + "='" + msgID + "'";
//			Db.execSQL(deleteQuery);
//		} catch (SQLiteException ignored) {
//		}
//	}
//
//	public int getMediaCountOfSpecificMessage(long chatID, String msg) {
//		String selectQuery = "SELECT COUNT(*) FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.CHAT_ID + "='" + chatID + "' AND " + TableNotes.Column.MSG + "='" + msg + "'";
//		Cursor cursor = Db.rawQuery(selectQuery, null);
//		int count=0;
//		try {
//			if (cursor.moveToFirst()) {
//				do {
//					count =  cursor.getInt(0);
//				} while (cursor.moveToNext());
//			}
//		} catch (SQLiteException ignored) {
//		} finally {
//		    cursor.close();
//		}
//		return count;
//	}
//
//	//<editor-fold desc="CONVERSATION">
////______________________________________________________________________________________________________ CONVERSATION LIST
//	//CHANGE : Condition Added : check if msgID equals -1, do not fetch that message in conversation list, as msgID equals -1 is for getGpData group notes
//	public ArrayList<InterfaceConversation> get50MessageForConversation(long chatID, int fetchRow) {
//		String messageQuery ="SELECT * FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.CHAT_ID + "='" + chatID + "' AND " + TableNotes.Column.MSG_ID + "!='" + MsgID.DEFAULT + "' ORDER BY " + TableNotes.Column.ID + " DESC LIMIT " + fetchRow + "," + AppConst.NO_OF_MESSAGES_TO_FETCH; //limit from,count; limit 4,100 means return 100 rows start from 4
//		String sortQuery = "SELECT * FROM (" + messageQuery + ") ORDER BY " + TableNotes.Column.ID;
//		Cursor cursor = Db.rawQuery(sortQuery, null);
//		try {
//			ArrayList<InterfaceConversation> itemList = new ArrayList<>();
//			if (cursor.moveToFirst()) {
//				do {
//					itemList.add(DBUtil.getConversationItem(cursor));
//				} while (cursor.moveToNext());
//			}
//			return itemList;
//		} catch (SQLiteException ignored) {
//		} finally {
//		    cursor.close();
//		}
//		return null;
//	}
//
//	//get all starred notes
//	public ArrayList<InterfaceConversation> getStarredMessage() {
//		String messageQuery ="SELECT * FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.MSG_STAR + "='" + MsgStar.YES + "' ORDER BY " + TableNotes.Column.CHAT_ID + " DESC";
//		Cursor cursor = Db.rawQuery(messageQuery, null);
//		try {
//			ArrayList<InterfaceConversation> itemList = new ArrayList<>();
//			if (cursor.moveToFirst()) {
//				do {
//					itemList.add(DBUtil.getConversationItem(cursor));
//				} while (cursor.moveToNext());
//			}
//			return itemList;
//		} catch (SQLiteException ignored) {
//		} finally {
//			cursor.close();
//		}
//		return null;
//	}
//
//	//get all groups all members
//
//	public int getMessageCount(long chatID) {
//		String selectQuery = "SELECT  COUNT(*) FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.CHAT_ID + "='" + chatID + "' AND " + TableNotes.Column.MSG_ID + "!='" + MsgID.DEFAULT + "'";
//		Cursor cursor = Db.rawQuery(selectQuery, null);
//		int count=0;
//		try {
//			if (cursor.moveToFirst()) {
//				do {
//					count = cursor.getInt(0);
//				} while (cursor.moveToNext());
//			}
//		} catch (SQLiteException ignored) {
//		} finally {
//		    cursor.close();
//		}
//		return count;
//	}
//
//	public ArrayList<MediaItem> getMediaList(long chatID) {
//		String selectQuery = "SELECT "
//				+ TableNotes.Column.CHAT_ID + ","
//				+ TableNotes.Column.MSG + ","
//				+ TableNotes.Column.MSG_ID + ","
//				+ TableNotes.Column.MSG_FLOW + ","
//				+ TableNotes.Column.MSG_TIMESTAMP
//				+ " FROM " + TableNotes.NOTES + " WHERE ("
//				+ TableNotes.Column.CHAT_ID + "='" + chatID + "') AND (" + TableNotes.Column.MEDIA_STATUS + "='" + MediaStatus.COMPLETED + "') AND (" + TableNotes.Column.MSG_KIND + "='" + MsgKind.M1_IMAGE + "') ORDER BY " + TableNotes.Column.ID + " ASC";
//		Cursor cursor = Db.rawQuery(selectQuery, null);
//		try {
//			ArrayList<MediaItem> itemList = new ArrayList<>();
//			if (cursor.moveToFirst()) {
//				do {
//					itemList.add(new MediaItem(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getLong(4)));
//				} while (cursor.moveToNext());
//			}
//			return itemList;
//		} catch (SQLiteException ignored) {
//		} finally {
//		    cursor.close();
//		}
//		return null;
//	}
//
////__________________________________________________________________________________________________ Email Root
//	public String getEmailContent(long chatID) {
//		StringBuilder builder = new StringBuilder();
//		String selectQuery ="SELECT * FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.CHAT_ID + "=" + chatID;
//		Cursor cursor = Db.rawQuery(selectQuery, null);
//		try {
//			if (cursor.moveToFirst()) {
//				do {
//					ConversationItem item = DBUtil.getConversationItem(cursor);
//					String DateTime = DateUtil.getDateInStringComplete(item.msgTimestamp) + ":";
//					String msg = "";
//					String name;
//					if (item.msgFlow == MsgFlow.SEND)
//						name = Msg.YOU + ":";
//					else
//						name = getChatName(chatID) + ":";
//					switch (item.msgKind) {
//						case MsgKind._CHANGE_NAME:
//						case MsgKind._CHANGE_IMAGE:
//						case MsgKind.TEXT:
//						case MsgKind.LINK:
//							msg = item.msg;
//							break;
//						case MsgKind.CONTACT:
//							msg = "Sent a " + Msg.CHAT_CONTACT + ": " + item.msg;
//							break;
//						case MsgKind.M1_IMAGE:
//							msg = "Sent an " + Msg.CHAT_IMAGE;
//							break;
//						case MsgKind.M2_AUDIO:
//							msg = "Sent an " + Msg.CHAT_AUDIO;
//							break;
//						case MsgKind.M3_VIDEO:
//							msg = "Sent a " + Msg.CHAT_VIDEO;
//							break;
//						case MsgKind.M4_LONG_TEXT:
//							msg = "Sent a " + Msg.CHAT_LONG_TEXT;
//							break;
//						case MsgKind.LOCATION:
//							msg = "Sent a " + Msg.CHAT_LOCATION + ": " + item.msg;
//							break;
//						case MsgKind.ADDRESS:
//							FoursquareItem model = MsgAddressUtil.decodeModel(item.msg);
//							msg = "Sent an " + Msg.CHAT_ADDRESS + ": " + model.name + " | " + model.loc_address + " | " + Msg.CHAT_LOCATION + ": " + model.loc_lat + "," + model.loc_lng;
//							break;
//					}
//					builder.append(DateTime).append(" ").append(name).append(" ").append(msg).append("\r\n");
//				} while (cursor.moveToNext());
//			}
//		} catch (SQLiteException ignored) {
//		} finally {
//			cursor.close();
//		}
//		return builder.toString();
//	}
//
////__________________________________________________________________________________________________
//	//this will not delete chat identifier message
//	public void clearChat(long chatID) {
//		try {
//			String q = "DELETE FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.CHAT_ID + "='" + chatID + "' AND " + TableNotes.Column.MSG_ID + "!='" + MsgID.DEFAULT + "'";
//			Db.execSQL(q);
//		} catch (SQLiteException ignored) {
//		}
//	}
//
//	public void deleteChat(long chatID) {
//		try {
//			String q1 = "DELETE FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.CHAT_ID + "='" + chatID + "'";
//			Db.execSQL(q1);
//			String q2 = "DELETE FROM " + TableChats.CHATS + " WHERE " + TableChats.Column.ID + "='" + chatID + "'";
//			Db.execSQL(q2);
//		} catch (SQLiteException ignored) {
//			//LogUtil.exception(TAG, "deleteChat", ignored);
//		}
//	}
//
//	public void deleteAllChats(){
//		try {
//			Db.execSQL("DELETE FROM " + TableNotes.NOTES);
//			Db.execSQL("DELETE FROM " + TableChats.CHATS);
//		} catch (SQLiteException ignored) {
//		}
//	}
//
////__________________________________________________________________________________________________
//	public String getChatName(long chatID) {
//		String chatName=null;
//		String selectQuery = "SELECT " + TableChats.Column.NAME + " FROM chats where " + TableChats.Column.ID + "='" + chatID + "'";
//		Cursor cur = Db.rawQuery(selectQuery, null);
//		try {
//			if (cur.moveToFirst()) {
//				do {
//					chatName = cur.getString(TableChats.Column.ID_INDEX);
//				} while (cur.moveToNext());
//			}
//		} catch (SQLiteException ignored) {
//		} finally {
//			cur.close();
//		}
//		return chatName;
//	}
//
	//____________________________________________________________________________________________//
	//---------------------------------------INFORMATION------------------------------------------//
	//____________________________________________________________________________________________//
	//Column Starts With cursor.getString(0)
	// LIKE used for strings only ---> = for both
	// != ---> <>
	//Use '' for values
	//INSERT INTO chats (msg,id) values ('usa,one',123); Perfect
	//INSERT INTO chats (msg,id) values (usa,one,123); Exception
	//SELECT * FROM NOTES WHERE MSG_STAR = 1  -------> (not ==)
	//SQL #(Index) starts with 1
	//SQL AUTOINCREMENT id in Chats table starts with 0


}
