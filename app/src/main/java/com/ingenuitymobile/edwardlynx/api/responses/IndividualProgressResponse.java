package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.IndividualProgress;

import java.util.List;

/**
 * Created by memengski on 7/6/17.
 * API model for individual progress response.
 */

public class IndividualProgressResponse {

  @SerializedName("items")
  public List<IndividualProgress> items;
}
