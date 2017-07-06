package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 7/6/17.
 */

public class AddUserBody {

  @SerializedName("users")
  public List<Id> users;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
