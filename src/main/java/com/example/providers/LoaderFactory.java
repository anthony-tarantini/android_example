package com.example.providers;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;

import java.util.HashMap;
import java.util.Map;

public class LoaderFactory {
    private int mLoaderId = 1;
    private Map<Class<? extends Fragment>, Integer> mIds;

    public LoaderFactory(){
        mIds = new HashMap<>();
    }

    public int initLoader(final Activity activity, final Class<? extends Fragment> klass, final LoaderManager.LoaderCallbacks callbacks) {
        final LoaderManager loaderManager = activity.getLoaderManager();
        if (!mIds.containsKey(klass)){
            mIds.put(klass, mLoaderId++);
        }
        final Integer loaderId = mIds.get(klass);
        loaderManager.initLoader(loaderId, null, callbacks);
        return loaderId;
    }
}
