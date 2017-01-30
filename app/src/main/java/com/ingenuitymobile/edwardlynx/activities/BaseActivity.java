package com.ingenuitymobile.edwardlynx.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

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
}
