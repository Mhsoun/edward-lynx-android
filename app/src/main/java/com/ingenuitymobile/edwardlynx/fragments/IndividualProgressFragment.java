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
import com.ingenuitymobile.edwardlynx.activities.AddIndividualUserActivity;
import com.ingenuitymobile.edwardlynx.adapters.IndividualProgressAdapter;
import com.ingenuitymobile.edwardlynx.api.models.IndividualProgress;
import com.ingenuitymobile.edwardlynx.api.responses.IndividualProgressResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 6/21/17.
 */

public class IndividualProgressFragment extends BaseFragment {

  private View               mainView;
  private TextView           emptyText;
  private SwipeRefreshLayout refreshLayout;

  private ArrayList<IndividualProgress> data;
  private IndividualProgressAdapter     adapter;

  public IndividualProgressFragment() {
    data = new ArrayList<>();
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
      LogUtil.e("AAA onCreateView AllReportsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_individual_progress, container, false);
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
    final RecyclerView recyclerView = (RecyclerView) mainView.findViewById(
        R.id.list_individual_progress);
    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    emptyText.setText(getString(R.string.no_development_plans));
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new IndividualProgressAdapter(data);
    recyclerView.setAdapter(adapter);

    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);

    mainView.findViewById(R.id.image_create).setOnClickListener(onClickListener);
  }

  private void getData() {
    subscription.add(Shared.apiClient.getIndividualProgress(
        new Subscriber<IndividualProgressResponse>() {
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
          public void onNext(IndividualProgressResponse response) {
            data.clear();
            data.addAll(response.items);
          }
        }));
  }

  private void setData() {
    adapter.notifyDataSetChanged();
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
  }

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      startActivity(new Intent(getActivity(), AddIndividualUserActivity.class));
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
