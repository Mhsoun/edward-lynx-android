package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.responses.Response;

/**
 * Created by mEmEnG-sKi on 15/09/2016.
 */
public class User extends Response {

  public static final String ADMIN             = "admin";
  public static final String SUPER_ADMIN       = "superAdmin";
  public static final String SUPERVISOR        = "supervisor";
  public static final String PARTICIPANT       = "participant";
  public static final String FEEDBACK_PROVIDER = "eedback-provider";
  public static final String ANALYST           = "analyst";

  @SerializedName("id")
  public long   id;
  @SerializedName("name")
  public String name;
  @SerializedName("email")
  public String email;
  @SerializedName("info")
  public String info;
  @SerializedName("lang")
  public String lang;
  @SerializedName("navcolor")
  public String navcolor;
  @SerializedName("type")
  public String type;
  @SerializedName("registeredOn")
  public String registeredOn;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
