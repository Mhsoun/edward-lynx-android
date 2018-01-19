package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 6/1/17.
 * API model for question rate.
 */

public class QuestionRate {

  @SerializedName("role_name")
  public String name;
  @SerializedName("role_style")
  public String roleStyle;
  @SerializedName("category")
  public String category;
  @SerializedName("question")
  public String question;
  @SerializedName("answerType")
  public Answer answerType;
  @SerializedName("candidates")
  public int    candidates;
  @SerializedName("others")
  public int    others;
}
