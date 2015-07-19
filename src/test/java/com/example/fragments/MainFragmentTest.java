package com.example.fragments;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.example.BuildConfig;
import com.example.activities.MainActivity;
import com.example.dagger.Injector;
import com.example.providers.ErrorReceiver;
import com.example.providers.ExampleContentProvider;
import com.example.providers.LoaderFactory;
import com.example.services.ApiIntentService;
import com.example.tasks.ApiTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.util.FragmentTestUtil;

import javax.inject.Inject;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//TODO: move some of these tests to parent fragment, and also test mQuery field
@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants=BuildConfig.class)
public class MainFragmentTest {
    @Inject
    LoaderFactory mLoaderFactory;

    @Inject
    LocalBroadcastManager mLocalBroadcastManager;

    @Before
    public void setup() {
        ((Injector) RuntimeEnvironment.application).inject(this);
    }

    @Test
    public void newInstance(){
        final MainFragment fragment = MainFragment.newInstance("test");
        assertThat(fragment).isNotNull();

        final String argument = fragment
                .getArguments()
                .getString(MainFragment.Arguments.QUERY);

        assertThat(argument).isEqualTo("test");
    }

    @Test
    public void onStart_sendsOffRequestToService() {
        FragmentTestUtil.startFragment(MainFragment.newInstance("testQuery"), MainActivity.class);

        final Intent intent = Shadows.shadowOf(RuntimeEnvironment.application).getNextStartedService();

        assertThat(intent).hasComponent(RuntimeEnvironment.application, ApiIntentService.class);
        assertThat(intent).hasExtra(ApiIntentService.Extras.TASK);
        assertThat(intent.getParcelableExtra(ApiIntentService.Extras.TASK)).isInstanceOf(ApiTask.class);
    }

    @Test
    public void onStart_initsLoader() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");
        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        verify(mLoaderFactory).initLoader(fragment.getActivity(), MainFragment.class, fragment);
    }

    @Test
    public void onStart_registersErrorReceiver() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");
        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        verify(mLocalBroadcastManager).registerReceiver(fragment.mErrorReceiver, fragment.mIntentFilter);
    }

    @Test
    public void onStop_unregistersErrorReceiver() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");
        FragmentTestUtil.startFragment(fragment, MainActivity.class);
        final ErrorReceiver errorReceiver = fragment.mErrorReceiver;

        fragment.onStop();

        verify(mLocalBroadcastManager).unregisterReceiver(errorReceiver);
        assertThat(fragment.mErrorReceiver).isNull();
    }

    @Test
    public void onAttach_injectsFragment() {
        final MainFragment fragment = new MainFragment();

        final MainActivity mockActivity = mock(MainActivity.class);
        fragment.onAttach(mockActivity);
        verify(mockActivity).inject(fragment);
    }

    @Test
    public void onCreateLoader_createsACursorLoader() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");
        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        final CursorLoader cursorLoader = fragment.onCreateLoader(0, Bundle.EMPTY);
        assertThat(cursorLoader.getUri()).isEqualTo(ExampleContentProvider.RequestUris.BEER_REQUEST);
    }

    @Test
    public void onLoaderFinished_swapsInNewCursor() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");
        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        fragment.mCursorAdapter = mock(SimpleCursorAdapter.class);
        final Cursor mockCursor = mock(Cursor.class);
        doReturn(5).when(mockCursor).getCount();
        fragment.onLoadFinished(null, mockCursor);
        verify(mockCursor).setNotificationUri(fragment.getActivity().getContentResolver(), ExampleContentProvider.RequestUris.BEER_REQUEST);
        verify(fragment.mCursorAdapter).swapCursor(mockCursor);
    }

    @Test
    public void onLoaderReset_swapsInNullCursor() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");
        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        fragment.mCursorAdapter = mock(SimpleCursorAdapter.class);
        fragment.onLoaderReset(null);
        verify(fragment.mCursorAdapter).swapCursor(null);
    }

    @Test
    public void onError_swapsInNullCursor() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");
        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        fragment.mCursorAdapter = mock(SimpleCursorAdapter.class);
        fragment.onError();
        verify(fragment.mCursorAdapter).swapCursor(null);
    }

    @Test
    public void onRequestComplete_showsContentView() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");

        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        fragment.mContentView.setVisibility(View.GONE);
        fragment.mLoadingView.setVisibility(View.VISIBLE);
        fragment.mErrorView.setVisibility(View.VISIBLE);

        fragment.onRequestComplete();

        assertThat(fragment.mContentView).hasVisibility(View.VISIBLE);
        assertThat(fragment.mLoadingView).hasVisibility(View.GONE);
        assertThat(fragment.mErrorView).hasVisibility(View.GONE);
    }

    @Test
    public void onRequestError_showsErrorView() {
        final MainFragment fragment = MainFragment.newInstance("testQuery");

        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        fragment.mContentView.setVisibility(View.VISIBLE);
        fragment.mLoadingView.setVisibility(View.VISIBLE);
        fragment.mErrorView.setVisibility(View.GONE);

        fragment.onRequestError();

        assertThat(fragment.mContentView).hasVisibility(View.GONE);
        assertThat(fragment.mLoadingView).hasVisibility(View.GONE);
        assertThat(fragment.mErrorView).hasVisibility(View.VISIBLE);
    }
}