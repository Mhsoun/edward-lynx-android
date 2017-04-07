package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.FeedbackFrequency;
import com.ingenuitymobile.edwardlynx.api.models.Links;
import com.ingenuitymobile.edwardlynx.api.models.SurveyFrequency;

import java.util.List;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyResultsResponse extends Response {

  @SerializedName("_links")
  public Links                 links;
  @SerializedName("frequencies")
  public List<SurveyFrequency> frequencies;
  @SerializedName("totalAnswers")
  public int                   totalAnswers;
}
