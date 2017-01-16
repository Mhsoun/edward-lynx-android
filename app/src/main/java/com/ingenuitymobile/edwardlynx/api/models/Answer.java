package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class Answer {

  @SerializedName("id")
  public long         id;
  @SerializedName("description")
  public String       decscription;
  @SerializedName("help")
  public String       help;
  @SerializedName("isText")
  public boolean      isText;
  @SerializedName("isNumeric")
  public boolean      isNumeric;
  @SerializedName("options")
  public List<Option> options;
  @SerializedName("oder")
  public int          order;
}
