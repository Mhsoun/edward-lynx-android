package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 * API model for answers params.
 */

public class AnswerParam {

  @SerializedName("key")
  public String           key;
  @SerializedName("final")
  public boolean          isFinal;
  @SerializedName("answers")
  public List<AnswerBody> answers;


  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
