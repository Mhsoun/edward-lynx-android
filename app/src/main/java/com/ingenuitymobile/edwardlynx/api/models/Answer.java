package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class Answer {

  public static final int NUMERIC_1_5_SCALE             = 0;
  public static final int NUMERIC_1_10_SCALE            = 1;
  public static final int AGREEMENT_SCALE               = 2;
  public static final int YES_OR_NO                     = 3;
  public static final int STRONG_AGREEMENT_SCALE        = 4;
  public static final int CUSTOM_TEXT                   = 5;
  public static final int REVERSE_AGREEMENT_SCALE       = 6;
  public static final int NUMERIC_1_10_WITH_EXPLANATION = 7;
  public static final int CUSTOM_SCALE                  = 8;

  @SerializedName("id")
  public long         id;
  @SerializedName("type")
  public int          type;
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
  @SerializedName("order")
  public int          order;
}
