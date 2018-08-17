package com.wisn.pushmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

/**
 * Created by wisn on 2017/12/11.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "MainActivity";

    @Override
    public void onReceive(Context context, Intent intent) {
        Date nowTime = new Date(System.currentTimeMillis());
        Log.e(TAG, " onReceive 时间：" + nowTime);
//        Toast.makeText(context,""+nowTime,Toast.LENGTH_SHORT).show();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmUtils.setAlarmTime(context, SystemClock.elapsedRealtime() + 1000, "ararm_action", SystemClock.elapsedRealtime() + 1000);
        }*/
//        AlarmUtils.startTimer(context.getApplicationContext());
    }
}
