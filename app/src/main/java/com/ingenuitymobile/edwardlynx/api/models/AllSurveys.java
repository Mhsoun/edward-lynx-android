package com.ingenuitymobile.edwardlynx.api.models;

/**
 * Created by memengski on 4/3/17.
 * API model for all surveys.
 */

public class AllSurveys {
  public Survey   survey;
  public Feedback feedback;

  public AllSurveys(Survey survey, Feedback feedback) {
    this.survey = survey;
    this.feedback = feedback;
  }
}
