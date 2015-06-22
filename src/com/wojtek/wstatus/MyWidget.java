package com.wojtek.wstatus;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.widget.RemoteViews;

public class MyWidget extends AppWidgetProvider{

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		
		this.onUpdate(context, appWidgetManager, appWidgetManager
				.getAppWidgetIds(new ComponentName(context, MyWidget.class)));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// get the latest tweet
		Cursor cursor = context.getContentResolver().query(
				WStatusContract.ContentURI, null, null, null,
				WStatusContract.DEFAULT_SORT);

		if (!cursor.moveToFirst()) return;
			
		String user = cursor.getString(cursor
				.getColumnIndex(WStatusContract.Column.USER));
		String message = cursor.getString(cursor
				.getColumnIndex(WStatusContract.Column.MESSAGE));
		long createdAt = cursor.getLong(cursor
				.getColumnIndex(WStatusContract.Column.CREATED_AT));

		PendingIntent operation = PendingIntent.getActivity(context, -1,
				new Intent(context, WMainActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		for (int appWidgetId : appWidgetIds) {
			RemoteViews view = new RemoteViews(context.getPackageName(),
					R.layout.widget);

			// update the remote view
			view.setTextViewText(R.id.list_item_text_user, user);
			view.setTextViewText(R.id.list_item_text_message, message);
			view.setTextViewText(R.id.list_item_text_created_at,
					DateUtils.getRelativeTimeSpanString(createdAt));
			view.setOnClickPendingIntent(R.id.list_item_text_user, operation);
			view.setOnClickPendingIntent(R.id.list_item_text_message, operation);
			// Update the widget
			appWidgetManager.updateAppWidget(appWidgetId, view);
		}
	}
	
	

}
