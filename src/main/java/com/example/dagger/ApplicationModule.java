package com.example.dagger;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.example.fragments.MainFragment;
import com.example.networking.LCBOApiClient;
import com.example.networking.NetworkInterceptor;
import com.example.providers.LoaderFactory;
import com.example.tasks.ApiTask;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(
        injects = {
                ApiTask.class,
                MainFragment.class
        },
        library = true,
        complete = false
)

public class ApplicationModule {

    private final Context mContext;

    public ApplicationModule(final Context context) {
        mContext = context;
    }
    @Provides @Singleton @ForApplication
    Context providesContext() {
        return mContext;
    }

    @Provides
    @Singleton
    RestAdapter providesRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint("https://lcboapi.com")
                .setRequestInterceptor(new NetworkInterceptor())
                .build();
    }

    @Provides
    @Singleton
    LCBOApiClient providesLcboApiClient(final RestAdapter adapter) {
        return adapter.create(LCBOApiClient.class);
    }

    @Provides
    @Singleton
    LocalBroadcastManager providesLocalBroadcastManager(@ForApplication final Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

    @Provides
    @Singleton
    LoaderFactory providesLoaderFactory() {
        return new LoaderFactory();
    }
}
