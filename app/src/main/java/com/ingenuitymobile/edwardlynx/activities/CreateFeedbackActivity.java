package com.ingenuitymobile.edwardlynx.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.CustomScaleAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.QuestionBody;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.utils.AnswerTypeUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class CreateFeedbackActivity extends BaseActivity {

  private final int REQUEST_CODE = 100;

  private Spinner        spinner;
  private EditText       questionText;
  private CheckBox       isAnonymousCheckbox;
  private CheckBox       isNA;
  private RelativeLayout isNALayout;

  private LinearLayout customScaleLayout;
  private EditText     addOptionEdit;

  private ArrayList<String> options;

  private CustomScaleAdapter adapter;

  private int type;

  private String[] answerTypes;

  private TextView       questionPreviewText;
  private SegmentedGroup segmentedGroup;
  private RadioGroup     previewRadiogroup;
  private EditText       freeTextEdit;

  public CreateFeedbackActivity() {
    type = -1;
    options = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_feedback);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    answerTypes = new String[]{
        getString(R.string.yes_or_no),
        getString(R.string.free_text),
        getString(R.string.numeric_1_to_10),
        getString(R.string.custom_scale)
    };

    initViews();
    setPreview();
    hideKeyboard();
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
    questionText.setOnFocusChangeListener(onFocusChangeListener);
    isAnonymousCheckbox = (CheckBox) findViewById(R.id.checkbox_is_anonymous);
    isNA = (CheckBox) findViewById(R.id.checkbox_is_na);
    isNALayout = (RelativeLayout) findViewById(R.id.layout_isNA);
    spinner = (Spinner) findViewById(R.id.spinner);

    questionPreviewText = (TextView) findViewById(R.id.text_question);
    segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented_group);
    previewRadiogroup = (RadioGroup) findViewById(R.id.radiogroup);
    freeTextEdit = (EditText) findViewById(R.id.free_text_edit);

    final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
        this,
        R.layout.spinner_style,
        answerTypes
    );

    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(dataAdapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = AnswerTypeUtil.getIntType(context, answerTypes[i]);

        isNALayout.setVisibility(type == Answer.CUSTOM_TEXT ? View.GONE : View.VISIBLE);
        customScaleLayout.setVisibility(type == Answer.CUSTOM_SCALE ? View.VISIBLE : View.GONE);
        setPreview();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    questionText.addTextChangedListener(textWatcher);

    customScaleLayout = (LinearLayout) findViewById(R.id.layout_custom_scale);
    addOptionEdit = (EditText) findViewById(R.id.edit_add_option);
    addOptionEdit.setOnEditorActionListener(editorActionListener);

    isNA.setOnCheckedChangeListener(onCheckedChangeListener);

    final RecyclerView optionList = (RecyclerView) findViewById(R.id.list_options);
    optionList.setHasFixedSize(true);
    optionList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new CustomScaleAdapter(options, listener);
    optionList.setAdapter(adapter);

    questionText.setOnFocusChangeListener(onFocusChangeListener);
    addOptionEdit.setOnFocusChangeListener(onFocusChangeListener);
  }

  public void add(View v) {
    addOptionEdit.onEditorAction(EditorInfo.IME_ACTION_DONE);
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

  private void setPreview() {
    if (type == Answer.CUSTOM_TEXT) {
      previewRadiogroup.setVisibility(View.GONE);
      freeTextEdit.setVisibility(View.VISIBLE);
      freeTextEdit.setEnabled(false);
    } else {
      freeTextEdit.setVisibility(View.GONE);

      previewRadiogroup.setVisibility(type == Answer.NUMERIC_1_10_SCALE ? View.GONE : View.VISIBLE);
      segmentedGroup.setVisibility(type == Answer.NUMERIC_1_10_SCALE ? View.VISIBLE : View.GONE);

      previewRadiogroup.removeAllViews();
      segmentedGroup.removeAllViews();

      ArrayList<String> strings = new ArrayList<>();

      if (type == Answer.CUSTOM_SCALE) {
        strings = options;
      } else if (type == Answer.YES_OR_NO) {
        strings.add("No");
        strings.add("Yes");
      } else if (type == Answer.NUMERIC_1_10_SCALE) {
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        strings.add("7");
        strings.add("8");
        strings.add("9");
        strings.add("10");
      }

      for (String string : strings) {
        if (type == Answer.NUMERIC_1_10_SCALE) {
          createSegmentedButton(segmentedGroup, this, string);
        } else {
          createRadioButton(previewRadiogroup, this, string);
        }
      }

      if (isNA.isChecked()) {
        if (type == Answer.NUMERIC_1_10_SCALE) {
          createSegmentedButton(segmentedGroup, this, context.getString(R.string.not_available));
        } else {
          createRadioButton(previewRadiogroup, this, context.getString(R.string.not_available));
        }
      }
    }
  }

  private void createRadioButton(final RadioGroup radioGroup, final Context context,
      final String description) {
    final RadioButton radioButton = new RadioButton(context);
    final ViewGroup.LayoutParams lparam = new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    radioButton.setLayoutParams(lparam);
    radioButton.setEnabled(false);
    radioButton.setTextColor(context.getResources().getColor(R.color.white));
    radioButton.setText(description);
    radioGroup.addView(radioButton);
  }

  private void createSegmentedButton(
      final SegmentedGroup radioGroup,
      final Context context,
      final String description) {

    final RadioButton radioButton = new RadioButton(context);
    final RadioGroup.LayoutParams lparam = new RadioGroup.LayoutParams(
        0,
        LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

    lparam.gravity = Gravity.CENTER;
    radioButton.setLayoutParams(lparam);
    radioButton.setMinimumHeight(ViewUtil.dpToPx(40, context.getResources()));
    radioButton.setText(description);
    radioButton.setGravity(Gravity.CENTER);
    radioButton.setButtonDrawable(new StateListDrawable());
    radioButton.setTextSize(11);
    radioGroup.addView(radioButton);
    radioGroup.updateBackground();
    radioGroup.invalidate();
  }

  private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton
      .OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      setPreview();
    }
  };

  private TextWatcher textWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      questionPreviewText.setText(s.toString());
    }
  };

  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        hideKeyboard(textView);
        addOptionEdit.clearFocus();

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
          setPreview();
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
