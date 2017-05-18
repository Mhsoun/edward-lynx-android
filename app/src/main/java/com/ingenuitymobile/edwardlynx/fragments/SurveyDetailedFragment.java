package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by memengski on 5/18/17.
 */

public class SurveyDetailedFragment extends BaseFragment {

  private View mainView;

  private TextView titleText;
  private TextView evaluatedText;
  private TextView descriptionText;

  private Survey survey;

  public SurveyDetailedFragment() {
    survey = new Survey();
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
      LogUtil.e("AAA onCreateView SurveyDetailedFragment1");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_survey_details, container, false);
    initViews();
    setData();
    LogUtil.e("AAA onCreateView SurveyDetailedFragment2");
    return mainView;
  }

  private void initViews() {
    titleText = (TextView) mainView.findViewById(R.id.text_title);
    evaluatedText = (TextView) mainView.findViewById(R.id.text_evaluate);
    descriptionText = (TextView) mainView.findViewById(R.id.text_description);
  }

  public void setSurvey(Survey survey) {
    if (survey != null) {
      this.survey = survey;
      if (mainView != null) {
        setData();
      }
    }
  }

  private void setData() {
    titleText.setText(survey.getType(getActivity(), false) + ": " + survey.name);
    evaluatedText.setText(survey.personsEvaluatedText);
    descriptionText.setText(survey.description);
  }
}
