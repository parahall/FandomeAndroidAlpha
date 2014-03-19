package com.fandome.application;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class MasterPageActivity  extends SherlockFragmentActivity{
	private LinearLayout placeholder;
	private LinearLayout topRightPlaceholder;
	private int height;
	private int width;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        
		super.setContentView(R.layout.masterpage_layout);
		placeholder = (LinearLayout)findViewById(R.id.placeholder);
		topRightPlaceholder = (LinearLayout)findViewById(R.id.myFanDome);
		View topRightView = getTopRightView();
		if(topRightView!=null){
			topRightPlaceholder.addView(topRightView);
		}
	}
	
	protected int getScreenHeight(){
		return height;
	}
	
	protected int getScreenWidth() {
		return width;
	}
	
	public void setContentView(int id) {
		LayoutInflater inflater = getLayoutInflater(); 
		inflater.inflate(id, placeholder);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance().activityStop(this);
	  }
	
	
	public View getTopRightView(){
		return null;
	}
}
