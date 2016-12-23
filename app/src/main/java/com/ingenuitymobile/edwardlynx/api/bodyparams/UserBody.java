package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 19/09/2016.
 */
public class UserBody {

  @SerializedName("name")
  public String name;
  @SerializedName("info")
  public String info;
  @SerializedName("currentPassword")
  public String currentPassword;
  @SerializedName("password")
  public String password;
}
