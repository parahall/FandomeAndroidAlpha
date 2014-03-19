package com.fandome.infra;

import android.os.AsyncTask;

public class AsyncHelper {
	public static void doInAsync(final Runnable runnable){
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				return null;
			}
			
			protected void onPostExecute(Void result) {
				runnable.run();
		     }
			
		}.execute();
	}
}
