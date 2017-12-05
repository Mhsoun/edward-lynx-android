package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 6/1/17.
 * API model for individual rate.
 */

public class IndividualRate {

  @SerializedName("highest")
  public Rate highest;
  @SerializedName("lowest")
  public Rate lowest;
}
