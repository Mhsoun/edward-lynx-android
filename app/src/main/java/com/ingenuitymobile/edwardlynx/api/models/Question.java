package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 * API model for question.
 */

public class Question {

  @SerializedName("id")
  public long   id;
  @SerializedName("text")
  public String text;
  @SerializedName("optional")
  public int    optional;
  @SerializedName("isNA")
  public int    isNA;
  @SerializedName("isFollowUpQuestion")
  public int    isFollowUpQuestion;
  @SerializedName("answer")
  public Answer answer;
  @SerializedName("order")
  public int    order;
  @SerializedName("value")
  public Object value;
  @SerializedName("explanation")
  public String explanation;

  public boolean isSectionHeader;
  public String  description;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
