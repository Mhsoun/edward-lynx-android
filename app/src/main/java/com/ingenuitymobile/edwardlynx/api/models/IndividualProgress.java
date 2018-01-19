package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 7/6/17.
 * API model for individual progress.
 */

public class IndividualProgress extends Model {

  @SerializedName("name")
  public String                name;
  @SerializedName("devPlans")
  public List<DevelopmentPlan> devPlans;
}
