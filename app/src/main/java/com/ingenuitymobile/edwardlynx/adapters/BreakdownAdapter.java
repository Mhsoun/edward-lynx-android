package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Breakdown;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;
import com.ingenuitymobile.edwardlynx.views.CustomBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class BreakdownAdapter extends RecyclerView.Adapter<BreakdownAdapter.ViewHolder> {

  private List<Breakdown> data;

  public BreakdownAdapter(List<Breakdown> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView           titleText;
    HorizontalBarChart horizontalBarChart;

    ViewHolder(View itemView) {
      super(itemView);

      titleText = (TextView) itemView.findViewById(R.id.text_title);
      horizontalBarChart = (HorizontalBarChart) itemView.findViewById(R.id.horizontal_bar_chart);

      horizontalBarChart.setDrawBarShadow(false);
      horizontalBarChart.getDescription().setEnabled(false);
      horizontalBarChart.setPinchZoom(false);
      horizontalBarChart.setDrawGridBackground(false);
      horizontalBarChart.setDoubleTapToZoomEnabled(false);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_breakdown, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final Breakdown breakdown = data.get(position);

    holder.titleText.setText(breakdown.category);
    final float FONT_SIZE = 11;

    ArrayList<BarEntry> yVals1 = new ArrayList<>();

    for (int i = 0; i < breakdown.dataPoints.size(); i++) {
      BreakdownItem breakdownItem = breakdown.dataPoints.get(i);
      BarEntry barEntry = new BarEntry(i, breakdownItem.percentage * 100);
      barEntry.setData(breakdownItem.roleStyle);
      yVals1.add(barEntry);
    }

    final CustomBarDataSet set = new CustomBarDataSet(context, yVals1, "");
    set.setDrawValues(true);
    set.setValueTextSize(FONT_SIZE);
    set.setValueTextColor(context.getResources().getColor(R.color.white));
    set.setHighlightEnabled(false);
    set.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value);
      }
    });

    BarData barData = new BarData(set);
    barData.setBarWidth(0.6f);

    holder.horizontalBarChart.setData(barData);
    holder.horizontalBarChart.getLegend().setEnabled(false);
    holder.horizontalBarChart.setVisibleXRangeMaximum(breakdown.dataPoints.size());
    holder.horizontalBarChart.setVisibleXRangeMinimum(breakdown.dataPoints.size());
    holder.horizontalBarChart.setFitBars(false);

    holder.horizontalBarChart.getLayoutParams().height = (110 * breakdown.dataPoints.size());
    holder.horizontalBarChart.invalidate();
    holder.horizontalBarChart.clearAnimation();

    XAxis xl = holder.horizontalBarChart.getXAxis();
    xl.setPosition(XAxis.XAxisPosition.BOTTOM);
    xl.setDrawGridLines(false);
    xl.setDrawAxisLine(true);
    xl.setGranularity(1f);
    xl.setGranularityEnabled(true);
    xl.setLabelCount(breakdown.dataPoints.size());
    xl.setTextColor(Color.WHITE);
    xl.setTextSize(FONT_SIZE);
    xl.setAxisLineColor(context.getResources().getColor(R.color.survey_line));
    xl.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        return breakdown.dataPoints.get((int) value).title;
      }
    });

    YAxis yl = holder.horizontalBarChart.getAxisLeft();
    yl.setDrawAxisLine(false);
    yl.setDrawGridLines(false);
    yl.setAxisMinimum(0f);
    yl.setAxisMaximum(110f);
    yl.setDrawTopYLabelEntry(true);
    yl.setDrawLabels(false);

    LimitLine limitLine = new LimitLine(70, "");
    limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
    limitLine.setLineColor(context.getResources().getColor(R.color.survey_line));
    limitLine.setTextColor(context.getResources().getColor(R.color.white));
    limitLine.setTextSize(FONT_SIZE);
    yl.addLimitLine(limitLine);

    limitLine = new LimitLine(100, "");
    limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
    limitLine.setLineColor(context.getResources().getColor(R.color.survey_line));
    limitLine.setTextSize(FONT_SIZE);
    limitLine.setTextColor(context.getResources().getColor(R.color.white));
    yl.addLimitLine(limitLine);

    YAxis yr = holder.horizontalBarChart.getAxisRight();
    yr.setDrawGridLines(false);
    yr.setDrawAxisLine(false);
    yr.setDrawLabels(true);
    yr.setAxisMinimum(0f);
    yr.setLabelCount(10);
    yr.setAxisMaximum(112f);
    yr.setTextColor(Color.WHITE);
    yr.setTextSize(FONT_SIZE);
    yr.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        if ((int) value == 70) {
          return "70%";
        } else if ((int) value == 100) {
          return "100%";
        }
        return "";
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
