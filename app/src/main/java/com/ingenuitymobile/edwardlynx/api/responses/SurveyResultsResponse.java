package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.Average;
import com.ingenuitymobile.edwardlynx.api.models.BlindSpot;
import com.ingenuitymobile.edwardlynx.api.models.Breakdown;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;
import com.ingenuitymobile.edwardlynx.api.models.Comment;
import com.ingenuitymobile.edwardlynx.api.models.DetailedSummary;
import com.ingenuitymobile.edwardlynx.api.models.IndividualRate;
import com.ingenuitymobile.edwardlynx.api.models.Links;
import com.ingenuitymobile.edwardlynx.api.models.SurveyFrequency;
import com.ingenuitymobile.edwardlynx.api.models.YesNo;

import java.util.List;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyResultsResponse extends Response {

  @SerializedName("_links")
  public Links                 links;
  @SerializedName("frequencies")
  public List<SurveyFrequency> frequencies;
  @SerializedName("response_rate")
  public List<BreakdownItem>   responseRates;
  @SerializedName("average")
  public List<Average>         averages;
  @SerializedName("ioc")
  public List<Average>         ioc;
  @SerializedName("radar_diagram")
  public List<Average>         radarDiagrams;
  @SerializedName("comments")
  public List<Comment>         comments;
  @SerializedName("highestLowestIndividual")
  public IndividualRate        rate;
  @SerializedName("blindspot")
  public BlindSpot             blindspot;
  @SerializedName("breakdown")
  public List<Breakdown>       breakdown;
  @SerializedName("detailed_answer_summary")
  public List<DetailedSummary> detailedSummaries;
  @SerializedName("yes_or_no")
  public List<YesNo>           yesNos;
}
