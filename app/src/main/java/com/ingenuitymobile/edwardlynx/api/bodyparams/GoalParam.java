package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
  @SerializedName("categoryId")
  public long   categoryId;
  @SerializedName("position")
  public int    position;
  @SerializedName("actions")
  public List<ActionParam> actions;

  public GoalParam() {
    actions = new ArrayList<>();
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
