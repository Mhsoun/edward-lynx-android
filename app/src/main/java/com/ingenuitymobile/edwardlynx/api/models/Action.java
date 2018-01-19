package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 03/02/2017.
 * API model for action.
 */

public class Action extends Model {

  @SerializedName("title")
  public String title;
  @SerializedName("description")
  public String description;
  @SerializedName("checked")
  public int    checked;
  @SerializedName("position")
  public int    position;

  public boolean isCompleted;
  public boolean isAddAction;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
