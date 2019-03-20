package com.chattynotes.util;

import com.chattynotes.constant.IntentKeys;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.chattynotes.mvp.activities.Conversation;
import com.chattynotes.R;

public class PasswordUtil {
	
//___________________________________________________________________________________________________________
	public static void verifyPassword(final Context ctx, final Bundle conversationBundle, final Boolean finishTheCallingActivity) {
    	
		final String _number = conversationBundle.getString(IntentKeys.CHAT_ID);
		if(true) // not encrypted
    		openConversation(ctx, conversationBundle, finishTheCallingActivity);
    	else {
    		AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
    		alert.setTitle("Enter password");
    		alert.setMessage("If you forgot the password, you can decrypt chat back!");
			final View view = View.inflate(ctx, R.layout.dialog_password, null);
			final EditText input = (EditText)view.findViewById(R.id.eT_password);
			final CheckBox cBox = (CheckBox)view.findViewById(R.id.cBox_password);
			cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
					} else {
						input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
				}
			});
			alert.setView(view);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
					String storePassword = "ty"; //PrefsEncrypt.getChatEncrypted(_number, ctx);
					String enterPassword = input.getText().toString();
					if(enterPassword.equals(storePassword))
						openConversation(ctx, conversationBundle, finishTheCallingActivity);
					else
						Toast.makeText(ctx, "Wrong password", Toast.LENGTH_SHORT).show();
				}
			});
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
			alert.show();
    	}
    }
	
	private static void openConversation(Context ctx, Bundle conversationBundle, Boolean finishTheCallingActivity) {
		//[jugaar_: NOTIFICATION]
		//if you have 5 message in Conversation1, 5 message in Conversation2
		//you move to Conversation1 , (notification bar will be clear)
		//and now you get msg in Conversation2, you will get following message in notice bar
		//(1 Message in Conversation2) but in actual you should have get (6 Messages in Conversation2)
		Intent in = new Intent(ctx, Conversation.class);
		in.putExtra(IntentKeys.BUNDLE_CONVERSATION, conversationBundle);
		ctx.startActivity(in);
		if(finishTheCallingActivity)
			((Activity)ctx).finish();
	}

}
