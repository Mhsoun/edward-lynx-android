package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 5/29/17.
 * API model for blind spot item.
 */

public class BlindSpotItem {

  @SerializedName("id")
  public long   id;
  @SerializedName("title")
  public String title;
  @SerializedName("category")
  public String category;
  @SerializedName("answerType")
  public Answer answerType;
  @SerializedName("self")
  public int    self;
  @SerializedName("others")
  public int    others;
  @SerializedName("gap")
  public int    gap;
}
