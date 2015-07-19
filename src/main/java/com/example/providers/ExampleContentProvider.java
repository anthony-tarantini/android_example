package com.example.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.SparseArray;

import com.example.tables.ProductTable;
import com.example.tables.TaskTable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExampleContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.providers";
    public static final String CONTENT = "content://";
    public static final String BASE_URI = CONTENT + AUTHORITY;

    private final SparseArray<DatabaseSet> mMappings = new SparseArray<DatabaseSet>();
    private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private volatile SQLiteDatabase mDatabase;

    synchronized protected SQLiteDatabase getDatabase() {
        if (mDatabase == null) {
            createDatabase();
        }
        return mDatabase;
    }

    synchronized private void createDatabase() {
        if (mDatabase == null) {
            final ExampleDatabase database = new ExampleDatabase(getContext(), getSets());
            mDatabase = database.getWritableDatabase();
        }
    }

    synchronized protected void destroyDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    private Collection<DatabaseSet> getSets() {
        final int size = mMappings.size();
        final Set<DatabaseSet> sets = new LinkedHashSet<DatabaseSet>(size);
        for (int i = 0; i < size; i++) {
            sets.add(mMappings.get(mMappings.keyAt(i)));
        }
        return sets;
    }

    private static Uri createUri(final String path) {
        return Uri.parse(BASE_URI + "/" + path);
    }

    public interface Paths {
        String BEER_REQUEST = "beer_request";
    }

    public interface DatabaseUris {
        Uri PRODUCT_TABLE_URI = createUri(Tables.PRODUCT);
        Uri TASK_TABLE_URI = createUri(Tables.TASK);
    }

    public interface RequestUris {
        Uri BEER_REQUEST = createUri(Paths.BEER_REQUEST);
    }

    private interface Tables {
        String PRODUCT = ProductTable.TABLE_NAME;
        String TASK = TaskTable.TABLE_NAME;
    }

    private interface Views {
    }

    private interface Codes {
        int PRODUCTS = 1;
        int BEER_REQUEST = 2;
        int TASK = 3;
    }

    @Override
    public boolean onCreate() {
		mUriMatcher.addURI(AUTHORITY, Tables.PRODUCT, Codes.PRODUCTS);
		mMappings.append(Codes.PRODUCTS, new ProductTable());

        mUriMatcher.addURI(AUTHORITY, Paths.BEER_REQUEST, Codes.BEER_REQUEST);
        mMappings.append(Codes.BEER_REQUEST, new ProductTable());

        mUriMatcher.addURI(AUTHORITY, Tables.TASK, Codes.TASK);
        mMappings.append(Codes.TASK, new TaskTable());

        createDatabase();
        return true;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        final int match = mUriMatcher.match(uri);
        final DatabaseSet databaseSet = mMappings.get(match);
        final long id = databaseSet.insert(getDatabase(), uri, values);
        final String idValue = String.valueOf(id);
        return uri.buildUpon().appendPath(idValue).build();
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final int match = mUriMatcher.match(uri);
        final DatabaseSet databaseSet = mMappings.get(match);
        return databaseSet.query(getDatabase(), uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);
        final DatabaseSet databaseSet = mMappings.get(match);
        return databaseSet.delete(getDatabase(), uri, selection, selectionArgs);
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String whereClause, final String[] whereArgs) {
        final int match = mUriMatcher.match(uri);
        final DatabaseSet databaseSet = mMappings.get(match);
        return databaseSet.update(getDatabase(), uri, values, whereClause, whereArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
