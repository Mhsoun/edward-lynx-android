package com.ingenuitymobile.edwardlynx.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import rx.Subscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mEmEnG-sKi on 14/12/2016.
 */

public class BaseActivity extends AppCompatActivity {

  protected Subscription subscription;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (subscription != null) {
      subscription.unsubscribe();
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
