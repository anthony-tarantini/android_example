package com.example.providers;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;

import com.example.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants=BuildConfig.class)
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
            final int loaderId = mLoaderFactory.initLoader(spyActivity, TestFragmentOne.class, mockCallbacks);
            verify(mockLoaderManager).initLoader(1, null, mockCallbacks);
            assertThat(loaderId).isEqualTo(1);
        }
        reset(mockLoaderManager);
        {
            final int loaderId = mLoaderFactory.initLoader(spyActivity, TestFragmentTwo.class, mockCallbacks);
            verify(mockLoaderManager).initLoader(2, null, mockCallbacks);
            assertThat(loaderId).isEqualTo(2);
        }
    }

    @Test
    public void initLoaderForSameClass_returnsSameLoader() {
        final LoaderManager.LoaderCallbacks mockCallbacks = mock(LoaderManager.LoaderCallbacks.class);
        final Activity spyActivity = spy(new Activity());
        final LoaderManager mockLoaderManager = mock(LoaderManager.class);
        doReturn(mockLoaderManager).when(spyActivity).getLoaderManager();
        {
            final int loaderId = mLoaderFactory.initLoader(spyActivity, TestFragmentOne.class, mockCallbacks);
            verify(mockLoaderManager).initLoader(1, null, mockCallbacks);
            assertThat(loaderId).isEqualTo(1);
        }
        reset(mockLoaderManager);
        {
            final int loaderId = mLoaderFactory.initLoader(spyActivity, TestFragmentOne.class, mockCallbacks);
            verify(mockLoaderManager).initLoader(1, null, mockCallbacks);
            assertThat(loaderId).isEqualTo(1);
        }
    }

    public static class TestFragmentOne extends Fragment {
    }

    public static class TestFragmentTwo extends Fragment {
    }
}