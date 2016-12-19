package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 15/09/2016.
 */
public class Authentication extends Response {

  @SerializedName("access_token")
  public String accessToken;
  @SerializedName("token_type")
  public String tokenType;
  @SerializedName("expires_in")
  public String expiresIn;
  @SerializedName("refresh_token")
  public String refresh_token;
}
