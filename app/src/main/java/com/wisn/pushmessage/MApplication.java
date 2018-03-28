package com.wisn.pushmessage;

import android.app.Application;

import com.wisn.pushmessage.mipush.MiPushCom;

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
        MiPushCom  miPushCom=new MiPushCom();
        miPushCom.init(this,getResources().getString(R.string.APP_ID),getResources().getString(R.string.APP_KEY));
//        HMSAgent.init(this);
    }
}
