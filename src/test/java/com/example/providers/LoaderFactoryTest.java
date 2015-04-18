package com.example.providers;

import android.app.Activity;
import android.app.LoaderManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class LoaderFactoryTest {

    private LoaderFactory mLoaderFactory;

    @Before
    public void setup() {
        mLoaderFactory = new LoaderFactory();
    }

    @Test
    public void createLoader_incrementsAndReturnsLoaderId() {
        final LoaderManager.LoaderCallbacks mockCallbacks = mock(LoaderManager.LoaderCallbacks.class);
        final Activity spyActivity = spy(new Activity());
        final LoaderManager mockLoaderManager = mock(LoaderManager.class);
        doReturn(mockLoaderManager).when(spyActivity).getLoaderManager();
        {
            final int loaderId = mLoaderFactory.createLoader(spyActivity, mockCallbacks);
            verify(mockLoaderManager).initLoader(1, null, mockCallbacks);
            assertThat(loaderId).isEqualTo(1);
        }
        {
            final int loaderId = mLoaderFactory.createLoader(spyActivity, mockCallbacks);
            verify(mockLoaderManager).initLoader(2, null, mockCallbacks);
            assertThat(loaderId).isEqualTo(2);
        }
    }
}