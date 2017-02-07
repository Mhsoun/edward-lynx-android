package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.CustomScaleAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.QuestionBody;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import java.util.ArrayList;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class CreateFeedbackActivity extends BaseActivity {

  private final int REQUEST_CODE = 100;

  private EditText       questionText;
  private CheckBox       isAnonymousCheckbox;
  private CheckBox       isNA;
  private RelativeLayout isNALayout;

  private LinearLayout customScaleLayout;
  private EditText     addOptionEdit;

  private ArrayList<String> options;

  private CustomScaleAdapter adapter;

  private int type;

  public CreateFeedbackActivity() {
    type = -1;
    options = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_feedback);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    initViews();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
      finish();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    questionText = (EditText) findViewById(R.id.edit_question);
    isAnonymousCheckbox = (CheckBox) findViewById(R.id.checkbox_is_anonymous);
    isNA = (CheckBox) findViewById(R.id.checkbox_is_na);
    isNALayout = (RelativeLayout) findViewById(R.id.layout_isNA);

    final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup_answer_type);

    for (int i = 0; i < radioGroup.getChildCount(); i++) {
      final int index = i;
      radioGroup.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          String title = "";
          String description = "";
          switch (Integer.parseInt((String) radioGroup.getChildAt(index).getTag())) {
          case Answer.YES_OR_NO:
            title = getString(R.string.yes_or_no);
            description = getString(R.string.yes_or_no_description);
            break;
          case Answer.CUSTOM_TEXT:
            title = getString(R.string.free_text);
            description = getString(R.string.text_description);
            break;
          case Answer.NUMERIC_1_10_SCALE:
            title = getString(R.string.numeric_1_to_10);
            description = getString(R.string.numeric_1_10_description);
            break;
          case Answer.CUSTOM_SCALE:
            title = getString(R.string.custom_scale);
            description = getString(R.string.custom_scale_description);
            break;
          }

          ViewUtil.showAlert(CreateFeedbackActivity.this, title, description);
          return true;
        }
      });
    }

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
        type = Integer.parseInt((String) radioButton.getTag());
        LogUtil.e("AAA " + type);

        isNALayout.setVisibility(type == Answer.CUSTOM_TEXT ? View.GONE : View.VISIBLE);
        customScaleLayout.setVisibility(type == Answer.CUSTOM_SCALE ? View.VISIBLE : View.GONE);
      }
    });

    customScaleLayout = (LinearLayout) findViewById(R.id.layout_custom_scale);
    addOptionEdit = (EditText) findViewById(R.id.edit_add_option);
    addOptionEdit.setOnEditorActionListener(editorActionListener);

    final RecyclerView optionList = (RecyclerView) findViewById(R.id.list_options);
    optionList.setHasFixedSize(true);
    optionList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new CustomScaleAdapter(options, listener);
    optionList.setAdapter(adapter);
  }

  public void invite(View v) {
    final String question = questionText.getText().toString();

    if (TextUtils.isEmpty(question)) {
      Toast.makeText(CreateFeedbackActivity.this, getString(R.string.question_required_label),
          Toast.LENGTH_SHORT).show();
    } else if (type == -1) {
      Toast.makeText(CreateFeedbackActivity.this, getString(R.string.answer_required_label),
          Toast.LENGTH_SHORT).show();
    } else {
      Intent intent = new Intent(this, InviteActivity.class);

      InstantFeedbackBody body = new InstantFeedbackBody();
      body.lang = Shared.user.lang;
      body.isAnonymous = isAnonymousCheckbox.isChecked();
      QuestionBody questionBody = new QuestionBody();
      questionBody.text = question;
      questionBody.isNA = isNA.isChecked();
      AnswerBody answerBody = new AnswerBody();
      answerBody.type = type;
      if (type == 8 && options.isEmpty()) {
        Toast.makeText(CreateFeedbackActivity.this, getString(R.string.custom_scale_required_label),
            Toast.LENGTH_SHORT).show();
        return;
      }

      answerBody.options = options;
      body.questionBodies.add(questionBody);
      questionBody.answerBody = answerBody;

      intent.putExtra("body", body.toString());

      startActivityForResult(intent, REQUEST_CODE);
    }
  }

  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        hideKeyboard();

        final String string = addOptionEdit.getText().toString();
        if (!TextUtils.isEmpty(string)) {
          if (options.contains(string)) {
            Toast.makeText(CreateFeedbackActivity.this, getString(R.string.existing_option),
                Toast.LENGTH_SHORT).show();
            return true;
          }

          options.add(string);
          adapter.notifyDataSetChanged();
          addOptionEdit.setText("");
        }
        return true;
      }
      return false;
    }
  };

  private CustomScaleAdapter.OnDeleteListener listener = new CustomScaleAdapter.OnDeleteListener
      () {
    @Override
    public void onDelete(int position) {
      options.remove(position);
      adapter.notifyDataSetChanged();
    }
  };
}
