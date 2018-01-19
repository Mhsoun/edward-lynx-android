package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 06/02/2017.
 * API model for token param.
 */

public class TokenParam {

  @SerializedName("token")
  public String token;
  @SerializedName("deviceId")
  public String deviceId;

  public TokenParam() {}

  public TokenParam(String token, String deviceId) {
    this.token = token;
    this.deviceId = deviceId;
  }
}
