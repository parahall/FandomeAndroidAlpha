package com.fandome.receivers;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.fandome.services.ChatService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChatReceiver extends BroadcastReceiver {
	private static final String TAG = "ChatReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();
			String channel = intent.getExtras().getString("com.parse.Channel");
			String jsonStr = intent.getExtras().getString(
					"com.parse.Data");
			JSONObject json = new JSONObject(jsonStr);

			Log.d(TAG, "got action " + action + " on channel " + channel
					+ " with:");
			Iterator itr = json.keys();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				Log.d(TAG, "..." + key + " => " + json.getString(key));
			}
			Intent service = new Intent(context, ChatService.class);
			service.setAction(intent.getAction());
			service.putExtra("com.parse.Data", jsonStr);
			service.putExtra("com.parse.Channel", channel);
		    context.startService(service);
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
}
