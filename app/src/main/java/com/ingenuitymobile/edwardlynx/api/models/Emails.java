package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 * API model for emails.
 */

public class Emails {

  @SerializedName("invitation")
  public EmailContent invitation;
  @SerializedName("manualReminding")
  public EmailContent manualReminding;
  @SerializedName("toEvaluate")
  public EmailContent toEvaluate;
  @SerializedName("inviteOthersReminding")
  public EmailContent inviteOthersReminding;
  @SerializedName("candidateInvitation")
  public EmailContent candidateInvitation;
}
