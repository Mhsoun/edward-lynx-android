package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.CreateDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.activities.CreateFeedbackActivity;
import com.ingenuitymobile.edwardlynx.activities.FeedbackRequestsActivity;
import com.ingenuitymobile.edwardlynx.activities.InviteSurveyActivity;
import com.ingenuitymobile.edwardlynx.activities.MainActivity.ChangeFragment;
import com.ingenuitymobile.edwardlynx.activities.MainActivity.OnChangeFragmentListener;
import com.ingenuitymobile.edwardlynx.activities.ReportsActivity;
import com.ingenuitymobile.edwardlynx.activities.TeamActivity;
import com.ingenuitymobile.edwardlynx.adapters.DevelopmentPlanAdapter;
import com.ingenuitymobile.edwardlynx.adapters.ReminderAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Reminder;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.DashboardResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.views.BadgeView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class DashboardFragment extends BaseFragment {

  private View mainView;

  private LinearLayout   topLayout;
  private RelativeLayout devPlanLayout;
  private RelativeLayout instantFeedbackLayout;
  private RelativeLayout lynxLayout;
  private RelativeLayout teamLayout;

  private RelativeLayout answerLayout;
  private TextView       resultsText;
  private TextView       inviteText;
  private TextView       createText;

  private TextView seeMoreText;

  private TextView emptyReminderText;
  private TextView emptyDevPlanText;

  private OnChangeFragmentListener listener;

  private List<DevelopmentPlan>  devPlanData;
  private DevelopmentPlanAdapter devPlanAdapter;

  private List<Reminder>  reminders;
  private ReminderAdapter reminderAdapter;

  private BadgeView badgeView;

  /**
   * Fragment to display the main page dashboard.
   * @param ctx the context to be used in the fragment
   * @param listener the listener for changing fragments
   * @return the created dashboard fragment
   */
  public static DashboardFragment newInstance(Context ctx, OnChangeFragmentListener listener) {
    DashboardFragment fragment = new DashboardFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.drawer_dashboard).toUpperCase());
    fragment.setArguments(bundle);
    fragment.listener = listener;
    return fragment;
  }

  public DashboardFragment() {
    devPlanData = new ArrayList<>();
    reminders = new ArrayList<>();
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
    LogUtil.e("AAA onResume Dashboard");
    getDasboard();
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    topLayout = (LinearLayout) mainView.findViewById(R.id.layout_top);
    devPlanLayout = (RelativeLayout) mainView.findViewById(R.id.layout_dev_plan);
    instantFeedbackLayout = (RelativeLayout) mainView.findViewById(R.id.layout_instant_feedback);
    lynxLayout = (RelativeLayout) mainView.findViewById(R.id.layout_lynx_measurement);
    teamLayout = (RelativeLayout) mainView.findViewById(R.id.layout_team);

    answerLayout = (RelativeLayout) mainView.findViewById(R.id.layout_answer);
    resultsText = (TextView) mainView.findViewById(R.id.text_results);
    inviteText = (TextView) mainView.findViewById(R.id.text_invite);
    createText = (TextView) mainView.findViewById(R.id.text_create);

    seeMoreText = (TextView) mainView.findViewById(R.id.text_see_more);

    devPlanLayout.setOnClickListener(onClickListener);
    instantFeedbackLayout.setOnClickListener(onClickListener);
    lynxLayout.setOnClickListener(onClickListener);
    teamLayout.setOnClickListener(onClickListener);

    answerLayout.setOnClickListener(onClickListener);
    resultsText.setOnClickListener(onClickListener);
    inviteText.setOnClickListener(onClickListener);
    createText.setOnClickListener(onClickListener);
    seeMoreText.setOnClickListener(onClickListener);

    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.recycler_dev_plans);
    surveyList.setNestedScrollingEnabled(false);
    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);
    surveyList.setLayoutManager(new LinearLayoutManager(getActivity()));

    devPlanAdapter = new DevelopmentPlanAdapter(devPlanData, false);
    surveyList.setAdapter(devPlanAdapter);

    final RecyclerView reminderList = (RecyclerView) mainView.findViewById(R.id.recycler_reminders);
    reminderList.setNestedScrollingEnabled(false);
    reminderList.setLayoutManager(new LinearLayoutManager(getActivity()));
    emptyReminderText = (TextView) mainView.findViewById(R.id.text_empty_reminders);
    emptyDevPlanText = (TextView) mainView.findViewById(R.id.text_empty_dev_plan);


    reminderAdapter = new ReminderAdapter(reminders, listener);
    reminderList.setAdapter(reminderAdapter);

    badgeView = new BadgeView(getActivity(), answerLayout);
  }

  /**
   * updates the fragment depending on the access availability, displays
   * team management when the logged in user is administrator or supervisor
   */
  private void setupViews() {
    teamLayout.setVisibility(View.GONE);
    topLayout.setWeightSum(3);
    if (Shared.user != null && !TextUtils.isEmpty(Shared.user.type) &&
        (Shared.user.type.equals(User.ADMIN) ||
            Shared.user.type.equals(User.SUPER_ADMIN) ||
            Shared.user.type.equals(User.SUPERVISOR))) {
      teamLayout.setVisibility(View.VISIBLE);
      topLayout.setWeightSum(4);
    }
  }

  /**
   * retrieves dashboard details of the user from the API
   */
  private void getDasboard() {
    subscription.add(
        Shared.apiClient.getUserDashboard(new Subscriber<DashboardResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            devPlanAdapter.notifyDataSetChanged();
            reminderAdapter.notifyDataSetChanged();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
          }

          @Override
          public void onNext(final DashboardResponse response) {
            LogUtil.e("AAA onNext ");
            devPlanData.clear();
            if (response.developmentPlans.size() > 2) {
              devPlanData.add(response.developmentPlans.get(0));
              devPlanData.add(response.developmentPlans.get(1));
            } else {
              devPlanData.addAll(response.developmentPlans);
            }

            emptyDevPlanText.setVisibility(devPlanData.isEmpty() ? View.VISIBLE : View.GONE);
            seeMoreText.setVisibility(devPlanData.isEmpty() ? View.GONE : View.VISIBLE);

            reminders.clear();
            reminders.addAll(response.reminders);
            emptyReminderText.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);

            badgeView.setText(String.valueOf(response.answerableCount));
            if (response.answerableCount == 0) {
              badgeView.hide();
            } else {
              badgeView.show();
            }
          }
        }));
  }

  /**
   * listener for clicking a menu item in the dashboard fragment
   */
  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.layout_dev_plan:
        listener.onChange(ChangeFragment.DEVPLANS);
        break;
      case R.id.layout_instant_feedback:
        listener.onChange(ChangeFragment.SURVEYS_FEEDBACK);
        break;
      case R.id.layout_lynx_measurement:
        listener.onChange(ChangeFragment.SURVEYS_LYNX);
        break;
      case R.id.layout_team:
        startActivity(new Intent(getActivity(), TeamActivity.class));
        break;
      case R.id.layout_answer:
        startActivity(new Intent(getActivity(), FeedbackRequestsActivity.class));
        break;
      case R.id.text_results:
        startActivity(new Intent(getActivity(), ReportsActivity.class));
        break;
      case R.id.text_invite:
        startActivity(new Intent(getActivity(), InviteSurveyActivity.class));
        break;
      case R.id.text_create:
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setMessage(getString(R.string.create_new_dashboard));
        alertBuilder.setNegativeButton(getString(R.string.dev_plan_dashboard),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), CreateDevelopmentPlanActivity.class));
              }
            });
        alertBuilder.setPositiveButton(getString(R.string.instant_feedback_dashboard),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), CreateFeedbackActivity.class));
              }
            });
        alertBuilder.create().show();
        break;
      case R.id.text_see_more:
        listener.onChange(ChangeFragment.DEVPLANS);
        break;
      }
    }
  };
}
