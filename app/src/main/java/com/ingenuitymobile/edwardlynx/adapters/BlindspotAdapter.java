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
import com.ingenuitymobile.edwardlynx.api.models.BlindSpotItem;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.views.CustomBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 6/1/17.
 */

public class BlindspotAdapter extends RecyclerView.Adapter<BlindspotAdapter.ViewHolder> {

  private List<BlindSpotItem> data;

  public BlindspotAdapter(List<BlindSpotItem> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView           titleText;
    TextView           questionText;
    HorizontalBarChart horizontalBarChart;

    ViewHolder(View itemView) {
      super(itemView);

      titleText = (TextView) itemView.findViewById(R.id.text_title);
      questionText = (TextView) itemView.findViewById(R.id.text_question);
      horizontalBarChart = (HorizontalBarChart) itemView.findViewById(R.id.horizontal_bar_chart);

      horizontalBarChart.setDrawBarShadow(false);
      horizontalBarChart.getDescription().setEnabled(false);
      horizontalBarChart.setPinchZoom(false);
      horizontalBarChart.setDrawGridBackground(false);
      horizontalBarChart.setDoubleTapToZoomEnabled(false);
      horizontalBarChart.setExtraRightOffset(30f);
      horizontalBarChart.invalidate();
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_blindspot, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final BlindSpotItem item = data.get(position);

    holder.titleText.setText(item.category);
    holder.questionText.setText(item.title);

    final float FONT_SIZE = 11;
    final int size = 2;

    ArrayList<BarEntry> yVals1 = new ArrayList<>();

    BarEntry barEntry = new BarEntry(0, item.self);
    barEntry.setData(BreakdownItem.SELF_COLOR);
    yVals1.add(barEntry);

    BarEntry entry = new BarEntry(1, item.others);
    entry.setData(BreakdownItem.OTHERS_COLOR);
    yVals1.add(entry);

    CustomBarDataSet set = new CustomBarDataSet(context, yVals1, "");
    set.setDrawValues(true);
    set.setValueTextSize(FONT_SIZE);
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

    holder.horizontalBarChart.clear();
    holder.horizontalBarChart.setData(barData);
    holder.horizontalBarChart.getLegend().setEnabled(false);
    holder.horizontalBarChart.setVisibleXRangeMaximum(size);
    holder.horizontalBarChart.setVisibleXRangeMinimum(size);
    holder.horizontalBarChart.setFitBars(false);

    holder.horizontalBarChart.getLayoutParams().height = (110 * size);
    holder.horizontalBarChart.notifyDataSetChanged();
    holder.horizontalBarChart.invalidate();
    holder.horizontalBarChart.clearAnimation();

    XAxis xl = holder.horizontalBarChart.getXAxis();
    xl.setPosition(XAxis.XAxisPosition.BOTTOM);
    xl.setDrawGridLines(false);
    xl.setDrawAxisLine(true);
    xl.setGranularity(1f);
    xl.setGranularityEnabled(true);
    xl.setLabelCount(size);
    xl.setTextColor(Color.WHITE);
    xl.setTextSize(FONT_SIZE);
    xl.setAxisLineColor(context.getResources().getColor(R.color.survey_line));
    xl.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        final int index = (((int) value));
        if (index >= 0) {
          return context.getString(index == 0 ? R.string.others_combined : R.string.self);
        }
        return "";
      }
    });

    YAxis yl = holder.horizontalBarChart.getAxisLeft();
    yl.setDrawAxisLine(false);
    yl.setDrawTopYLabelEntry(true);
    yl.setDrawGridLines(false);
    yl.setDrawLabels(false);
    yl.setAxisMinimum(0f);
    yl.setAxisMaximum(100f);

    if (item.answerType.isNumeric) {
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
    } else {
      yl.removeAllLimitLines();
    }

    YAxis yr = holder.horizontalBarChart.getAxisRight();
    yr.setDrawAxisLine(false);
    yr.setDrawLabels(true);
    yr.setDrawGridLines(!item.answerType.isNumeric);
    yr.setDrawLimitLinesBehindData(!item.answerType.isNumeric);
    yr.setLabelCount(
        item.answerType.isNumeric ? 10 : item.answerType.options.size(),
        !item.answerType.isNumeric
    );

    yr.setAxisMinimum(0f);
    yr.setAxisMaximum(100f);
    yr.setTextColor(Color.WHITE);
    yr.setTextSize(8.5f);
    yr.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        if (item.answerType.isNumeric) {
          if ((int) value == 70) {
            return "70%";
          } else if ((int) value == 100) {
            return "100%";
          }
        } else {
          int index = 0;
          if (value != 0) {
            index = (int) value / (100 / (item.answerType.options.size() - 1));
          }
          return item.answerType.options.get(index).description;
        }

        return "";
      }
    });

    holder.horizontalBarChart.invalidate();
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
