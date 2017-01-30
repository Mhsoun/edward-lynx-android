package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class InstantFeedbackBody {

  @SerializedName("lang")
  public String             lang;
  @SerializedName("anonymous")
  public boolean                isAnonymous;
  @SerializedName("questions")
  public List<QuestionBody> questionBodies;
  @SerializedName("recipients")
  public List<Id>           recipients;

  public InstantFeedbackBody() {
    questionBodies = new ArrayList<>();
    recipients = new ArrayList<>();
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
