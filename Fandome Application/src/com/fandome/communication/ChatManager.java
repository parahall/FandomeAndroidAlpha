package com.fandome.communication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fandome.application.ChatActivity;
import com.fandome.infra.Keys;
import com.fandome.models.ChatRowModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class ChatManager {
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
	public static final int ROWS_LIMIT = 30;
	private static ChatManager instance = new ChatManager();

	private ChatManager() {
	}

	public static ChatManager getInstance() {
		return instance;
	}
	
	public void subscribe(Context context){
		PushService.subscribe(context, "chat", ChatActivity.class);
	}
	
	public void unsubscribe(Context context){
		PushService.unsubscribe(context, "chat");
	}
	
	public List<ChatRowModel> map(List<ParseObject> rows) {
		List<ChatRowModel> result = new ArrayList<ChatRowModel>();
		if (rows != null) {
			for (ParseObject row : rows) {
				result.add(map(row));
			}
		}
		return result;
	}

	public ParseObject map(ChatRowModel row){
		ParseUser user = UserManager.getInstance().GetCurrentUser();
		ParseObject parseObject = new ParseObject("ChatRow");
		parseObject.put("user", user);
		parseObject.put("message", row.getMessage());
		parseObject.put("nickname", row.getNickname());
		return parseObject;
	}
	
	public ChatRowModel map(ParseObject row) {
		ChatRowModel result = new ChatRowModel();
		result.setMessage(row.getString("message"));
		result.setNickname(row.getString("nickname"));
		result.setCreatedAt(row.getCreatedAt());
		return result;
	}
	
	public JSONObject mapToJson(ChatRowModel row) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("action", Keys.Actions.ChatUpdate);
		json.put("message", row.getMessage());
		json.put("nickname", row.getNickname());
		json.put("time", simpleDateFormat.format(row.getCreatedAt()));
		return json;
	}
	
	public ChatRowModel map(JSONObject json) throws JSONException{
		String message = json.getString("message");
		String name = json.getString("nickname");
		String time = json.getString("time");
		ChatRowModel row = new ChatRowModel();
		row.setMessage(message);
		row.setNickname(name);
		try {
			row.setCreatedAt(simpleDateFormat.parse(time));
		} catch (java.text.ParseException e) {
			row.setCreatedAt(new Date());
		}
		return row;
	}
	
	public ChatRowModel sendMessage(String message){
		ChatRowModel row = new ChatRowModel();
		row.setMessage(message);
		row.setCreatedAt(new Date());
		row.setNickname(UserManager.getInstance().getNickname());
		sendMessage(row);
		return row;
		
	}
	
	private void sendMessage(ChatRowModel row){
		ParseObject chatRowModel = map(row);
		JSONObject data;
		try {
			data = mapToJson(row);
			ParsePush push = new ParsePush();
			push.setChannel("chat");
			push.setData(data);
			push.sendInBackground();
		} catch (JSONException e) {
			Log.e(getClass().getSimpleName(), "can't send chat message in push", e);
		}
		
		
		chatRowModel.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e != null && e.getMessage() != null)
					Log.e(getClass().getSimpleName(), e.getMessage(), e);
			}
		});
		
		
	}

	public void getAll(Date from, final ICallback callback) {
		new AsyncTask<Date, Void, Result<List<ChatRowModel>>>() {

			@Override
			protected Result<List<ChatRowModel>> doInBackground(Date... params) {
				ParseQuery query = getQuery()
						.whereGreaterThan("createdAt", params[0]);
				query.orderByDescending("createdAt");
				query.setLimit(ROWS_LIMIT);
				

				List<ChatRowModel> rows = null;
				try {
					List<ParseObject> rowsResult = query.find();
					Collections.reverse(rowsResult);
					rows = map(rowsResult);
				} catch (ParseException e) {
					return new Result<List<ChatRowModel>>(e);
				}

				return new Result<List<ChatRowModel>>(rows);
			}


			protected void onPostExecute(Result<List<ChatRowModel>> result) {
				if(result.getError() == null)
					callback.onrowsArrived(result.getResult());
				else
					callback.onError(result.getError());
			}

		}.execute(from);
	}


	private ParseQuery getQuery() {
		ParseQuery query = new ParseQuery("ChatRow");
		return query;
	}

	public SimpleDateFormat getSimpleDateFormat() {
		return simpleDateFormat;
	}

	public interface ICallback {
		void onrowsArrived(List<ChatRowModel> rows);

		void onError(Exception e);
	}
}
