package com.example.services;

import android.net.Uri;

import com.example.tasks.ApiTask;

public class ContentRequest {
    private String mPath;
    private Uri mUri;
    private String mSortOrder;
    private String[] mSelectionArgs;
    private String mSelection;
    private String[] mProjection;
    private ApiTask mTask;

    public String getPath() {
        return mPath;
    }

    public void setPath(final String path) {
        mPath = path;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(final Uri uri) {
        mUri = uri;
        mPath = uri.getPath();
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(final String sortOrder) {
        mSortOrder = sortOrder;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(final String[] selectionArgs) {
        mSelectionArgs = selectionArgs;
    }

    public String getSelection() {
        return mSelection;
    }

    public void setSelection(final String selection) {
        mSelection = selection;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public void setProjection(final String[] projection) {
        mProjection = projection;
    }

    public void setTask(final ApiTask task) {
        mTask = task;
    }

    public ApiTask getTask() {
        return mTask;
    }
}
