package com.ingenuitymobile.edwardlynx.api.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class Goal extends Model implements ParentListItem {

  @SerializedName("title")
  public String title;
  @SerializedName("description")
  public String description;
  @SerializedName("checked")
  public int    checked;
  @SerializedName("position")
  public int    position;
  @SerializedName("dueDate")
  public String dueDate;
  @SerializedName("reminderSent")
  public int    reminderSent;
  @SerializedName("actions")
  public List<Action> actions;

  @Override
  public List<?> getChildItemList() {
    return actions;
  }

  @Override
  public boolean isInitiallyExpanded() {
    return false;
  }
}
