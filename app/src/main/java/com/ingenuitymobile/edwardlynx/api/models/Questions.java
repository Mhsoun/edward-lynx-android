package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 * API model for questions.
 */

public class Questions {

  @SerializedName("_links")
  public Links          links;
  @SerializedName("items")
  public List<Category> items;
}
