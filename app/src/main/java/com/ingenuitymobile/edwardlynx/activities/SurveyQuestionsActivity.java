package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.QuestionsAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.api.models.Questions;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

public class SurveyQuestionsActivity extends BaseActivity {

  private TextView titleText;

  private long                id;
  private QuestionsAdapter    adapter;
  private ArrayList<Question> data;

  public SurveyQuestionsActivity() {
    data = new ArrayList<>();
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
    titleText = (TextView) findViewById(R.id.text_title);
    final RecyclerView questionsList = (RecyclerView) findViewById(R.id.list_questions);


    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    questionsList.addItemDecoration(dividerItemDecoration);
    questionsList.setHasFixedSize(true);
    questionsList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new QuestionsAdapter(data, listener);
    questionsList.setAdapter(adapter);
  }

  private void getData() {
    LogUtil.e("AAA getData questions");
    Shared.apiClient.getSurveyQuestions(id, new Subscriber<Questions>() {
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
      public void onNext(Questions questions) {
        LogUtil.e("AAA onNext ");
        titleText.setText(questions.items.get(0).title);
        data.clear();
        data.addAll(questions.items.get(0).questions);
      }
    });
  }

  private QuestionsAdapter.OnAnswerItemListener listener = new QuestionsAdapter
      .OnAnswerItemListener() {
    @Override
    public void onAnswer(long id, String value) {

    }
  };
}
