package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.CreateDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.activities.CreateFeedbackActivity;
import com.ingenuitymobile.edwardlynx.activities.FeedbackRequestsActivity;
import com.ingenuitymobile.edwardlynx.activities.ReportsActivity;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class DashboardFragment extends BaseFragment {

  private View           mainView;
  private RelativeLayout createFeedbackLayout;
  private RelativeLayout createDevelopmenPlanLayout;
  private RelativeLayout viewReportsLayout;
  private RelativeLayout viewSurveyLayout;
  private RelativeLayout feedback360Layout;
  private RelativeLayout newReportsLayout;
  private RelativeLayout instantFeedbackLayout;

  public static DashboardFragment newInstance() {
    DashboardFragment fragment = new DashboardFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", "DASHBOARD");
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (mainView != null) {
      ViewGroup parent = (ViewGroup) mainView.getParent();
      if (parent == null) {
        parent = container;
      }
      parent.removeView(mainView);
      LogUtil.e("AAA onCreateView dash1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);
    LogUtil.e("AAA onCreateView dash2");
    initViews();
    setupViews();
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    LogUtil.e("AAA onResume dashboard");
  }

  @Override
  public void onPause() {
    super.onPause();
    LogUtil.e("AAA  onPause dashboard");
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    LogUtil.e("AAA  onDestroyView dashboard");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    LogUtil.e("AAA  onDestroy dashboard");
  }

  @Override
  public void onDetach() {
    super.onDetach();
    LogUtil.e("AAA  onDetach dashboard");
  }

  @Override
  public void onStart() {
    super.onStart();
    LogUtil.e("AAA  onStart dashboard");
  }

  @Override
  public void onStop() {
    super.onStop();
    LogUtil.e("AAA  onStop dashboard ");
  }

  private void initViews() {
    createFeedbackLayout = (RelativeLayout) mainView.findViewById(R.id.layout_create_feedback);
    createDevelopmenPlanLayout = (RelativeLayout) mainView.findViewById(
        R.id.layout_create_development_plan);
    viewReportsLayout = (RelativeLayout) mainView.findViewById(R.id.layout_view_report);
    viewSurveyLayout = (RelativeLayout) mainView.findViewById(R.id.layout_view_survey);
    feedback360Layout = (RelativeLayout) mainView.findViewById(R.id.layout_360_feedback);
    newReportsLayout = (RelativeLayout) mainView.findViewById(R.id.layout_new_report);
    instantFeedbackLayout = (RelativeLayout) mainView.findViewById(R.id.layout_instant_feedback);

    createFeedbackLayout.setOnClickListener(onClickListener);
    instantFeedbackLayout.setOnClickListener(onClickListener);
    viewReportsLayout.setOnClickListener(onClickListener);
    createDevelopmenPlanLayout.setOnClickListener(onClickListener);
  }

  private void setupViews() {
    final int disabledColor = Color.parseColor("#3f244f");
    if (Shared.user.type.equals(User.FEEDBACK_PROVIDER)) {
      createDevelopmenPlanLayout.setBackgroundColor(disabledColor);
      viewReportsLayout.setBackgroundColor(disabledColor);
    } else if (Shared.user.type.equals(User.ANALYST)) {
      createDevelopmenPlanLayout.setBackgroundColor(disabledColor);
    }
  }

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.layout_create_feedback:
        startActivity(new Intent(getActivity(), CreateFeedbackActivity.class));
        break;
      case R.id.layout_instant_feedback:
        startActivity(new Intent(getActivity(), FeedbackRequestsActivity.class));
        break;
      case R.id.layout_view_report:
        startActivity(new Intent(getActivity(), ReportsActivity.class));
        break;
      case R.id.layout_create_development_plan:
        startActivity(new Intent(getActivity(), CreateDevelopmentPlanActivity.class));
        break;
      }
    }
  };
}
