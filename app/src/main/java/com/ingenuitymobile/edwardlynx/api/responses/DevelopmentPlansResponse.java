package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Links;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 * API model for development plans response.
 */

public class DevelopmentPlansResponse extends Response {

  @SerializedName("_links")
  public Links links;

  @SerializedName("items")
  public List<DevelopmentPlan> items;
}
