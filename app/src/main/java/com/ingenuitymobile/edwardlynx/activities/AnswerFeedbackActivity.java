package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackQuestionsAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerParam;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class AnswerFeedbackActivity extends BaseActivity {

  private long                     id;
  private String                   key;
  private FeedbackQuestionsAdapter adapter;
  private ArrayList<Question>      data;
  private ArrayList<AnswerBody>    bodies;


  public AnswerFeedbackActivity() {
    data = new ArrayList<>();
    bodies = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_answer_feedback);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);

    initViews();
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
    final RecyclerView questionsList = (RecyclerView) findViewById(R.id.list_questions);


    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    questionsList.addItemDecoration(dividerItemDecoration);
    questionsList.setHasFixedSize(true);
    questionsList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new FeedbackQuestionsAdapter(data, listener);
    questionsList.setAdapter(adapter);
  }

  private void getData() {
    LogUtil.e("AAA getData questions");
    subscription.add(Shared.apiClient.getInstantFeedback(id, new Subscriber<Feedback>() {
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
      public void onNext(Feedback feedback) {
        LogUtil.e("AAA onNext ");
        data.clear();
        data.addAll(feedback.questions);
        key = feedback.key;
      }
    }));
  }

  public void submit(View v) {
    final TextView textView = (TextView) v;
    AnswerParam param = new AnswerParam();
    param.answers = bodies;
    param.key = key;

    LogUtil.e("AAA " + param.toString());
    LogUtil.e("AAA id + " + id);
    progressDialog.show();
    subscription.add(
        Shared.apiClient.postInstantFeedbackAnswers(id, param, new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted");
            finish();
          }

          @Override
          public void onError(Throwable e) {
            progressDialog.dismiss();
            LogUtil.e("AAA onError " + e);
            if (e != null && ((RetrofitError) e).getResponse().getStatus() == 422) {
              Toast.makeText(context, getString(R.string.required_fields),
                  Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onNext(Response response) {
            LogUtil.e("AAA onNext");
            progressDialog.dismiss();
            Toast.makeText(AnswerFeedbackActivity.this, getString(R.string.instant_feed_submitted),
                Toast.LENGTH_SHORT).show();
          }
        }));
  }

  private FeedbackQuestionsAdapter.OnAnswerItemListener listener = new FeedbackQuestionsAdapter
      .OnAnswerItemListener() {
    @Override
    public void onAnswer(long id, String value) {
      for (int x = 0; x < bodies.size(); x++) {
        AnswerBody body = bodies.get(x);
        if (id == body.question) {
          body.answer = value;
          return;
        }
      }

      AnswerBody body = new AnswerBody();
      body.question = id;
      body.answer = value;
      bodies.add(body);
    }
  };
}

