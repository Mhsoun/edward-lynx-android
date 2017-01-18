package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.SurveyAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class SurveysFragment extends BaseFragment {

  private View mainView;

  private ArrayList<Survey> data;
  private SurveyAdapter     adapter;

  public static SurveysFragment newInstance() {
    SurveysFragment fragment = new SurveysFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", "Surveys");
    fragment.setArguments(bundle);
    return fragment;
  }

  public SurveysFragment() {
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
      LogUtil.e("AAA onCreateView survey1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_surveys, container, false);
    initViews();
    LogUtil.e("AAA onCreateView survey2");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    getData();
    LogUtil.e("AAA onResume survey");
  }

  private void initViews() {
    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.list_survey);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);
    surveyList.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new SurveyAdapter(data);
    surveyList.setAdapter(adapter);
  }

  private void getData() {
    LogUtil.e("AAA getData survey");
    subscription = Shared.apiClient.getSurveys(1, new Subscriber<Surveys>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted ");
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
      }

      @Override
      public void onNext(final Surveys surveys) {
        LogUtil.e("AAA onNext ");
        data.clear();
        data.addAll(surveys.items);
      }
    });
  }
}
