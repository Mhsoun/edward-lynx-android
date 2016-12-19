package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 01/09/2016.
 */
public class Response {

  @SerializedName("error")
  public String error;
  @SerializedName("message")
  public String message;

  public boolean isNotAuthenticated;
}
