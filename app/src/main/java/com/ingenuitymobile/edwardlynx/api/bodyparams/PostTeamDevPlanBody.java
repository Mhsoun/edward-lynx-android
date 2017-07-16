package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 7/17/17.
 */

public class PostTeamDevPlanBody {

  @SerializedName("name")
  public String name;
  @SerializedName("lang")
  public String lang;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
