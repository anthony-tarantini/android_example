package com.example.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.R;
import com.example.dagger.Injector;

public class MainActivity extends Activity implements Injector {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void inject(final Object object) {
    ((Injector) getApplication()).inject(object);
  }
}
