package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class Survey {

  @SerializedName("_links")
  public Links   links;
  @SerializedName("id")
  public long    id;
  @SerializedName("name")
  public String  name;
  @SerializedName("type")
  public int     type;
  @SerializedName("lang")
  public String  lang;
  @SerializedName("startDate")
  public String  startDate;
  @SerializedName("endDate")
  public String  endDate;
  @SerializedName("enableAutoReminding")
  public boolean enableAutoReminding;
  @SerializedName("autoRemindingDate")
  public String  autoRemindingDate;
  @SerializedName("description")
  public String  description;
  @SerializedName("thankYouText")
  public String  thankYouText;
  @SerializedName("questionInfoText")
  public String  questionInfoText;
}
