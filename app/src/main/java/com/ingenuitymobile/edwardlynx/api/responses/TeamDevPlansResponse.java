package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.TeamDevPlan;

import java.util.List;

/**
 * Created by memengski on 7/5/17.
 * API model for list of team development plans response.
 */

public class TeamDevPlansResponse {

  @SerializedName("items")
  public List<TeamDevPlan> items;
}
