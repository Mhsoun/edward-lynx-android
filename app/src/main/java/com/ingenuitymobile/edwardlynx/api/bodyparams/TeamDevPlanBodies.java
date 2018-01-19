package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 7/16/17.
 * API model for team development plan bodies.
 */

public class TeamDevPlanBodies {

  @SerializedName("items")
  public List<TeamDevPlanBody> items;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
