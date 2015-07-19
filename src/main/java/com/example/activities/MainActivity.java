package com.example.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.R;
import com.example.dagger.Injector;
import com.example.fragments.ChoiceFragment;

public class MainActivity extends Activity implements Injector, ApplicationNavigator {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    navigateTo(ChoiceFragment.newInstance());
  }

  @Override
  public void inject(final Object object) {
    ((Injector) getApplication()).inject(object);
  }

  @Override
  public void navigateTo(final Fragment fragment) {
    final FragmentManager fragmentManager = getFragmentManager();
    fragmentManager
            .beginTransaction()
            .replace(R.id.activity_main_content_view, fragment)
            .commit();
  }
}
