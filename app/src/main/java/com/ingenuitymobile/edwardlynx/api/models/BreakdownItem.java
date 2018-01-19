package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 5/31/17.
 * API model for breakdown item.
 */

public class BreakdownItem {

  public static final String ORANGE_COLOR = "orangeColor";
  public static final String SELF_COLOR   = "selfColor";
  public static final String OTHERS_COLOR = "otherColor";

  @SerializedName("title")
  public String title;
  @SerializedName("percentage")
  public float  percentage;
  @SerializedName("role_style")
  public String roleStyle;
}
