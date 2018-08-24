package com.wisn.pushmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wisn.pushmessage.jpush.MyReceiver;


public class MainActivity extends AppCompatActivity {
    public static final String INFO_REQUEST_ACTION = "com.laiyifen.login.INFO_REQUEST_ACTION";
    public static final String INFO_RESPONSE_ACTION = "com.laiyifen.login.INFO_RESPONSE_ACTION";
    public static boolean isForeground = false;
    public static final String KEY_EXTRAS = "extras";
    private TextView textView;
    private TokenResponseReceiverd tokenResponseReceiverd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        try {
            getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void getToken() {
        //注册token 结果回调监听
        tokenResponseReceiverd = new TokenResponseReceiverd();
        IntentFilter intentFilter = new IntentFilter(INFO_RESPONSE_ACTION);
        this.registerReceiver(tokenResponseReceiverd, intentFilter);
    }

    public  class TokenResponseReceiverd extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
           if (INFO_RESPONSE_ACTION.equals(intent.getAction())) {
                String token = intent.getStringExtra(MyReceiver.TAG);
                if(textView!=null){
                    textView.setText(token);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(tokenResponseReceiverd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
