package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class CreateFeedbackActivity extends BaseActivity {

  private final int REQUEST_CODE = 100;

  private EditText       questionText;
  private Spinner        typeSpinner;
  private CheckBox       isAnonymousCheckbox;
  private CheckBox       isNA;
  private RelativeLayout isNALayout;

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
    typeSpinner = (Spinner) findViewById(R.id.spinner_type);
    isAnonymousCheckbox = (CheckBox) findViewById(R.id.checkbox_is_anonymous);
    isNA = (CheckBox) findViewById(R.id.checkbox_is_na);
    isNALayout = (RelativeLayout) findViewById(R.id.layout_isNA);

    typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        isNALayout.setVisibility(i == Answer.CUSTOM_TEXT ? View.GONE : View.VISIBLE);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
  }

  public void invite(View v) {
    final String question = questionText.getText().toString();

    if (TextUtils.isEmpty(question)) {
      Toast.makeText(CreateFeedbackActivity.this, "Question is required", Toast.LENGTH_SHORT)
          .show();
    } else {
      Intent intent = new Intent(this, InviteActivity.class);

      intent.putExtra("question", question);
      intent.putExtra("question_type", String.valueOf(typeSpinner.getSelectedItemPosition()));
      intent.putExtra("is_anonymous", isAnonymousCheckbox.isChecked());
      intent.putExtra("is_na", isNA.isChecked());
      LogUtil.e("AAA " + isNA.isChecked());
      startActivityForResult(intent, REQUEST_CODE);
    }
  }
}
