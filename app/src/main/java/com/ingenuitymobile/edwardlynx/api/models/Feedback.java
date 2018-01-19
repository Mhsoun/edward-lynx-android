package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.responses.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 * API model for feedback response.
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
  @SerializedName("anonymous")
  public int            anonymous;
  @SerializedName("createdAt")
  public String         createdAt;
  @SerializedName("questions")
  public List<Question> questions;
  @SerializedName("shares")
  public List<Long>     shares;
  @SerializedName("key")
  public String         key;
  @SerializedName("stats")
  public Stats          stats;
  @SerializedName("author")
  public User           author;

  @SerializedName("recipients")
  public List<User> recipients;

  public Feedback() {
    questions = new ArrayList<>();
    shares = new ArrayList<>();
  }
}
