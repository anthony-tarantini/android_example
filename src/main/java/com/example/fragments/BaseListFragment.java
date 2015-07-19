package com.example.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.annotations.IntentionalNoOp;
import com.example.dagger.Injector;
import com.example.providers.ErrorReceiver;
import com.example.providers.LoaderFactory;
import com.example.services.ApiIntentService;
import com.example.services.ContentRequest;

import javax.inject.Inject;

public abstract class BaseListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ErrorReceiver.ErrorListener {
    @Inject
    LoaderFactory mLoaderFactory;

    @Inject
    LocalBroadcastManager mLocalBroadcastManager;

    ListView mListView;

    protected CursorAdapter mCursorAdapter;
    protected ErrorReceiver mErrorReceiver;
    protected IntentFilter mIntentFilter;
    protected ContentRequest mRequest;

    public abstract CursorAdapter createAdapter();
    public abstract int getListViewId();

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        ((Injector) activity).inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() == null){
            return;
        }

        mListView = (ListView) getView().findViewById(getListViewId());
    }

    protected void execute(final ContentRequest request) {
        mRequest = request;
        mErrorReceiver = new ErrorReceiver(this, request.getPath());
        mIntentFilter = mErrorReceiver.createIntentFilter();
        mLocalBroadcastManager.registerReceiver(mErrorReceiver, mIntentFilter);

        mLoaderFactory.initLoader(getActivity(), this.getClass(), this);
        mCursorAdapter = createAdapter();
        mListView.setAdapter(mCursorAdapter);

        if (request.getTask() != null) {
            ApiIntentService.startService(getActivity(), request.getTask());
        }
    }

    @Override
    public void onError() {
        mCursorAdapter.swapCursor(null);
        onRequestError();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocalBroadcastManager.unregisterReceiver(mErrorReceiver);
        mErrorReceiver = null;
    }

    @Override
    public CursorLoader onCreateLoader(final int i, final Bundle bundle) {
        return new CursorLoader(getActivity(),
                mRequest.getUri(),
                mRequest.getProjection(),
                mRequest.getSelection(),
                mRequest.getSelectionArgs(),
                mRequest.getSortOrder()
        );
    }

    @Override
    public void onLoadFinished(final Loader loader, final Cursor cursor) {
        cursor.setNotificationUri(getActivity().getContentResolver(), mRequest.getUri());
        if (cursor.getCount() > 0) {
            mCursorAdapter.swapCursor(cursor);
            onRequestComplete();
        }
    }

    @Override
    public void onLoaderReset(final Loader loader) {
        mCursorAdapter.swapCursor(null);
    }

    @IntentionalNoOp(reason = "Allow for Override without Abstraction")
    public void onRequestComplete() {}
    @IntentionalNoOp(reason = "Allow for Override without Abstraction")
    public void onRequestError() {}
}
