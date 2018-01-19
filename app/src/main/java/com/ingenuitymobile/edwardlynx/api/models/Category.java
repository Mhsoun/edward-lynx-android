package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 * API model for category.
 */

public class Category {

  @SerializedName("id")
  public long           id;
  @SerializedName("title")
  public String         title;
  @SerializedName("description")
  public String         description;
  @SerializedName("isSurvey")
  public int            isSurvey;
  @SerializedName("order")
  public int            order;
  @SerializedName("questions")
  public List<Question> questions;
}
