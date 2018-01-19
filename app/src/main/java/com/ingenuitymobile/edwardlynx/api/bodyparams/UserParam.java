package com.ingenuitymobile.edwardlynx.api.bodyparams;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.R;

/**
 * Created by memengski on 5/24/17.
 * API model for user param.
 */

public class UserParam {

  public static final int COLLEAGUE      = 2;
  public static final int MANAGER        = 3;
  public static final int CUSTOMER       = 4;
  public static final int MATRIX_MANAGER = 5;
  public static final int STAKEHOLDER    = 6;
  public static final int DIRECT_REPORT  = 7;

  @SerializedName("name")
  public String name;
  @SerializedName("email")
  public String email;
  @SerializedName("role")
  public int    role;

  public String getRole(Context ctx) {
    String string = "";
    switch (role) {
    case COLLEAGUE:
      string = ctx.getResources().getString(R.string.colleague);
      break;
    case MANAGER:
      string = ctx.getResources().getString(R.string.manager);
      break;
    case CUSTOMER:
      string = ctx.getResources().getString(R.string.customer);
      break;
    case MATRIX_MANAGER:
      string = ctx.getResources().getString(R.string.matrix_manager);
      break;
    case STAKEHOLDER:
      string = ctx.getResources().getString(R.string.other_stakeholder);
      break;
    case DIRECT_REPORT:
      string = ctx.getResources().getString(R.string.direct_report);
      break;
    default:
      return string;
    }
    return string;
  }

  public int getRole(Context ctx, String role) {
    if (role.equals(ctx.getString(R.string.colleague))) {
      return COLLEAGUE;
    } else if (role.equals(ctx.getString(R.string.manager))) {
      return MANAGER;
    } else if (role.equals(ctx.getString(R.string.customer))) {
      return CUSTOMER;
    } else if (role.equals(ctx.getString(R.string.matrix_manager))) {
      return MATRIX_MANAGER;
    } else if (role.equals(ctx.getString(R.string.other_stakeholder))) {
      return STAKEHOLDER;
    } else if (role.equals(ctx.getString(R.string.direct_report))) {
      return DIRECT_REPORT;
    }
    return -1;
  }
}
