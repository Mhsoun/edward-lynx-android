package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by memengski on 7/5/17.
 */

public class TeamReportsFragment extends BaseFragment {

  private View mainView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (mainView != null) {
      ViewGroup parent = (ViewGroup) mainView.getParent();
      if (parent == null) {
        parent = container;
      }
      parent.removeView(mainView);
      LogUtil.e("AAA onCreateView TeamReportsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_team_reports, container, false);
    initViews();
    LogUtil.e("AAA onCreateView TeamReportsFragment2");
    return mainView;
  }

  private void initViews() {

  }
}
