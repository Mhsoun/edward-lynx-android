package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by memengski on 5/30/17.
 * Activity to be called when triggering a deep link action.
 */

public class DeeplinkingActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new Intent(context, SplashActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setData(getIntent().getData());
    startActivity(intent);
    finish();
  }
}
