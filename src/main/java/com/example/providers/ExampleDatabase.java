package com.example.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ExampleApplication;

import java.util.Collection;

public class ExampleDatabase extends SQLiteOpenHelper {

    private final Collection<DatabaseSet> mSets;

    public ExampleDatabase(final Context context, final Collection<DatabaseSet> sets) {
		super(context, ExampleApplication.DATABASE_NAME, null, ExampleApplication.DATABASE_VERSION);
        mSets = sets;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		createTables(db);
        createViews(db);
	}

    private void createTables(final SQLiteDatabase db) {
        for (final DatabaseSet set : mSets) {
            if (set instanceof DatabaseTable) {
                set.onCreate(db);
            }
        }
    }

    private void createViews(final SQLiteDatabase db) {
        for (final DatabaseSet set : mSets) {
            if (set instanceof DatabaseView) {
                set.onCreate(db);
            }
        }
    }

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final DatabaseSet set : mSets) {
			set.onUpgrade(db);
		}
	}
}