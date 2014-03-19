package com.fandome.application;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fandome.communication.FacebookManager;
import com.fandome.communication.MyFandomeManager;
import com.google.analytics.tracking.android.EasyTracker;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends SherlockFragmentActivity {
	
	private SharedPreferences mPreferences;
	public static final String PREFS_NAME = "FanDomePrefsFile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		EasyTracker.getInstance().setContext(getApplicationContext());
		mPreferences = getSharedPreferences(PREFS_NAME, 0);
		boolean firstTime = mPreferences.getBoolean("firstTime", true);
		if (firstTime) { 
		    SharedPreferences.Editor editor = mPreferences.edit();
		    editor.putBoolean("firstTime", false);
		    editor.commit();
		    showTutorialDialog();
		}
	}
	
	private void showTutorialDialog() {
		 final Dialog dialog = new Dialog(MainActivity.this);
         dialog.setContentView(R.layout.tutorial_dialog);
         dialog.setTitle(R.string.AlertTitle);
         dialog.setCancelable(true);

         TextView text = (TextView) dialog.findViewById(R.id.txtViewBegin);
         text.setText(R.string.tutorial_text_part_before_star);
         
         TextView text1 = (TextView) dialog.findViewById(R.id.txtViewEnd);
         text1.setText(R.string.tutorial_text_part_after_star);
         
         TextView text2 = (TextView) dialog.findViewById(R.id.txtViewLogin);
         text2.setText(R.string.tutorial_text_before_login);
         

         ImageButton btn = (ImageButton) dialog.findViewById(R.id.btnFacebook);
         btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dialog.dismiss();
				FacebookManager.getInstance().facebookConnect(MainActivity.this);
			}
		});
         
  
         dialog.show();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
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

	public void NewsClicked(View v) {
		startActivity(new Intent(this, NewsActivity.class));
	}

	public void TeamsClicked(View v) {
		startActivity(new Intent(this, TeamsActivity.class));
	}

	public void ImagesClicked(View v) {
		startActivity(new Intent(this, GalleryActivity.class));
	}

	public void ChatClicked(View v) {
		startActivity(new Intent(this, ChatActivity.class));
	}

	public void DigitalClicked(View v) {
		OpenDigitalProgram();
		Toast.makeText(this, R.string.sendingRequest, Toast.LENGTH_SHORT).show();
	}

	private void OpenDigitalProgram() {
		EasyTracker.getTracker().sendEvent("activity", "Click", "Digital Program", (long) 1);
		
		ParseQuery query = new ParseQuery("digitalProgram");
		query.getFirstInBackground(new GetCallback() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					ParseFile digitalProgram = (ParseFile) object.get("pdf");
					digitalProgram.getUrl();
					openFile(digitalProgram.getUrl());
				} else {
					if (e.getCode() == 101) {
						MyFandomeManager.getInstance().sendDigitalProgramRequest();
						showAlertDialog();
					} else {
						Log.d("Digital Program", e.getMessage());
					}
				}
			}

		});
	}

	protected void showAlertDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.ProgramTitle);
		alert.setMessage(R.string.digitalProgram_message);

		alert.setCancelable(true);

		alert.setPositiveButton("Yes", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//store positive feedback
				MyFandomeManager.getInstance().sendDigitalProgramFeedback("Yes");
			}
		});
		alert.setNeutralButton("I would pay less", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MyFandomeManager.getInstance().sendDigitalProgramFeedback("I would pay less");
			}
		});
		alert.setNegativeButton("No", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//Store negative feedback
				MyFandomeManager.getInstance().sendDigitalProgramFeedback("No");
			}
		});
		alert.show();

	}


	protected void openFile(String url) {

//		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			Intent downloadIntent = new Intent(Intent.ACTION_VIEW);
			downloadIntent.setData(Uri.parse(url));
			startActivity(downloadIntent);
//		} else {
//			DownloadManager.Request request = new DownloadManager.Request(
//					Uri.parse(url));
//			request.setDestinationInExternalPublicDir(
//					Environment.DIRECTORY_DOWNLOADS, "fandome.pdf");
//			DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//			manager.enqueue(request);
//		}

	}

}
