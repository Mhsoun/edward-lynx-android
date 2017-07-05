package com.ingenuitymobile.edwardlynx.fragments;

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
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by memengski on 6/21/17.
 */

public class IndividualProgressFragment extends BaseFragment {

  private View               mainView;
  private TextView           emptyText;
  private SwipeRefreshLayout refreshLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (mainView != null) {
      ViewGroup parent = (ViewGroup) mainView.getParent();
      if (parent == null) {
        parent = container;
      }
      parent.removeView(mainView);
      LogUtil.e("AAA onCreateView AllReportsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_survey_list, container, false);
    initViews();
    LogUtil.e("AAA onCreateView AllReportsFragment2");
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
    emptyText.setText(getString(R.string.no_development_plans));
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    feedbackList.addItemDecoration(dividerItemDecoration);
    feedbackList.setHasFixedSize(true);
    feedbackList.setLayoutManager(new LinearLayoutManager(getActivity()));

    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);
  }

  private void getData() {

  }

  private void setData() {
//    emptyText.setVisibility(displayData.isEmpty() ? View.VISIBLE : View.GONE);
  }

  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      getData();
    }
  };

}
