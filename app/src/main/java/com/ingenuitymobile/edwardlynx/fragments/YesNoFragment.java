package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.DetailedSummaryAdapter;
import com.ingenuitymobile.edwardlynx.adapters.YesNoAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DetailedSummary;
import com.ingenuitymobile.edwardlynx.api.models.YesNo;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 6/1/17.
 */

public class YesNoFragment extends BaseFragment {

  private View mainView;

  private List<YesNo> yesNos;

  private YesNoAdapter adapter;

  public YesNoFragment() {
    yesNos = new ArrayList<>();
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

    mainView = inflater.inflate(R.layout.fragment_yes_no, container, false);
    LogUtil.e("AAA onCreateView DetailedSummaryFragment");
    initViews();
    setData();
    return mainView;
  }

  private void initViews() {
    final RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new YesNoAdapter(yesNos);
    recyclerView.setAdapter(adapter);
  }

  public void setDataSet(List<YesNo> yesNos) {
    this.yesNos = yesNos;
    if (mainView != null) {
      setData();
    }
  }

  private void setData() {
    adapter.notifyDataSetChanged();
    mainView.findViewById(R.id.text_empty).setVisibility(
        yesNos.isEmpty() ? View.VISIBLE : View.GONE);
  }

}
