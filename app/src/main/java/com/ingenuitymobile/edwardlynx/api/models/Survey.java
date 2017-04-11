package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class Survey {

  public static final int OPEN        = 0;
  public static final int UNFINISHED  = 1;
  public static final int COMPLETED   = 2;
  public static final int NOT_INVITED = 3;


  @SerializedName("_links")
  public Links  links;
  @SerializedName("id")
  public long   id;
  @SerializedName("name")
  public String name;
  @SerializedName("type")
  public int    type;
  @SerializedName("lang")
  public String lang;
  @SerializedName("startDate")
  public String startDate;
  @SerializedName("endDate")
  public String endDate;
  @SerializedName("enableAutoReminding")
  public int    enableAutoReminding;
  @SerializedName("autoRemindingDate")
  public String autoRemindingDate;
  @SerializedName("description")
  public String description;
  @SerializedName("thankYouText")
  public String thankYouText;
  @SerializedName("questionInfoText")
  public String questionInfoText;
  @SerializedName("status")
  public int    status;
  @SerializedName("personsEvaluatedText")
  public String personsEvaluatedText;
  @SerializedName("stats")
  public Stats  stats;


  @SerializedName("key")
  public String key;
}
