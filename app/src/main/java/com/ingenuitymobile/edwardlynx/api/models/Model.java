package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 08/09/2016.
 */
public class Model {

  @SerializedName("id")
  public long    id;
  @SerializedName("createdAt")
  public String  createdAt;
  @SerializedName("updatedAt")
  public String  updatedAt;
  @SerializedName("registeredOn")
  public boolean registeredOn;

  public Model() {
    id = 0L;
  }
}
