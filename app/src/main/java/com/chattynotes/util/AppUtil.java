package com.chattynotes.util;

import android.app.Activity;
import android.content.Context;
import com.chattynotes.application.App;
import com.chattynotes.mvp.activities.Conversation;
import com.chattynotes.mvp.activities.MainActivity;
import com.chattynotes.constant.AppConst;
import com.chattynotes.constant.ChatID;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.preferences.Prefs;

import java.util.UUID;

public class AppUtil {

    public static Boolean isChatWithChattyNotes(long chatID) {
        return chatID == ChatID.CHATTY_NOTES;
    }

    //______________________________________________________________________________________________________________ Registration
    //Use at CameraActivity
    //To make sure user has fetched all groups,contacts and everything, and is able to send a chat message
    public static Boolean isRegisteredAndOnChatScreen() {
        return Prefs.getStage(App.applicationContext) >= MainActivity.STAGE_CHAT;
    }

    public static Boolean isUserActiveNotification(long chatID) {
        return (AppConst.ACTIVE_WINDOW.equals(IntentKeys.CONVERSATION_STATE_RESUMED) && Conversation.CONVERSATION_ID == chatID);
    }

    public static boolean isUserOrGroupActiveMessageRead(long chatID) {
        return Conversation.CONVERSATION_ID == chatID;
    }

    //FAKE
    /*public static Boolean isUserActiveServerReceipt() {
		return Conversation.ACTIVE_WINDOW.equals(ApplicationLoader.applicationContext.getResources().getString(R.string.conversation_resume));
	}*/

    public static boolean isMsgTextOrLongTextOrLink(int msgKind) {
        switch (msgKind) {
            case MsgKind.TEXT:
            case MsgKind.M4_LONG_TEXT:
            case MsgKind.LINK:
                return true;
        }
        return false;
    }

    public static boolean isMsgHasMediaCaption(int msgKind) {
        switch (msgKind) {
            case MsgKind.M1_IMAGE:
            case MsgKind.M3_VIDEO:
            case MsgKind.M4_LONG_TEXT:
                return true;
        }
        return false;
    }

    public static boolean isMsgMedia(int msgKind) {
        switch (msgKind) {
            case MsgKind.M1_IMAGE:
            case MsgKind.M2_AUDIO:
            case MsgKind.M3_VIDEO:
            case MsgKind.M4_LONG_TEXT:
                return true;
        }
        return false;
    }

    public static boolean isMsgMediaExceptVideo(int msgKind) {
        switch (msgKind) {
            case MsgKind.M1_IMAGE:
            case MsgKind.M2_AUDIO:
            case MsgKind.M4_LONG_TEXT:
                return true;
        }
        return false;
    }


    //this method is also used in AdapterConversation for bubble date
    public static boolean isMsgHasThumb(int msgKind) {
        switch (msgKind) {
            case MsgKind.M1_IMAGE:
            case MsgKind.M3_VIDEO:
                return true;
        }
        return false;
    }

   //______________________________________________________________________________________________________________ OTHER
    public static void deleteAccount(Context _ctx) {
        AndServiceUtil.stopServiceApp(_ctx);

        //update preferences
        Prefs.putStage(_ctx, MainActivity.STAGE_FIRST_SCREEN);

        //We are not resetting these as the user may come back also, so we will require his acCount for Re-connection
        //Prefs.putAccountCount(DeleteAccount.this, PDefaultValue.DEFAULT_ACCOUNT_COUNT);
        //Prefs.putNick(DeleteAccount.this, PDefaultValue.DEFAULT_NICK);
        //Prefs.putStatus(DeleteAccount.this, PDefaultValue.DEFAULT_STATUS);
        //Prefs.putPin(DeleteAccount.this, PDefaultValue.DEFAULT_PIN);

        //start Registration activity
        AndIntentUtil.restartApp(_ctx);

        //finish current activity
        ((Activity) _ctx).finish();
    }
}
