package com.wojtek.wstatus;

import android.net.Uri;
import android.provider.BaseColumns;

public class WStatusContract {

	public static final String DB_Name = "wstatus.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "statusTable";
	public static final String AUTHORITY = "com.wojtek.wstatus.WStatusProvider";

	public static final Uri ContentURI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final int STATUS_ITEM = 1;
	public static final int STATUS_DIR = 2;
	public static final String STATUS_TYPE_ITEM = "vnd.android.cursor.item/vnd.com.wojtek.wstatus";
	public static final String STATUS_TYPE_DIR = "vnd.android.cursor.dir/vnd.com.wojtek.wstatus";

	public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";

	public class Column {

		public static final String ID = BaseColumns._ID;
		public static final String USER = "user";
		public static final String MESSAGE = "message";
		public static final String CREATED_AT = "created_at";

	}

}
