package com.huwei.db;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.widget.Switch;

public class ConnectProvider extends ContentProvider {
	public static final String AUTHORITY = "com.hwant.im.friend";
	public static Uri uri = Uri.parse("content://" + AUTHORITY);
	public static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		matcher.addURI(AUTHORITY, "friend", 1);
		matcher.addURI(AUTHORITY, "friend/#", 2);
	}
	private SQLiteDatabase database = null;
	private MySQLiteOpenHelper sqlite = null;

	@Override
	public boolean onCreate() {
		sqlite = new MySQLiteOpenHelper(getContext());

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		database = sqlite.getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables("connect");
		switch (matcher.match(uri)) {
		case 1:
			break;
		case 2:
			builder.appendWhere("jid='" + uri.getPathSegments().get(1) + "'");
			break;
		default:
			throw new IllegalArgumentException("uri错误!");
		}
		Cursor cursor = builder.query(database, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Cursor cursor = database.query("friend", projection, selection,
		// selectionArgs, null, null, sortOrder);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		String str = "";
		switch (matcher.match(uri)) {
		case 1:
			str = "vnd.android.cursor.dir/com.hwant.im.friend";
			break;
		case 2:
			str = "vnd.android.cursor.item/com.hwant.im.friend";
			break;
		default:
			throw new IllegalArgumentException("uri错误!");
		}
		return str;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (matcher.match(uri) != 1) {
			throw new IllegalArgumentException("uri错误!");
		}
		if (values == null) {
			throw new IllegalArgumentException("ContentValues对象不能为空");
		}
		database = sqlite.getWritableDatabase();
		// long rowid = database.insert("friend", "_id", values);
		// 防止重复插入
		// long rowid = database.replace("connect", null, values);
		// Uri newuri = ContentUris.withAppendedId(uri, rowid);
		StringBuilder builder = new StringBuilder(
				"insert or ignore into connect (");
		for (String field : values.keySet()) {
			builder.append(field).append(",");
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		builder.append(") values (");
		for (String field : values.keySet()) {
			builder.append("?,");
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		builder.append(")");
		// Object[] objects=new Object[values.size()];
		ArrayList<Object> objects = new ArrayList<Object>();
		for (String field : values.keySet()) {
			objects.add(values.get(field));
		}
		database.execSQL(builder.toString(), objects.toArray());
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (matcher.match(uri) != 1) {
			throw new IllegalArgumentException("uri错误!");
		}
		database = sqlite.getWritableDatabase();
		int rowid = database.delete("connect", selection, selectionArgs);
		return rowid;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (matcher.match(uri) != 1) {
			throw new IllegalArgumentException("uri错误!");
		}
		database = sqlite.getWritableDatabase();
		int count = database
				.update("connect", values, selection, selectionArgs);
		return count;
	}

}
