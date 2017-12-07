package com.wisn.pushmessage;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wisn on 2017/12/7.
 */

public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
