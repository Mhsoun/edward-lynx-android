package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 15/09/2016.
 */
public class User extends Model {

  @SerializedName("name")
  public String name;
  @SerializedName("email")
  public String email;
  @SerializedName("info")
  public String info;
  @SerializedName("lang")
  public String lang;
  @SerializedName("navcolor")
  public String navcolor;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
