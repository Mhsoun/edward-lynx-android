package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseMainActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());

    initViews();
    setUserCrashlytics();
  }

  private void initViews() {

  }


  private void setUserCrashlytics() {
    // TODO: Use the current user's information
    // You can call any combination of these three methods
    Crashlytics.setUserIdentifier("12345");
    Crashlytics.setUserEmail("user@fabric.io");
    Crashlytics.setUserName("Test User");
  }
}

