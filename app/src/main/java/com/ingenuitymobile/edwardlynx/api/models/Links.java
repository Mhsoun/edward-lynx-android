package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class Links {

  @SerializedName("self")
  public GenericLinks self;
  @SerializedName("questions")
  public GenericLinks questions;
}
