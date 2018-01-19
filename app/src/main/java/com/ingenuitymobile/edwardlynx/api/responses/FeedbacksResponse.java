package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Links;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 * API model for feedback response.
 */

public class FeedbacksResponse extends Response {

  @SerializedName("_links")
  public Links links;

  @SerializedName("items")
  public List<Feedback> items;
}
