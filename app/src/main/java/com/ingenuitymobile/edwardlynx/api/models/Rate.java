package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 6/1/17.
 */

public class Rate {

  @SerializedName("Manager")
  public List<QuestionRate> managers;
  @SerializedName("Others combined")
  public List<QuestionRate> others;
}
