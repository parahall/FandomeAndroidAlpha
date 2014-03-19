package com.fandome.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fandome.application.R;

public class MenuFragment extends BaseFragment {
	private ImageButton NewsImg;
	private ImageButton ImageImg;
	private ImageButton ChatImg;
	private ImageButton TeamImg;
	private ImageButton digital;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.menu_fragment, container, false);
		NewsImg = (ImageButton) view.findViewById(R.id.newsItem);
		ImageImg = (ImageButton) view.findViewById(R.id.imageItem);
		ChatImg = (ImageButton) view.findViewById(R.id.chatItem);
		TeamImg = (ImageButton) view.findViewById(R.id.teamItem);
		digital = (ImageButton) view.findViewById(R.id.orderDigital);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("NEWS IMAGE SIZE",
				+NewsImg.getWidth() + "x" + NewsImg.getHeight());
		Log.d("IMAGE IMAGE SIZE",
				+ImageImg.getWidth() + "x" + ImageImg.getHeight());
		Log.d("CHAT IMAGE SIZE",
				+ChatImg.getWidth() + "x" + ChatImg.getHeight());
		Log.d("TEAM IMAGE SIZE",
				+TeamImg.getWidth() + "x" + TeamImg.getHeight());
		Log.d("TEAM IMAGE SIZE",
				+digital.getWidth() + "x" + digital.getHeight());
	}

}
