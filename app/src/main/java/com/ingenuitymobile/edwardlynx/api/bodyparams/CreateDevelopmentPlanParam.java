package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 01/02/2017.
 */

public class CreateDevelopmentPlanParam {

  @SerializedName("name")
  public String          name;
  @SerializedName("goals")
  public List<GoalParam> goals;

  public CreateDevelopmentPlanParam() {
    goals = new ArrayList<>();
  }


  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
