package com.wojtek.wstatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WNotificationReceiver extends BroadcastReceiver{

	public static final int ID = 10;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		
		int count = intent.getIntExtra("count", 0);
		
		PendingIntent operation = PendingIntent.getActivity(context, -1, new Intent(context, WMainActivity.class), PendingIntent.FLAG_ONE_SHOT);
		
		Notification notification = new Notification.Builder(context)
									.setContentTitle("New message")
									.setContentText("New statuses: " + count) 
									.setSmallIcon(R.drawable.ic_launcher)
									.setContentIntent(operation).setAutoCancel(true)
									.getNotification();

							notificationManager.notify(ID, notification);
									
	}

}
