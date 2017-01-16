package com.ingenuitymobile.edwardlynx.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import rx.Subscription;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class BaseFragment extends Fragment {

  protected Subscription subscription;

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }


  protected void hideKeyboard() {
    try {
      InputMethodManager inputManager = (InputMethodManager)
          getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    } catch (Exception e) {
      LogUtil.e("Error in hiding keyboard", e);
    }
  }

  protected void showKeyboard() {
    try {
      InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
          Activity.INPUT_METHOD_SERVICE);
      inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
          InputMethodManager.HIDE_IMPLICIT_ONLY);
    } catch (Exception e) {
      LogUtil.e("Error in hiding keyboard", e);
    }
  }
}
