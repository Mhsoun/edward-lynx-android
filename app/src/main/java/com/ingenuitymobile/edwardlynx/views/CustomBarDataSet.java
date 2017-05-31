package com.ingenuitymobile.edwardlynx.views;

import android.content.Context;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;

import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class CustomBarDataSet extends BarDataSet {

  private List<BarEntry> yVals;

  public CustomBarDataSet(Context ctx, List<BarEntry> yVals, String label) {
    super(yVals, label);
    this.yVals = yVals;
    setColors(new int[]{
        ctx.getResources().getColor(R.color.colorAccent),
        ctx.getResources().getColor(R.color.lynx_color),
        ctx.getResources().getColor(R.color.lynx_red_color),
    });
  }

  @Override
  public int getColor(int index) {
    final String role = (String) yVals.get(index).getData();
    if (role.equals(BreakdownItem.ORANGE_COLOR)) {
      return mColors.get(0);
    } else if (role.equals(BreakdownItem.SELF_COLOR)) {
      return mColors.get(1);
    } else {
      return mColors.get(2);
    }
  }
}
