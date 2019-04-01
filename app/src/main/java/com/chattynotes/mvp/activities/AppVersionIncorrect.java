package com.chattynotes.mvp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.chattynoteslite.R;

public class AppVersionIncorrect extends AppCompatActivity {
    //both cases [obsolete or delete]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_version_incorrect);
    }

}
