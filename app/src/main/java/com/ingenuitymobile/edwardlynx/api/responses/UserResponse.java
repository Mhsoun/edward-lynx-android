package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.User;

/**
 * Created by mEmEnG-sKi on 16/09/2016.
 */
public class UserResponse extends Response {

  @SerializedName("data")
  public User user;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
