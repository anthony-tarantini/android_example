package com.example.fragments;

import android.content.Intent;

import com.example.activities.MainActivity;
import com.example.dagger.Injector;
import com.example.providers.LoaderFactory;
import com.example.services.ApiService;
import com.example.tasks.ApiTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.FragmentTestUtil;

import javax.inject.Inject;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class MainFragmentTest {

    @Inject
    LoaderFactory mLoaderFactory;

    @Before
    public void setup() {
        ((Injector) Robolectric.application).inject(this);
    }

    @Test
    public void onStart_sendsOffRequestToService() {
        FragmentTestUtil.startFragment(new MainFragment(), MainActivity.class);

        final Intent intent = Robolectric.getShadowApplication().getNextStartedService();

        assertThat(intent).hasComponent(Robolectric.application, ApiService.class);
        assertThat(intent).hasExtra(ApiService.Extras.TASK);
        assertThat(intent.getParcelableExtra(ApiService.Extras.TASK)).isInstanceOf(ApiTask.class);
    }

    @Test
    public void onStart_createsLoader() {
        final MainFragment fragment = new MainFragment();
        FragmentTestUtil.startFragment(fragment, MainActivity.class);

        verify(mLoaderFactory).createLoader(fragment.getActivity(), fragment);
    }
}