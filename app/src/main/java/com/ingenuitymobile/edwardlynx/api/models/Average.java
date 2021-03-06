package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 4/12/17.
 * API model for average.
 */

public class Average {

  @SerializedName("id")
  public long   id;
  @SerializedName("name")
  public String name;
  @SerializedName("average")
  public float  average;

  // ioc, and radar diagram
  @SerializedName("roles")
  public List<Average> roles;
}
