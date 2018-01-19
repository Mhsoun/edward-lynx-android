package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.responses.Response;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 * API model for development plan.
 */

public class DevelopmentPlan extends Response {

  @SerializedName("id")
  public long       id;
  @SerializedName("name")
  public String     name;
  @SerializedName("createdAt")
  public String     createdAt;
  @SerializedName("updatedAt")
  public String     updatedAt;
  @SerializedName("checked")
  public int        checked;
  @SerializedName("shared")
  public int        shared;
  @SerializedName("goals")
  public List<Goal> goals;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
