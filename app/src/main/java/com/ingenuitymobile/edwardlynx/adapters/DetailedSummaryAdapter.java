package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;
import com.ingenuitymobile.edwardlynx.api.models.DetailedSummary;
import com.ingenuitymobile.edwardlynx.api.models.DetailedSummaryItem;
import com.ingenuitymobile.edwardlynx.views.CustomBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class DetailedSummaryAdapter extends
    RecyclerView.Adapter<DetailedSummaryAdapter.ViewHolder> {

  private static final float FONT_SIZE = 10;

  private List<DetailedSummary> data;

  /**
   * Adapter for the displaying the detailed summary.
   * @param data the list of detailed summaries
   */
  public DetailedSummaryAdapter(List<DetailedSummary> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView titleText;
    BarChart barChart;

    ViewHolder(View itemView) {
      super(itemView);

      titleText = (TextView) itemView.findViewById(R.id.text_title);
      barChart = (BarChart) itemView.findViewById(R.id.bar_chart);
      barChart.setNoDataText(itemView.getContext().getString(R.string.no_chart_data_available));
      barChart.invalidate();

      barChart.setDrawBarShadow(false);
      barChart.setDrawValueAboveBar(true);
      barChart.getDescription().setEnabled(false);

      barChart.setPinchZoom(false);
      barChart.setDoubleTapToZoomEnabled(false);

      barChart.setDrawGridBackground(false);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_detailed_summary, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final DetailedSummary item = data.get(position);

    holder.titleText.setText(item.category);

    ArrayList<BarEntry> goalBars = new ArrayList<>();

    for (DetailedSummaryItem detailedSummaryItem : item.dataPoints) {

      BarEntry entry = new BarEntry(goalBars.size() + 1, detailedSummaryItem.value);
      entry.setData(BreakdownItem.SELF_COLOR);
      goalBars.add(entry);
    }

    final CustomBarDataSet set = new CustomBarDataSet(context, goalBars, holder.barChart, "");
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

    final ArrayList<IBarDataSet> dataSets = new ArrayList<>();
    dataSets.add(set);

    final BarData data = new BarData(dataSets);
    data.setBarWidth(0.6f);

    holder.barChart.setData(data);
    holder.barChart.getLegend().setEnabled(false);
    holder.barChart.setVisibleXRangeMaximum(item.dataPoints.size());
    holder.barChart.setVisibleXRangeMinimum(item.dataPoints.size());
    holder.barChart.setHighlightPerTapEnabled(false);
    holder.barChart.clearAnimation();
    holder.barChart.invalidate();

    XAxis xAxis = holder.barChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);
    xAxis.setDrawAxisLine(true);
    xAxis.setDrawLabels(true);
    xAxis.setTextColor(Color.WHITE);
    xAxis.setGranularity(1f);
    xAxis.setLabelCount(item.dataPoints.size());
    xAxis.setTextSize(FONT_SIZE);
    xAxis.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        final int index = (((int) value) - 1);
        if (index >= 0) {
          final Object object = item.dataPoints.get(index).question;
          if (object instanceof Double) {
            return String.valueOf(((Double) object).intValue());
          }
          return String.valueOf(object);
        }
        return "";
      }
    });

    YAxis leftAxis = holder.barChart.getAxisLeft();
    leftAxis.setDrawGridLines(true);
    leftAxis.setDrawLabels(true);
    leftAxis.setDrawLimitLinesBehindData(true);
    leftAxis.setLabelCount(5, true);
    leftAxis.setDrawTopYLabelEntry(true);
    leftAxis.setAxisMinimum(0f);
    leftAxis.setAxisMaximum(100f);
    leftAxis.setTextColor(Color.WHITE);
    leftAxis.setDrawAxisLine(false);
    leftAxis.setTextSize(FONT_SIZE);
    leftAxis.setAxisLineColor(context.getResources().getColor(R.color.survey_line));
    leftAxis.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        return String.valueOf((int) value) + "%";
      }
    });


    YAxis rightAxis = holder.barChart.getAxisRight();
    rightAxis.setDrawGridLines(false);
    rightAxis.setDrawLabels(false);
    rightAxis.setDrawAxisLine(false);

    holder.barChart.invalidate();
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
