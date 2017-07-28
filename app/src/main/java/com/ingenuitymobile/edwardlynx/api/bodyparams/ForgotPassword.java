package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 7/28/17.
 */

public class ForgotPassword {

  @SerializedName("email")
  public String email;

  public ForgotPassword() {}

  public ForgotPassword(String email) {
    this.email = email;
  }


  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
