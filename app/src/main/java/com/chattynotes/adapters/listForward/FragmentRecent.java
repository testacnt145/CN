package com.chattynotes.adapters.listForward;

import com.chattynotes.adapters.listForward.model.InterfaceForward;
import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.mvp.activities.Forward;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MimeType;
import com.chattynotes.util.storage.PathUtil;
import java.util.ArrayList;
import java.util.Collections;
import com.chattynotes.mvp.activities.MainActivity;
import com.chattynoteslite.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;

public class FragmentRecent extends Fragment implements OnItemClickListener{

	ArrayList<InterfaceForward> recentList = new ArrayList<>();
	ListView listview = null;
	AdapterForwardTabs adapter;
	
	//only for shared message
	Boolean isSharedMessage;
	Bundle sharedBundle;

	public FragmentRecent() {
		// Required empty public constructor
	}

	//RELEASE_BUILD__
	@SuppressLint("ValidFragment")
	FragmentRecent(ArrayList<InterfaceForward> _recentList, Boolean _isSharedMessage, Bundle _shared_bundle) {
		recentList = _recentList;
		isSharedMessage = _isSharedMessage;
		sharedBundle = _shared_bundle;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//setHasOptionsMenu(true); //for searching

		View rootView = inflater.inflate(R.layout.fragment_forward, container, false);
		listview = (ListView) rootView.findViewById(R.id.list_forward);
    	adapter = new AdapterForwardTabs(getActivity(), recentList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		//sort
		Collections.sort(recentList, Collections.reverseOrder(new ComparatorForwardTime()));
		adapter.notifyDataSetChanged();

		//Scrolling in TabLayout issue
		//https://stackoverflow.com/a/42304784
		ViewCompat.setNestedScrollingEnabled(listview, true);

		return rootView;
	}
	
//______________________________________________________________________________________________________________
	//http://stackoverflow.com/questions/15653737/oncreateoptionsmenu-inside-fragments
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.menu__search, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	    
	    // Associate search able configuration with the SearchView
	    SearchManager searchManager = (SearchManager) getActivity().getSystemService(Forward.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
	    SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
               adapter.getFilter().filter(query);
               return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
	}
//______________________________________________________________________________________________________________
	@Override
	public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
		final InterfaceForward item = (InterfaceForward)parent.getAdapter().getItem(position);
		final Chats chatItem = (Chats) item;
		if(isSharedMessage) 
			shareMessage(chatItem.getChatName(), chatItem.getChatId());
		else
			forwardMessage(chatItem.getChatName(), chatItem.getChatId());
	}
	
//_____________________________________________________________________________________________________
	void forwardMessage(final String chatName, final long chatID) {
		//custom layout
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View custom_view = inflater.inflate(R.layout.dialog_text, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
		final TextView txtView = (TextView) custom_view.findViewById(R.id.dialog_text_view);
				
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.setView(custom_view)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	Intent in = new Intent();
		    	in.putExtra(IntentKeys.CHAT_NAME, chatName);
				in.putExtra(IntentKeys.CHAT_ID, chatID);
				getActivity().setResult(Activity.RESULT_CANCELED, in);
				getActivity().finish();
		   }})
		.setNegativeButton(android.R.string.no, null)
		.create();
		String text = String.format(getString(R.string.forward_to), chatName);
		txtView.setText(text);
		alertDialog.show();	
	}
	
	void shareMessage(final String chatName, final long chatID) {
		//custom layout
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View custom_view = inflater.inflate(R.layout.dialog_text, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
		final TextView txtView = (TextView) custom_view.findViewById(R.id.dialog_text_view);
				
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.setView(custom_view)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
				//use Main Activity instead of Chats,
				//because it is highly possible that Application is not Registered
				if(sharedBundle.containsKey(IntentKeys.SHARE_MSG_TEXT) ||
						sharedBundle.containsKey(IntentKeys.SHARE_MSG_IMAGE_URI) ||
							sharedBundle.containsKey(IntentKeys.MEDIA_VIDEO_URI) ) {
					Intent in = new Intent(getActivity(), MainActivity.class);
					//putting everything into bundle
					sharedBundle.putLong(IntentKeys.CHAT_ID, chatID);
					String mediaType=MimeType.MEDIA_UNKNOWN;
					if(sharedBundle.containsKey(IntentKeys.SHARE_MSG_IMAGE_URI))
						mediaType = MimeType.MEDIA_IMAGE;
					else if(sharedBundle.containsKey(IntentKeys.MEDIA_VIDEO_URI))
						mediaType = MimeType.MEDIA_VIDEO;
					//select different mediaName because, same media_name will cause same Input and Output path which will result in error in FFmpeg
					String mediaName = PathUtil.generateFileNameUnix(mediaType);
					sharedBundle.putString(IntentKeys.MEDIA_NAME, mediaName);
					//since sharedBundle already has text/image/video, putting bundle into intent
					in.putExtra(IntentKeys.BUNDLE_SHARED, sharedBundle);
					in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
				}
				getActivity().finish();
		   }})
	   .setNegativeButton(android.R.string.no, null)
	   .create();
		String text = String.format(getString(R.string.share_with), chatName);
		txtView.setText(text);
		alertDialog.show();
	}
}