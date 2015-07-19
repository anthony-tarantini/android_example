package com.example.services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.BuildConfig;
import com.example.tasks.SyncStrategy;
import com.example.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants=BuildConfig.class)
public class ApiIntentServiceTest {
    private ApiIntentService mApiIntentService;
    private TestTask mMockTask;
    private SyncStrategy mMockStrategy;
    private Application mApplication;
    private ShadowApplication mShadowApplication;

    @Before
    public void setup() {
        mApiIntentService = new ApiIntentService();
        mMockTask = mock(TestTask.class);
        mMockStrategy = mock(SyncStrategy.class);
        mApplication = RuntimeEnvironment.application;
        mShadowApplication = Shadows.shadowOf(mApplication);
        doReturn(mMockStrategy).when(mMockTask).getSyncStrategy();
        doReturn("testTask").when(mMockTask).getIdentifier();
    }

    @Test
    public void staticStartService_sendsOutServiceIntent() {
        ApiIntentService.startService(mApplication, mMockTask);

        final Intent intent = mShadowApplication.getNextStartedService();

        assertThat(intent).hasComponent(mApplication, ApiIntentService.class);
        assertThat(intent).hasExtra(ApiIntentService.Extras.TASK);
        assertThat((Task) intent.getParcelableExtra(ApiIntentService.Extras.TASK)).isEqualTo(mMockTask);
    }

    @Test
    public void onHandleIntent_setsTaskContextToItself() throws Exception {
        final Intent intent = new Intent(mApplication, ApiIntentService.class);
        intent.putExtra(ApiIntentService.Extras.TASK, mMockTask);

        mApiIntentService.onHandleIntent(intent);

        verify(mMockTask).setContext(mApiIntentService);
    }

    @Test
    public void onHandleIntent_delegatesWorkToTaskSuccessfully() throws Exception {
        final Object response = new Object();

        doReturn(response).when(mMockTask).executeNetworking();
        doReturn(true).when(mMockStrategy).checkSyncNeeded();

        final Intent intent = new Intent(mApplication, ApiIntentService.class);
        intent.putExtra(ApiIntentService.Extras.TASK, mMockTask);

        mApiIntentService.onHandleIntent(intent);

        verify(mMockStrategy).checkSyncNeeded();
        verify(mMockTask).executeNetworking();
        verify(mMockTask).processResponse(response);
        verify(mMockTask).notifySuccess();
    }

    @Test
    public void onHandleIntent_delegatesWorkToTaskUnsuccessfully() throws Exception {
        doThrow(new Exception("ERROR")).when(mMockTask).executeNetworking();
        doReturn(true).when(mMockStrategy).checkSyncNeeded();

        final Intent intent = new Intent(mApplication, ApiIntentService.class);
        intent.putExtra(ApiIntentService.Extras.TASK, mMockTask);

        mApiIntentService.onHandleIntent(intent);

        verify(mMockStrategy).checkSyncNeeded();
        verify(mMockTask).executeNetworking();
        verify(mMockTask).notifyFailure();
    }

    @Test
    public void onHandleIntent_doesntMakeRequestWhenNoSyncNeeded() {
        doReturn(false).when(mMockStrategy).checkSyncNeeded();

        final Intent intent = new Intent(mApplication, ApiIntentService.class);
        intent.putExtra(ApiIntentService.Extras.TASK, mMockTask);

        mApiIntentService.onHandleIntent(intent);

        verify(mMockTask).setContext(any(Context.class));
        verify(mMockTask).getSyncStrategy();
        verify(mMockStrategy).checkSyncNeeded();
        verifyNoMoreInteractions(mMockTask);
    }

    private abstract static class TestTask extends Task<Object> {
        public TestTask(final Context context) {
            super(context);
        }
    }
}