package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.TeamCategory;

import java.util.List;

/**
 * Created by memengski on 7/5/17.
 */

public class TeamCategoriesResponse {

  @SerializedName("items")
  public List<TeamCategory> items;
}
