package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Links;
import com.ingenuitymobile.edwardlynx.api.models.Reminder;

import java.util.List;

/**
 * Created by memengski on 4/11/17.
 * API model for dashboard response.
 */

public class DashboardResponse extends Response {

  @SerializedName("_links")
  public Links                 links;
  @SerializedName("reminders")
  public List<Reminder>        reminders;
  @SerializedName("answerableCount")
  public int                   answerableCount;
  @SerializedName("developmentPlans")
  public List<DevelopmentPlan> developmentPlans;
}
