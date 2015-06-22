package com.wojtek.wstatus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class WDataBase extends SQLiteOpenHelper {
	
	private static final String TAG = WDataBase.class.getSimpleName();

	public WDataBase(Context context) {
		super(context, WStatusContract.DB_Name, null, WStatusContract.DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = String
				.format("create table %s (%s int primary key, %s text, %s text, %s int)",
						WStatusContract.TABLE_NAME,
						WStatusContract.Column.ID, 
						WStatusContract.Column.USER,
						WStatusContract.Column.MESSAGE,
						WStatusContract.Column.CREATED_AT);

		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("drop table if exist" + WStatusContract.TABLE_NAME);
		onCreate(db);

	}

}
