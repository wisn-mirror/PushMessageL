package com.wisn.pushmessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {
    public static boolean isForeground = false;
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
