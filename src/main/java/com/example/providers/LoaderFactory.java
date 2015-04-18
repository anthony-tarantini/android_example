package com.example.providers;

import android.app.Activity;
import android.app.LoaderManager;

public class LoaderFactory {
    private int mLoaderId = 1;
    public LoaderFactory(){}

    public int createLoader(final Activity activity, final LoaderManager.LoaderCallbacks callbacks) {
        activity.getLoaderManager().initLoader(mLoaderId, null, callbacks);
        return mLoaderId++;
    }
}
