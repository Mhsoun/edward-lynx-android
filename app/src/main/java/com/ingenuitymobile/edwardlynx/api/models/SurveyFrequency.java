package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyFrequency {

  @SerializedName("id")
  public long                    id;
  @SerializedName("counts")
  public List<FeedbackFrequency> counts;
}
