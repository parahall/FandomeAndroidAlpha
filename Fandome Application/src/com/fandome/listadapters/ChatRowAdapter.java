package com.fandome.listadapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fandome.application.R;
import com.fandome.communication.ChatManager;
import com.fandome.communication.UserManager;
import com.fandome.models.ChatRowModel;

public class ChatRowAdapter extends BaseAdapter {
	private List<ChatRowModel> rows = new ArrayList<ChatRowModel>();
	private LayoutInflater layoutInflater;
	protected Context context;
	private LinearLayout wrapper;
	private LinearLayout mainWrapper;

	public ChatRowAdapter(Context ctx) {
		context = ctx;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return rows.size();
	}

	public Object getItem(int index) {
		return rows.get(index);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.single_chat_row, null);
		}

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView message = (TextView) convertView.findViewById(R.id.message);
		wrapper = (LinearLayout) convertView.findViewById(R.id.msgWrapper);
		mainWrapper = (LinearLayout) convertView
				.findViewById(R.id.msgMainWrapper);

		ChatRowModel rowModel = (ChatRowModel) getItem(position);
		if (rowModel != null) {
			name.setText(rowModel.getNickname() + ":");
			time.setText(ChatManager.getInstance().getSimpleDateFormat()
					.format(rowModel.getCreatedAt()));
			message.setText(rowModel.getMessage());

			if (rowModel.getNickname().equals(
					UserManager.getInstance().getNickname())) {
				wrapper.setBackgroundResource(R.drawable.bubble_green);
				mainWrapper.setGravity(Gravity.RIGHT);
			} else {
				wrapper.setBackgroundResource(R.drawable.bubble_yellow);
				mainWrapper.setGravity(Gravity.LEFT);
			}
		}

		return convertView;
	}

	public void addRow(ChatRowModel row) {
		rows.add(row);
		if (rows.size() > ChatManager.ROWS_LIMIT)
			rows.remove(0);
		notifyDataSetChanged();
	}

	public boolean contain(ChatRowModel row) {
		return rows.contains(row);
	}

}
