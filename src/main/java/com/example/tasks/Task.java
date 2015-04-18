package com.example.tasks;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public abstract class Task<T> implements Parcelable {
    public static final String ERROR = "error";

    public abstract String createIdentifier();
    public abstract T executeNetworking() throws Exception;
    public abstract void processResponse(final T response) throws Exception;
    public abstract void notifySuccess();
    public abstract void notifyFailure();
    public abstract void inject();

    protected final String mIdentifier;
    protected Context mContext;

    public Task(final Context context) {
        mContext = context;
        mIdentifier = createIdentifier();
        inject();
    }

    public Task(final Parcel parcel) {
        mIdentifier = parcel.readString();
    }

    public void setContext(final Context context) {
        mContext = context;
        inject();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(mIdentifier);
    }
}
