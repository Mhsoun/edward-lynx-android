package com.ingenuitymobile.edwardlynx.api.models;

import android.content.Context;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.R;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class Survey implements ParentListItem {

  public static final int OPEN        = 0;
  public static final int UNFINISHED  = 1;
  public static final int COMPLETED   = 2;
  public static final int NOT_INVITED = 3;
  public List<String> reports;

  @SerializedName("_links")
  public Links  links;
  @SerializedName("id")
  public long   id;
  @SerializedName("name")
  public String name;
  @SerializedName("type")
  public int    type;
  @SerializedName("lang")
  public String lang;
  @SerializedName("startDate")
  public String startDate;
  @SerializedName("endDate")
  public String endDate;
  @SerializedName("enableAutoReminding")
  public int    enableAutoReminding;
  @SerializedName("autoRemindingDate")
  public String autoRemindingDate;
  @SerializedName("description")
  public String description;
  @SerializedName("thankYouText")
  public String thankYouText;
  @SerializedName("questionInfoText")
  public String questionInfoText;
  @SerializedName("status")
  public int    status;
  @SerializedName("personsEvaluatedText")
  public String personsEvaluatedText;
  @SerializedName("stats")
  public Stats  stats;


  @SerializedName("key")
  public String key;

  public String getType(Context ctx, boolean isUpperCase) {
    String string = "";
    switch (type) {
    case 0:
      string = ctx.getResources().getString(R.string.lynx_360);
      break;
    case 1:
      string = ctx.getResources().getString(R.string.lynx_management);
      break;
    case 2:
      string = ctx.getResources().getString(R.string.lynx_progress);
      break;
    case 3:
      string = ctx.getResources().getString(R.string.lynx_survey);
      break;
    case 4:
      string = ctx.getResources().getString(R.string.lynx_team_tool);
      break;
    default:
      return string;
    }
    string = isUpperCase ? string.toUpperCase() : string;
    return string;
  }

  @Override
  public List<?> getChildItemList() {
    return reports;
  }

  @Override
  public boolean isInitiallyExpanded() {
    return false;
  }
}
