package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.AnswerFeedbackActivity;
import com.ingenuitymobile.edwardlynx.adapters.AllSurveysAdapter;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackAdapter;
import com.ingenuitymobile.edwardlynx.api.models.AllSurveys;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 4/3/17.
 */

public class AllSurveysFragment extends BaseFragment {

  private final static int NUM = 10;

  private View                  mainView;
  private ArrayList<AllSurveys> data;
  private ArrayList<AllSurveys> displayData;
  private AllSurveysAdapter     adapter;
  private TextView              emptyText;
  private SwipeRefreshLayout    refreshLayout;

  public AllSurveysFragment() {
    data = new ArrayList<>();
    displayData = new ArrayList<>();
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
      LogUtil.e("AAA onCreateView AllSurveysFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_survey_list, container, false);
    initViews();
    LogUtil.e("AAA onCreateView AllSurveysFragment2");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    getSurveys();
  }

  private void initViews() {
    final RecyclerView feedbackList = (RecyclerView) mainView.findViewById(R.id.list_survey);
    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    feedbackList.addItemDecoration(dividerItemDecoration);
    feedbackList.setHasFixedSize(true);
    feedbackList.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new AllSurveysAdapter(displayData, listener);
    feedbackList.setAdapter(adapter);

    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);
  }

  private void getSurveys() {
    subscription.add(Shared.apiClient.getSurveys(1, NUM, "answerable", new Subscriber<Surveys>() {
      @Override
      public void onCompleted() {
        adapter.notifyDataSetChanged();
        getInstantFeedbacks();
      }

      @Override
      public void onError(Throwable e) {
        refreshLayout.setRefreshing(false);
      }

      @Override
      public void onNext(final Surveys surveys) {
        data.clear();
        for (Survey survey : surveys.items) {
          data.add(new AllSurveys(survey, null));
        }
      }
    }));
  }

  private void getInstantFeedbacks() {
    LogUtil.e("AAA getData FeedbackRequestsFragment");
    subscription.add(
        Shared.apiClient.getInstantFeedbacks("to_answer", new Subscriber<FeedbacksResponse>() {
          @Override
          public void onCompleted() {
            refreshLayout.setRefreshing(false);
            setData();
          }

          @Override
          public void onError(Throwable e) {
            refreshLayout.setRefreshing(false);
          }

          @Override
          public void onNext(final FeedbacksResponse response) {
            for (Feedback feedback : response.items) {
              data.add(new AllSurveys(null, feedback));
            }
          }
        }));
  }

  private void setData() {
    displayData.clear();
    for (AllSurveys allSurveys : data) {
      if (allSurveys.survey != null) {
        if (allSurveys.survey.name.toLowerCase().contains(queryString.toLowerCase())) {
          displayData.add(allSurveys);
        }
      } else {
        if (!allSurveys.feedback.questions.isEmpty()) {
          if (allSurveys.feedback.questions.get(0).text.toLowerCase().contains(
              queryString.toLowerCase())) {
            displayData.add(allSurveys);
          }
        }
      }
    }

    adapter.notifyDataSetChanged();
    emptyText.setVisibility(displayData.isEmpty() ? View.VISIBLE : View.GONE);
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
    if (adapter != null && emptyText != null) {
      setData();
    }
  }

  private FeedbackAdapter.OnSelectFeedbackListener listener = new FeedbackAdapter
      .OnSelectFeedbackListener() {
    @Override
    public void onSelect(long id, String key) {
      Intent intent = new Intent(getActivity(), AnswerFeedbackActivity.class);
      intent.putExtra("id", id);
      startActivity(intent);
    }
  };

  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      getSurveys();
    }
  };
}
