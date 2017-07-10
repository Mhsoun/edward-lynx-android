package com.ingenuitymobile.edwardlynx.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.TeamResultsAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 7/5/17.
 */

public class TeamReportsFragment extends BaseFragment {

  private View mainView;
  private Button testButton;
  private RecyclerView recyclerView;
  private ArrayList<Survey> results;
  private TeamResultsAdapter adapter;

  public TeamReportsFragment(){
    results = new ArrayList<>();
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

    results.add(survey);
    results.add(survey2);
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
      LogUtil.e("AAA onCreateView TeamReportsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_team_reports, container, false);
    initViews();
    LogUtil.e("AAA onCreateView TeamReportsFragment2");
    return mainView;
  }

  private void initViews() {
    testButton = (Button) mainView.findViewById(R.id.button_test);
    testButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        testButton.setText("hello");
      }
    });

    recyclerView = (RecyclerView) mainView.findViewById(R.id.list_reports);
    adapter = new TeamResultsAdapter(generateResults(results));
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(adapter);
  }

  private List<ParentListItem> generateResults(List<Survey> surveys) {
    List<ParentListItem> parentListItems = new ArrayList<>();
    for (Survey survey : surveys) {
      parentListItems.add(survey);
    }
    return parentListItems;
  }


}
