package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 6/1/17.
 */

public class IndividualRate {

  @SerializedName("highest")
  public Rate highest;
  @SerializedName("lowest")
  public Rate lowest;
}
