package com.chattynotes.mvp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.chattynoteslite.R;
import com.chattynotes.constant.IntentKeys;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView browser = (WebView) findViewById(R.id.webview);
        String url = getIntent().getStringExtra(IntentKeys.URL);
        if(browser != null)
            browser.loadUrl(url);
    }
}
