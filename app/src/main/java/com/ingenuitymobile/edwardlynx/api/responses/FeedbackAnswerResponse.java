package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.FeedbackFrequency;
import com.ingenuitymobile.edwardlynx.api.models.Links;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 * API model for feedback answer response.
 */

public class FeedbackAnswerResponse extends Response {

  @SerializedName("_links")
  public Links                   links;
  @SerializedName("frequencies")
  public List<FeedbackFrequency> frequencies;
  @SerializedName("totalAnswers")
  public int                     totalAnswers;
  @SerializedName("totalAnonymousAnswers")
  public int                     totalAnonymousAnswers;
}
