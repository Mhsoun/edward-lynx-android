package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 3/31/17.
 */

public class Reminder extends Model {

  public enum Type {
    GOAL("GOAL"),
    INVITE_FEEDBACK("INVITE FEEDBACK"),
    ANSWER_FEEDBACK("ANSWER INSTANT FEEDBACK");

    private String string;

    Type(String name) {string = name;}

    @Override
    public String toString() {
      return string;
    }
  }

  @SerializedName("type")
  public String type;
  @SerializedName("name")
  public String name;
  @SerializedName("description")
  public String description;
  @SerializedName("due")
  public String due;
}
