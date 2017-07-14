package com.ingenuitymobile.edwardlynx.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.TeamResultParentAdapter;
import com.ingenuitymobile.edwardlynx.adapters.TeamResultsAdapter;
import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.TeamReport;
import com.ingenuitymobile.edwardlynx.api.models.TeamReportItem;
import com.ingenuitymobile.edwardlynx.api.models.TeamReportSurvey;
import com.ingenuitymobile.edwardlynx.api.responses.TeamReportResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by memengski on 7/5/17.
 */

public class TeamReportsFragment extends BaseFragment {

  private ApiClient apiClient;
  private View mainView;
  private RecyclerView recyclerView;
  private List<TeamReportSurvey> surveys;
  private List<TeamReportItem> items;
  private TeamResultParentAdapter adapter;

  public TeamReportsFragment(){
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    String[] reps = {"Report Link 1", "Report Link 2", "Report Link 3"};
    Survey survey = new Survey();
    Survey survey2 = new Survey();

    survey.reports = new ArrayList<>();
    survey2.reports = new ArrayList<>();

    // Placeholder data
    survey.name = "Survey Name";
    survey2.name = "Survey2 Name";

    // Add reports
    for(String rep : reps){
      survey.reports.add(rep);
      survey2.reports.add(rep);
    }

  }

  public void getReports(){
    subscription.add(apiClient.getSurveyReports()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(AndroidSchedulers.mainThread())
    .subscribe(new Subscriber<TeamReportResponse>() {
      @Override
      public void onCompleted() {
        adapter = new TeamResultParentAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
      }

      @Override
      public void onError(Throwable e) {
        Log.wtf("Team Report Error", e.getLocalizedMessage());
      }

      @Override
      public void onNext(TeamReportResponse teamReportResponse) {

        for (int i=0;i<teamReportResponse.items.size();i++) {
          items.add(teamReportResponse.items.get(i));
          for (int y=0;y<teamReportResponse.items.get(i).surveys.size();y++) {
            surveys.add(teamReportResponse.items.get(i).surveys.get(y));
            Log.wtf("user", teamReportResponse.items.get(i).name);
            Log.wtf("Surveys", teamReportResponse.items.get(i).surveys.get(y).name);
          }
        }

      }
    }));
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    apiClient = Shared.apiClient;
    surveys = new ArrayList<>();
    items = new ArrayList<>();

    if (mainView != null) {
      ViewGroup parent = (ViewGroup) mainView.getParent();
      if (parent == null) {
        parent = container;
      }
      parent.removeView(mainView);
      LogUtil.e("AAA onCreateView TeamReportsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_team_reports, container, false);
    initViews();
    LogUtil.e("AAA onCreateView TeamReportsFragment2");
    return mainView;
  }

  private void initViews() {
    getReports();
    recyclerView = (RecyclerView) mainView.findViewById(R.id.list_reports);

  }

  private List<ParentListItem> generateResults(List<TeamReportSurvey> surveys) {
    List<ParentListItem> parentListItems = new ArrayList<>();
    for (TeamReportSurvey survey : surveys) {
      parentListItems.add(survey);
    }
    return parentListItems;
  }


}
