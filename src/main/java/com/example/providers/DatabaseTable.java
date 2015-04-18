package com.example.providers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.utils.DBUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DatabaseTable extends DatabaseSet {
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
	
	public interface Columns {
		String _ID = "_id";
	}
	
	protected Map<String, String> getColumnTypes(){
		final Map<String, String> columnTypes = new LinkedHashMap<String, String>();
		columnTypes.put(Columns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		return columnTypes;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db) {
		final String query = DROP_TABLE + getName();
		db.execSQL(query);
		onCreate(db);
	}

	protected String getConstraint() {
		return null;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		final Map<String, String> columns = getColumnTypes();
        final String tableName = getName();
        final String columnsString = DBUtils.mapToString(columns);
        final String constraintString = getConstraintString();
        final String query = CREATE_TABLE + tableName + " (" + columnsString +  constraintString + ");";
		database.execSQL(query);
	}

	private String getConstraintString() {
		final String constraint = getConstraint();
		return constraint != null ? ", " + constraint : "";
	}

	public void truncate(final SQLiteDatabase database) {
		delete(database, null, null);
	}

	@Override
	public void drop(final SQLiteDatabase database) {
		final String query = DROP_TABLE + getName();
		database.execSQL(query);
	}

	@Override
	public long insert(final SQLiteDatabase database, final ContentValues values) {
		return insert(database, null, values);
	}

	@Override
	public long insert(final SQLiteDatabase database, final Uri uri, final ContentValues values) {
		return database.insert(getName(), null, values);
	}

	@Override
	public int bulkInsert(final SQLiteDatabase database, final ContentValues[] values) {
		return bulkInsert(database, null, values);
	}

	@Override
	public int bulkInsert(final SQLiteDatabase database, final Uri uri, final ContentValues[] values) {
		int inserts = 0;
		database.beginTransaction();
		try {
			for (final ContentValues value : values) {
				if (insert(database, uri, value) > -1)
					inserts++;
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		return inserts;
	}

	@Override
	public int update(final SQLiteDatabase database, final ContentValues values, final String selection, final String[] selectionArgs) {
		return update(database, null, values, selection, selectionArgs);
	}

	@Override
	public int update(final SQLiteDatabase database, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		return database.update(getName(), values, selection, selectionArgs);
	}

	@Override
	public int delete(final SQLiteDatabase database, final String selection, final String[] selectionArgs) {
		return delete(database, null, selection, selectionArgs);
	}

	@Override
	public int delete(final SQLiteDatabase database, final Uri uri, final String selection, final String[] selectionArgs) {
		return database.delete(getName(), selection, selectionArgs);
	}
}