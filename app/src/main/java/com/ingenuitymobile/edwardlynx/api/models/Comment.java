package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by memengski on 5/29/17.
 */

public class Comment {

  @SerializedName("id")
  public long   id;
  @SerializedName("title")
  public String title;

  @SerializedName("answer")
  public List<CommentItem> answer;
}
