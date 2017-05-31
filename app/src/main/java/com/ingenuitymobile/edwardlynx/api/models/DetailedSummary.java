package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class DetailedSummary {

  @SerializedName("category")
  public String                    category;
  @SerializedName("dataPoints")
  public List<DetailedSummaryItem> dataPoints;
}
