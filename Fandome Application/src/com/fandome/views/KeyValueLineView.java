package com.fandome.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fandome.application.R;

public class KeyValueLineView extends LinearLayout {
	private TextView key;
	private TextView value;
	
	public KeyValueLineView(Context context) {
		this(context, null);
		
	}
	
	public KeyValueLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public KeyValueLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.player_details_stats_row, this);
		
		key = (TextView)findViewById(R.id.key);
		value = (TextView)findViewById(R.id.value);
	}
	
	public void setKeyValue(String key, String value){
		this.key.setText(key);
		this.value.setText(value);
	}
}
