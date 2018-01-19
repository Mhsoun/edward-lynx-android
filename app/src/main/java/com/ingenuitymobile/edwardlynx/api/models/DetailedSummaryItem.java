package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 5/31/17.
 * API model for detailed summary item.
 */

public class DetailedSummaryItem {

  @SerializedName("question")
  public Object question;
  @SerializedName("percentage")
  public float  percentage;
  @SerializedName("percentage_1")
  public int    value;
}
