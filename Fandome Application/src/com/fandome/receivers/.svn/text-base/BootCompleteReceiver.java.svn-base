package com.fandome.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fandome.services.ResourcesDownloadService;

public class BootCompleteReceiver extends BroadcastReceiver{
	private static final int RESOURCE_MANAGER_CYCLE_TIME = 10*20*1000; //10 mins
	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent intent = new Intent(context, ResourcesDownloadService.class);
		intent.setAction(ResourcesDownloadService.Operation.LoadResources);
		context.startService(intent);
//		PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);
//
//		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), RESOURCE_MANAGER_CYCLE_TIME, pintent); 
	}

}
