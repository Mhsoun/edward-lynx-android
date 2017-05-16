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
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.AnswerFeedbackActivity;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 3/31/17.
 */

public class FeedbackRequestsFragment extends BaseFragment {

  private View                mainView;
  private ArrayList<Feedback> data;
  private ArrayList<Feedback> displayData;
  private FeedbackAdapter     adapter;
  private TextView            emptyText;
  private SwipeRefreshLayout  refreshLayout;

  public FeedbackRequestsFragment() {
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
      LogUtil.e("AAA onCreateView FeedbackRequestsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_survey_list, container, false);
    initViews();
    LogUtil.e("AAA onCreateView FeedbackRequestsFragment2");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    getData();
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

    adapter = new FeedbackAdapter(displayData, listener);
    feedbackList.setAdapter(adapter);

    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);
  }

  private void getData() {
    LogUtil.e("AAA getData FeedbackRequestsFragment");
    subscription.add(
        Shared.apiClient.getInstantFeedbacks("to_answer", new Subscriber<FeedbacksResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            refreshLayout.setRefreshing(false);
            setData();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
            refreshLayout.setRefreshing(false);
            Toast.makeText(
                getActivity(),
                getString(R.string.cant_connect),
                Toast.LENGTH_SHORT
            ).show();
          }

          @Override
          public void onNext(final FeedbacksResponse response) {
            LogUtil.e("AAA onNext ");
            data.clear();
            data.addAll(response.items);
            refreshLayout.setRefreshing(false);
          }
        }));
  }

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
      getData();
    }
  };
}
