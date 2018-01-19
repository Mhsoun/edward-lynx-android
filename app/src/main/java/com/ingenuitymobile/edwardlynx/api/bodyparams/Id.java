package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 * API model for id.
 */

public class Id {

  @SerializedName("id")
  public Integer id;
  @SerializedName("email")
  public String  email;
  @SerializedName("name")
  public String  name;

  public Id(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public Id(String id) {
    this.id = Integer.parseInt(id);
  }

  public Id() {}
}
