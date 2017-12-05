package com.ingenuitymobile.edwardlynx.api.bodyparams;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by memengski on 7/16/17.
 * API model for team development plan body.
 */

public class TeamDevPlanBody {

  @SerializedName("id")
  public long id;
  @SerializedName("position")
  public int  position;
  @SerializedName("visible")
  public int  visible;

  public TeamDevPlanBody() {}

  public TeamDevPlanBody(long id, int position, int visible) {
    this.id = id;
    this.position = position;
    this.visible = visible;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
