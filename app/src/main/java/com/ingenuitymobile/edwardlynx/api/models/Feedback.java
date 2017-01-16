package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.responses.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class Feedback extends Response {

  @SerializedName("_links")
  public Links links;

  @SerializedName("id")
  public long           id;
  @SerializedName("lang")
  public String         lang;
  @SerializedName("closed")
  public int            closed;
  @SerializedName("anonynous")
  public int            anonymous;
  @SerializedName("createdAt")
  public String         createdAt;
  @SerializedName("questions")
  public List<Question> questions;

  public Feedback() {
    questions = new ArrayList<>();
  }
}
