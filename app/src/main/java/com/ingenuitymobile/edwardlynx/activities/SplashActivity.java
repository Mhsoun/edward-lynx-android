package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ingenuitymobile.edwardlynx.R;

/**
 * Created by mEmEnG-sKi on 13/12/2016.
 */

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
    startActivity(intent);
    SplashActivity.this.finish();
  }
}
