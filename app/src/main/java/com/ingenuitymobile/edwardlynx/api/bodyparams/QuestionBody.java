package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 * API model for question body.
 */

public class QuestionBody {

  @SerializedName("text")
  public String     text;
  @SerializedName("isNA")
  public boolean    isNA;
  @SerializedName("answer")
  public AnswerBody answerBody;
}
