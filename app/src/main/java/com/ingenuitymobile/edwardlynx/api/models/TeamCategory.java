package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 7/5/17.
 */

public class TeamCategory extends Model {

  @SerializedName("name")
  public String name;
  @SerializedName("ownerId")
  public long   ownerId;
  @SerializedName("position")
  public int    position;
  @SerializedName("checked")
  public int    checked;
  @SerializedName("visible")
  public int    visible;
  @SerializedName("progress")
  public float  progress;
}
