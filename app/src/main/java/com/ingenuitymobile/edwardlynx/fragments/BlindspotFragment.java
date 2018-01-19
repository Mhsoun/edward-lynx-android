package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.BlindspotAdapter;
import com.ingenuitymobile.edwardlynx.api.models.BlindSpotItem;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 6/1/17.
 */

public class BlindspotFragment extends BaseFragment {

  private View mainView;

  private List<BlindSpotItem> blindSpotItems;

  private BlindspotAdapter adapter;

  private String title;
  private String details;

  /**
   * Fragment to display blindspots.
   */
  public BlindspotFragment() {
    blindSpotItems = new ArrayList<>();
    title = "";
    details = "";
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

    mainView = inflater.inflate(R.layout.fragment_blindspot_attributes, container, false);
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

    adapter = new BlindspotAdapter(blindSpotItems);
    recyclerView.setAdapter(adapter);
  }

  /**
   * sets the data to be used in the fragment
   * @param blindSpotItems the list of blindspots
   * @param title the title to be displayed
   * @param details the details to be displayed
   */
  public void setDataSet(List<BlindSpotItem> blindSpotItems, String title, String details) {
    this.blindSpotItems = blindSpotItems;
    this.title = title;
    this.details = details;
    if (mainView != null) {
      setData();
    }
  }

  /**
   * updates the data to be displayed in the view
   */
  private void setData() {
    ((TextView) mainView.findViewById(R.id.text_title)).setText(title);
    ((TextView) mainView.findViewById(R.id.text_details)).setText(details);
    adapter.notifyDataSetChanged();
    mainView.findViewById(R.id.text_empty).setVisibility(
        blindSpotItems.isEmpty() ? View.VISIBLE : View.GONE);
  }
}
