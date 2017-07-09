package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 7/9/17.
 */

public class TeamCategory extends Model {

  @SerializedName("name")
  public String     name;
  @SerializedName("goals")
  public List<Goal> goals;
}
