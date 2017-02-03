package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.Category;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class CategoriesResponse extends Response {

  @SerializedName("items")
  public List<Category> items;
}
