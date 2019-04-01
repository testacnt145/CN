package com.chattynotes.mvp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.chattynoteslite.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.keyboard.Keyboard;
import com.chattynotes.mvp.presenter.HelpPresenter;
import com.chattynotes.mvp.repository.JavaStringRepository;
import com.chattynotes.mvp.repository.data.HelpOptions;
import com.chattynotes.mvp.view.HelpView;

public class Help extends AppCompatActivity implements HelpView, OnItemClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);
		mvp();
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
//__________________________________________________________________________________________________
	void mvp() {
		HelpView helpView = this;
		JavaStringRepository helpRepository = new HelpOptions();
		HelpPresenter presenter = new HelpPresenter(helpView, helpRepository);
		presenter.loadHelpOption();
	}

	@Override
	public void displayHelpOptions(String[] helpOptions) {
		ListView listview = (ListView) findViewById(R.id.list_help);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, helpOptions);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent in;
		switch(position) {
			case 0:
				in = new Intent(this, WebViewActivity.class);
				in.putExtra(IntentKeys.URL, Keyboard.baseUrl_how_to_use.getValue());
				startActivity(in);
				break;
			case 1:
				in = new Intent(this, WebViewActivity.class);
				in.putExtra(IntentKeys.URL, Keyboard.baseUrl_privacy_policy.getValue());
				startActivity(in);
				break;
		}
	}

}