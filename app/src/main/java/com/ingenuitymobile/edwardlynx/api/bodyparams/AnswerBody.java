package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class AnswerBody {

  @SerializedName("type")
  public int          type;
  @SerializedName("options")
  public List<String> options;

  @SerializedName("question")
  public long   question;
  @SerializedName("value")
  public String answer;

  @SerializedName("explanation")
  public String explanation;
}
