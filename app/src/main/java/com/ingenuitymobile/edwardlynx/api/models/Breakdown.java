package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 5/31/17.
 * API model for breakdown.
 */

public class Breakdown {

  @SerializedName("category")
  public String              category;
  @SerializedName("dataPoints")
  public List<BreakdownItem> dataPoints;
}
