package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.CustomScaleAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Option;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 22/02/2017.
 */

public class InstantFeedbackDetailedActivity extends BaseActivity {

  private EditText       questionText;
  private CheckBox       isAnonymousCheckbox;
  private CheckBox       isNA;
  private RelativeLayout isNALayout;
  private RadioGroup     answerTypeRadioGroup;

  private LinearLayout customScaleLayout;

  private ArrayList<String> options;

  private CustomScaleAdapter adapter;

  public InstantFeedbackDetailedActivity() {
    options = new ArrayList<>();
  }

  private long id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_instant_feedback_detailed);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);
    context = this;
    initViews();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume() {
    super.onResume();
    getData();
  }

  private void initViews() {
    questionText = (EditText) findViewById(R.id.edit_question);
    isAnonymousCheckbox = (CheckBox) findViewById(R.id.checkbox_is_anonymous);
    isNA = (CheckBox) findViewById(R.id.checkbox_is_na);
    isNALayout = (RelativeLayout) findViewById(R.id.layout_isNA);
    answerTypeRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_answer_type);
    customScaleLayout = (LinearLayout) findViewById(R.id.layout_custom_scale);

    final RecyclerView optionList = (RecyclerView) findViewById(R.id.list_options);
    optionList.setHasFixedSize(true);
    optionList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new CustomScaleAdapter(options, null);
    optionList.setAdapter(adapter);
  }

  private void getData() {
    subscription.add(Shared.apiClient.getInstantFeedback(id, new Subscriber<Feedback>() {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
        Toast.makeText(
            context,
            context.getString(R.string.cant_connect),
            Toast.LENGTH_SHORT
        ).show();
      }

      @Override
      public void onNext(Feedback feedback) {
        final Question question = feedback.questions.get(0);
        questionText.setText(question.text);
        isNA.setChecked(question.isNA == 1);
        isAnonymousCheckbox.setChecked(feedback.anonymous == 1);

        for (int i = 0; i < answerTypeRadioGroup.getChildCount(); i++) {
          answerTypeRadioGroup.getChildAt(i).setEnabled(false);

          if (answerTypeRadioGroup.getChildAt(i).getTag().equals(
              String.valueOf(question.answer.type))) {
            ((RadioButton) answerTypeRadioGroup.getChildAt(i)).setChecked(true);
          }
        }

        isNALayout.setVisibility(
            question.answer.type == Answer.CUSTOM_TEXT ? View.GONE : View.VISIBLE);
        customScaleLayout.setVisibility(
            question.answer.type == Answer.CUSTOM_SCALE ? View.VISIBLE : View.GONE);

        if (question.answer.type == Answer.CUSTOM_SCALE) {
          for (Option option : question.answer.options) {
            options.add(option.description);
          }
          adapter.notifyDataSetChanged();
        }
      }
    }));
  }

  public void add(View v) {
    Intent intent = new Intent(context, AddMoreParticipantsActivity.class);
    intent.putExtra("id", id);
    startActivity(intent);
  }
}
