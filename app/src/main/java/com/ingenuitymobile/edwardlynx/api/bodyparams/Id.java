package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class Id {

  @SerializedName("id")
  public long id;

  public Id() {}

  public Id(String id) {
    this.id = Long.parseLong(id);
  }
}
