package com.chattynotes.mvp.activities;

import java.util.ArrayList;
import com.chattynoteslite.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.ItemType;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.adapters.listChat.AdapterChat;
import com.chattynotes.adapters.listChat.model.InterfaceChat;
import com.chattynotes.adapters.listChat.model.StringChatItem;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.util.DBUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;

import io.realm.Realm;

public class EmailChat extends AppCompatActivity implements OnItemClickListener {
//From (1):	ChatSettings
	
	ListView listview = null;
	AdapterChat adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_chat);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_email_chat);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		ArrayList<InterfaceChat> emailList = new ArrayList<>();
		Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
		QueryNotesDB realmQuery = new QueryNotesDB(realm);
		emailList.addAll(realmQuery.getChatList());
		realm.close();
		
		if(emailList.isEmpty())
			emailList.add(new StringChatItem(getString(R.string.no_chats_found)));
		
		listview = (ListView) findViewById(R.id.list_email_chat);
		adapter = new AdapterChat(this, emailList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		InterfaceChat item = (InterfaceChat)arg0.getAdapter().getItem(position);
		if (item.itemType().equals(ItemType.CHAT)) {
			Intent in = new Intent();
			Chats chat = (Chats) item;
			in.putExtra(IntentKeys.CHAT_ID, chat.getChatId());
			in.putExtra(IntentKeys.CHAT_NAME, chat.getChatName());
			setResult(RESULT_OK, in);
			finish();
		}
	}
}
