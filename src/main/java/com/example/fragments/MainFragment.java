package com.example.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.R;
import com.example.dagger.Injector;
import com.example.providers.ErrorReceiver;
import com.example.providers.ExampleContentProvider;
import com.example.providers.LoaderFactory;
import com.example.services.ApiService;
import com.example.tables.ProductTable;
import com.example.tasks.ApiTask;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ErrorReceiver.ErrorListener {

    @Inject
    LoaderFactory mLoaderFactory;

    @Inject
    LocalBroadcastManager mLocalBroadcastManager;

    @InjectView(R.id.list_view)
    ListView mListView;

    private SimpleCursorAdapter mCursorAdapter;
    private ErrorReceiver mErrorReceiver;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        ((Injector) activity).inject(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        mLoaderFactory.createLoader(activity, this);
        mCursorAdapter = new SimpleCursorAdapter(activity,
                android.R.layout.simple_list_item_2, null,
                new String[] {ProductTable.Columns.NAME, ProductTable.Columns.ORIGIN},
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);
        mListView.setAdapter(mCursorAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Activity activity = getActivity();
        ApiService.startService(activity, new ApiTask(activity, null));
        mErrorReceiver = new ErrorReceiver(this, ExampleContentProvider.Paths.BEER_REQUEST);
        mLocalBroadcastManager.registerReceiver(mErrorReceiver, mErrorReceiver.createIntentFilter());
    }

    @Override
    public void onError() {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocalBroadcastManager.unregisterReceiver(mErrorReceiver);
        mErrorReceiver = null;
    }

    @Override
    public CursorLoader onCreateLoader(final int i, final Bundle bundle) {
        return new CursorLoader(getActivity(), ExampleContentProvider.RequestUris.BEER_REQUEST, null, null, null, null);
    }

    @Override
    public void onLoadFinished(final Loader loader, final Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(final Loader loader) {
        mCursorAdapter.swapCursor(null);
    }
}
