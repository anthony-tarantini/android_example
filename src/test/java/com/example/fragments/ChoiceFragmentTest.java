package com.example.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.example.BuildConfig;
import com.example.R;
import com.example.activities.ApplicationNavigator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.FragmentTestUtil;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants=BuildConfig.class)
public class ChoiceFragmentTest {

    @Test
    public void newInstance() {
        assertThat(ChoiceFragment.newInstance()).isNotNull();
    }

    @Test
    public void onClick_startsNewFragment() {
        final ChoiceFragment fragment = ChoiceFragment.newInstance();
        FragmentTestUtil.startFragment(fragment, TestActivity.class);
        fragment.getView().findViewById(R.id.fragment_choice_beer).performClick();
        final TestActivity activity = (TestActivity) fragment.getActivity();
        assertThat(activity.mFragment).isInstanceOf(MainFragment.class);
        final Bundle arguments = activity.mFragment.getArguments();
        assertThat(arguments.getString(MainFragment.Arguments.QUERY)).isEqualTo("beer");
    }

    public static class TestActivity extends Activity implements ApplicationNavigator {
        private Fragment mFragment;

        @Override
        public void navigateTo(final Fragment fragment) {

            mFragment = fragment;
        }
    }
}