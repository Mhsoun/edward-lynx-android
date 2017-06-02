package com.ingenuitymobile.edwardlynx.utils;

import android.content.Context;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;

/**
 * Created by memengski on 6/2/17.
 */

public class ColorUtil {

  public static int getRoleColor(Context ctx, String role) {
    if (role.equals(BreakdownItem.ORANGE_COLOR)) {
      return ctx.getResources().getColor(R.color.colorAccent);
    } else if (role.equals(BreakdownItem.SELF_COLOR)) {
      return ctx.getResources().getColor(R.color.lynx_color);
    } else {
      return ctx.getResources().getColor(R.color.lynx_red_color);
    }
  }
}
