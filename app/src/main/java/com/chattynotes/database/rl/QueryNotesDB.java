package com.chattynotes.database.rl;

import com.chattynotes.adapters.listConversation.model.InterfaceConversation;
import com.chattynotes.adapters.listConversation.utility.MsgAddressUtil;
import com.chattynotes.adapters.location.FoursquareItem;
import com.chattynotes.adapters.mediaViewer.model.Media;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.MediaStatus;
import com.chattynotes.constant.Msg;
import com.chattynotes.constant.MsgFlow;
import com.chattynotes.constant.MsgID;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.constant.MsgStar;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Fields.ChatsField;
import com.chattynotes.database.rl.model.Fields.NotesField;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.util.DateUtil;
import com.chattynotes.util.storage.json.model.ChatDetail;
import com.chattynotes.util.storage.json.model.LastBackupNote;
import java.util.ArrayList;
import java.util.UUID;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

//S: Select (get)
//I: Insert
//U: Update
//D: Delete
public class QueryNotesDB {
    private Realm realm;

    public QueryNotesDB(Realm realm) {
        this.realm = realm;
    }

    //__________________________________________________________________________________________________
    private static final int START_CUSTOM_CHAT_ID = 1; //because ChatID.CHATTY_NOTES = 0

    //https://stackoverflow.com/a/43950858
    public long getIncrementedChatId() {
        Number currentId = realm.where(Chats.class).max(ChatsField.CHAT_ID);
        int nextId;
        if (currentId == null) {
            nextId = START_CUSTOM_CHAT_ID;
        } else {
            nextId = currentId.intValue() + 1;
        }
        return nextId;
    }

    private static final int START_CUSTOM_NOTE_ID = 1;
    public long getIncrementedNoteId() {
        Number currentId = realm.where(Notes.class).max(NotesField.NOTE_ID);
        int nextId;
        if (currentId == null) {
            nextId = START_CUSTOM_NOTE_ID;
        } else {
            nextId = currentId.intValue() + 1;
        }
        return nextId;
    }

    public static String getMsgId() {
        return UUID.randomUUID().toString();
    }

    //______________________________________________________________________________________________
    //----------------------------------------------------------------------------------------------
    //______________________________________________________________________________________________
    //use for Chatty Notes
	public void insertOrUpdateChat(final Chats chats) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(chats);
            }
        });
    }

    //insert with null note
    public void insertChat(final Chats chats) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(chats);
            }
        });
    }

    public void insertNote(final Notes note) {
        //when insert/delete note, make sure to update chat

        realm.beginTransaction();

        Notes mNote = realm.copyToRealm(note); // Persist un-managed objects (https://realm.io/docs/java/latest/)

        //update Chats note(with last one)
        //method 1
        //[bug_: Realm_| this will update the name, but will insert the null values in other columns]
        //Chats chat = new Chats();
        //chat.setChatId(mNote.getChatId());
        //chat.setNote(mNote);
        //realm.copyToRealmOrUpdate(chat);
        //method 2
        Chats mChat = realm.where(Chats.class).equalTo(ChatsField.CHAT_ID, mNote.getChatId()).findFirst();
        mChat.setNote(mNote);

        realm.commitTransaction();
    }



    public ArrayList<Chats> getChatList() {
        RealmResults<Chats> results = realm.where(Chats.class).findAll();
        //sort on basis of last message timestamp
        //but Realm does not support sub table field (NotesField.MSG_TIMESTAMP)
        //therefore no sorting
        //results = results.sort(NotesField.MSG_TIMESTAMP, Sort.DESCENDING);
        ArrayList<Chats> mChatList = new ArrayList<>();
        mChatList.addAll(results.subList(0, results.size()));
        //mChatList = manage chat list
        //if we need to update the view, and if we change mChatList objects (chat.setName())
        //it will give exception: java.lang.IllegalStateException: Cannot modify managed objects outside of a write transaction
        /*
        https://stackoverflow.com/q/41334732
            [bug_: Realm_| Misuse of Realm]
            With normal use, there is also no reason to call copyFromRealm()
            copyFromRealm() is typically a mis-use of Realm API

            copyFromRealm() will copy complete list to memory which will effect the performance,
            but we need to return interface as InterfaceConversation implements
                1- ConversationDateSeparatorItem
                2- ConversationLoadEarlierItem
                3- ConversationUnknownItem
            which are not RealmObjects,
            and If you want to work with ArrayList<RealmObject> instead of ArrayList<Interface>,
            you will need to define all 3 as RealmObjects
            https://stackoverflow.com/a/30098398
             Christian from Realm here. You can only save objects that extend RealmObject inside a Realm.
             This is because Realm is not a schemaless database.

            Best practice and how to implement RealmList that need to support different types of objects
            https://stackoverflow.com/questions/36884573/best-practice-and-how-to-implement-realmlist-that-need-to-support-different-type
         */
        /*
        https://stackoverflow.com/a/40758983
            Typically mixing realm.copyFromRealm anywhere else for any other reason shows a fundamental
            misunderstanding of what Realm was designed for. It's a zero-copy database, after all.
            Copying from a zero-copy database is iffy.
         */
        /*
        https://www.reddit.com/r/androiddev/comments/5ko1ri/realmcopyfromrealm_how_to_professionally_misuse/
         */
        return (ArrayList<Chats>) realm.copyFromRealm(mChatList); //convert to un-managed
    }

    /* OLD METHOD working fine with List<POJO> */
//    public ArrayList<InterfaceConversation> getNoteList(long chatId, int from) {
//        //https://stackoverflow.com/a/29875237
//        /*
//            The cool thing is that you don't need to worry about that with Realm. The result object
//            returned from a query is lazily loading the objects and its fields when you access them.
//             Your objects are never copied and thus only represented once in memory/disk.
//         */
//        //But since we are converting RealmResults (managed) to list(un-managed) therefore, do this using load earlier
//        //https://stackoverflow.com/a/43293338
//        RealmResults<Notes> results = realm.where(Notes.class)
//                .equalTo(NotesField.CHAT_ID, chatId)
//                .notEqualTo(NotesField.MSG_ID, MsgID.DEFAULT)
//                .findAllSorted(NotesField.NOTE_ID, Sort.ASCENDING);
//        //results = results.sort(NotesField.NOTE_ID, Sort.DESCENDING);
//        ArrayList<Notes> mNoteList = new ArrayList<>();
//        mNoteList.addAll(results.subList(0, results.size()));
//        //mNoteList.addAll(results.subList(from, AppConst.NO_OF_MESSAGES_TO_FETCH)); //https://stackoverflow.com/a/43293338
//        ArrayList<Notes> noteList = (ArrayList<Notes>) realm.copyFromRealm(mNoteList);  //convert to un-managed
//        ArrayList<InterfaceConversation> noteInterface = new ArrayList<>();
//        noteInterface.addAll(noteList);
//        return noteInterface;
//    }

    public ArrayList<InterfaceConversation> getNoteList(long chatId, int rowFetched) {
        //fetched in descending order (bottom to top) [100-91 then 81-90 then 71-80]

        RealmResults<Notes> results = realm.where(Notes.class)
                .equalTo(NotesField.CHAT_ID, chatId)
                .notEqualTo(NotesField.MSG_ID, MsgID.DEFAULT)
                .findAllSorted(NotesField.NOTE_ID, Sort.ASCENDING);
        //results = results.sort(NotesField.NOTE_ID, Sort.DESCENDING);
        ArrayList<Notes> mNoteList = new ArrayList<>(); //complete list
        mNoteList.addAll(results.subList(0, results.size()));

        int total = mNoteList.size(); //100
        int msgRemaining = total - rowFetched; //100-10=90;   rowFetched=10 now we want (80-90)
        int from;
        //(from and till) should be less than equal to (AppConst.NO_OF_MESSAGES_TO_FETCH)
        ArrayList<Notes> mSubList = new ArrayList<>();
        if(msgRemaining <= AppConst.NO_OF_MESSAGES_TO_FETCH) {
            from = 0;
            mSubList.addAll(results.subList(from, msgRemaining)); //https://stackoverflow.com/a/43293338
        } else {
            from = msgRemaining-AppConst.NO_OF_MESSAGES_TO_FETCH;
            mSubList.addAll(results.subList(from, msgRemaining));
        }
        //LogUtil.e("QueryNotesDB","getNoteList ->" + "total:" + total + "rowFetched:" + rowFetched + "msgRemaining:" + msgRemaining);
        //LogUtil.e("QueryNotesDB","getNoteList ->" +"from:" + from + "till:" + msgRemaining + "fetched:" + mSubList.size());

        ArrayList<Notes> noteList = (ArrayList<Notes>) realm.copyFromRealm(mSubList);  //convert to un-managed
        ArrayList<InterfaceConversation> noteInterface = new ArrayList<>();
        noteInterface.addAll(noteList);
        return noteInterface;
    }

    public void updateChatName(final Chats chat) {
        realm.beginTransaction();
        Chats mChat = realm.where(Chats.class).equalTo(ChatsField.CHAT_ID, chat.getChatId()).findFirst();
        mChat.setChatName(chat.getChatName());
        realm.commitTransaction();
    }

    public void updateChatDraft(long chatId, String draft) {
        realm.beginTransaction();
        Chats mChat = realm.where(Chats.class).equalTo(ChatsField.CHAT_ID, chatId).findFirst();
        mChat.setDraft(draft);
        realm.commitTransaction();
    }

    public String getChatName(long chatId) {
        Chats mChat = realm.where(Chats.class).equalTo(ChatsField.CHAT_ID, chatId).findFirst();
        return mChat.getChatName();
    }

    public Chats getSingleChat(long chatId) {
        return realm.where(Chats.class)
                .equalTo(ChatsField.CHAT_ID, chatId)
                .findFirst();
    }

    public Notes getSingleNote(String msgId) {
        return realm.where(Notes.class)
                .equalTo(NotesField.MSG_ID, msgId)
                .findFirst();
    }

    public Notes getSingleNoteChatCreated(long chatId) {
        return realm.where(Notes.class)
                .equalTo(NotesField.MSG_ID, MsgID.DEFAULT)
                .equalTo(NotesField.CHAT_ID, chatId)
                .findFirst();
    }

    private Notes getSingleNoteLatest(long chatId) {
        return realm.where(Notes.class)
                .equalTo(ChatsField.CHAT_ID, chatId)
                .findAllSorted(NotesField.NOTE_ID, Sort.DESCENDING)
                .first();
        //or
        //      .findAllSorted(NotesField.NOTE_ID)
        //      .last();
    }

    private Notes getSingleNoteLatest() {
        return realm.where(Notes.class)
                .findAllSorted(NotesField.NOTE_ID, Sort.DESCENDING)
                .first();
    }

    public int getChatCount() {
        return realm.where(Chats.class)
                .findAll()
                .size();
    }

    public int getChatNotesCount(long chatId) {
        return realm.where(Notes.class)
                .equalTo(NotesField.CHAT_ID, chatId)
                .notEqualTo(NotesField.MSG_ID, MsgID.DEFAULT)
                .findAll()
                .size();
    }

    public int getNotesCount() {
        return realm.where(Notes.class)
                .notEqualTo(NotesField.MSG_ID, MsgID.DEFAULT)
                .findAll()
                .size();
    }

    public void updateDoneTimestamp(String msgId, int msgStatus, long msgTimestampDone) {
        realm.beginTransaction();
        Notes mNote = realm.where(Notes.class).equalTo(NotesField.MSG_ID, msgId).findFirst();
        mNote.setMsgStatus(msgStatus);
        mNote.setMsgTimestampDone(msgTimestampDone);
        realm.commitTransaction();
    }

    public void deleteSelectedNote(final String msgID, final long chatId) {
        //when insert/delete note, make sure to update chat
        realm.beginTransaction();
        RealmResults<Notes> mNoteList = realm.where(Notes.class).equalTo(NotesField.MSG_ID, msgID).findAll();
        mNoteList.deleteAllFromRealm();
        Chats mChat = realm.where(Chats.class)
                .equalTo(ChatsField.CHAT_ID, chatId)
                .findFirst();
        Notes mNote = getSingleNoteLatest(chatId);
        mChat.setNote(mNote);
        realm.commitTransaction();
    }

    public void deleteChat(long chatId) {
        //when insert/delete note, make sure to update chat
        realm.beginTransaction();
        RealmResults<Chats> chat = realm.where(Chats.class).equalTo(ChatsField.CHAT_ID, chatId).findAll();
        chat.deleteAllFromRealm();
        RealmResults<Notes> note = realm.where(Notes.class).equalTo(NotesField.CHAT_ID, chatId).findAll();
        note.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void clearChat(long chatId) {
        //when insert/delete note, make sure to update chat
        realm.beginTransaction();
        RealmResults<Notes> note = realm.where(Notes.class)
                .equalTo(NotesField.CHAT_ID, chatId)
                .notEqualTo(NotesField.MSG_ID, MsgID.DEFAULT)
                .findAll();
        note.deleteAllFromRealm();
        Chats mChat = realm.where(Chats.class).equalTo(ChatsField.CHAT_ID, chatId).findFirst();
        mChat.setNote(getSingleNoteChatCreated(chatId));
        realm.commitTransaction();
    }

    public void deleteAllChats() {
        //when insert/delete note, make sure to update chat
        realm.beginTransaction();
        RealmResults<Chats> mChat = realm.where(Chats.class).findAll();
        mChat.deleteAllFromRealm();
        RealmResults<Notes> mNote = realm.where(Notes.class).findAll();
        mNote.deleteAllFromRealm();
        realm.commitTransaction();
    }

    //__________________
    public ArrayList<Media> getMediaList(long chatId) {
        RealmResults<Notes> results = realm.where(Notes.class)
                .equalTo(NotesField.CHAT_ID, chatId)
                .equalTo(NotesField.MSG_KIND, MsgKind.M1_IMAGE)
                .equalTo(NotesField.MEDIA_STATUS, MediaStatus.COMPLETED)
                .findAllSorted(NotesField.NOTE_ID);
        ArrayList<Notes> mNoteList = new ArrayList<>();
        mNoteList.addAll(results.subList(0, results.size()));
        ArrayList<Media> mediaList = new ArrayList<>();
        for (Notes note : mNoteList) {
            Media media = new Media();
            media.setChatId(note.getChatId());
            media.setMsg(note.getMsg());
            media.setMsgFlow(note.getMsgFlow());
            media.setMsgId(note.getMsgId());
            media.setMsgTimestamp(note.getMsgTimestamp());
            mediaList.add(media);
        }
        return mediaList;

    }

    public int getMediaCountOfSpecificMessage(long chatId, String msg) {
        return realm.where(Notes.class)
                .equalTo(NotesField.CHAT_ID, chatId)
                .equalTo(NotesField.MSG, msg)
                .findAll()
                .size();
    }

    public void updateMediaStatus(String msgId, int mediaStatus) {
        realm.beginTransaction();
        Notes mNote = realm.where(Notes.class).equalTo(NotesField.MSG_ID, msgId).findFirst();
        mNote.setMediaStatus(mediaStatus);
        realm.commitTransaction();
    }

    //___________________
    public void starSelectedMessages(String msgId, int msgStar) {
        //todo check in new version
        realm.beginTransaction();
        Notes mNote = realm.where(Notes.class).equalTo(NotesField.MSG_ID, msgId).findFirst();
        mNote.setMsgStar(msgStar);
        realm.commitTransaction();
    }

    public ArrayList<InterfaceConversation> getStarredMessage() {
        //todo check in new version
        RealmResults<Notes> results = realm.where(Notes.class)
                .equalTo(NotesField.MSG_STAR, MsgStar.YES)
                .findAllSorted(NotesField.CHAT_ID);
        ArrayList<Notes> mNoteList = new ArrayList<>();
        mNoteList.addAll(results.subList(0, results.size()));
        ArrayList<Notes> noteList = (ArrayList<Notes>) realm.copyFromRealm(mNoteList);  //convert to un-managed
        ArrayList<InterfaceConversation> noteInterface = new ArrayList<>();
        noteInterface.addAll(noteList);
        return noteInterface;
    }

    public void unstarAllMessages() {
        //todo check in new version
        realm.beginTransaction();
        RealmResults<Notes> mNoteList = realm.where(Notes.class)
                .equalTo(NotesField.MSG_STAR, MsgStar.YES)
                .findAll();
        for (Notes mNote: mNoteList) {
            mNote.setMsgStar(MsgStar.NO);
        }
        realm.commitTransaction();
    }

    //___________________
    public String getEmailContent(long chatId) {
        RealmResults<Notes> mNoteList = realm.where(Notes.class)
                .equalTo(NotesField.CHAT_ID, chatId)
                .findAll();
        StringBuilder builder = new StringBuilder();
        for (Notes mNote: mNoteList) {
            String DateTime = DateUtil.getDateInStringComplete(mNote.getMsgTimestamp()) + ":";
            String msg = "";
            String name;
            if (mNote.getMsgFlow() == MsgFlow.SEND)
                name = Msg.YOU + ":";
            else
                name = getChatName(mNote.getChatId()) + ":";
            switch (mNote.getMsgKind()) {
                case MsgKind._CHANGE_NAME:
                case MsgKind._CHANGE_IMAGE:
                case MsgKind.TEXT:
                case MsgKind.LINK:
                    msg = mNote.getMsg();
                    break;
                case MsgKind.CONTACT:
                    msg = "Sent a " + Msg.CHAT_CONTACT + ": " + mNote.getMsg();
                    break;
                case MsgKind.M1_IMAGE:
                    msg = "Sent an " + Msg.CHAT_IMAGE;
                    break;
                case MsgKind.M2_AUDIO:
                    msg = "Sent an " + Msg.CHAT_AUDIO;
                    break;
                case MsgKind.M3_VIDEO:
                    msg = "Sent a " + Msg.CHAT_VIDEO;
                    break;
                case MsgKind.M4_LONG_TEXT:
                    msg = "Sent a " + Msg.CHAT_LONG_TEXT;
                    break;
                case MsgKind.LOCATION:
                    msg = "Sent a " + Msg.CHAT_LOCATION + ": " + mNote.getMsg();
                    break;
                case MsgKind.ADDRESS:
                    FoursquareItem model = MsgAddressUtil.decodeModel(mNote.getMsg());
                    msg = "Sent an " + Msg.CHAT_ADDRESS + ": " + model.name + " | " + model.loc_address + " | " + Msg.CHAT_LOCATION + ": " + model.loc_lat + "," + model.loc_lng;
                    break;
            }
            builder.append(DateTime).append(" ").append(name).append(" ").append(msg).append("\r\n");
        }
        return builder.toString();
    }

    //___________________
    public ArrayList<ChatDetail> getChatDetail() {
        ArrayList<ChatDetail> chatDetailList = new ArrayList<>();
        RealmResults<Chats> results = realm.where(Chats.class).findAll();
        for (int i=0; i<results.size(); i++) {
            Chats mChat = results.get(i);
            ChatDetail chatDetail = new ChatDetail();
            chatDetail.setChatName(mChat.getChatName());
            chatDetail.setMsgCount(getChatNotesCount(mChat.getChatId()));
            chatDetailList.add(chatDetail);
        }
        return chatDetailList;
    }

    public LastBackupNote getLastBackupNote() {
        Notes mNote = getSingleNoteLatest();
        String msg = "";
        switch (mNote.getMsgKind()) {
            case MsgKind._CHANGE_NAME:
            case MsgKind._CHANGE_IMAGE:
            case MsgKind.TEXT:
            case MsgKind.LINK:
                msg = mNote.getMsg();
                break;
            case MsgKind.CONTACT:
                msg = Msg.CHAT_CONTACT + ": " + mNote.getMsg();
                break;
            case MsgKind.M1_IMAGE:
                msg = Msg.CHAT_IMAGE;
                break;
            case MsgKind.M2_AUDIO:
                msg = Msg.CHAT_AUDIO;
                break;
            case MsgKind.M3_VIDEO:
                msg = Msg.CHAT_VIDEO;
                break;
            case MsgKind.M4_LONG_TEXT:
                msg = Msg.CHAT_LONG_TEXT;
                break;
            case MsgKind.LOCATION:
                msg = Msg.CHAT_LOCATION + ": " + mNote.getMsg();
                break;
            case MsgKind.ADDRESS:
                FoursquareItem model = MsgAddressUtil.decodeModel(mNote.getMsg());
                msg = Msg.CHAT_ADDRESS + ": " + model.name + " | " + model.loc_address + " | " + Msg.CHAT_LOCATION + ": " + model.loc_lat + "," + model.loc_lng;
                break;
        }
        LastBackupNote lastBackupNote = new LastBackupNote();
        lastBackupNote.setChatName(getChatName(mNote.getChatId()));
        lastBackupNote.setText(msg);
        lastBackupNote.setTimestamp(mNote.getMsgTimestamp());
        return lastBackupNote;
    }


    //____________________________________________________________________________________________//
    //---------------------------------------INFORMATION------------------------------------------//
    //____________________________________________________________________________________________//
    // realm.createObject creates <index> 0
    // custom ID starts with 0
    // <index> of realm is not AUTOINCREMENT if you have (1,2,3,4,5) you delete (3) next will be inserted at (3) not (6)
    // <index> of realm  ==   # of sqlite
    // DEFAULT values (if you dont provide default values, realm will insert primitive defaults by itself [null for String], [0 for long])
    // fetching data of only one column (not possible in Realm) - https://stackoverflow.com/a/42645871
}
