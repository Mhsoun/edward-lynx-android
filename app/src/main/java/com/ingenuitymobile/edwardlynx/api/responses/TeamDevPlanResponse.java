package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.TeamCategory;

import java.util.List;

/**
 * Created by memengski on 7/17/17.
 */

public class TeamDevPlanResponse {

  @SerializedName("id")
  public long               id;
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
  public boolean            visible;
  @SerializedName("progress")
  public float              progress;
  @SerializedName("goals")
  public List<TeamCategory> goals;
}
