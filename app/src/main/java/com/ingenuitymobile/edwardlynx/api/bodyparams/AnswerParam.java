package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class AnswerParam {

  @SerializedName("answers")
  public List<AnswerBody> answers;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
