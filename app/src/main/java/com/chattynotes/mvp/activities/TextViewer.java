package com.chattynotes.mvp.activities;

import com.chattynotes.R;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgConstant;
import com.chattynotes.util.storage.PathUtil;
import com.chattynotes.util.textformatter.TextFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

public class TextViewer extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_viewer);
		String msg 	= getIntent().getStringExtra(IntentKeys.MSG);
		int msgFlow	= getIntent().getIntExtra(IntentKeys.MSG_FLOW, MsgConstant.DEFAULT_MSG_FLOW);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_text_viewer);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
			//actionBar.hide(); //instead of hiding show transparent
		}
		readFile(msgFlow, msg);
	}

	void readFile(int msgFlow, String msg) {
		File f = PathUtil.getExternalMediaTextFile(msgFlow, msg);
		if (f.exists()) {
			try {
				BufferedReader fileReader = new BufferedReader(new FileReader(f));
			    StringBuilder strBuilder = new StringBuilder();
			    String line;
			    while((line = fileReader.readLine()) != null) {
			        strBuilder.append(line);
			        strBuilder.append("\n");
			    }
			    fileReader.close();
			    //strBuilder.trimToSize();
			    String contentsOfFile = strBuilder.toString();
				TextView txtView = (TextView)findViewById(R.id.txtView_LongText);
				TextFormatter.applyFormatting(txtView, contentsOfFile);
			} catch(Exception e) {
				finish();
			}
		} else {
			finish();
		}
	}


//__________________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
