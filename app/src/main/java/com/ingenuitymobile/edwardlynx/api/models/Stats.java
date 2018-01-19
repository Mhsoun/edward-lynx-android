package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 4/11/17.
 * API model for stats.
 */

public class Stats {

  @SerializedName("invited")
  public int invited;
  @SerializedName("answered")
  public int answered;
}
