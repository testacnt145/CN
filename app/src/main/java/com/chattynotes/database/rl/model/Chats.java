package com.chattynotes.database.rl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chattynotes.adapters.listChat.model.InterfaceChat;
import com.chattynotes.adapters.listForward.model.InterfaceForward;
import com.chattynotes.constant.ChatConstant;
import com.chattynotes.constant.ItemType;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Chats extends RealmObject implements Parcelable, InterfaceChat, InterfaceForward {
    //Parcelable (for passing intent)
    //Copy Constructor (for copying/assigning) https://stackoverflow.com/a/869078

    public Chats() {
    }


    //https://stackoverflow.com/a/869078
    public Chats(Chats copy) {
        this.chatId = copy.getChatId();
        this.chatName = copy.getChatName();
        this.timestamp = copy.getTimestamp();
        this.draft = copy.getDraft();
        this.passwordStatus = copy.getPasswordStatus();
        this.password = copy.getPassword();
        this.passwordHint = copy.getPasswordHint();
        this.passwordCode = copy.getPasswordCode();
        this.note = copy.getNote();
    }

    @PrimaryKey
    /*
        not obeying the definition of Primary key + Auto Increment,
        as Primary key + Auto Increment should always generate 2 when 1 is deleted
        see bug below
    */
    private long chatId;                                                //1
    /* chatId - unique key - incrementing from last(max) record present in database */
    /*
        /*[bug_: issue with AUTO-INCREMENT on deleting]
            you create 'ONE'(chatId=1) with chat image [MAX chatId = 1]
            you deleted 'ONE' [MAX chatId = 0]
            you created 'TWO'(chatId will be 1 again),
            if you do not delete chat image of 'ONE', it will be reused by 'TWO'
            it means you have to delete everything that 'ONE'(chatId=1) uses when deleting chat
        */
     /*
        because Realm does not support unique check, we have to do it manually everytime,
        and Since it is Primary Key, therefore Realm will raise exception, if key is used twice,
        but we have to manually make sure its uniqueness by java
     */
    private String chatName;                                                //2
    private long timestamp = ChatConstant.DEFAULT_CHAT_TIMESTAMP();     //3
    private String draft = ChatConstant.DEFAULT_DRAFT;                  //4
    private int passwordStatus = ChatConstant.DEFAULT_PASSWORD_STATUS;  //5
    private String password = ChatConstant.DEFAULT_PASSWORD;            //6
    private String passwordHint = ChatConstant.DEFAULT_PASSWORD_HINT;   //7
    private long passwordCode = ChatConstant.DEFAULT_PASSWORD_CODE;     //8
    private Notes note;                                                 //9 last note

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public int getPasswordStatus() {
        return passwordStatus;
    }

    public void setPasswordStatus(int passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public long getPasswordCode() {
        return passwordCode;
    }

    public void setPasswordCode(long passwordCode) {
        this.passwordCode = passwordCode;
    }

    public Notes getNote() {
        return note;
    }

    public void setNote(Notes note) {
        this.note = note;
    }

    //______________________________________________________________________________________________
    @Override
    public String itemType() {
        return ItemType.CHAT;
    }

    @Override
    public long getLastNoteTimestamp() {
        //[bug_: NPE_| possibility]
        return note.getMsgTimestamp();
    }

    //__________________________________________________________________________________________________ Parcelable
    public Chats(Parcel in) {
        note = in.readParcelable(Notes.class.getClassLoader()); //place first
        chatId = in.readLong();
        chatName = in.readString();
        timestamp = in.readLong();
        draft = in.readString();
        passwordStatus = in.readInt();
        password = in.readString();
        passwordHint = in.readString();
        passwordCode = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(note, flags); //place first
        dest.writeLong(chatId);
        dest.writeString(chatName);
        dest.writeLong(timestamp);
        dest.writeString(draft);
        dest.writeInt(passwordStatus);
        dest.writeString(password);
        dest.writeString(passwordHint);
        dest.writeLong(passwordCode);
    }

    //@SuppressWarnings("unused")
    public static final Parcelable.Creator<Chats> CREATOR = new Parcelable.Creator<Chats>() {
        @Override
        public Chats createFromParcel(Parcel in) {
            return new Chats(in);
        }

        @Override
        public Chats[] newArray(int size) {
            return new Chats[size];
        }
    };


}
