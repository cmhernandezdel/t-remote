package com.example.tremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class KeylogActivity extends AppCompatActivity {

    // TODO: implement keylogger and onExit notify server (need its IP address)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keylog);
    }
}
