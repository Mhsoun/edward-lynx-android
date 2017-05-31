package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 6/1/17.
 */

public class YesNo {

  @SerializedName("id")
  public long   id;
  @SerializedName("category")
  public String category;
  @SerializedName("question")
  public String question;
  @SerializedName("yesPercentage")
  public int    yesPercentage;
  @SerializedName("noPercentage")
  public int    noPercentage;
  @SerializedName("naPercentage")
  public int    naPercentage;
}
