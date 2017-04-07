package com.ingenuitymobile.edwardlynx.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.responses.SurveyResultsResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rx.Subscriber;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyReportActivity extends BaseActivity {

  private static final float FONT_SIZE = 11;

  private long              id;
  private ArrayList<String> data;

  private HorizontalBarChart barChart;
  private HorizontalBarChart mulitpleBarChart;
  private TextView           dateText;
  private TextView           detailsText;

  private Survey survey;

  public SurveyReportActivity() {
    data = new ArrayList<>();
    data.add("Category 1");
    data.add("Category 2");
    data.add("Category 3");
    data.add("Category 4");
    data.add("Category 5");
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

    setBarChart();
    setMulitpleBarChart();
  }

  private void getData() {
    LogUtil.e("AAA getData Survey details");
    subscription.add(Shared.apiClient.getSurvey(id, new Subscriber<Survey>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA Survey details onCompleted ");
        getSurveyQuestions();
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
    LogUtil.e("AAA getData questions");
    subscription.add(Shared.apiClient.getSurveyResults(id, new Subscriber<SurveyResultsResponse>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA questions onCompleted ");

      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA questions onError " + e);
      }

      @Override
      public void onNext(SurveyResultsResponse response) {
        detailsText.setText(getString(R.string.details_circle_chart, 0,
            response.totalAnswers));
      }
    }));
  }

  private void setBarChart() {
    setChart(barChart);

    ArrayList<BarEntry> yVals1 = new ArrayList<>();

    for (int i = 0; i < data.size(); i++) {
      float val = (float) (Math.random()) * 100f;
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

    barChart.setVisibleXRangeMaximum(data.size());
    barChart.setVisibleXRangeMinimum(data.size());
    barChart.setFitBars(false);

    barChart.getLayoutParams().height = (100 * data.size());
    barChart.animateXY(1000, 1000);
    barChart.invalidate();
  }

  private void setMulitpleBarChart() {
    setChart(mulitpleBarChart);

    Legend l = mulitpleBarChart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    l.setDrawInside(true);
    l.setTextColor(Color.WHITE);
    l.setYOffset(0f);
    l.setXOffset(20f);
    l.setYEntrySpace(0f);
    l.setTextSize(FONT_SIZE);

    ArrayList<BarEntry> yVals1 = new ArrayList<>();
    ArrayList<BarEntry> yVals2 = new ArrayList<>();

    for (int i = 0; i < data.size(); i++) {
      float val = (float) (Math.random()) * 100f;
      yVals1.add(new BarEntry(i, val));
      val = (float) (Math.random()) * 100f;
      yVals2.add(new BarEntry(i, val));
    }

    BarDataSet set1 = new BarDataSet(yVals1, getString(R.string.self));
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

    BarDataSet set2 = new BarDataSet(yVals2, getString(R.string.others_ombined));
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

    BarData barData = new BarData(set2, set1);
    barData.setBarWidth(0.4f);

    mulitpleBarChart.setData(barData);

    mulitpleBarChart.setVisibleXRangeMaximum(data.size());
    mulitpleBarChart.setVisibleXRangeMinimum(data.size());
    mulitpleBarChart.setFitBars(true);

    float groupSpace = 0.15f;
    float barSpace = 0.0f; // x4 DataSet
    // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

    mulitpleBarChart.getXAxis().setCenterAxisLabels(true);
    mulitpleBarChart.setVisibleXRangeMaximum(data.size());
    mulitpleBarChart.setVisibleXRangeMinimum(data.size());
    mulitpleBarChart.setFitBars(true);

    mulitpleBarChart.getLayoutParams().height = (77 * data.size() * 2);
    mulitpleBarChart.getXAxis().setAxisMinimum(0f);
    mulitpleBarChart.groupBars(0, groupSpace, barSpace);
    mulitpleBarChart.animateXY(1000, 1000);

    mulitpleBarChart.invalidate();
  }

  private void setChart(HorizontalBarChart horizontalBarChart) {
    horizontalBarChart.setMaxVisibleValueCount(15);
    horizontalBarChart.setDrawBarShadow(false);
    horizontalBarChart.getDescription().setEnabled(false);
    horizontalBarChart.setPinchZoom(false);
    horizontalBarChart.setDrawGridBackground(false);
    horizontalBarChart.setDoubleTapToZoomEnabled(false);

    XAxis xl = horizontalBarChart.getXAxis();
    xl.setPosition(XAxis.XAxisPosition.BOTTOM);
    xl.setDrawGridLines(false);
    xl.setDrawAxisLine(true);
    xl.setGranularity(1f);
    xl.setGranularityEnabled(true);
    xl.setLabelCount(data.size());
    xl.setTextColor(Color.WHITE);
    xl.setTextSize(FONT_SIZE);
    xl.setAxisLineColor(getResources().getColor(R.color.survey_line));
    xl.setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        final int index = (int) value;
        if (index >= 0 && index < data.size()) {
          return data.get(index);
        }
        return "";
      }
    });

    YAxis yl = horizontalBarChart.getAxisLeft();
    yl.setDrawAxisLine(false);
    yl.setDrawGridLines(false);
    yl.setAxisMinimum(0f);
    yl.setAxisMaximum(110f);
    yl.setDrawTopYLabelEntry(true);
    yl.setDrawLabels(false);

    LimitLine limitLine = new LimitLine(70, "70%");
    limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
    limitLine.setLineColor(getResources().getColor(R.color.survey_line));
    limitLine.setTextColor(getResources().getColor(R.color.white));
    limitLine.setTextSize(FONT_SIZE);
    yl.addLimitLine(limitLine);

    limitLine = new LimitLine(100, "100%");
    limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
    limitLine.setLineColor(getResources().getColor(R.color.survey_line));
    limitLine.setTextSize(FONT_SIZE);
    limitLine.setTextColor(getResources().getColor(R.color.white));
    yl.addLimitLine(limitLine);

    YAxis yr = horizontalBarChart.getAxisRight();
    yr.setDrawGridLines(false);
    yr.setDrawAxisLine(false);
    yr.setDrawLabels(false);
  }
}
