package com.example.activities;

import android.app.Fragment;
import android.app.FragmentManager;

import com.example.BuildConfig;
import com.example.R;
import com.example.fragments.ChoiceFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.ANDROID.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
public class MainActivityTest {

    @Test
    public void onCreate_insertsChoiceFragment() throws Exception {
        final MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .create().start().resume().get();
        final FragmentManager fragmentManager = activity.getFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentById(R.id.activity_main_content_view);
        assertThat(fragment).isNotNull();
        assertThat(fragment).isInstanceOf(ChoiceFragment.class);
    }

    @Test
    public void onNavigateTo_replacesMainFragment() {
        final MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .create().start().resume().get();
        final FragmentManager fragmentManager = activity.getFragmentManager();
        {
            final Fragment fragment = fragmentManager.findFragmentById(R.id.activity_main_content_view);
            assertThat(fragment).isNotNull();
            assertThat(fragment).isInstanceOf(ChoiceFragment.class);
        }
        activity.navigateTo(new TestFragment());
        {
            final Fragment fragment = fragmentManager.findFragmentById(R.id.activity_main_content_view);
            assertThat(fragment).isNotNull();
            assertThat(fragment).isInstanceOf(TestFragment.class);
        }
    }

    public static class TestFragment extends Fragment {}
}