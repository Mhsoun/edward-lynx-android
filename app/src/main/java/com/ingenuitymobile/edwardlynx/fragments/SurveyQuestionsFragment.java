package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.SurveyQuestionsAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerBody;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 4/3/17.
 */

public class SurveyQuestionsFragment extends BaseFragment {

  private View                   mainView;
  private SurveyQuestionsAdapter adapter;
  private List<Question>         data;
  private List<AnswerBody>       bodies;
  private boolean                isEnabled;

  /**
   * Fragment to display the survey questions list.
   */
  public SurveyQuestionsFragment() {
    data = new ArrayList<>();
    bodies = new ArrayList<>();
    isEnabled = true;
  }

  private SurveyQuestionsAdapter.OnAnswerItemListener listener;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (mainView != null) {
      ViewGroup parent = (ViewGroup) mainView.getParent();
      if (parent == null) {
        parent = container;
      }
      parent.removeView(mainView);
      LogUtil.e("AAA onCreateView SurveyQuestionsFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_survey_questions, container, false);
    initViews();
    LogUtil.e("AAA onCreateView SurveyQuestionsFragment2");
    return mainView;
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    final RecyclerView questionsList = (RecyclerView) mainView.findViewById(R.id.list_questions);

    questionsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapter = new SurveyQuestionsAdapter(data, listener);
    questionsList.setAdapter(adapter);
    questionsList.setItemViewCacheSize(adapter.getItemCount());
    adapter.isEnabled(isEnabled);
  }

  /**
   * updates the data to be displayed in the fragment
   * @param data the list of questions to be displayed
   * @param bodies the list of answer bodies expected
   * @param listener the listener for answering an item
   * @param isEnabled indicator if the questions is for answering or viewing only
   */
  public void setData(List<Question> data,
      List<AnswerBody> bodies,
      SurveyQuestionsAdapter.OnAnswerItemListener listener,
      boolean isEnabled) {
    this.data = data;
    this.bodies = bodies;
    this.listener = listener;
    this.isEnabled = isEnabled;
    if (adapter != null) {
      adapter.isEnabled(isEnabled);
    }
  }
}
