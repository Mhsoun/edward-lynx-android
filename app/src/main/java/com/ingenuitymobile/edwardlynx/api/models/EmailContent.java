package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 * API model for email content.
 */

public class EmailContent {

  @SerializedName("subject")
  public String subject;
  @SerializedName("text")
  public String text;
}
