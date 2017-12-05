package com.ingenuitymobile.edwardlynx.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;

import rx.subscriptions.CompositeSubscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mEmEnG-sKi on 14/12/2016.
 * Base activity to be used for all other activities with reusable functions and components.
 */

public class BaseActivity extends AppCompatActivity {

  protected CompositeSubscription subscription;
  protected Context               context;

  protected ProgressDialog progressDialog;

  public BaseActivity() {
    subscription = new CompositeSubscription();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage(getString(R.string.loading));
    progressDialog.setCancelable(false);
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

  /**
   * hideKeyboard hides the onscreen keyboard if it is shown
   */
  protected void hideKeyboard() {
    try {
      InputMethodManager inputManager = (InputMethodManager) getSystemService(
          Activity.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    } catch (Exception e) {}
  }

  /**
   * hideKeyboard hides the onscreen keyboard if it is shown
   */
  protected void hideKeyboard(View v) {
    try {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
          Activity.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    } catch (Exception e) {}
  }

  /**
   * broadcast receiver for receiving notification
   */
  private BroadcastReceiver notiffMain = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
    }
  };

  /**
   * listener to check if a view has focus and hides keyboard from view
   */
  protected View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
      if (!hasFocus) {
        hideKeyboard(v);
      }
    }
  };
}
