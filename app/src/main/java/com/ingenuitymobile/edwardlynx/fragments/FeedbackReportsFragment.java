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
import com.ingenuitymobile.edwardlynx.activities.InstantFeedbackReportActivity;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackReportsAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 4/4/17.
 */

public class FeedbackReportsFragment extends BaseFragment {

  private View                   mainView;
  private ArrayList<Feedback>    data;
  private ArrayList<Feedback>    displayData;
  private FeedbackReportsAdapter adapter;
  private TextView               emptyText;
  private SwipeRefreshLayout     refreshLayout;

  /**
   * Fragment to display the feedback reports.
   */
  public FeedbackReportsFragment() {
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
      LogUtil.e("AAA onCreateView FeedbackReportsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_feedback_reports, container, false);
    initViews();
    LogUtil.e("AAA onCreateView FeedbackReportsFragment2");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    getData();
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    final RecyclerView feedbackList = (RecyclerView) mainView.findViewById(R.id.list_reports);
    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    emptyText.setText(getString(R.string.no_results));
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    feedbackList.addItemDecoration(dividerItemDecoration);
    feedbackList.setHasFixedSize(true);
    feedbackList.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new FeedbackReportsAdapter(displayData, listener);
    feedbackList.setAdapter(adapter);

    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);
  }

  /**
   * retrieves the answered feedback for the user from the API
   */
  private void getData() {
    LogUtil.e("AAA getData instant feedbaks");
    subscription.add(
        Shared.apiClient.getInstantFeedbacks("mine", new Subscriber<FeedbacksResponse>() {
          @Override
          public void onCompleted() {
            refreshLayout.setRefreshing(false);
            setData();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
            refreshLayout.setRefreshing(false);
          }

          @Override
          public void onNext(final FeedbacksResponse response) {
            LogUtil.e("AAA onNext ");
            data.clear();
            data.addAll(response.items);
          }
        }));
  }

  /**
   * sets the data to be used in the fragment
   */
  private void setData() {
    displayData.clear();
    for (Feedback feedback : data) {
      if (!feedback.questions.isEmpty()) {
        if (feedback.questions.get(0).text.toLowerCase().contains(
            queryString.toLowerCase())) {
          displayData.add(feedback);
        }
      }
    }

    adapter.notifyDataSetChanged();
    emptyText.setVisibility(displayData.isEmpty() ? View.VISIBLE : View.GONE);
  }

  /**
   * updates the query string and filters the feedback reports list
   * @param queryString the string for filtering the list
   */
  public void setQueryString(String queryString) {
    this.queryString = queryString;
    if (adapter != null && emptyText != null) {
      setData();
    }
  }

  /**
   * listener for selecting a feedback from the list
   */
  private FeedbackReportsAdapter.OnSelectFeedbackListener listener = new FeedbackReportsAdapter
      .OnSelectFeedbackListener() {
    @Override
    public void onSelect(long id, String key) {
      Intent intent = new Intent(getActivity(), InstantFeedbackReportActivity.class);
      intent.putExtra("id", id);
      startActivity(intent);
    }
  };

  /**
   * listener for the pull to refresh functionality
   */
  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      getData();
    }
  };
}
