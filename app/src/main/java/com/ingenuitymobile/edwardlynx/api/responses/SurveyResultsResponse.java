package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.Average;
import com.ingenuitymobile.edwardlynx.api.models.BlindSpot;
import com.ingenuitymobile.edwardlynx.api.models.Comment;
import com.ingenuitymobile.edwardlynx.api.models.Links;
import com.ingenuitymobile.edwardlynx.api.models.SurveyFrequency;

import java.util.List;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyResultsResponse extends Response {

  @SerializedName("_links")
  public Links                 links;
  @SerializedName("frequencies")
  public List<SurveyFrequency> frequencies;
  @SerializedName("average")
  public List<Average>         averages;
  @SerializedName("ioc")
  public List<Average>         ioc;
  @SerializedName("radar_diagram")
  public List<Average>         radarDiagrams;
  @SerializedName("comments")
  public List<Comment>         comments;
  @SerializedName("blindspot")
  public BlindSpot             blindspot;

}
