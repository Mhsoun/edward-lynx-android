package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.responses.Response;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 * API model for survey response.
 */

public class Surveys extends Response {

  @SerializedName("_links")
  public Links        links;
  @SerializedName("total")
  public int          total;
  @SerializedName("num")
  public int          num;
  @SerializedName("pages")
  public int          pages;
  @SerializedName("items")
  public List<Survey> items;
}
