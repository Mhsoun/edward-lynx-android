package com.ingenuitymobile.edwardlynx.views;

import android.content.Context;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class CustomBarDataSet extends BarDataSet {

  private List<BarEntry> yVals;
  private List<Integer>  colors;
  private BarChart       barChart;

  public CustomBarDataSet(Context ctx, List<BarEntry> yVals, BarChart barChart, String label) {
    super(yVals, label);
    this.yVals = yVals;
    this.barChart = barChart;
    colors = new ArrayList<>();
    colors.add(ctx.getResources().getColor(R.color.colorAccent));
    colors.add(ctx.getResources().getColor(R.color.lynx_color));
    colors.add(ctx.getResources().getColor(R.color.lynx_red_color));

    setColors(colors);
    setValueTextColors(colors);
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

  @Override
  public int getValueTextColor(int index) {
    if (barChart instanceof HorizontalBarChart) {
      if (index != 0) {
        index = index / 2;
      }
    }
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
