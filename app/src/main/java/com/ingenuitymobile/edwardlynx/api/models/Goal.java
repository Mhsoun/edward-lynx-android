package com.ingenuitymobile.edwardlynx.api.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class Goal extends Model implements ParentListItem {

  @SerializedName("title")
  public String       title;
  @SerializedName("description")
  public String       description;
  @SerializedName("checked")
  public int          checked;
  @SerializedName("position")
  public int          position;
  @SerializedName("categoryId")
  public long         categoryId;
  @SerializedName("dueDate")
  public String       dueDate;
  @SerializedName("reminderSent")
  public int          reminderSent;
  @SerializedName("actions")
  public List<Action> actions;

  public boolean isInitiallyExpanded;

  public Goal() {
    actions = new ArrayList<>();
    id = 0L;
  }

  @Override
  public List<?> getChildItemList() {
    List<Action> temp = new ArrayList<>(actions);
    Action action = new Action();
    action.id = this.id;
    action.position = actions.size() + 1;
    action.isAddAction = true;
    temp.add(action);
    return temp;
  }

  @Override
  public boolean isInitiallyExpanded() {
    return isInitiallyExpanded;
  }

  @Override
  public String toString() {
    return new GsonBuilder()
        .registerTypeAdapter(Goal.class, new GoalSerializer())
        .create().toJson(this);
  }

  public boolean isCompleted() {
    int actionCount = 0;
    int actionSize = 0;
    if (actions != null && !actions.isEmpty()) {
      actionSize = actions.size();
      for (Action action : actions) {
        if (action.checked == 1) {
          actionCount++;
        }
      }
    }
    return actionCount != 0 && actionCount == actionSize;
  }

  public boolean isUnfinished() {
    if (actions != null) {
      for (Action action : this.actions) {
        if (action.checked == 1) {
          return true;
        }
      }
    }
    return false;
  }

  public static class GoalSerializer implements JsonSerializer<Goal> {

    @Override
    public JsonElement serialize(Goal goal, Type type, JsonSerializationContext jsc) {
      JsonObject jObj = (JsonObject) new GsonBuilder().create().toJsonTree(goal);
      if (goal.id == 0L && goal.categoryId == 0L) {
        jObj.remove("categoryId");
      }
      return jObj;
    }
  }
}
