package com.ingenuitymobile.edwardlynx.views;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class CustomBarDataSet extends BarDataSet {

  private List<BarEntry> yVals;
  private List<Integer>  colors;

  public CustomBarDataSet(Context ctx, List<BarEntry> yVals, String label) {
    super(yVals, label);
    this.yVals = yVals;
    colors = new ArrayList<>();
    colors.add(ctx.getResources().getColor(R.color.colorAccent));
    colors.add(ctx.getResources().getColor(R.color.lynx_color));
    colors.add(ctx.getResources().getColor(R.color.lynx_red_color));

    setColors(colors);
    setValueTextColor(Color.WHITE);
  }

  @Override
  public int getColor(int index) {
    final String role = (String) yVals.get(index).getData();
    if (role.equals(BreakdownItem.ORANGE_COLOR)) {
      return colors.get(0);
    } else if (role.equals(BreakdownItem.SELF_COLOR)) {
      return colors.get(1);
    } else {
      return colors.get(2);
    }
  }
}
