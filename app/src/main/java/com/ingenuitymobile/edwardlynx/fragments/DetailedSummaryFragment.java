package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.DetailedSummaryAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DetailedSummary;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class DetailedSummaryFragment extends BaseFragment {

  private View mainView;

  private List<DetailedSummary> detailedSummaries;

  private DetailedSummaryAdapter adapter;

  /**
   * Fragment to display detailed summary in the survey report.
   */
  public DetailedSummaryFragment() {
    detailedSummaries = new ArrayList<>();
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
      LogUtil.e("AAA onCreateView DetailedSummaryFragment");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_detailed_summary, container, false);
    LogUtil.e("AAA onCreateView DetailedSummaryFragment");
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

    adapter = new DetailedSummaryAdapter(detailedSummaries);
    recyclerView.setAdapter(adapter);
  }

  /**
   * sets the data to be used in the fragment
   * @param detailedSummaries the list of detailed summary for a survey report
   */
  public void setDataSet(List<DetailedSummary> detailedSummaries) {
    this.detailedSummaries = detailedSummaries;
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
        detailedSummaries.isEmpty() ? View.VISIBLE : View.GONE);
  }
}
