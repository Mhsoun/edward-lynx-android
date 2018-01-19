package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 21/12/2016.
 * API model for validation errors.
 */

public class ValidationErrors {

  @SerializedName("currentPassword")
  public List<String> currentPassword;

  @SerializedName("password")
  public List<String> password;

  public ValidationErrors() {
    currentPassword = new ArrayList<>();
  }
}
