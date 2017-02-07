package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.SurveyQuestionsAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerParam;
import com.ingenuitymobile.edwardlynx.api.models.Category;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.api.models.Questions;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

public class SurveyQuestionsActivity extends BaseActivity {

  private TextView submitText;

  private long                   id;
  private SurveyQuestionsAdapter adapter;
  private ArrayList<Question>    data;
  private ArrayList<AnswerBody>  bodies;
  private Survey                 survey;

  public SurveyQuestionsActivity() {
    data = new ArrayList<>();
    bodies = new ArrayList<>();
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_survey_questions);

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
    submitText = (TextView) findViewById(R.id.text_submit);
    final RecyclerView questionsList = (RecyclerView) findViewById(R.id.list_questions);

    questionsList.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SurveyQuestionsAdapter(data, listener);
    questionsList.setAdapter(adapter);
  }

  private void getData() {
    LogUtil.e("AAA getData Survey details");
    subscription.add(Shared.apiClient.getSurvey(id, new Subscriber<Survey>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA Survey details onCompleted ");
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA Survey details onError " + e);
      }

      @Override
      public void onNext(Survey surveyResponse) {
        LogUtil.e("AAA Survey details onNext ");
        survey = surveyResponse;
        setTitle(surveyResponse.name);
        adapter.isEnabled(surveyResponse.status != Survey.COMPLETED);
        submitText.setVisibility(survey.status == Survey.COMPLETED ? View.GONE : View.VISIBLE);
        LogUtil.e("AAA " + survey.key);
      }
    }));


    LogUtil.e("AAA getData questions");
    subscription.add(Shared.apiClient.getSurveyQuestions(id, new Subscriber<Questions>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA questions onCompleted ");
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA questions onError " + e);
      }

      @Override
      public void onNext(Questions questions) {
        LogUtil.e("AAA questions onNext");
        data.clear();
        for (Category category : questions.items) {
          Question question = new Question();
          question.isSectionHeader = true;
          question.text = category.title;
          question.description = category.description;
          data.add(question);
          data.addAll(category.questions);
        }
      }
    }));
  }

  public void submit(View v) {
    AnswerParam param = new AnswerParam();
    param.answers = bodies;
    param.key = survey.key;
    param.isFinal = bodies.size() == data.size();

    LogUtil.e("AAA id " + id);
    LogUtil.e("AAA " + param.toString());
    submitText.setText(getString(R.string.loading));
    subscription.add(Shared.apiClient.postAnswerSurvey(id, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted");
            finish();
          }

          @Override
          public void onError(Throwable e) {
            submitText.setText(R.string.submit);
            LogUtil.e("AAA onError" + e);
            Toast.makeText(SurveyQuestionsActivity.this, getString(R.string.cant_connect),
                Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onNext(Response response) {
            LogUtil.e("AAA onNext");
            Toast.makeText(SurveyQuestionsActivity.this,
                getString(R.string.survey_answers_submitted), Toast.LENGTH_SHORT).show();
          }
        }));

  }

  private SurveyQuestionsAdapter.OnAnswerItemListener listener = new SurveyQuestionsAdapter
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
