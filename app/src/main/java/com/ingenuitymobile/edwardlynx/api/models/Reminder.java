package com.ingenuitymobile.edwardlynx.api.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.R;

/**
 * Created by memengski on 3/31/17.
 * API model for reminders.
 */

public class Reminder extends Model {

  public enum Type {
    GOAL("development-plan-goal"),
    INVITE_FEEDBACK("instant-feedback"),
    SURVEY("survey");

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
  @SerializedName("key")
  public String key;

  public String getType(Context ctx) {
    if (type.equals(Type.GOAL.toString())) {
      return ctx.getResources().getString(R.string.goal).toUpperCase();
    } else if (type.equals(Type.SURVEY.toString())) {
      return ctx.getResources().getString(R.string.survey).toUpperCase();
    } else {
      return ctx.getResources().getString(R.string.instant_feedback).toUpperCase();
    }
  }
}
