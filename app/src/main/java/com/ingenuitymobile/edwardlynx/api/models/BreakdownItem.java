package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 5/31/17.
 */

public class BreakdownItem {

  public static final String OTHERS = "orangeColor";
  public static final String SELF   = "selfColor";

  @SerializedName("title")
  public String title;
  @SerializedName("percentage")
  public float  percentage;
  @SerializedName("role_style")
  public String roleStyle;
}
