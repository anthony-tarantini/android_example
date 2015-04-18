package com.example.services;

import android.content.Context;
import android.content.Intent;

import com.example.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class ApiServiceTest {
    private ApiService mApiService;

    @Before
    public void setup() {
        mApiService = new ApiService();
    }

    @Test
    public void staticStartService_sendsOutServiceIntent() {
        final Task mockTask = mock(Task.class);
        ApiService.startService(Robolectric.application, mockTask);

        final Intent intent = Robolectric.getShadowApplication().getNextStartedService();

        assertThat(intent).hasComponent(Robolectric.application, ApiService.class);
        assertThat(intent).hasExtra(ApiService.Extras.TASK);
        assertThat((Task) intent.getParcelableExtra(ApiService.Extras.TASK)).isEqualTo(mockTask);
    }

    @Test
    public void onHandleIntent_setsTaskContextToItself() throws Exception {
        final TestTask mockTask = mock(TestTask.class);

        final Intent intent = new Intent(Robolectric.application, ApiService.class);
        intent.putExtra(ApiService.Extras.TASK, mockTask);

        mApiService.onHandleIntent(intent);

        verify(mockTask).setContext(mApiService);
    }


    @Test
    public void onHandleIntent_delegatesWorkToTaskSuccessfully() throws Exception {
        final Object response = new Object();

        final TestTask mockTask = mock(TestTask.class);
        doReturn(response).when(mockTask).executeNetworking();

        final Intent intent = new Intent(Robolectric.application, ApiService.class);
        intent.putExtra(ApiService.Extras.TASK, mockTask);

        mApiService.onHandleIntent(intent);

        verify(mockTask).executeNetworking();
        verify(mockTask).processResponse(response);
        verify(mockTask).notifySuccess();
    }

    @Test
    public void onHandleIntent_delegatesWorkToTaskUnsuccessfully() throws Exception {
        final TestTask mockTask = mock(TestTask.class);
        doThrow(new Exception("ERROR")).when(mockTask).executeNetworking();

        final Intent intent = new Intent(Robolectric.application, ApiService.class);
        intent.putExtra(ApiService.Extras.TASK, mockTask);

        mApiService.onHandleIntent(intent);

        verify(mockTask).executeNetworking();
        verify(mockTask).notifyFailure();
    }

    private abstract static class TestTask extends Task<Object> {
        public TestTask(final Context context) {
            super(context);
        }
    }
}