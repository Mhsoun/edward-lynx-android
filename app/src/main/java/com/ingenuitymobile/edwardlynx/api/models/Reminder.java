package com.ingenuitymobile.edwardlynx.api.models;

/**
 * Created by memengski on 3/31/17.
 */

public class Reminder {

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

  public String type;
  public String description;
}
