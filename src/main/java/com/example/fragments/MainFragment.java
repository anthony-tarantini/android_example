package com.example.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.R;
import com.example.providers.ExampleContentProvider;
import com.example.services.ContentRequest;
import com.example.tables.ProductTable;
import com.example.tasks.ApiTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainFragment extends BaseListFragment {
    @InjectView(R.id.content_view)
    protected View mContentView;

    @InjectView(R.id.loading_spinner)
    protected View mLoadingView;

    @InjectView(R.id.error_view)
    protected View mErrorView;

    @InjectView(R.id.fragment_main_title_text)
    protected TextView mTitleTextView;

    private String mQuery;

    public static MainFragment newInstance(final String query) {
        final MainFragment mainFragment = new MainFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(Arguments.QUERY, query);
        mainFragment.setArguments(arguments);
        return mainFragment;
    }

    @Override
    public CursorAdapter createAdapter() {
        return new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[] {ProductTable.Columns.NAME, ProductTable.Columns.ORIGIN},
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);
    }

    @Override
    public int getListViewId() {
        return R.id.list_view;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        mQuery = getArguments().getString(Arguments.QUERY);
        ButterKnife.inject(this, view);
        mTitleTextView.setText(mQuery.toUpperCase());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final ApiTask apiTask = new ApiTask(getActivity(), mQuery);
        final ContentRequest contentRequest = new ContentRequest();
        contentRequest.setTask(apiTask);
        contentRequest.setUri(ExampleContentProvider.RequestUris.BEER_REQUEST);
        execute(contentRequest);
    }

    @Override
    public void onRequestComplete() {
        mContentView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public void onRequestError() {
        mErrorView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
    }

    public interface Arguments {
        String QUERY = "QUERY";
    }
}
