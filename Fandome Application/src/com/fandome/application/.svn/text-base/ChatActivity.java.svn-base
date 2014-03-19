package com.fandome.application;

import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fandome.communication.ChatManager;
import com.fandome.communication.UserManager;
import com.fandome.infra.GeneralUtility;
import com.fandome.infra.Keys;
import com.fandome.listadapters.ChatRowAdapter;
import com.fandome.models.ChatRowModel;
import com.fandome.receivers.ChatReceiver;
import com.fandome.services.ChatService;

public class ChatActivity extends MasterPageActivity implements
		ChatService.IChatCallback, ChatManager.ICallback {
	private ChatService service;
	private ChatReceiver chatReceiver;
	private ProgressDialog progressDialog;
	private ChatRowAdapter chatRowAdapter;
	private ListView listView;
	private EditText messageText;
	private TextView nicknameTextView;
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = ((ChatService.ChatBinder) binder).getService();
			service.setChatCallback(ChatActivity.this);
		}

		public void onServiceDisconnected(ComponentName className) {
			service = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		nicknameTextView = (TextView) findViewById(R.id.nickname);
		nicknameTextView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showChooseNicknameDialog(false);
			}
		});
		nicknameTextView.setVisibility(View.GONE);
		askForNickName();
	}

	private void askForNickName() {
		if (!UserManager.getInstance().isNicknameExist()) {
			showChooseNicknameDialog(true);
		} else {
			chatLayoutRendering();
		}
		nicknameTextView.setText(UserManager.getInstance().getNickname());

	}

	private void chatLayoutRendering() {
		progressDialog = GeneralUtility.getCancelableProgressDialog(
				"Chat Messages Loading", "Please wait", this);
		chatRowAdapter = new ChatRowAdapter(this);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(chatRowAdapter);
		messageText = (EditText) findViewById(R.id.messageText);
		messageText.setFocusable(true);
		messageText.setFocusableInTouchMode(true);
		doBindService();
		chatReceiver = new ChatReceiver();
		registerReceiver(chatReceiver,
				new IntentFilter(Keys.Actions.ChatUpdate));
		ChatManager.getInstance().subscribe(this);
		ChatManager.getInstance().getAll(
				new GregorianCalendar(2013, 1, 1).getTime(), this);
	}

	private void showChooseNicknameDialog(final boolean finishOnCancel) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Chat Nickname");
		alert.setMessage("Please Choose Nickname");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				UserManager.getInstance().setNewNickname(value);
				chatLayoutRendering();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (finishOnCancel) {
							if (progressDialog != null
									&& progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							finish();
						}
					}
				});

		// alert.setCancelable(false);
		alert.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					finish();
				}
				return true;

			}
		});
		alert.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (chatReceiver != null) {
				unregisterReceiver(chatReceiver);
				chatReceiver = null;
				ChatManager.getInstance().unsubscribe(this);
			}
			unbindService(mConnection);
		} catch (Exception e) {
		}
	}

	public void onSendClicked(View view) {
		ChatRowModel row = ChatManager.getInstance().sendMessage(
				messageText.getText().toString());
		chatRowAdapter.addRow(row);
		messageText.setText("");
		listView.setSelectionFromTop(listView.getBottom(), 0);
	}

	void doBindService() {
		bindService(new Intent(this, ChatService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	public void onNewMessage(ChatRowModel row) {
		if (chatRowAdapter.contain(row)) {
			chatRowAdapter.addRow(row);
		}
		listView.setSelectionFromTop(listView.getBottom(), 0);
	}

	public void onrowsArrived(List<ChatRowModel> rows) {
		if (progressDialog.isShowing())
			progressDialog.dismiss();
		for (ChatRowModel row : rows) {
			chatRowAdapter.addRow(row);
		}
		listView.setSelectionFromTop(listView.getBottom(), 0);
	}

	public void onError(Exception e) {
	}

}
