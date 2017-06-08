package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 5/29/17.
 */

public class CommentItem {

  @SerializedName("recipient_id")
  public long   recipientId;
  @SerializedName("text")
  public String text;
}
