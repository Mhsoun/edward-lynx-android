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
import com.ingenuitymobile.edwardlynx.activities.SurveyReportActivity;
import com.ingenuitymobile.edwardlynx.adapters.SurveyReportsAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyReportsFragment extends BaseFragment {

  private final static int NUM = 10;

  private View                 mainView;
  private ArrayList<Survey>    data;
  private ArrayList<Survey>    displayData;
  private SurveyReportsAdapter adapter;
  private TextView             emptyText;
  private SwipeRefreshLayout   refreshLayout;

  private int                 page;
  private LinearLayoutManager manager;

  private boolean loading;
  private int     previousTotal;

  public SurveyReportsFragment() {
    data = new ArrayList<>();
    displayData = new ArrayList<>();
    loading = true;
    page = 1;
    previousTotal = 0;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    mainView = inflater.inflate(R.layout.fragment_survey_list, container, false);
    initViews();
    LogUtil.e("AAA onCreateView SurveysListFragment");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    getData(true);
    LogUtil.e("AAA onResume SurveysListFragment");
  }

  private void initViews() {
    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.list_survey);

    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);

    manager = new LinearLayoutManager(getActivity());
    surveyList.setLayoutManager(manager);

    adapter = new SurveyReportsAdapter(displayData, listener);
    surveyList.setAdapter(adapter);

    refreshLayout.setOnRefreshListener(refreshListener);
    surveyList.setOnScrollListener(onScrollListener);

    refreshLayout.setRefreshing(true);
  }

  private void notifyAdapter() {
    emptyText.setVisibility(displayData.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  private void getData(final boolean isRefresh) {
    LogUtil.e("AAA getData survey");
    if (isRefresh) {
      page = 1;
      previousTotal = 0;
    } else {
      page++;
    }

    subscription.add(Shared.apiClient.getSurveys(page, NUM, null, new Subscriber<Surveys>() {
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
      }

      @Override
      public void onNext(final Surveys surveys) {
        LogUtil.e("AAA onNext ");
        if (isRefresh) {
          data.clear();
        }
        data.addAll(surveys.items);
      }
    }));
  }

  private void setData() {
    displayData.clear();
    for (Survey survey : data) {
      if (survey.name.toLowerCase().contains(queryString.toLowerCase())) {
        displayData.add(survey);
      }
    }

    notifyAdapter();
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
    if (adapter != null && emptyText != null) {
      setData();
    }
  }


  private SurveyReportsAdapter.OnSelectFeedbackListener listener =
      new SurveyReportsAdapter.OnSelectFeedbackListener() {
        @Override
        public void onSelect(long id, String key) {
          Intent intent = new Intent(getActivity(), SurveyReportActivity.class);
          intent.putExtra("id", id);
          startActivity(intent);
        }
      };

  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      page = 1;
      getData(true);
    }
  };

  private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);
      if (dy > 0) {
        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = manager.getItemCount();
        final int firstVisibleItem = manager.findFirstVisibleItemPosition();

        if (loading) {
          if (totalItemCount > previousTotal) {
            loading = false;
            previousTotal = totalItemCount;
          }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + NUM)) {
          getData(false);
          LogUtil.e("AAA loading");
          loading = true;
        }
      }
    }
  };
}
