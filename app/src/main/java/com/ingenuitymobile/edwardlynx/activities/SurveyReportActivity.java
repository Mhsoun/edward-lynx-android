package com.ingenuitymobile.edwardlynx.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.models.Average;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.responses.SurveyResultsResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rx.Subscriber;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyReportActivity extends BaseActivity {

  private static final float FONT_SIZE = 11;

  private long               id;
  private ArrayList<Average> averages;
  private ArrayList<Average> ioc;

  private HorizontalBarChart barChart;
  private HorizontalBarChart mulitpleBarChart;
  private TextView           dateText;
  private TextView           detailsText;

  private Survey survey;

  public SurveyReportActivity() {
    averages = new ArrayList<>();
    ioc = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_survey_report);

    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.findViewById(R.id.image_share).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO
      }
    });

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    id = getIntent().getLongExtra("id", 0L);
    initViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getData();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    barChart = (HorizontalBarChart) findViewById(R.id.bar_chart);
    mulitpleBarChart = (HorizontalBarChart) findViewById(R.id.mulitple_bar_chart);
    dateText = (TextView) findViewById(R.id.text_date);
    detailsText = (TextView) findViewById(R.id.text_date_details);

    dateText.setText("January 3, 2017");
    detailsText.setText(getString(R.string.details_circle_chart, 0, 0));
  }

  private void getData() {
    LogUtil.e("AAA getData Survey details");
    subscription.add(Shared.apiClient.getSurvey(id, new Subscriber<Survey>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA Survey details onCompleted ");

        detailsText.setText(getString(R.string.details_circle_chart, survey.stats.invited,
            survey.stats.answered));

        if (survey.stats.answered != 0) {
          getSurveyQuestions();
        }
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA Survey details onError " + e);
      }

      @Override
      public void onNext(Survey surveyResponse) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
        final SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy");

        survey = surveyResponse;
        setTitle(surveyResponse.name);

        try {
          Date date = format.parse(survey.endDate);
          dateText.setText(displayFormat.format(date));
        } catch (Exception e) {
          dateText.setText("");
        }
      }
    }));
  }

  private void getSurveyQuestions() {
    LogUtil.e("AAA getData questions " + id);
    subscription.add(Shared.apiClient.getSurveyResults(id, new Subscriber<SurveyResultsResponse>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA questions onCompleted ");
        setBarChart();
        setMulitpleBarChart();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA questions onError " + e);
      }

      @Override
      public void onNext(SurveyResultsResponse response) {
        ioc.clear();
        ioc.addAll(response.ioc);

        averages.clear();
        averages.addAll(response.averages);
      }
    }));
  }

  private void setBarChart() {
    final int size = averages.size();
    LogUtil.e("AAA " + size);
    setChart(barChart);

    ArrayList<BarEntry> yVals1 = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      float val = averages.get(i).average * 100f;
      yVals1.add(new BarEntry(i, val));
    }

    BarDataSet set1 = new BarDataSet(yVals1, "");
    set1.setDrawValues(true);
    set1.setValueTextSize(FONT_SIZE);
    set1.setValueTextColor(context.getResources().getColor(R.color.lynx_color));
    set1.setHighlightEnabled(false);
    set1.setColor(context.getResources().getColor(R.color.lynx_color));
    set1.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value) + "%";
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
      others.add(new BarEntry(i, val));

      float selfValue = 0;
      if (average.roles.size() >= 2) {
        selfValue = average.roles.get(1).average * 100f;
      }
      self.add(new BarEntry(i, selfValue));
    }

    BarDataSet set1 = new BarDataSet(self, getString(R.string.self));
    set1.setDrawValues(true);
    set1.setValueTextSize(FONT_SIZE);
    set1.setValueTextColor(context.getResources().getColor(R.color.lynx_color));
    set1.setHighlightEnabled(false);
    set1.setColor(context.getResources().getColor(R.color.lynx_color));
    set1.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value) + "%";
      }
    });

    BarDataSet set2 = new BarDataSet(others, getString(R.string.others_ombined));
    set2.setDrawValues(true);
    set2.setValueTextSize(FONT_SIZE);
    set2.setValueTextColor(context.getResources().getColor(R.color.colorAccent));
    set2.setHighlightEnabled(false);
    set2.setColor(context.getResources().getColor(R.color.colorAccent));
    set2.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value) + "%";
      }
    });

    BarData barData = new BarData(set1, set2);
    barData.setBarWidth(0.4f);

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
