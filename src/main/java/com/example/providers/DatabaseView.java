package com.example.providers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class DatabaseView extends DatabaseSet {
	private static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM ( %s );";
	private static final String DROP_VIEW = "DROP VIEW IF EXISTS %s;";

	abstract protected String getSelectString();

	@Override
	public void onCreate(final SQLiteDatabase db) {		
		final String query = String.format(CREATE_VIEW, getName(), getSelectString());
		db.execSQL(query);
	}
	
	@Override
	public void onUpgrade(final SQLiteDatabase db) {
		final String query = String.format(DROP_VIEW, getName());
		db.execSQL(query);
		onCreate(db);
	}

	@Override
	public void drop(final SQLiteDatabase db) {
		final String query = String.format(DROP_VIEW, getName());
		db.execSQL(query);
	}
	@Override
	public int delete(final SQLiteDatabase database, final Uri uri, final String selection, final String[] selectionArgs) {
		return 0;
	}

	@Override
	public int delete(final SQLiteDatabase database, final String selection, final String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(final SQLiteDatabase database, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(final SQLiteDatabase database, final ContentValues values, final String selection, final String[] selectionArgs) {
		return 0;
	}

	@Override
	public int bulkInsert(final SQLiteDatabase database, final Uri uri, final ContentValues[] values) {
		return 0;
	}

	@Override
	public int bulkInsert(final SQLiteDatabase database, final ContentValues[] values) {
		return 0;
	}

	@Override
	public long insert(final SQLiteDatabase database, final Uri uri, final ContentValues values) {
		return 0;
	}

	@Override
	public long insert(final SQLiteDatabase database, final ContentValues values) {
		return 0;
	}
}