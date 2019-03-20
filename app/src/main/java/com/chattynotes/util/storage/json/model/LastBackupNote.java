package com.chattynotes.util.storage.json.model;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.chattynotes.util.DateUtil;

public class LastBackupNote {

    private String chatName;
    private String text;
    private long timestamp;

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    static SpannableStringBuilder getLastBackupNote(LastBackupNote lastBackupNote) {
        SpannableString s1 = new SpannableString("Chat name: ");
        SpannableString s2 = new SpannableString("\nNote: ");
        SpannableString s3 = new SpannableString("\nTime: ");
        // set the style
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s1.setSpan(new StyleSpan(Typeface.BOLD), 0, s1.length(), flag);
        s2.setSpan(new StyleSpan(Typeface.BOLD), 0, s2.length(), flag);
        s3.setSpan(new StyleSpan(Typeface.BOLD), 0, s3.length(), flag);

        // build the string
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s1);
        builder.append(lastBackupNote.getChatName());
        builder.append(s2);
        builder.append(lastBackupNote.getText());
        builder.append(s3);
        builder.append(DateUtil.getDateInStringComplete(lastBackupNote.getTimestamp()));
        return builder;
    }
}
