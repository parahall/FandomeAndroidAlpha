package com.fandome.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fandome.communication.MyFandomeManager;
import com.fandome.intefaces.IFinishNotify;
import com.parse.PushService;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);
		
		PushService.setDefaultPushCallback(this, SplashScreenActivity.class);
		MyFandomeManager.getInstance().init(new IFinishNotify<Void>() {
			
			public void onFinished(Void result) {
				Intent intent = new Intent(SplashScreenActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
			
			public void onError(Exception e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
				builder
				.setTitle("Can't load application, please try again")
				.setPositiveButton("OK", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create().show();
			}
		});
		
	}


}
