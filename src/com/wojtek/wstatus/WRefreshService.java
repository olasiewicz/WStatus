package com.wojtek.wstatus;

import java.util.List;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class WRefreshService extends IntentService {

	private static final String TAG = WRefreshService.class.getSimpleName();
	private SharedPreferences prefs;
	private Handler mHandler;

	public WRefreshService() {
		super(TAG);

	}

	@Override
	public void onCreate() {
		super.onCreate();

		mHandler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		final String username = prefs.getString("username", "");
		final String password = prefs.getString("password", "");
		final String url = prefs.getString("URL", "");

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Intent dialogIntent = new Intent(this, WSettingsActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(dialogIntent);

			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(),
							R.string.Toast_checkUsernameAndPassword,
							Toast.LENGTH_LONG).show();
				}
			});
			return;
		}


		WDataBase dataBase = new WDataBase(this);
		SQLiteDatabase db = dataBase.getWritableDatabase();
		ContentValues values = new ContentValues();

		YambaClient refreshCloud = new YambaClient(username, password, url);

		try {
            int count = 0;
			List<Status> list = refreshCloud.getTimeline(10);
			for (Status status : list) {

				values.clear();
				values.put(WStatusContract.Column.ID, status.getId());
				values.put(WStatusContract.Column.USER, status.getUser());
				values.put(WStatusContract.Column.MESSAGE, status.getMessage());
				values.put(WStatusContract.Column.CREATED_AT, status
						.getCreatedAt().getTime());

//				db.insertWithOnConflict(WStatusContract.TABLE_NAME, null,
//						values, SQLiteDatabase.CONFLICT_IGNORE);
				
				Uri uri = getContentResolver().insert(WStatusContract.ContentURI, values);
				if(uri != null) count ++;
			}
			
			if(count > 0) {
				sendBroadcast(new Intent("com.wojtek.wstatus.NEW_NOT").putExtra("count", count));
			}
			
		} catch (YambaClientException e) {

			e.printStackTrace();
			
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}
}
