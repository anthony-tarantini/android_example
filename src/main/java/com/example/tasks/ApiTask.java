package com.example.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.dagger.Injector;
import com.example.models.Product;
import com.example.networking.LCBOApiClient;
import com.example.providers.ExampleContentProvider;
import com.example.tables.ProductTable;

import java.util.List;

import javax.inject.Inject;

public class ApiTask extends Task<List<Product>> implements Parcelable {

    @Inject
    LCBOApiClient mApiClient;

    @Inject
    LocalBroadcastManager mLocalBroadcastManager;

    private final String mQuery;

    public ApiTask(final Context context, final String query) {
        super(context);
        mQuery = query;
    }

    @Override
    public String createIdentifier() {
        return String.format("%s/%s", ApiTask.class.getCanonicalName(), mQuery);
    }

    @Override
    public List<Product> executeNetworking() throws Exception {
        return mApiClient.listProducts(mQuery);
    }

    @Override
    public void processResponse(final List<Product> products) throws Exception {
        final ContentValues[] contentValues = ProductTable.toContentValues(products);
        mContext.getContentResolver().bulkInsert(ExampleContentProvider.DatabaseUris.PRODUCT_TABLE_URI, contentValues);
    }

    @Override
    public void notifySuccess() {
        mContext.getContentResolver().notifyChange(ExampleContentProvider.RequestUris.BEER_REQUEST, null);
    }

    @Override
    public void notifyFailure() {
        final Intent intent = new Intent();
        intent.setData(ExampleContentProvider.RequestUris.BEER_REQUEST);
        intent.setAction(ERROR);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void inject() {
        ((Injector) mContext).inject(this);
    }
}
