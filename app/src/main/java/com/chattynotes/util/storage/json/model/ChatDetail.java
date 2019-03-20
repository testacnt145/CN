package com.chattynotes.util.storage.json.model;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import java.util.ArrayList;

public class ChatDetail {

    private String chatName;
    private int msgCount;

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    private int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

//__________________________________________________________________________________________________
    private static final String CLOSING_BRACKET = ")";
    private static final String OPENING_BRACKET = "(";
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";
    static SpannableStringBuilder getChatDetail(ArrayList<ChatDetail> chatDetailList) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i=0; i<chatDetailList.size(); i++) {
            ChatDetail chatDetail = chatDetailList.get(i);
            //bold name
            SpannableString s1 = new SpannableString(chatDetail.getChatName());
            SpannableString s2 = new SpannableString(Integer.toString(chatDetail.getMsgCount()));
            int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
            s1.setSpan(new StyleSpan(Typeface.BOLD), 0, s1.length(), flag);
            s2.setSpan(new StyleSpan(Typeface.BOLD), 0, s2.length(), flag);
            builder.append(Integer.toString(i+1));//1
            builder.append(CLOSING_BRACKET);//)
            builder.append(SPACE);//
            builder.append(s1);//Home
            builder.append(SPACE);//
            builder.append(OPENING_BRACKET);//(
            builder.append(s2);//45
            builder.append(SPACE);//
            builder.append(chatDetail.getMsgCount()==1?"note":"notes");//notes
            builder.append(CLOSING_BRACKET);//)
            builder.append(NEW_LINE);
            //1) Home (45 notes)
        }
        return builder;
    }
}
