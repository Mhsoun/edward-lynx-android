package com.ingenuitymobile.edwardlynx.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Average;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/29/17.
 */

public class RadarFragment extends BaseFragment {

  private static final float FONT_SIZE = 11;

  private View mainView;

  private List<Average> averages;

  private RadarChart radarChart;

  public RadarFragment() {
    averages = new ArrayList<>();
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
      LogUtil.e("AAA onCreateView RadarFragment");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_radar, container, false);
    LogUtil.e("AAA onCreateView RadarFragment");
    initViews();
    setData();
    return mainView;
  }

  private void initViews() {
    radarChart = (RadarChart) mainView.findViewById(R.id.radar_chart);
    radarChart.setNoDataText(getString(R.string.no_chart_data_available));
    radarChart.invalidate();
  }

  public void setDataSet(List<Average> averages) {
    this.averages = averages;
    if (mainView != null) {
      setData();
    }
  }

  private void setData() {
    final int size = averages.size();

    radarChart.setLogEnabled(true);
    radarChart.setWebLineWidth(1f);
    radarChart.setWebColor(getResources().getColor(R.color.survey_line));
    radarChart.setWebLineWidthInner(1f);
    radarChart.setWebColorInner(getResources().getColor(R.color.survey_line));
    radarChart.getDescription().setEnabled(false);
    radarChart.setRotationEnabled(false);
    radarChart.clearAnimation();

    ArrayList<RadarEntry> self = new ArrayList<>();
    ArrayList<RadarEntry> others = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      Average average = averages.get(i);
      float val = average.roles.get(0).average * 100f;
      others.add(new RadarEntry(val));

      float selfValue = 0;
      if (average.roles.size() >= 2) {
        selfValue = average.roles.get(1).average * 100f;
      }
      self.add(new RadarEntry(selfValue));
    }

    RadarDataSet set1 = new RadarDataSet(self, getString(R.string.self));
    set1.setColor(context.getResources().getColor(R.color.lynx_color));
    set1.setDrawFilled(false);
    set1.setLineWidth(2f);
    set1.setDrawHighlightCircleEnabled(true);
    set1.setDrawHighlightIndicators(false);

    RadarDataSet set2 = new RadarDataSet(others, getString(R.string.others_combined));
    set2.setColor(context.getResources().getColor(R.color.lynx_red_color));
    set2.setDrawFilled(false);
    set2.setLineWidth(2f);
    set2.setDrawHighlightCircleEnabled(true);
    set2.setDrawHighlightIndicators(false);

    RadarData data = new RadarData(set1, set2);
    data.setDrawValues(false);

    radarChart.setData(data);
    radarChart.invalidate();

    XAxis xl = radarChart.getXAxis();
    xl.setTextSize(FONT_SIZE);
    xl.setTextColor(Color.WHITE);
    xl.setYOffset(0f);
    xl.setXOffset(0f);
    xl.setAxisMinimum(0f);
    xl.setAxisMaximum(60f);
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

    YAxis yr = radarChart.getYAxis();
    yr.setLabelCount(5, false);
    yr.setTextSize(FONT_SIZE);
    xl.setXOffset(0f);
    yr.setYOffset(0f);
    yr.setAxisMinimum(0f);
    yr.setAxisMaximum(80f);
    yr.setDrawLabels(true);
    yr.setTextColor(Color.WHITE);

    Legend l = radarChart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    l.setDrawInside(true);
    l.setTextColor(Color.WHITE);
    l.setXOffset(0f);
    l.setYOffset(0f);
    l.setYEntrySpace(0f);
    l.setTextSize(FONT_SIZE);

    radarChart.invalidate();
  }
}
