package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 * API model for share param.
 */

public class ShareParam {

  @SerializedName("users")
  public List<Long> users;

  public ShareParam() {
    users = new ArrayList<>();
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
