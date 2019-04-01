package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.util.AppUtil;
import com.chattynotes.util.storage.PathUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatInfo extends AppCompatActivity {
	//From (1) : Conversation

	String chatName;
	long chatID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_info);
		Bundle infoBundle = getIntent().getBundleExtra(IntentKeys.BUNDLE_INFO);
		if (infoBundle != null) {
			chatName = infoBundle.getString(IntentKeys.CHAT_NAME);
			chatID = infoBundle.getLong(IntentKeys.CHAT_ID);
		}
		//image (display from internal)
		ImageView imgView = (ImageView) findViewById(R.id.image);
		if(AppUtil.isChatWithChattyNotes(chatID))
			imgView.setImageResource(R.mipmap.icon_hd);
		else {
			if ((PathUtil.getInternalChatImageFile(String.valueOf(chatID)).exists())) {
				ImageLoaderPath imageLoader = new ImageLoaderPath(this);
				String external = PathUtil.getExternalChatImageUri(String.valueOf(chatID)).toString();
				String internal = PathUtil.getInternalChatImageUri(String.valueOf(chatID)).toString();
				imageLoader.displayImageElseThumb(external, 0, imgView, internal, 300, 300);
			} else
				imgView.setImageResource(R.drawable.avatar_large);
		}
		imgView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in = new Intent(ChatInfo.this, ChatImageViewer.class);
				Bundle infoBundle =  new Bundle();
				infoBundle.putString(IntentKeys.CHAT_NAME, chatName);
				infoBundle.putLong(IntentKeys.CHAT_ID, chatID);
				in.putExtra(IntentKeys.BUNDLE_INFO, infoBundle);
				startActivity(in);
				finish();
			}
		});
		//chat name
		TextView txtViewName = (TextView) findViewById(R.id.name);
		txtViewName.setText(chatName);
		txtViewName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!AppUtil.isChatWithChattyNotes(chatID)) {
					Intent in = new Intent(ChatInfo.this, ChangeChatName.class);
					Bundle infoBundle = new Bundle();
					infoBundle.putString(IntentKeys.CHAT_NAME, chatName);
					infoBundle.putLong(IntentKeys.CHAT_ID, chatID);
					in.putExtra(IntentKeys.BUNDLE_INFO, infoBundle);
					startActivity(in);
					finish();
				}
			}
		});
		//hint
		if(AppUtil.isChatWithChattyNotes(chatID))
			findViewById(R.id.hint).setVisibility(View.GONE);
	}
}
