package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.TeamResultParentAdapter;
import com.ingenuitymobile.edwardlynx.api.models.TeamReportItem;
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

  private View                    mainView;
  private List<TeamReportItem>    items;
  private TeamResultParentAdapter adapter;

  public TeamReportsFragment() {
    items = new ArrayList<>();
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

  @Override
  public void onResume() {
    super.onResume();
    getReports();
  }

  private void initViews() {
    final RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.list_reports);

    adapter = new TeamResultParentAdapter(items);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(adapter);
  }

  public void getReports() {
    subscription.add(Shared.apiClient.getSurveyReports()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<TeamReportResponse>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            Log.wtf("Team Report Error", e.getLocalizedMessage());
          }

          @Override
          public void onNext(TeamReportResponse teamReportResponse) {
            items.addAll(teamReportResponse.items);
            adapter.notifyDataSetChanged();
          }
        }));
  }

}
