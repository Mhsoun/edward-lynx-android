package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class ActionParam {

  @SerializedName("title")
  public String title;
  @SerializedName("position")
  public int    position;
  @SerializedName("checked")
  public int   checked;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
