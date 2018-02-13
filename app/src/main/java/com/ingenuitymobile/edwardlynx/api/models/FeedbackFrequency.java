package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 * API model for feedback frequency.
 */

public class FeedbackFrequency {

  @SuppressWarnings("description")
  public String description;
  @SuppressWarnings("value")
  public String value;
  @SuppressWarnings("count")
  public int    count;
  @SerializedName("submissions")
  public List<Submission> submissions;
}
