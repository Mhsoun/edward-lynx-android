package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 5/24/17.
 */

public class InviteUserParam {

  @SerializedName("recipients")
  public List<UserParam> recipients;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
