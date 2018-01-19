package com.ingenuitymobile.edwardlynx.adapters;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.QuestionRate;
import com.ingenuitymobile.edwardlynx.fragments.BaseFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 6/1/17.
 */

public class QuestionRateFragment extends BaseFragment {

  private View mainView;

  private List<QuestionRate> questionRates;

  private QuestionRateAdapter adapter;

  private String title;

  /**
   * Fragment for displaying question rates.
   */
  public QuestionRateFragment() {
    questionRates = new ArrayList<>();
    title = "";
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
      LogUtil.e("AAA onCreateView QuestionRateFragment");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_blindspot_attributes, container, false);
    LogUtil.e("AAA onCreateView QuestionRateFragment");
    initViews();
    setData();
    return mainView;
  }

  private void initViews() {
    final RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new QuestionRateAdapter(questionRates);
    recyclerView.setAdapter(adapter);
  }

  public void setDataSet(List<QuestionRate> questionRates, String title) {
    this.questionRates = questionRates;
    this.title = title;
    if (mainView != null) {
      setData();
    }
  }

  private void setData() {
    ((TextView) mainView.findViewById(R.id.text_title)).setText(title);
    mainView.findViewById(R.id.text_details).setVisibility(View.GONE);
    adapter.notifyDataSetChanged();
    mainView.findViewById(R.id.text_empty).setVisibility(
        questionRates.isEmpty() ? View.VISIBLE : View.GONE);
  }
}
