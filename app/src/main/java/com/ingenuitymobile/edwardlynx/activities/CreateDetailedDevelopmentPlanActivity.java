package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class CreateDetailedDevelopmentPlanActivity extends BaseActivity {

  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");

  private EditText                developmentNameEdit;
  private EditText                descriptionEdit;
  private AppCompatCheckBox       remindCheckbox;
  private SingleDateAndTimePicker datePicker;
  private int                     index;
  private String                  developmentName;
  private String                  dueDate;
  private String                  description;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_detailed_development_plan);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    developmentName = getIntent().getStringExtra("development_plan");
    description = getIntent().getStringExtra("description");
    dueDate = getIntent().getStringExtra("dueDate");
    index = getIntent().getIntExtra("index", -1);

    initViews();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    developmentNameEdit = (EditText) findViewById(R.id.edit_development_name);
    descriptionEdit = (EditText) findViewById(R.id.edit_development_description);
    remindCheckbox = (AppCompatCheckBox) findViewById(R.id.checkbox_remind);
    datePicker = (SingleDateAndTimePicker) findViewById(R.id.date_picker);

    final boolean isRemind = !TextUtils.isEmpty(dueDate);
    remindCheckbox.setChecked(isRemind);
    datePicker.setVisibility(isRemind ? View.VISIBLE : View.GONE);

    remindCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        datePicker.setVisibility(b ? View.VISIBLE : View.GONE);
      }
    });

    Calendar calendar = Calendar.getInstance();
    try {
      if (isRemind) {
        calendar.setTime(format.parse(dueDate));
      }
    } catch (Exception e) {LogUtil.e("AAA ", e);}
    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);

    datePicker.setMustBeOnFuture(true);
    datePicker.setStepMinutes(15);
    datePicker.selectDate(calendar);

    if (!TextUtils.isEmpty(developmentName)) {
      developmentNameEdit.setText(developmentName);
      developmentNameEdit.setSelection(developmentNameEdit.getText().length());
    }

    if (!TextUtils.isEmpty(description)) {
      descriptionEdit.setText(description);
      descriptionEdit.setSelection(developmentNameEdit.getText().length());
    }
  }

  public void save(View v) {
    final String developmentPlan = developmentNameEdit.getText().toString();
    final String description = descriptionEdit.getText().toString();

    if (TextUtils.isEmpty(developmentPlan)) {
      Toast.makeText(context, "Name is required", Toast.LENGTH_SHORT)
          .show();
      return;
    }


    Intent intent = new Intent();
    intent.putExtra("development_plan", developmentPlan);
    intent.putExtra("dueDate",
        remindCheckbox.isChecked() ? format.format(datePicker.getDate()) : null);
    intent.putExtra("index", index);

    if (!TextUtils.isEmpty(description)) {
      intent.putExtra("description", description);
    }

    setResult(RESULT_OK, intent);
    finish();
  }
}
