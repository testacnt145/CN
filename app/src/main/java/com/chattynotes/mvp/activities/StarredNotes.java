package com.chattynotes.mvp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.chattynotes.R;
import com.chattynotes.database.rl.QueryNotesDB;
import com.chattynotes.database.rl.model.Notes;
import com.chattynotes.database.rl.util.DBUtil;
import com.chattynotes.lazylistPath.ImageLoaderPath;
import com.chattynotes.adapters.listConversation.AdapterConversation;
import com.chattynotes.adapters.listConversation.model.ConversationUnknownItem;
import com.chattynotes.adapters.listConversation.model.InterfaceConversation;
import java.util.ArrayList;
import io.realm.Realm;

public class StarredNotes extends AppCompatActivity {

    ListView listview = null;
    AdapterConversation adapter;

    ArrayList<InterfaceConversation> conversationList = new ArrayList<>();
    ImageLoaderPath imageLoaderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_notes);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_starred_notes);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //---------------------------->>> DATABASE, PREFERENCE, INTENTS
        Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
        QueryNotesDB realmQuery = new QueryNotesDB(realm);

        //---------------------------->>> LIST
        //notes
        long chatIDChanged = 0;
        String chatName;
        ArrayList<InterfaceConversation> tempList = realmQuery.getStarredMessage();
        for (int i = 0; i < tempList.size(); i++) {
            Notes note = (Notes) tempList.get(i);
            //save first number
            if(i==0) {
                chatIDChanged = note.getChatId();
                chatName = realmQuery.getChatName(chatIDChanged);
                conversationList.add(new ConversationUnknownItem("\uD83C\uDF1F Starred notes of " + chatName));
            }
            //look up for number changed
            if(note.getChatId() != chatIDChanged) {
                chatIDChanged = note.getChatId();
                chatName = realmQuery.getChatName(chatIDChanged);
                conversationList.add(new ConversationUnknownItem("\uD83C\uDF1F Starred notes of " + chatName));
            }
            conversationList.add(tempList.get(i));
        }
        //---------------------------->>> LISTVIEW
        imageLoaderList = new ImageLoaderPath(this);
        listview = (ListView) findViewById(R.id.list_conversation);
        adapter = new AdapterConversation(this, conversationList, imageLoaderList, realmQuery);
        listview.setAdapter(adapter);
        realm.close();
    }


//______________________________________________________________________________________________________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_starred_notes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    finish();
                return false;
            case R.id.menu_unstar_all:
                Realm realm = Realm.getInstance(DBUtil.getRealmConfiguration());
                QueryNotesDB realmQuery = new QueryNotesDB(realm);
                realmQuery.unstarAllMessages();
                realm.close();
                //remove from conversation list and update
                conversationList.clear();
                adapter.notifyDataSetChanged();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
