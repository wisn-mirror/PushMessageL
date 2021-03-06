package com.wisn.pushmessage.jpush;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.wisn.pushmessage.MainActivity;
import com.wisn.pushmessage.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	public static final String TAG = "JIGUANG-Example";
	public static final String TAG2 = "JIGUANG-Example2";
	private static String regId;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));


			ToastUtil.showToast("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle),context);
			//发送获取token请求
			Intent responseIntent = new Intent(MainActivity.INFO_RESPONSE_ACTION);
			responseIntent.putExtra(TAG,"[MyReceiver] onReceive -registerId "+regId+" action" + intent.getAction() + ", extras: " + printBundle(bundle));
			context.sendBroadcast(responseIntent);
			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...
//				JPushInterface.setAlias(context, 111,"15038267031");
				Set<String> set=new HashSet<>();
				set.add("student");
				set.add("male");
				set.add("agg");
//				JPushInterface.setTags(context,111,set);
				JPushInterface.setTags(context, set, new TagAliasCallback() {
					@Override
					public void gotResult(int i, String s, Set<String> set) {
						Log.d(TAG, "[MyReceiver] setTags TagAliasCallback - " + i + ", s: "+s);
						if(set==null) return ;
						Iterator<String> iterator = set.iterator();
						while (iterator!=null&&iterator.hasNext()){
							String next = iterator.next();
							Log.d(TAG, "[MyReceiver] setTags next - " +next);

						}

					}
				});
				JPushInterface.setAlias(context, "15038267031", new TagAliasCallback() {
					@Override
					public void gotResult(int i, String s, Set<String> set) {
						Log.d(TAG, "[MyReceiver]  TagAliasCallback - " + i + ", s: "+s);
						if(set==null) return ;
						Iterator<String> iterator = set.iterator();
						while (iterator!=null&&iterator.hasNext()){
							String next = iterator.next();
							Log.d(TAG, "[MyReceiver]  next - " +next);

						}

					}
				});
			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

				//打开自定义的Activity
				Intent i = new Intent(context, MainActivity.class);
				i.putExtras(bundle);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(context,MainActivity.class);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}
}
