package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class AnswerBody {

  @SerializedName("type")
  public int type;

  @SerializedName("question")
  public long   question;
  @SerializedName("answer")
  public String answer;
}
