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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.CustomScaleAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

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

  private LinearLayout customScaleLayout;
  private RecyclerView optionsList;
  private EditText     addOptionEdit;

  private ArrayList<String> options;

  private CustomScaleAdapter adapter;

  public CreateFeedbackActivity() {
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
    typeSpinner = (Spinner) findViewById(R.id.spinner_type);
    isAnonymousCheckbox = (CheckBox) findViewById(R.id.checkbox_is_anonymous);
    isNA = (CheckBox) findViewById(R.id.checkbox_is_na);
    isNALayout = (RelativeLayout) findViewById(R.id.layout_isNA);

    typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        isNALayout.setVisibility(i == Answer.CUSTOM_TEXT ? View.GONE : View.VISIBLE);
        customScaleLayout.setVisibility(i == Answer.CUSTOM_SCALE ? View.VISIBLE : View.GONE);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

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

  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        hideKeyboard();

        final String string = addOptionEdit.getText().toString();
        if (!TextUtils.isEmpty(string)) {
          if (options.contains(string)) {
            Toast.makeText(CreateFeedbackActivity.this, "There is an existing option",
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
