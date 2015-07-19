package com.example.providers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.tasks.ApiTask;

public class ErrorReceiver extends BroadcastReceiver {

    private final String mPath;

    private ErrorListener mErrorListener;

    public ErrorReceiver(final ErrorListener errorListener, final String path){
        mPath = path;
        mErrorListener = errorListener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        mErrorListener.onError();
    }

    public IntentFilter createIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter(ApiTask.ERROR);
        intentFilter.addCategory(mPath.substring(1));
        return intentFilter;
    }

    public interface ErrorListener{
        void onError();
    }
}
