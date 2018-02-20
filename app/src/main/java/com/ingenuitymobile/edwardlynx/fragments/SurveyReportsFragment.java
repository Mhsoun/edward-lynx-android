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

  /**
   * Fragment for displaying survey reports.
   */
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

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.list_survey);

    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    emptyText.setText(getString(R.string.no_results));
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

  /**
   * notifyAdapter notifies the list adapter of data changes
   */
  private void notifyAdapter() {
    emptyText.setVisibility(displayData.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  /**
   * retrieves all surveys from the API
   * @param isRefresh indication if the function call is for data refresh or appending
   */
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

  /**
   * updates the data to be displayed in the fragment
   */
  private void setData() {
    displayData.clear();
    for (Survey survey : data) {
      if (survey.name.toLowerCase().contains(queryString.toLowerCase())) {
        displayData.add(survey);
      }
    }

    notifyAdapter();
  }

  /**
   * filters the list using the query string
   * @param queryString the string for filtering
   */
  public void setQueryString(String queryString) {
    this.queryString = queryString;
    if (adapter != null && emptyText != null) {
      setData();
    }
  }

  /**
   * listener for selecting a feedback from the list, opens the
   * survey report activity
   */
  private SurveyReportsAdapter.OnSelectFeedbackListener listener =
      new SurveyReportsAdapter.OnSelectFeedbackListener() {
        @Override
        public void onSelect(long id, String surveyName, String surveyEndDate, int surveyInvited, int surveyAnswered) {
          Intent intent = new Intent(getActivity(), SurveyReportActivity.class);
          intent.putExtra("id", id);
          intent.putExtra("surveyName", surveyName);
          intent.putExtra("surveyEndDate", surveyEndDate);
          intent.putExtra("surveyInvited", surveyInvited);
          intent.putExtra("surveyAnswered", surveyAnswered);
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
      page = 1;
      getData(true);
    }
  };

  /**
   * listener for the scrolling of the list, retrieves the next page of data
   * from the API and appends it to the list
   */
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
