package com.wisn.pushmessage.mipush;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by mac on 2018/3/28.
 */

public class MiPushCom {

    private   boolean shouldInit(Application application) {
        ActivityManager am = ((ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = application.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
    public  void init(Application application,String APP_ID,String APP_KEY){
        if (shouldInit(application)) {
            MiPushClient.registerPush(application, APP_ID, APP_KEY);
        }
    }

}
