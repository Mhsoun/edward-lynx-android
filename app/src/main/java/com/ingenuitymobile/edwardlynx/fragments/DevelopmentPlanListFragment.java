package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.DevelopmentPlanAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class DevelopmentPlanListFragment extends BaseFragment {

  private View                   mainView;
  private List<DevelopmentPlan>  data;
  private List<DevelopmentPlan>  displayData;
  private DevelopmentPlanAdapter adapter;
  private TextView               emptyText;

  public boolean isFromTeam;

  public DevelopmentPlanListFragment() {
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
      LogUtil.e("AAA onCreateView SurveyListFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_dev_plan_list, container, false);
    initViews();
    LogUtil.e("AAA onCreateView SurveyListFragment2");
    return mainView;
  }

  private void initViews() {
    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.list_survey);
    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);
    surveyList.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new DevelopmentPlanAdapter(displayData, isFromTeam);
    surveyList.setAdapter(adapter);
  }

  public void setData(List<DevelopmentPlan> data) {
    if (data != null) {
      this.data = data;
      if (adapter != null && emptyText != null) {
        setDisplayData();
      }
    }
  }

  private void setDisplayData() {
    displayData.clear();
    for (DevelopmentPlan plan : data) {
      if (plan.name.toLowerCase().contains(queryString.toLowerCase())) {
        displayData.add(plan);
      }
    }

    if (adapter != null) {
      adapter.notifyDataSetChanged();
    }
    if (emptyText != null) {
      emptyText.setVisibility(displayData.isEmpty() ? View.VISIBLE : View.GONE);
    }
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
    if (adapter != null && emptyText != null) {
      setDisplayData();
    }
  }
}
