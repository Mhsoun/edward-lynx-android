package com.ingenuitymobile.edwardlynx.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.views.CustomBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class ResponseRateFragment extends BaseFragment {

  private static final float FONT_SIZE = 11;

  private View mainView;

  private List<BreakdownItem> responseRates;

  private BarChart barChart;

  public ResponseRateFragment() {
    responseRates = new ArrayList<>();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (mainView != null) {
      ViewGroup parent = (ViewGroup) mainView.getParent();
      if (parent == null) {
        parent = container;
      }
      parent.removeView(mainView);
      LogUtil.e("AAA onCreateView ResponseRateFragment");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_response_rate, container, false);
    LogUtil.e("AAA onCreateView ResponseRateFragment");
    initViews();
    setData();
    return mainView;
  }

  private void initViews() {
    barChart = (BarChart) mainView.findViewById(R.id.bar_chart);
    barChart.setNoDataText(getString(R.string.no_chart_data_available));
    barChart.invalidate();

    barChart.setDrawBarShadow(false);
    barChart.setDrawValueAboveBar(false);
    barChart.getDescription().setEnabled(false);

    barChart.setPinchZoom(false);
    barChart.setDoubleTapToZoomEnabled(false);

    barChart.setDrawGridBackground(false);
  }

  public void setDataSet(List<BreakdownItem> responseRates) {
    this.responseRates = responseRates;
    if (mainView != null) {
      setData();
    }
  }

  private void setData() {
    ArrayList<BarEntry> goalBars = new ArrayList<>();
    float highest = 0f;

    for (BreakdownItem item : responseRates) {
      BarEntry entry = new BarEntry(goalBars.size(), item.percentage);
      entry.setData(item.roleStyle);
      goalBars.add(entry);

      if (highest < item.percentage) {
        highest = item.percentage;
      }
    }

    final CustomBarDataSet set = new CustomBarDataSet(getActivity(), goalBars, barChart, "");
    set.setHighlightEnabled(false);

    final ArrayList<IBarDataSet> dataSets = new ArrayList<>();
    dataSets.add(set);

    final BarData data = new BarData(dataSets);
    data.setBarWidth(0.4f);
    data.setDrawValues(false);

    barChart.setData(data);
    barChart.getLegend().setEnabled(false);

    barChart.setHighlightPerTapEnabled(false);
    barChart.clearAnimation();
    barChart.invalidate();

    XAxis xAxis = barChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);
    xAxis.setDrawAxisLine(true);
    xAxis.setDrawLabels(true);
    xAxis.setTextColor(Color.WHITE);
    xAxis.setGranularity(1f);
    xAxis.setTextSize(FONT_SIZE);
    xAxis.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        final int index = (((int) value));
        if (index >= 0) {
          return responseRates.get(index).title;
        }
        return "";
      }
    });

    YAxis leftAxis = barChart.getAxisLeft();
    leftAxis.setDrawGridLines(true);
    leftAxis.setDrawLabels(true);
    leftAxis.setDrawLimitLinesBehindData(true);
    leftAxis.setLabelCount(0, true);
    leftAxis.setDrawTopYLabelEntry(true);
    leftAxis.setAxisMinimum(0f);
    leftAxis.setAxisMaximum(highest);
    leftAxis.setTextColor(Color.WHITE);
    leftAxis.setDrawAxisLine(false);
    leftAxis.setTextSize(FONT_SIZE);
    leftAxis.setAxisLineColor(getResources().getColor(R.color.survey_line));


    YAxis rightAxis = barChart.getAxisRight();
    rightAxis.setDrawGridLines(false);
    rightAxis.setDrawLabels(false);
    rightAxis.setDrawAxisLine(false);

    barChart.invalidate();
  }
}
