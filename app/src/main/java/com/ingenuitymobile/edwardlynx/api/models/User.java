package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
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
  public static final String FEEDBACK_PROVIDER = "feedback-provider";
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

  @SerializedName("department")
  public String department;
  @SerializedName("role")
  public String role;
  @SerializedName("gender")
  public String gender;

  @SerializedName("country")
  public String country;
  @SerializedName("city")
  public String city;

  @SerializedName("isUser")
  public boolean isUser;

  @Expose(serialize = false, deserialize = false)
  public boolean isAddedbyEmail;
  @Expose(serialize = false, deserialize = false)
  public boolean isDisabled;


  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
