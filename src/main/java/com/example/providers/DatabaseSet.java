package com.example.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class DatabaseSet {
	public abstract String getName();
	
	public abstract void onCreate(final SQLiteDatabase database);
	public abstract void onUpgrade(final SQLiteDatabase database);
	
	public void onDowngrade(final SQLiteDatabase database, final int oldVersion, final int newVersion){
		drop(database);
		onCreate(database);
	};
	
	public abstract void drop(final SQLiteDatabase database);
	
	public Cursor query(final SQLiteDatabase database, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder){
		return database.query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
	}
	
	public abstract int delete(final SQLiteDatabase database, final Uri uri, final String selection, final String[] selectionArgs);
	public abstract int delete(final SQLiteDatabase database, final String selection, final String[] selectionArgs);
	public abstract int update(final SQLiteDatabase database, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs);
	public abstract int update(final SQLiteDatabase database, final ContentValues values, final String selection, final String[] selectionArgs);
	public abstract int bulkInsert(final SQLiteDatabase database, final Uri uri, final ContentValues[] values);
	public abstract int bulkInsert(final SQLiteDatabase database, final ContentValues[] values);
	public abstract long insert(final SQLiteDatabase database, final Uri uri, final ContentValues values);
	public abstract long insert(final SQLiteDatabase database, final ContentValues values);
}