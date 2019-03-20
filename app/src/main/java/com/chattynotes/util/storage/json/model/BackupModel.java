package com.chattynotes.util.storage.json.model;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.chattynotes.constant.BackupType;
import com.chattynotes.util.DateUtil;
import java.util.ArrayList;

public class BackupModel {

    //[refactor_: if you refactor these variables,]
    //info file will get corrupt
    //causing database backup to fail
    private int type;
    private int versionCode;
    private int chatCount;
    private int msgCount;
    private long backupTimestamp;
    private ArrayList<ChatDetail> chatDetails;
    private LastBackupNote lastBackupNote;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getChatCount() {
        return chatCount;
    }

    public void setChatCount(int chatCount) {
        this.chatCount = chatCount;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public long getBackupTimestamp() {
        return backupTimestamp;
    }

    public void setBackupTimestamp(long backupTimestamp) {
        this.backupTimestamp = backupTimestamp;
    }

    private ArrayList<ChatDetail> getChatDetails() {
        return chatDetails;
    }

    public void setChatDetails(ArrayList<ChatDetail> chatDetails) {
        this.chatDetails = chatDetails;
    }

    private LastBackupNote getLastBackupNote() {
        return lastBackupNote;
    }

    public void setLastBackupNote(LastBackupNote lastBackupNote) {
        this.lastBackupNote = lastBackupNote;
    }

    //______________________________________________________________________________________________

    //https://stackoverflow.com/a/41953808/4754141
    public static SpannableStringBuilder getBackupModelDetail(BackupModel backupModel) {
        SpannableString s1 = new SpannableString("Type: ");
        SpannableString s2 = new SpannableString("\nApp Version: ");
        SpannableString s3 = new SpannableString("\nBackup date: ");
        SpannableString s4 = new SpannableString("\nTotal chats: ");
        SpannableString s5 = new SpannableString("\nTotal notes: ");
        SpannableString s6 = new SpannableString("\n\nChat details: \n");
        SpannableString s7 = new SpannableString("\nLast backup note: \n");
        // set the style
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s1.setSpan(new StyleSpan(Typeface.BOLD), 0, s1.length(), flag);
        s2.setSpan(new StyleSpan(Typeface.BOLD), 0, s2.length(), flag);
        s3.setSpan(new StyleSpan(Typeface.BOLD), 0, s3.length(), flag);
        s4.setSpan(new StyleSpan(Typeface.BOLD), 0, s4.length(), flag);
        s5.setSpan(new StyleSpan(Typeface.BOLD), 0, s5.length(), flag);
        s6.setSpan(new StyleSpan(Typeface.BOLD), 0, s6.length(), flag);
        s6.setSpan(new UnderlineSpan(), 0, s6.length(), flag);
        s7.setSpan(new StyleSpan(Typeface.BOLD), 0, s7.length(), flag);
        s7.setSpan(new UnderlineSpan(), 0, s7.length(), flag);

        // build the string
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s1);
        builder.append(getType(backupModel.getType()));
        builder.append(s2);
        builder.append(Integer.toString(backupModel.getVersionCode()));
        builder.append(s3);
        builder.append(DateUtil.getDateInStringComplete(backupModel.getBackupTimestamp()));
        builder.append(s4);
        builder.append(Integer.toString(backupModel.getChatCount()));
        builder.append(s5);
        builder.append(Integer.toString(backupModel.getMsgCount()));
        builder.append(s6);
        builder.append(ChatDetail.getChatDetail(backupModel.getChatDetails()));
        builder.append(s7);
        builder.append(LastBackupNote.getLastBackupNote(backupModel.getLastBackupNote()));

        return builder;

//        return "<b>Type: </b>Local backup<br/>" +
//            "<b>App Version: </b>"+ backupModel.getVersionCode() + "<br/>" +
//            "<b>Backup date: </b>"+ backupModel.getBackupTimestamp() + "<br/>" +
//            "<b>Total chats: </b>"+ backupModel.getChatCount() + "<br/>" +
//            "<b>Total notes: </b>"+ backupModel.getMsgCount() + "<br/>" +
//            "<b><u>Chat details:</u></b><br/>" + ChatDetail.getChatDetail(backupModel.getChatDetails()) + "<br/>" +
//            "<b><u>Last backup note:</u></b><br/>" + LastBackupNote.getLastBackupNote(backupModel.getLastBackupNote());
    }

    public static SpannableStringBuilder getBackupModel(BackupModel backupModel) {
        SpannableString s1 = new SpannableString("App Version: ");
        SpannableString s2 = new SpannableString("\nBackup date: ");
        SpannableString s3 = new SpannableString("\nTotal chats: ");
        SpannableString s4 = new SpannableString("\nTotal notes: ");
        // set the style
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s1.setSpan(new StyleSpan(Typeface.BOLD), 0, s1.length(), flag);
        s2.setSpan(new StyleSpan(Typeface.BOLD), 0, s2.length(), flag);
        s3.setSpan(new StyleSpan(Typeface.BOLD), 0, s3.length(), flag);
        s4.setSpan(new StyleSpan(Typeface.BOLD), 0, s4.length(), flag);
        // build the string
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s1);
        builder.append(Integer.toString(backupModel.getVersionCode()));
        builder.append(s2);
        builder.append(DateUtil.getDateInStringComplete(backupModel.getBackupTimestamp()));
        builder.append(s3);
        builder.append(Integer.toString(backupModel.getChatCount()));
        builder.append(s4);
        builder.append(Integer.toString(backupModel.getMsgCount()));
        return builder;
    }

    static String getType(int type) {
        switch (type) {
            case BackupType.LOCAL:
                return "Local backup";
            case BackupType.DRIVE:
                return "Google drive backup";
            case BackupType.ENCRYPTED:
                return "Encrypted backup";
        }
        return "";
    }
}
