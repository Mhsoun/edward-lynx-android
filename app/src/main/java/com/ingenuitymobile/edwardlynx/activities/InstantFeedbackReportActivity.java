package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.txusballesteros.widgets.FitChart;

import java.text.SimpleDateFormat;
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

  public InstantFeedbackReportActivity() {
    data = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_report);

    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.findViewById(R.id.image_share).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        share(v);
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
    questionText = (TextView) findViewById(R.id.text_question);
    emptyText = (TextView) findViewById(R.id.text_empty_state);
    feedbackList = (RecyclerView) findViewById(R.id.list_answers);
    horizontalBarChart = (HorizontalBarChart) findViewById(R.id.horizontal_bar_chart);
    dateText = (TextView) findViewById(R.id.text_date);
    detailsText = (TextView) findViewById(R.id.text_date_details);

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
        getAnswers();
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

  private void setData() {
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
    final SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy");
    final int type = feedback.questions.get(0).answer.type;

    int count = 0;
    if (feedback.shares != null) {
      count = feedback.shares.size();
    }
    data.clear();
    data.addAll(feedbackResponse.frequencies);

    questionText.setText(feedback.questions.get(0).text);
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);

    horizontalBarChart.setVisibility(View.GONE);
    feedbackList.setVisibility(View.GONE);

    if (!data.isEmpty()) {
      if (type == Answer.CUSTOM_TEXT) {
        feedbackList.setVisibility(View.VISIBLE);
        adapter.setType(type);
        adapter.setTotalAnswers(feedbackResponse.totalAnswers);
      } else {
        LogUtil.e("AAA " + data.size());

        for (FeedbackFrequency frequency : data) {
          LogUtil.e("AAA " + frequency.description + " " + frequency.count);
        }
        final float FONT_SIZE = 11;
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

        LimitLine limitLine = new LimitLine(70, "70%");
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        limitLine.setLineColor(getResources().getColor(R.color.survey_line));
        limitLine.setTextColor(getResources().getColor(R.color.white));
        limitLine.setTextSize(FONT_SIZE);
        yl.addLimitLine(limitLine);

        limitLine = new LimitLine(100, "100%");
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        limitLine.setLineColor(getResources().getColor(R.color.survey_line));
        limitLine.setTextSize(FONT_SIZE);
        limitLine.setTextColor(getResources().getColor(R.color.white));
        yl.addLimitLine(limitLine);

        YAxis yr = horizontalBarChart.getAxisRight();
        yr.setDrawGridLines(false);
        yr.setDrawAxisLine(false);
        yr.setDrawLabels(false);

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
          FeedbackFrequency frequency = data.get(i);
          final float val = frequency.count != 0 ?
              (((float) (frequency.count) /
                  (float) feedbackResponse.totalAnswers) * 100) : 0;
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

        horizontalBarChart.setData(barData);
        horizontalBarChart.getLegend().setEnabled(false);

        horizontalBarChart.setVisibleXRangeMaximum(data.size());
        horizontalBarChart.setVisibleXRangeMinimum(data.size());
        horizontalBarChart.setFitBars(false);

        horizontalBarChart.getLayoutParams().height = (110 * data.size());
        horizontalBarChart.animateXY(1000, 1000);
        horizontalBarChart.invalidate();
      }
    }

    try {
      Date date = format.parse(feedback.createdAt);
      dateText.setText(displayFormat.format(date));
    } catch (Exception e) {
      dateText.setText("");
    }

    detailsText.setText(getString(R.string.details_circle_chart,
        feedback.stats.answered,
        feedback.stats.invited));
  }

  public void share(View v) {
    Intent intent = new Intent(InstantFeedbackReportActivity.this, ShareReportActivity.class);
    intent.putExtra("id", id);
    startActivity(intent);
  }

  public void addMore(View v) {
    Intent intent = new Intent(InstantFeedbackReportActivity.this,
        AddMoreParticipantsActivity.class);
    intent.putExtra("id", id);
    startActivity(intent);
  }
}
