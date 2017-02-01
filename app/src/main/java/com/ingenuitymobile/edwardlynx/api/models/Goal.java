package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class Goal extends Model {

  @SerializedName("title")
  public String title;
  @SerializedName("description")
  public String description;
  @SerializedName("checked")
  public int    checked;
  @SerializedName("position")
  public int    position;
  @SerializedName("dueDate")
  public String dueDate;
  @SerializedName("reminderSent")
  public int    reminderSent;
}
