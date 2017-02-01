package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 01/02/2017.
 */

public class GoalParam {

  @SerializedName("title")
  public String title;
  @SerializedName("description")
  public String description;
  @SerializedName("dueDate")
  public String dueDate;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
