package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.CreateDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.activities.CreateFeedbackActivity;
import com.ingenuitymobile.edwardlynx.activities.FeedbackRequestsActivity;
import com.ingenuitymobile.edwardlynx.activities.ReportsActivity;
import com.ingenuitymobile.edwardlynx.adapters.DevelopmentPlanAdapter;
import com.ingenuitymobile.edwardlynx.adapters.ReminderAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.models.Reminder;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.activities.MainActivity.OnChangeFragmentListener;
import com.ingenuitymobile.edwardlynx.activities.MainActivity.ChangeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class DashboardFragment extends BaseFragment {

  private View mainView;

  private RelativeLayout devPlanLayout;
  private RelativeLayout instantFeedbackLayout;
  private RelativeLayout lynxLayout;

  private TextView answerText;
  private TextView resultsText;
  private TextView inviteText;
  private TextView createText;

  private TextView seeMoreText;

  private TextView emptyReminderText;

  private OnChangeFragmentListener listener;

  private List<DevelopmentPlan>  devPlanData;
  private DevelopmentPlanAdapter devPlanAdapter;

  private List<Reminder>  reminders;
  private ReminderAdapter reminderAdapter;


  public static DashboardFragment newInstance(Context ctx, OnChangeFragmentListener listener) {
    DashboardFragment fragment = new DashboardFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.dashboard_bold));
    fragment.setArguments(bundle);
    fragment.listener = listener;
    return fragment;
  }

  public DashboardFragment() {
    devPlanData = new ArrayList<>();
    reminders = new ArrayList<>();

    Reminder reminder = new Reminder();
    reminder.type = Reminder.Type.GOAL.toString();
    reminder.description = "Provide Instant Feedback";
    reminders.add(reminder);

    reminder = new Reminder();
    reminder.type = Reminder.Type.INVITE_FEEDBACK.toString();
    reminder.description = "Provide Instant Feedback";
    reminders.add(reminder);

    reminder = new Reminder();
    reminder.type = Reminder.Type.ANSWER_FEEDBACK.toString();
    reminder.description = "Provide Instant Feedback";
    reminders.add(reminder);

    reminder = new Reminder();
    reminder.type = Reminder.Type.GOAL.toString();
    reminder.description = "Provide Instant Feedback";
    reminders.add(reminder);

    reminder = new Reminder();
    reminder.type = Reminder.Type.ANSWER_FEEDBACK.toString();
    reminder.description = "Provide Instant Feedback";
    reminders.add(reminder);

    reminder = new Reminder();
    reminder.type = Reminder.Type.INVITE_FEEDBACK.toString();
    reminder.description = "Provide Instant Feedback";
    reminders.add(reminder);

    reminder = new Reminder();
    reminder.type = Reminder.Type.GOAL.toString();
    reminder.description = "Provide Instant Feedback";
    reminders.add(reminder);
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
    getDevPlans();
  }

  private void initViews() {
    devPlanLayout = (RelativeLayout) mainView.findViewById(R.id.layout_dev_plan);
    instantFeedbackLayout = (RelativeLayout) mainView.findViewById(R.id.layout_instant_feedback);
    lynxLayout = (RelativeLayout) mainView.findViewById(R.id.layout_lynx_measurement);

    answerText = (TextView) mainView.findViewById(R.id.text_answer);
    resultsText = (TextView) mainView.findViewById(R.id.text_results);
    inviteText = (TextView) mainView.findViewById(R.id.text_invite);
    createText = (TextView) mainView.findViewById(R.id.text_create);

    seeMoreText = (TextView) mainView.findViewById(R.id.text_see_more);

    devPlanLayout.setOnClickListener(onClickListener);
    instantFeedbackLayout.setOnClickListener(onClickListener);
    lynxLayout.setOnClickListener(onClickListener);

    answerText.setOnClickListener(onClickListener);
    resultsText.setOnClickListener(onClickListener);
    inviteText.setOnClickListener(onClickListener);
    createText.setOnClickListener(onClickListener);
    seeMoreText.setOnClickListener(onClickListener);

    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.recycler_dev_plans);
    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);
    surveyList.setLayoutManager(new LinearLayoutManager(getActivity()));

    devPlanAdapter = new DevelopmentPlanAdapter(devPlanData);
    surveyList.setAdapter(devPlanAdapter);

    final RecyclerView reminderList = (RecyclerView) mainView.findViewById(R.id.recycler_reminders);
    reminderList.setLayoutManager(new LinearLayoutManager(getActivity()));
    emptyReminderText = (TextView) mainView.findViewById(R.id.text_empty_reminders);

    reminderAdapter = new ReminderAdapter(reminders);
    reminderList.setAdapter(reminderAdapter);
    LogUtil.e("AAA " + reminderAdapter.getItemCount());

    emptyReminderText.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);
  }

  private void setupViews() {
//    final int disabledColor = Color.parseColor("#3f244f");
//    if (Shared.user.type.equals(User.FEEDBACK_PROVIDER)) {
//      createDevelopmenPlanLayout.setBackgroundColor(disabledColor);
//      viewReportsLayout.setBackgroundColor(disabledColor);
//    } else if (Shared.user.type.equals(User.ANALYST)) {
//      createDevelopmenPlanLayout.setBackgroundColor(disabledColor);
//    }
  }

  private void getDevPlans() {
    subscription.add(
        Shared.apiClient.getDevelopmentPlans(new Subscriber<DevelopmentPlansResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            devPlanAdapter.notifyDataSetChanged();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
          }

          @Override
          public void onNext(final DevelopmentPlansResponse response) {
            LogUtil.e("AAA onNext ");
            devPlanData.clear();
            if (response.items.size() > 2) {
              devPlanData.add(response.items.get(0));
              devPlanData.add(response.items.get(2));
            } else {
              devPlanData.addAll(response.items);
            }
          }
        }));
  }

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.layout_dev_plan:
        break;
      case R.id.layout_instant_feedback:
        startActivity(new Intent(getActivity(), ReportsActivity.class));
        break;
      case R.id.layout_lynx_measurement:
        break;
      case R.id.text_answer:
        startActivity(new Intent(getActivity(), FeedbackRequestsActivity.class));
        break;
      case R.id.text_results:
        break;
      case R.id.text_invite:
        break;
      case R.id.text_create:
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setMessage(getString(R.string.create_new_dashboard));
        alertBuilder.setNegativeButton(getString(R.string.goals_dashboard),
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
        LogUtil.e("AAA onCLick");
        listener.onChange(ChangeFragment.DEVPLANS);
        break;
      }
    }
  };
}
