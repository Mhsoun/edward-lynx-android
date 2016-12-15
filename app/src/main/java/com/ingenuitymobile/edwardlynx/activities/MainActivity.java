package com.ingenuitymobile.edwardlynx.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.ingenuitymobile.edwardlynx.R;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_main);

    // TODO: Move this to where you establish a user session
    logUser();
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public void forceCrash(View view) {
    throw new RuntimeException("This is a crash");
  }

  private void logUser() {
    // TODO: Use the current user's information
    // You can call any combination of these three methods
    Crashlytics.setUserIdentifier("12345");
    Crashlytics.setUserEmail("user@fabric.io");
    Crashlytics.setUserName("Test User");
  }
}

