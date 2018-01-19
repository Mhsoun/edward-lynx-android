package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 7/5/17.
 * API model for team development plans.
 */

public class TeamDevPlan extends Model {

  @SerializedName("categoryId")
  public long               categoryId;
  @SerializedName("ownerId")
  public long               ownerId;
  @SerializedName("name")
  public String             name;
  @SerializedName("position")
  public int                position;
  @SerializedName("checked")
  public boolean            checked;
  @SerializedName("visible")
  public int                visible;
  @SerializedName("progress")
  public float              progress;
  @SerializedName("goals")
  public List<TeamCategory> goals;
}
