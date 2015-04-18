package com.example.tasks;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.example.TestExampleApplication;
import com.example.models.Product;
import com.example.networking.LCBOApiClient;
import com.example.providers.ExampleContentProvider;
import com.example.utils.ContentProviderUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import javax.inject.Inject;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class ApiTaskTest {

    @Inject
    LCBOApiClient mApiClient;

    @Inject
    LocalBroadcastManager mLocalBroadcastManager;

    @Before
    public void setup() {
        ((TestExampleApplication) Robolectric.application).inject(this);
        ContentProviderUtil.create();
    }

    @Test
    public void createIdentifier() {
        final ApiTask task = new ApiTask(Robolectric.application, "beer");
        final String identifier = task.createIdentifier();

        assertThat(identifier).isEqualTo("com.example.tasks.ApiTask/beer");
    }

    @Test
    public void executeNetworking_callsApiClient() throws Exception {
        final ApiTask task = new ApiTask(Robolectric.application, "beer");
        task.executeNetworking();
        verify(mApiClient).listProducts("beer");
    }

    @Test
    public void executeProcessing_insertsToDatabase() throws Exception {
        final ApiTask task = new ApiTask(Robolectric.application, "beer");
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
        task.processResponse(Arrays.asList(productOne, productTwo));

        final ContentResolver resolver = Robolectric.application.getContentResolver();
        final Cursor query = resolver.query(ExampleContentProvider.DatabaseUris.PRODUCT_TABLE_URI, null, null, null, null);
        assertThat(query).isNotNull();
        assertThat(query).hasCount(2);
        query.close();
    }

    @Test
    public void notifySuccess() {
        final ApiTask task = new ApiTask(Robolectric.application, "beer");
        final ContentObserver mockContentObserver = mock(ContentObserver.class);
        Robolectric.application.getContentResolver().registerContentObserver(ExampleContentProvider.RequestUris.BEER_REQUEST, true, mockContentObserver);
        task.notifySuccess();
        verify(mockContentObserver).dispatchChange(anyBoolean(), any(Uri.class));
    }

    @Test
    public void notifyFailure() {
        final ApiTask task = new ApiTask(Robolectric.application, "beer");
        task.notifyFailure();
        final Intent intent = new Intent();
        intent.setAction(ApiTask.ERROR);
        intent.setData(ExampleContentProvider.RequestUris.BEER_REQUEST);
        verify(mLocalBroadcastManager).sendBroadcast(intent);
    }
}