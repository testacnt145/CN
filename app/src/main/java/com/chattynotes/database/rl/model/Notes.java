package com.chattynotes.database.rl.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.chattynotes.adapters.listConversation.model.InterfaceConversation;
import com.chattynotes.constant.ItemType;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.constant.MsgID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Notes extends RealmObject implements Parcelable, InterfaceConversation {

    public Notes() {
    }

    @PrimaryKey /* not obeying the definition of Primary key see Chats.java */
    private long noteId;                                                    //1
    /* noteId - unique key - incrementing from last(max) record present in database */
    /* used for sorting, because we cannot rely on timestamp due to system date */
    private long chatId;                                                    //2
    private String msg;                                                     //3
    private String msgId = MsgConstant.DEFAULT_MSG_ID();                      //4
    private int msgFlow = MsgConstant.DEFAULT_MSG_FLOW;                     //5
    private int msgStatus = MsgConstant.DEFAULT_MSG_STATUS;                 //6
    private int msgKind = MsgConstant.DEFAULT_MSG_KIND;                     //7
    private long msgTimestamp = MsgConstant.DEFAULT_MSG_TIMESTAMP();        //8
    private long msgTimestampDone = MsgConstant.DEFAULT_MSG_TIMESTAMP_DONE; //9
    private int msgStar = MsgConstant.DEFAULT_MSG_STAR;                     //10
    private int mediaStatus = MsgConstant.DEFAULT_MEDIA_STATUS;             //11
    private String mediaCaption = MsgConstant.DEFAULT_MEDIA_CAPTION;        //12
    private int mediaSize = MsgConstant.DEFAULT_MEDIA_SIZE;                 //13
    private int mediaDuration = MsgConstant.DEFAULT_MEDIA_DURATION;         //14

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getMsgFlow() {
        return msgFlow;
    }

    public void setMsgFlow(int msgFlow) {
        this.msgFlow = msgFlow;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public int getMsgKind() {
        return msgKind;
    }

    public void setMsgKind(int msgKind) {
        this.msgKind = msgKind;
    }

    public long getMsgTimestamp() {
        return msgTimestamp;
    }

    public void setMsgTimestamp(long msgTimestamp) {
        this.msgTimestamp = msgTimestamp;
    }

    public long getMsgTimestampDone() {
        return msgTimestampDone;
    }

    public void setMsgTimestampDone(long msgTimestampDone) {
        this.msgTimestampDone = msgTimestampDone;
    }

    public int getMsgStar() {
        return msgStar;
    }

    public void setMsgStar(int msgStar) {
        this.msgStar = msgStar;
    }

    public int getMediaStatus() {
        return mediaStatus;
    }

    public void setMediaStatus(int mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public String getMediaCaption() {
        return mediaCaption;
    }

    public void setMediaCaption(String mediaCaption) {
        this.mediaCaption = mediaCaption;
    }

    public int getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(int mediaSize) {
        this.mediaSize = mediaSize;
    }

    public int getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(int mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

//__________________________________________________________________________________________________ Parcelable
    public Notes(Parcel in) {
        chatId = in.readLong();
        msg = in.readString();
        msgId = in.readString();
        msgFlow = in.readInt();
        msgStatus = in.readInt();
        msgKind = in.readInt();
        msgTimestamp = in.readLong();
        msgTimestampDone = in.readLong();
        msgStar = in.readInt();

        mediaStatus = in.readInt();
        mediaCaption = in.readString();
        mediaSize = in.readInt();
        mediaDuration = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(chatId);
        dest.writeString(msg);
        dest.writeString(msgId);
        dest.writeInt(msgFlow);
        dest.writeInt(msgStatus);
        dest.writeInt(msgKind);
        dest.writeLong(msgTimestamp);
        dest.writeLong(msgTimestampDone);
        dest.writeInt(msgStar);
        dest.writeInt(mediaStatus);
        dest.writeString(mediaCaption);
        dest.writeInt(mediaSize);
        dest.writeInt(mediaDuration);
    }

    //@SuppressWarnings("unused")
    public static final Parcelable.Creator<Notes> CREATOR = new Parcelable.Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

//__________________________________________________________________________________________________ Parcelable
    @Override
    public String itemType() {
        return ItemType.CONVERSATION_NOTE;
    }
}
