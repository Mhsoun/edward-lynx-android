package com.ingenuitymobile.edwardlynx.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Average;
import com.ingenuitymobile.edwardlynx.api.models.BreakdownItem;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;
import com.ingenuitymobile.edwardlynx.views.CustomBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/29/17.
 */

public class AveragesFragment extends BaseFragment {

  private static final float FONT_SIZE = 10;

  private View mainView;

  private List<Average> averages;
  private List<Average> ioc;

  private HorizontalBarChart barChart;
  private HorizontalBarChart mulitpleBarChart;

  /**
   * Fragment to display the averages in the survey report.
   */
  public AveragesFragment() {
    averages = new ArrayList<>();
    ioc = new ArrayList<>();
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
      LogUtil.e("AAA onCreateView AveragesFragment");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_averages, container, false);
    initViews();
    setData();
    LogUtil.e("AAA onCreateView AveragesFragment");
    return mainView;
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    barChart = (HorizontalBarChart) mainView.findViewById(R.id.bar_chart);
    barChart.setNoDataText(getString(R.string.no_chart_data_available));
    barChart.invalidate();

    mulitpleBarChart = (HorizontalBarChart) mainView.findViewById(R.id.mulitple_bar_chart);
    mulitpleBarChart.setNoDataText(getString(R.string.no_chart_data_available));
    mulitpleBarChart.invalidate();
  }

  /**
   * sets the data set with the provided data
   * @param averages the list of averages
   * @param ioc the list of ioc
   */
  public void setDataSet(List<Average> averages, List<Average> ioc) {
    this.averages = averages;
    this.ioc = ioc;
    if (mainView != null) {
      setData();
    }
  }

  /**
   * updates the data to be displayed in the view for both bar and multiple bar charts
   */
  private void setData() {
    setBarChart();
    setMulitpleBarChart();
  }

  /**
   * updates the bar chart view
   */
  private void setBarChart() {
    final int size = averages.size();
    LogUtil.e("AAA " + size);
    setChart(barChart);

    ArrayList<BarEntry> yVals1 = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      float val = averages.get(i).average * 100f;
      BarEntry barEntry = new BarEntry(i, val);
      barEntry.setData(BreakdownItem.SELF_COLOR);
      yVals1.add(barEntry);
    }

    CustomBarDataSet set1 = new CustomBarDataSet(getActivity(), yVals1, barChart, "");
    set1.setDrawValues(true);
    set1.setValueTextSize(FONT_SIZE);
    set1.setHighlightEnabled(false);
    set1.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value);
      }
    });

    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
    dataSets.add(set1);

    BarData barData = new BarData(dataSets);
    barData.setBarWidth(0.6f);

    barChart.setData(barData);
    barChart.getLegend().setEnabled(false);

    barChart.setVisibleXRangeMaximum(size);
    barChart.setVisibleXRangeMinimum(size);
    barChart.setFitBars(false);

    ViewGroup.LayoutParams params = barChart.getLayoutParams();
    params.height =
        (ViewUtil.dpToPx(15, getResources()) * size) +
            ViewUtil.dpToPx(40, getResources());
    barChart.setLayoutParams(params);
    barChart.invalidate();
  }

  /**
   * updates the multiple bar chart view
   */
  private void setMulitpleBarChart() {
    final int size = ioc.size();
    setChart(mulitpleBarChart);

    Legend l = mulitpleBarChart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    l.setDrawInside(false);
    l.setTextColor(Color.WHITE);
    l.setXOffset(20f);
    l.setYEntrySpace(5f);
    l.setTextSize(FONT_SIZE);

    ArrayList<BarEntry> self = new ArrayList<>();
    ArrayList<BarEntry> others = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      Average average = ioc.get(i);
      float val = average.roles.get(0).average * 100f;
      BarEntry barEntry = new BarEntry(i, val);
      barEntry.setData(BreakdownItem.OTHERS_COLOR);
      others.add(barEntry);

      float selfValue = 0;
      if (average.roles.size() >= 2) {
        selfValue = average.roles.get(1).average * 100f;
      }
      barEntry = new BarEntry(i, selfValue);
      barEntry.setData(BreakdownItem.SELF_COLOR);
      self.add(barEntry);
    }

    BarDataSet set1 = new BarDataSet(self, getString(R.string.self));
    set1.setDrawValues(true);
    set1.setValueTextSize(FONT_SIZE);
    set1.setColor(getActivity().getResources().getColor(R.color.lynx_color));
    set1.setValueTextColor(getActivity().getResources().getColor(R.color.lynx_color));
    set1.setHighlightEnabled(false);
    set1.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value);
      }
    });

    BarDataSet set2 = new BarDataSet(others, getString(R.string.others_combined));
    set2.setDrawValues(true);
    set2.setValueTextSize(FONT_SIZE);
    set2.setColor(getActivity().getResources().getColor(R.color.lynx_red_color));
    set2.setValueTextColor(getActivity().getResources().getColor(R.color.lynx_red_color));
    set2.setHighlightEnabled(false);
    set2.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value);
      }
    });

    BarData barData = new BarData(set1, set2);
    barData.setBarWidth(0.38f);

    mulitpleBarChart.setData(barData);

    float groupSpace = 0.11f;
    float barSpace = 0.0f;

    mulitpleBarChart.getXAxis().setCenterAxisLabels(true);
    mulitpleBarChart.setVisibleXRangeMaximum(size * 2);
    mulitpleBarChart.setVisibleXRangeMinimum(2);
    mulitpleBarChart.setFitBars(true);

    ViewGroup.LayoutParams params = mulitpleBarChart.getLayoutParams();
    params.height =
        (ViewUtil.dpToPx(17, getResources()) * size * 2) +
            ViewUtil.dpToPx(40, getResources());
    mulitpleBarChart.setLayoutParams(params);
    mulitpleBarChart.getXAxis().setAxisMinimum(0f);
    mulitpleBarChart.groupBars(0, groupSpace, barSpace);
    mulitpleBarChart.invalidate();
  }

  /**
   * updates the bar chart with the given horizontal bar chart
   * @param horizontalBarChart the horizontal bar chart
   */
  private void setChart(HorizontalBarChart horizontalBarChart) {
    horizontalBarChart.setMaxVisibleValueCount(15);
    horizontalBarChart.setDrawBarShadow(false);
    horizontalBarChart.getDescription().setEnabled(false);
    horizontalBarChart.setPinchZoom(false);
    horizontalBarChart.setDrawGridBackground(false);
    horizontalBarChart.setDoubleTapToZoomEnabled(false);
    horizontalBarChart.clearAnimation();

    XAxis xl = horizontalBarChart.getXAxis();
    xl.setPosition(XAxis.XAxisPosition.BOTTOM);
    xl.setDrawGridLines(false);
    xl.setDrawAxisLine(true);
    xl.setGranularity(1f);
    xl.setCenterAxisLabels(false);
    xl.setGranularityEnabled(true);
    xl.setLabelCount(averages.size());
    xl.setTextColor(Color.WHITE);
    xl.setTextSize(FONT_SIZE);
    xl.setAxisLineColor(getResources().getColor(R.color.survey_line));
    xl.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        final int index = (int) value;
        if (index >= 0 && index < averages.size()) {
          return averages.get(index).name;
        }
        return "";
      }
    });

    YAxis yl = horizontalBarChart.getAxisLeft();
    yl.setDrawAxisLine(false);
    yl.setDrawGridLines(false);
    yl.setAxisMinimum(0f);
    yl.setAxisMaximum(112f);
    yl.setDrawTopYLabelEntry(true);
    yl.setDrawLabels(false);

    LimitLine limitLine = new LimitLine(70, "");
    limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
    limitLine.setLineColor(getResources().getColor(R.color.survey_line));
    limitLine.setTextColor(getResources().getColor(R.color.white));
    limitLine.setTextSize(FONT_SIZE);
    yl.addLimitLine(limitLine);

    limitLine = new LimitLine(100, "");
    limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
    limitLine.setLineColor(getResources().getColor(R.color.survey_line));
    limitLine.setTextSize(FONT_SIZE);
    limitLine.setTextColor(getResources().getColor(R.color.white));
    yl.addLimitLine(limitLine);

    YAxis yr = horizontalBarChart.getAxisRight();
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


    horizontalBarChart.getLegend().setEnabled(true);
    horizontalBarChart.getLegend().setYEntrySpace(0f);
  }
}

