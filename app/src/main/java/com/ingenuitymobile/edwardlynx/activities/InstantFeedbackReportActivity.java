package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackAnswersAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.FeedbackFrequency;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbackAnswerResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 */

public class InstantFeedbackReportActivity extends BaseActivity {


  private long                         id;
  private ArrayList<FeedbackFrequency> data;
  private FeedbackAnswersAdapter       adapter;

  private TextView questionText;
  private TextView answerCountText;
  private TextView shareCountText;
  private TextView emptyText;

  public InstantFeedbackReportActivity() {
    data = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_report);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

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
    answerCountText = (TextView) findViewById(R.id.text_answer_count);
    shareCountText = (TextView) findViewById(R.id.text_share_count);
    emptyText = (TextView) findViewById(R.id.text_empty_state);
    final RecyclerView feedbackList = (RecyclerView) findViewById(R.id.list_answers);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    feedbackList.addItemDecoration(dividerItemDecoration);
    feedbackList.setHasFixedSize(true);
    feedbackList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new FeedbackAnswersAdapter(data);
    feedbackList.setAdapter(adapter);
  }

  private void getData() {
    LogUtil.e("AAA getData instant feedbaks detail");
    subscription.add(Shared.apiClient.getInstantFeedback(id, new Subscriber<Feedback>() {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
      }

      @Override
      public void onNext(Feedback feedback) {
        questionText.setText(feedback.questions.get(0).text);
        adapter.setType(feedback.questions.get(0).answer.type);
        int count = 0;
        if (feedback.shares != null) {
          count = feedback.shares.size();
        }
        shareCountText.setText("Total count shared to other people: " + count);
      }
    }));

    LogUtil.e("AAA getData instant feedbaks answers");
    subscription.add(
        Shared.apiClient.getInstantFeedbackAnswers(id, new Subscriber<FeedbackAnswerResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
          }

          @Override
          public void onNext(final FeedbackAnswerResponse response) {
            LogUtil.e("AAA onNext ");
            data.clear();
            data.addAll(response.frequencies);
            adapter.setTotalAnswers(response.totalAnswers);
            answerCountText.setText(getString(R.string.total_answers, response.totalAnswers));
            emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
          }
        }));
  }

  public void share(View v) {
    Intent intent = new Intent(InstantFeedbackReportActivity.this, ShareReportActivity.class);
    intent.putExtra("id", id);
    startActivity(intent);
  }
}
