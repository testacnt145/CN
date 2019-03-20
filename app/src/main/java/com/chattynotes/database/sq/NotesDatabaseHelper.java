package com.chattynotes.database.sq;

import com.chattynotes.application.App;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.database.sq.table.TableChats;
import com.chattynotes.database.sq.table.TableNotes;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import static com.chattynotes.database.sq.constant.DBConstant.DB_NAME;
import static com.chattynotes.database.sq.constant.DBConstant.DB_VERSION;

class NotesDatabaseHelper extends SQLiteOpenHelper {

	NotesDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//[sqlite_: old]
//		String createTableChats = "CREATE TABLE " + TableChats.CHATS + " (" +
//				TableChats.Column.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//				//[bug_: SQL_| AUTOINCREMENT keyword is important here otherwise SQL will reuse old IDs]
//				TableChats.Column.NAME + " TEXT," +
//				TableChats.Column.TIMESTAMP + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MSG_TIMESTAMP() +
//				");";
//		db.execSQL(createTableChats);
//		String createTableNotes = "CREATE TABLE " + TableNotes.NOTES + " (" +
//				TableNotes.Column.ID + " INTEGER PRIMARY KEY NOT NULL," +
//				TableNotes.Column.CHAT_ID + " TEXT," +
//				TableNotes.Column.MSG + " TEXT," +
//				TableNotes.Column.MSG_ID + " TEXT," +
//				TableNotes.Column.MSG_FLOW + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MSG_FLOW  + "," +
//				TableNotes.Column.MSG_STATUS + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MSG_STATUS  + "," +
//				TableNotes.Column.MSG_KIND + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MSG_KIND  + "," +
//				TableNotes.Column.MSG_TIMESTAMP + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MSG_TIMESTAMP()  + "," +
//				TableNotes.Column.MSG_TIMESTAMP_DONE + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MSG_TIMESTAMP_DONE  + "," +
//				TableNotes.Column.MSG_STAR + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MSG_STAR  + "," +
//				TableNotes.Column.MEDIA_STATUS + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MEDIA_STATUS + "," +
//				TableNotes.Column.MEDIA_CAPTION + " TEXT," +
//				TableNotes.Column.MEDIA_SIZE + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MEDIA_SIZE  + "," +
//				TableNotes.Column.MEDIA_DURATION + " INTEGER DEFAULT " + MsgConstant.DEFAULT_MEDIA_DURATION  + "," +
//				TableNotes.Column.LINK_MESSAGE + " TEXT" +
//				");";
//		db.execSQL(createTableNotes);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //LogUtil.e("NotesDatabaseHelper-sql", oldVersion + ":" + newVersion);
		//Migrate from Sqlite to Realm (https://stackoverflow.com/a/41457024)
		if (oldVersion < DB_VERSION) {
			Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
			realm.beginTransaction();
			//insert note first as we will required notes for chat table
			List<Notes> allNotes = getAllNotes(db);
			realm.insertOrUpdate(allNotes);
			List<Chats> allChats = getAllChats(db);
			realm.insertOrUpdate(allChats);
			realm.commitTransaction();
			realm.close();
			deleteSqliteDatabase(db);
		}

	}


	//_______________________________________________________________________________ DB_VERSION = 2
	private List<Chats> getAllChats(SQLiteDatabase _db) {
		List<Chats> chatsList = new ArrayList<>();
		String selectQuery = "SELECT * FROM " + TableChats.CHATS;
		Cursor cur = _db.rawQuery(selectQuery, null);
		try {
			if (cur.moveToFirst()) {
				do {
					Chats chat = new Chats();
					long chatId = cur.getLong(TableChats.Column.ID_INDEX);
					chat.setChatId(chatId);
					chat.setChatName(cur.getString(TableChats.Column.NAME_INDEX));
					chat.setTimestamp(cur.getLong(TableChats.Column.TIMESTAMP_INDEX));
					//get last note
					String queryNote = "SELECT * FROM " + TableNotes.NOTES + " WHERE " + TableNotes.Column.CHAT_ID + "=" + chatId + " ORDER BY " + TableNotes.Column.ID + " DESC LIMIT 1";
					Cursor curNote = _db.rawQuery(queryNote, null);
					if (curNote.moveToFirst()) {
						Notes note = new Notes();
						note.setNoteId(curNote.getLong(TableNotes.Column.ID_INDEX));
						note.setChatId(curNote.getLong(TableNotes.Column.CHAT_ID_INDEX));
						note.setMsg(curNote.getString(TableNotes.Column.MSG_INDEX));
						note.setMsgId(curNote.getString(TableNotes.Column.MSG_ID_INDEX));
						note.setMsgFlow(curNote.getInt(TableNotes.Column.MSG_FLOW_INDEX));
						note.setMsgStatus(curNote.getInt(TableNotes.Column.MSG_STATUS_INDEX));
						note.setMsgKind(curNote.getInt(TableNotes.Column.MSG_KIND_INDEX));
						note.setMsgTimestamp(curNote.getLong(TableNotes.Column.MSG_TIMESTAMP_INDEX));
						note.setMsgTimestampDone(curNote.getLong(TableNotes.Column.MSG_TIMESTAMP_DONE_INDEX));
						note.setMsgStar(curNote.getInt(TableNotes.Column.MSG_STAR_INDEX));
						note.setMediaStatus(curNote.getInt(TableNotes.Column.MEDIA_STATUS_INDEX));
						note.setMediaCaption(curNote.getString(TableNotes.Column.MEDIA_CAPTION_INDEX));
						note.setMediaSize(curNote.getInt(TableNotes.Column.MEDIA_SIZE_INDEX));
						note.setMediaDuration(curNote.getInt(TableNotes.Column.MEDIA_DURATION_INDEX));
						chat.setNote(note);
					}
					curNote.close();
					chatsList.add(chat);
				} while (cur.moveToNext());
			}
		} catch (SQLiteException ignored) {
		} finally {
			cur.close();
		}
		return chatsList;
	}

	private List<Notes> getAllNotes(SQLiteDatabase _db) {
		List<Notes> notesList = new ArrayList<>();
		String selectQuery = "SELECT * FROM " + TableNotes.NOTES;
		Cursor cur = _db.rawQuery(selectQuery, null);
		try {
			if (cur.moveToFirst()) {
				do {
					Notes note = new Notes();
					note.setNoteId(cur.getLong(TableNotes.Column.ID_INDEX));
					note.setChatId(cur.getLong(TableNotes.Column.CHAT_ID_INDEX));
					note.setMsg(cur.getString(TableNotes.Column.MSG_INDEX));
					note.setMsgId(cur.getString(TableNotes.Column.MSG_ID_INDEX));
					note.setMsgFlow(cur.getInt(TableNotes.Column.MSG_FLOW_INDEX));
					note.setMsgStatus(cur.getInt(TableNotes.Column.MSG_STATUS_INDEX));
					note.setMsgKind(cur.getInt(TableNotes.Column.MSG_KIND_INDEX));
					note.setMsgTimestamp(cur.getLong(TableNotes.Column.MSG_TIMESTAMP_INDEX));
					note.setMsgTimestampDone(cur.getLong(TableNotes.Column.MSG_TIMESTAMP_DONE_INDEX));
					note.setMsgStar(cur.getInt(TableNotes.Column.MSG_STAR_INDEX));
					note.setMediaStatus(cur.getInt(TableNotes.Column.MEDIA_STATUS_INDEX));
					note.setMediaCaption(cur.getString(TableNotes.Column.MEDIA_CAPTION_INDEX));
					note.setMediaSize(cur.getInt(TableNotes.Column.MEDIA_SIZE_INDEX));
					note.setMediaDuration(cur.getInt(TableNotes.Column.MEDIA_DURATION_INDEX));
					notesList.add(note);
				} while (cur.moveToNext());
			}
		} catch (SQLiteException ignored) {
		} finally {
			cur.close();
		}
		return notesList;
	}

	private void deleteSqliteDatabase(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TableNotes.NOTES);
		db.execSQL("DROP TABLE IF EXISTS " + TableChats.CHATS);
		//https://stackoverflow.com/a/4420083
		App.getContext().deleteDatabase(DB_NAME);
	}
}



//1000 (sqlite)
//1100 (sqlite)
//2000 (realm)
