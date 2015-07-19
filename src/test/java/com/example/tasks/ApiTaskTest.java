package com.example.tasks;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.example.BuildConfig;
import com.example.TestExampleApplication;
import com.example.models.Product;
import com.example.models.ProductResponse;
import com.example.networking.LCBOApiClient;
import com.example.providers.ExampleContentProvider;
import com.example.tables.TaskTable;
import com.example.utils.ContentProviderUtil;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Arrays;

import javax.inject.Inject;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants=BuildConfig.class)
public class ApiTaskTest {

    @Inject
    LCBOApiClient mApiClient;

    @Inject
    LocalBroadcastManager mLocalBroadcastManager;
    private Application mApplication;
    private ShadowApplication mShadowApplication;

    @Before
    public void setup() {
        mApplication = RuntimeEnvironment.application;
        mShadowApplication = Shadows.shadowOf(mApplication);
        ((TestExampleApplication) mApplication).inject(this);
        ContentProviderUtil.create();
    }

    @Test
    public void createIdentifier() {
        final ApiTask task = new ApiTask(mApplication, "beer");
        final String identifier = task.createIdentifier();

        assertThat(identifier).isEqualTo("com.example.tasks.ApiTask/beer");
    }

    @Test
    public void executeNetworking_callsApiClient() throws Exception {
        final ApiTask task = new ApiTask(mApplication, "beer");
        task.executeNetworking();
        verify(mApiClient).listProducts("beer");
    }

    @Test
    public void executeProcessing_insertsToDatabase() throws Exception {
        final ApiTask task = new ApiTask(mApplication, "beer");
        final Product productOne = Product.newBuilder()
                .name("Boneshaker")
                .origin("Toronto")
                .imageUrl("boneshakerImage")
                .build();
        final Product productTwo = Product.newBuilder()
                .name("Witchshark")
                .origin("Toronto")
                .imageUrl("witchSharkImage")
                .build();
        task.processResponse(ProductResponse.newBuilder().result(Arrays.asList(productOne, productTwo)).build());

        final ContentResolver resolver = mApplication.getContentResolver();
        final Cursor query = resolver.query(ExampleContentProvider.DatabaseUris.PRODUCT_TABLE_URI, null, null, null, null);
        assertThat(query).isNotNull();
        assertThat(query).hasCount(2);
        query.close();
    }

    @Test
    public void notifySuccess() {
        final ApiTask task = new ApiTask(mApplication, "beer");
        final ContentObserver mockContentObserver = mock(ContentObserver.class);
        mApplication.getContentResolver().registerContentObserver(ExampleContentProvider.RequestUris.BEER_REQUEST, true, mockContentObserver);
        task.notifySuccess();
        verify(mockContentObserver).dispatchChange(anyBoolean(), any(Uri.class));
    }

    @Test
    public void notifyFailure() {
        final ApiTask task = new ApiTask(mApplication, "beer");
        task.notifyFailure();
        final Intent intent = new Intent(ApiTask.ERROR);
        intent.addCategory(ExampleContentProvider.Paths.BEER_REQUEST);
        verify(mLocalBroadcastManager).sendBroadcast(intent);
    }

    @Test
    public void checkSyncNeeded() {
        final ApiTask task = new ApiTask(mApplication, "beer");

        final ContentValues contentValues = TaskTable.toContentValues(task);
        contentValues.put(TaskTable.Columns.LAST_SYNC_TIME, LocalDateTime.now().minusHours(1).minusMinutes(1).toString());
        mApplication.getContentResolver().insert(
                ExampleContentProvider.DatabaseUris.TASK_TABLE_URI,
                contentValues);

        assertThat(task.checkSyncNeeded()).isTrue();

        contentValues.put(TaskTable.Columns.LAST_SYNC_TIME, LocalDateTime.now().minusHours(1).plusMinutes(1).toString());
        mApplication.getContentResolver().insert(
                ExampleContentProvider.DatabaseUris.TASK_TABLE_URI,
                contentValues);

        assertThat(task.checkSyncNeeded()).isFalse();
    }
}