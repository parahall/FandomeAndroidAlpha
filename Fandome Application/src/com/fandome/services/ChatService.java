package com.fandome.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.fandome.communication.ChatManager;
import com.fandome.infra.Keys;
import com.fandome.models.ChatRowModel;

public class ChatService extends Service {
	private final IBinder mBinder = new ChatBinder();
	private IChatCallback callback;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(intent.getAction().equals(Keys.Actions.ChatUpdate)){
			String jsonStr = intent.getExtras().getString("com.parse.Data");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonStr);
				ChatRowModel row = ChatManager.getInstance().map(jsonObject);
				if(callback != null)
					callback.onNewMessage(row);
			} catch (JSONException e) {
				Log.e(getClass().getSimpleName(), "can't parse chat row\n"+jsonStr, e);
			}
			
			
		}
		
		return Service.START_NOT_STICKY;
	}
	
	public void setChatCallback(IChatCallback callback){
		this.callback = callback;
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class ChatBinder extends Binder {
		public ChatService getService() {
			return ChatService.this;
		}
	}
	
	public interface IChatCallback{
		void onNewMessage(ChatRowModel row);
	}

}
