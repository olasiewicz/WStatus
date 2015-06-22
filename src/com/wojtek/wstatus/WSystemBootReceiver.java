package com.wojtek.wstatus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class WSystemBootReceiver extends BroadcastReceiver {

	private static final long DEFAULT_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		long interval = Long.parseLong(pref.getString("interv", Long.toString(DEFAULT_INTERVAL)));

		PendingIntent operation = PendingIntent.getService(context, -1,
				new Intent(context, WRefreshService.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
		
		if(interval == 0) {
			alarmManager.cancel(operation);
		} else {
			alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, operation);
		}

	}

}
