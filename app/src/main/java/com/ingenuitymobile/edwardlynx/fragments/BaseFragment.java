package com.ingenuitymobile.edwardlynx.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class BaseFragment extends Fragment {

  protected CompositeSubscription subscription;

  protected String queryString;

  protected ProgressDialog progressDialog;

  public BaseFragment() {
    subscription = new CompositeSubscription();
    queryString = "";
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    progressDialog = new ProgressDialog(getActivity());
    progressDialog.setMessage(getString(R.string.loading));
    progressDialog.setCancelable(false);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (subscription.hasSubscriptions()) {
      subscription.clear();
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
