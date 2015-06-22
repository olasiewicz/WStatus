package com.wojtek.wstatus;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class WStatusProvider extends ContentProvider{
	
	public static final String TAG = WStatusProvider.class.getSimpleName();
	private WDataBase wDataBase;
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		uriMatcher.addURI(WStatusContract.AUTHORITY, WStatusContract.TABLE_NAME, WStatusContract.STATUS_DIR);
		uriMatcher.addURI(WStatusContract.AUTHORITY, WStatusContract.TABLE_NAME + "/#", WStatusContract.STATUS_ITEM);
	}

	@Override
	public boolean onCreate() {
		
		wDataBase = new WDataBase(getContext());
		
		return true;
	}


	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case WStatusContract.STATUS_DIR:
			return WStatusContract.STATUS_TYPE_DIR;

		case WStatusContract.STATUS_ITEM:
			return WStatusContract.STATUS_TYPE_ITEM;
			
		default:
			throw new IllegalArgumentException("Illegal uri " + uri);
		}
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri returnUri = null;
		
		if(uriMatcher.match(uri) != WStatusContract.STATUS_DIR) {
			throw new IllegalArgumentException("Illegal uri" + uri);	
		}
		
		SQLiteDatabase db = wDataBase.getWritableDatabase();
		long rowID = db.insertWithOnConflict(WStatusContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		
		if(rowID != -1) {
			long id = values.getAsLong(WStatusContract.Column.ID);
			returnUri = ContentUris.withAppendedId(uri, id); 
			
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return returnUri;
	}


	
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String where;
		
		switch (uriMatcher.match(uri)) {
		case WStatusContract.STATUS_DIR:
			
			//so we count deleted rows
			where = (selection == null) ? "1" : selection;
			break;
			
		case WStatusContract.STATUS_ITEM:
			long id = ContentUris.parseId(uri);
			where = WStatusContract.Column.ID + "=" + id + (TextUtils.isEmpty(selection) ? "" : " and ( " + selection + " )");
			break;

		default:
			throw new IllegalArgumentException("Illegal uri: " + uri);
		}
		
		SQLiteDatabase db = wDataBase.getWritableDatabase();
		int ret = db.delete(WStatusContract.TABLE_NAME, where, selectionArgs);
		
		if(ret > 0){
			//Notify that data for this uri has changed
			getContext().getContentResolver().notifyChange(uri, null);
		}
	
		return ret;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {


		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(WStatusContract.TABLE_NAME);
		
		switch (uriMatcher.match(uri)) {
		case WStatusContract.STATUS_DIR:
			break;

		case WStatusContract.STATUS_ITEM:
			qb.appendWhere(WStatusContract.Column.ID + "=" + uri.getLastPathSegment());
			break;
			
		default:
			throw new IllegalArgumentException("Illegal uri " + uri);
			
		}
		
		String sort = (TextUtils.isEmpty(sortOrder) ? WStatusContract.DEFAULT_SORT : sortOrder);
		SQLiteDatabase db = wDataBase.getReadableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sort);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	
}
