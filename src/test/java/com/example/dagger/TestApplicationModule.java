package com.example.dagger;

import android.support.v4.content.LocalBroadcastManager;

import com.example.fragments.MainFragment;
import com.example.fragments.MainFragmentTest;
import com.example.networking.LCBOApiClient;
import com.example.providers.LoaderFactory;
import com.example.tasks.ApiTask;
import com.example.tasks.ApiTaskTest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module(
        injects = {
                ApiTask.class,
                ApiTaskTest.class,
                MainFragment.class,
                MainFragmentTest.class
        },
        library = true,
        complete = false
)

public class TestApplicationModule {
    @Provides
    @Singleton
    LCBOApiClient providesLCBOApiClient() {
        return mock(LCBOApiClient.class);
    }

    @Provides
    @Singleton
    LocalBroadcastManager providesLocalBroadcastManager() {
        return mock(LocalBroadcastManager.class);
    }

    @Provides
    @Singleton
    LoaderFactory providesLoaderFactory() {
        return mock(LoaderFactory.class);
    }
}
