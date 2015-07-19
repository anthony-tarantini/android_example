package com.example.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.models.ProductResponse;
import com.example.networking.LCBOApiClient;
import com.example.providers.ExampleContentProvider;
import com.example.tables.ProductTable;
import com.example.tables.TaskTable;

import org.joda.time.LocalDateTime;

import javax.inject.Inject;

public class ApiTask extends Task<ProductResponse> implements Parcelable, SyncStrategy {

    @Inject
    LCBOApiClient mApiClient;

    @Inject
    LocalBroadcastManager mLocalBroadcastManager;

    private final String mQuery;

    public ApiTask(final Context context, final String query) {
        super(context);
        mQuery = query;
    }

    public ApiTask(Parcel in) {
        super(in);
        mQuery = in.readString();
    }

    @Override
    public String createIdentifier() {
        return String.format("%s/%s", ApiTask.class.getCanonicalName(), mQuery);
    }

    @Override
    public ProductResponse executeNetworking() throws Exception {
        return mApiClient.listProducts(mQuery);
    }

    @Override
    public void processResponse(final ProductResponse productResponse) throws Exception {
        final ContentValues[] contentValues = ProductTable.toContentValues(productResponse.getResult());
        mContext.getContentResolver().bulkInsert(ExampleContentProvider.DatabaseUris.PRODUCT_TABLE_URI, contentValues);
    }

    @Override
    public SyncStrategy getSyncStrategy() {
        return this;
    }

    @Override
    public void notifySuccess() {
        mContext.getContentResolver().notifyChange(ExampleContentProvider.RequestUris.BEER_REQUEST, null);
    }

    @Override
    public void notifyFailure() {
        final Intent intent = new Intent(ERROR);
        intent.addCategory(ExampleContentProvider.Paths.BEER_REQUEST);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public boolean checkSyncNeeded() {
        final Cursor query = mContext.getContentResolver().query(
                ExampleContentProvider.DatabaseUris.TASK_TABLE_URI,
                new String[]{TaskTable.Columns.LAST_SYNC_TIME},
                TaskTable.Columns.IDENTIFIER + "=?",
                new String[]{getIdentifier()},
                null);
        if (query.moveToFirst()) {
            final LocalDateTime lastSyncTime = LocalDateTime.parse(query.getString(0));
            query.close();
            return lastSyncTime.isBefore(LocalDateTime.now().minusHours(1));
        }
        query.close();
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mQuery);
    }

    public static final Creator<ApiTask> CREATOR = new Creator<ApiTask>() {
        public ApiTask createFromParcel(Parcel source) {
            return new ApiTask(source);
        }

        public ApiTask[] newArray(int size) {
            return new ApiTask[size];
        }
    };
}
