package com.example.providers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PatternMatcher;

import com.example.tasks.ApiTask;

public class ErrorReceiver extends BroadcastReceiver {

    private final String mPath;

    private ErrorListener mListener;

    public ErrorReceiver(final ErrorListener errorListener, final String path){
        mPath = path;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        mListener.onError();
    }

    public IntentFilter createIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ApiTask.ERROR);
        intentFilter.addDataScheme(ExampleContentProvider.CONTENT);
        intentFilter.addDataAuthority(ExampleContentProvider.AUTHORITY, null);
        intentFilter.addDataPath(mPath, PatternMatcher.PATTERN_PREFIX);
        return intentFilter;
    }

    public interface ErrorListener{
        void onError();
    }
}
