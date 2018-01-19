package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.BreakdownAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Breakdown;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class BreakdownFragments extends BaseFragment {

  private View mainView;

  private List<Breakdown> breakdown;

  private BreakdownAdapter adapter;

  /**
   * Fragment to display the breakdown in the survey reports.
   */
  public BreakdownFragments() {
    breakdown = new ArrayList<>();
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
      LogUtil.e("AAA onCreateView BreakdownFragments");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_breakdown, container, false);
    LogUtil.e("AAA onCreateView BreakdownFragments");
    initViews();
    setData();
    return mainView;
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    final RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new BreakdownAdapter(breakdown);
    recyclerView.setAdapter(adapter);
  }

  /**
   * sets the data to be used in the fragment
   * @param breakdown the list of breakdowns
   */
  public void setDataSet(List<Breakdown> breakdown) {
    this.breakdown = breakdown;
    if (mainView != null) {
      setData();
    }
  }

  /**
   * updates the data to be displayed in the view
   */
  private void setData() {
    adapter.notifyDataSetChanged();
    mainView.findViewById(R.id.text_empty).setVisibility(
        breakdown.isEmpty() ? View.VISIBLE : View.GONE);
  }
}
