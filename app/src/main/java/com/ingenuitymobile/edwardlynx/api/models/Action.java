package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 03/02/2017.
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
}
