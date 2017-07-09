package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.AddTeamDevPlanActivity;
import com.ingenuitymobile.edwardlynx.adapters.TeamProgressAdapter;
import com.ingenuitymobile.edwardlynx.api.models.TeamDevPlan;
import com.ingenuitymobile.edwardlynx.api.responses.TeamDevPlansResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 7/5/17.
 */

public class TeamProgressFragment extends BaseFragment {

  private View mainView;

  private ArrayList<TeamDevPlan> data;
  private TeamProgressAdapter    adapter;

  private TextView           emptyText;
  private SwipeRefreshLayout refreshLayout;

  public TeamProgressFragment() {
    data = new ArrayList<>();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
      LogUtil.e("AAA onCreateView TeamProgressFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_team_progress, container, false);
    initViews();
    LogUtil.e("AAA onCreateView TeamProgressFragment2");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    getData();
  }

  private void initViews() {
    final ImageView createImage = (ImageView) mainView.findViewById(R.id.image_create);
    final RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.list_team_progress);
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    adapter = new TeamProgressAdapter(data);
    recyclerView.setAdapter(adapter);

    createImage.setOnClickListener(listener);

    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);
  }

  private View.OnClickListener listener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Intent intent =  new Intent(getActivity(), AddTeamDevPlanActivity.class);
      startActivity(intent);
    }
  };

  private void getData() {
    subscription.add(Shared.apiClient.getTeamCategories(new Subscriber<TeamDevPlansResponse>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted ");
        refreshLayout.setRefreshing(false);
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
        refreshLayout.setRefreshing(false);
      }

      @Override
      public void onNext(TeamDevPlansResponse response) {
        data.clear();
        data.addAll(response.items);
        adapter.notifyDataSetChanged();
        emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
      }
    }));
  }

  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      getData();
    }
  };
}
