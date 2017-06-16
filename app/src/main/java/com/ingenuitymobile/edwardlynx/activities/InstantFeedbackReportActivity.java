package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.ingenuitymobile.edwardlynx.adapters.FeedbackAnswersAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.FeedbackFrequency;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbackAnswerResponse;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 */

public class InstantFeedbackReportActivity extends BaseActivity {


  private long                         id;
  private ArrayList<FeedbackFrequency> data;
  private FeedbackAnswersAdapter       adapter;
  private Feedback                     feedback;
  private FeedbackAnswerResponse       feedbackResponse;

  private RecyclerView       feedbackList;
  private HorizontalBarChart horizontalBarChart;
  private TextView           questionText;
  private TextView           emptyText;
  private TextView           dateText;
  private TextView           detailsText;
  private TextView           anonymousText;

  public InstantFeedbackReportActivity() {
    data = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_report);

    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.instant_feedback_answers).toUpperCase());

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
    switch (item.getItemId()) {
    case R.id.share_people:
      share();
      return true;
    case R.id.add_more:
      addMore();
      return true;
    case android.R.id.home:
      finish();
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_feedback_option, menu);
    return true;
  }

  private void initViews() {
    questionText = (TextView) findViewById(R.id.text_question);
    emptyText = (TextView) findViewById(R.id.text_empty_state);
    feedbackList = (RecyclerView) findViewById(R.id.list_answers);
    dateText = (TextView) findViewById(R.id.text_date);
    detailsText = (TextView) findViewById(R.id.text_date_details);
    anonymousText = (TextView) findViewById(R.id.text_anonymous);
    horizontalBarChart = (HorizontalBarChart) findViewById(R.id.horizontal_bar_chart);
    horizontalBarChart.setNoDataText(getString(R.string.no_chart_data_available));

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    feedbackList.addItemDecoration(dividerItemDecoration);
    feedbackList.setHasFixedSize(true);
    feedbackList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new FeedbackAnswersAdapter(data);
    feedbackList.setAdapter(adapter);

    dateText.setText("");
    detailsText.setText("");
  }

  private void getData() {
    LogUtil.e("AAA getData instant feedbaks detail");
    subscription.add(Shared.apiClient.getInstantFeedback(id, new Subscriber<Feedback>() {
      @Override
      public void onCompleted() {
        if (feedback.anonymous == 1) {
          if (feedback.stats.answered >= 3) {
            getAnswers();
          } else {
            horizontalBarChart.setNoDataText(getString(R.string.minimum_chart));
            horizontalBarChart.invalidate();
          }
        } else if (feedback.stats.answered != 0) {
          getAnswers();
        }
        setDetails();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
      }

      @Override
      public void onNext(Feedback response) {
        feedback = response;
      }
    }));
  }

  private void getAnswers() {
    LogUtil.e("AAA getData instant feedbaks answers");
    subscription.add(
        Shared.apiClient.getInstantFeedbackAnswers(id, new Subscriber<FeedbackAnswerResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            adapter.notifyDataSetChanged();
            setData();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
          }

          @Override
          public void onNext(final FeedbackAnswerResponse response) {
            feedbackResponse = response;
            LogUtil.e("AAA onNext ");
          }
        }));
  }

  private void setDetails() {
    questionText.setText(feedback.questions.get(0).text);

    try {
      Date date = DateUtil.getAPIFormat().parse(feedback.createdAt);
      dateText.setText(DateUtil.getDisplayFormat().format(date));
    } catch (Exception e) {
      dateText.setText("");
    }

    detailsText.setText(getString(R.string.details_circle_chart,
        feedback.stats.invited,
        feedback.stats.answered));
    anonymousText.setVisibility(feedback.anonymous == 1 ? View.VISIBLE : View.GONE);
  }

  private void setData() {
    final int type = feedback.questions.get(0).answer.type;

    data.clear();
    data.addAll(feedbackResponse.frequencies);

    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);

    horizontalBarChart.setVisibility(View.GONE);
    feedbackList.setVisibility(View.GONE);

    if (!data.isEmpty()) {
      if (type == Answer.CUSTOM_TEXT) {
        feedbackList.setVisibility(View.VISIBLE);
        adapter.setType(type);
        adapter.setTotalAnswers(feedbackResponse.totalAnswers);
      } else {
        final float FONT_SIZE = 10;
        horizontalBarChart.setVisibility(View.VISIBLE);

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
            return data.get((int) value).description;
          }
        });

        YAxis yl = horizontalBarChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(110f);
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

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
          FeedbackFrequency frequency = data.get(i);
          final float val =
              frequency.count != 0 ?
                  (((float) (frequency.count) / (float) feedback.stats.invited) * 100)
                  : 0;
          yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setDrawValues(true);
        set1.setValueTextSize(FONT_SIZE);
        set1.setValueTextColor(context.getResources().getColor(R.color.instant_feedback_color));
        set1.setHighlightEnabled(false);
        set1.setColor(context.getResources().getColor(R.color.instant_feedback_color));
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

        horizontalBarChart.setData(barData);
        horizontalBarChart.getLegend().setEnabled(false);

        horizontalBarChart.setVisibleXRangeMaximum(data.size());
        horizontalBarChart.setVisibleXRangeMinimum(data.size());
        horizontalBarChart.setFitBars(false);

        horizontalBarChart.getLayoutParams().height = (110 * data.size());
        horizontalBarChart.invalidate();
        horizontalBarChart.clearAnimation();
      }
    }
  }

  private void share() {
    Intent intent = new Intent(InstantFeedbackReportActivity.this, ShareReportActivity.class);
    intent.putExtra("id", id);
    startActivity(intent);
  }

  private void addMore() {
    Intent intent = new Intent(InstantFeedbackReportActivity.this,
        AddMoreParticipantsActivity.class);
    intent.putExtra("id", id);
    startActivity(intent);
  }
}
