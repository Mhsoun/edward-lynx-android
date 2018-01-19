package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 5/29/17.
 * API model for blindspot.
 */

public class BlindSpot {

  @SerializedName("overestimated")
  public List<BlindSpotItem> overestimated;
  @SerializedName("underestimated")
  public List<BlindSpotItem> underestimated;
}
