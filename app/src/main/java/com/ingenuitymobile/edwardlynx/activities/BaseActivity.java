package com.ingenuitymobile.edwardlynx.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import rx.subscriptions.CompositeSubscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mEmEnG-sKi on 14/12/2016.
 */

public class BaseActivity extends AppCompatActivity {

  protected CompositeSubscription subscription;
  protected Context               context;

  public BaseActivity() {
    subscription = new CompositeSubscription();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getApplicationContext();
  }

  @Override
  protected void onResume() {
    super.onResume();
    LocalBroadcastManager.getInstance(this).registerReceiver(notiffMain,
        new IntentFilter(Shared.UPDATE_DASHBOARD));
  }

  @Override
  protected void onPause() {
    super.onPause();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(notiffMain);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (subscription.hasSubscriptions()) {
      subscription.clear();
    }
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  protected void hideKeyboard() {
    try {
      InputMethodManager inputManager = (InputMethodManager) getSystemService(
          Activity.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    } catch (Exception e) {
      LogUtil.e("Error in hiding keyboard", e);
    }
  }

  private BroadcastReceiver notiffMain = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      LogUtil.e("AAA Receive from main");
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BaseActivity.this);
      alertBuilder.setTitle(intent.getExtras().getString("title"));
      alertBuilder.setMessage(intent.getExtras().getString("message"));
      alertBuilder.setPositiveButton(getString(android.R.string.ok), null);
      alertBuilder.create().show();
    }
  };
}
