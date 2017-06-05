package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.gson.Gson;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.CreateActionPlanAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.Category;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class CreateDetailedDevelopmentPlanActivity extends BaseActivity {

  private EditText                developmentNameEdit;
  private EditText                descriptionEdit;
  private AppCompatCheckBox       remindCheckbox;
  private AppCompatCheckBox       linkCategoryCheckbox;
  private RelativeLayout          spinnerLayout;
  private Spinner                 spinner;
  private SingleDateAndTimePicker datePicker;
  private EditText                addPlanEdit;
  private RelativeLayout          addPlanLayout;

  private CreateActionPlanAdapter adapter;

  public TextView emptyText;

  private Goal param;
  private int  index;
  private long planId;
  private long goalId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_detailed_development_plan);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    final String json = getIntent().getStringExtra("goal_param_body");
    param = !TextUtils.isEmpty(json) ?
        new Gson().fromJson(json, Goal.class) : new Goal();
    index = getIntent().getIntExtra("index", -1);

    planId = getIntent().getLongExtra("planId", 0L);
    goalId = getIntent().getLongExtra("goalId", 0L);

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
    linkCategoryCheckbox = (AppCompatCheckBox) findViewById(R.id.checkbox_link_category);
    addPlanEdit = (EditText) findViewById(R.id.edit_name);
    addPlanLayout = (RelativeLayout) findViewById(R.id.layout_add);
    spinnerLayout = (RelativeLayout) findViewById(R.id.layout_spinner);
    spinner = (Spinner) findViewById(R.id.spinner);

    addPlanEdit.setOnEditorActionListener(editorActionListener);
    emptyText = (TextView) findViewById(R.id.text_empty_state);
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_action_plan);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_divider));
    recyclerView.addItemDecoration(dividerItemDecoration);

    final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

    adapter = new CreateActionPlanAdapter(param.actions, listener);
    recyclerView.setAdapter(adapter);
    notifyAdapter();

    final boolean isRemind = !TextUtils.isEmpty(param.dueDate);
    remindCheckbox.setChecked(isRemind);
    datePicker.setVisibility(isRemind ? View.VISIBLE : View.GONE);

    List<String> categories = new ArrayList<>();
    int index = 0;
    for (int x = 0; x < Shared.categories.size(); x++) {
      final Category category = Shared.categories.get(x);
      categories.add(category.title);
      if (param.categoryId != 0L && category.id == param.categoryId) {
        index = x;
      }
    }

    final boolean isLinkCategory = param.categoryId != 0L;
    linkCategoryCheckbox.setChecked(isLinkCategory);
    spinnerLayout.setVisibility(isLinkCategory ? View.VISIBLE : View.GONE);

    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
        android.R.layout.simple_spinner_item, categories);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(dataAdapter);
    spinner.setSelection(index);

    Calendar calendar = Calendar.getInstance();
    try {
      if (isRemind) {
        calendar.setTime(DateUtil.getAPIFormat().parse(param.dueDate));
      }
    } catch (Exception e) {LogUtil.e("AAA ", e);}
    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);

    datePicker.setMustBeOnFuture(true);
    datePicker.setStepMinutes(15);
    datePicker.selectDate(calendar);

    if (!TextUtils.isEmpty(param.title)) {
      developmentNameEdit.setText(param.title);
      developmentNameEdit.setSelection(developmentNameEdit.getText().length());
    }

    if (!TextUtils.isEmpty(param.description)) {
      descriptionEdit.setText(param.description);
      descriptionEdit.setSelection(descriptionEdit.getText().length());
    }

    setListeners();
  }

  private void setListeners() {
    remindCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        datePicker.setVisibility(b ? View.VISIBLE : View.GONE);
      }
    });

    linkCategoryCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        spinnerLayout.setVisibility(b ? View.VISIBLE : View.GONE);
      }
    });

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        param.categoryId = Shared.categories.get(i).id;
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    developmentNameEdit.setOnFocusChangeListener(onFocusChangeListener);
    descriptionEdit.setOnFocusChangeListener(onFocusChangeListener);
    addPlanEdit.setOnFocusChangeListener(onFocusChangeListener);
  }

  private void notifyAdapter() {
    emptyText.setVisibility(param.actions.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  public void addPlan(View v) {
    if (addPlanLayout.getVisibility() == View.GONE) {
      addPlanLayout.setVisibility(View.VISIBLE);
      addPlanEdit.requestFocus();
    } else {
      addPlanEdit.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }
  }

  public void save(View v) {
    final String developmentPlan = developmentNameEdit.getText().toString();
    final String description = descriptionEdit.getText().toString();

    if (TextUtils.isEmpty(developmentPlan)) {
      Toast.makeText(context, getString(R.string.name_required), Toast.LENGTH_SHORT)
          .show();
      return;
    }

    param.title = developmentPlan;
    param.dueDate = remindCheckbox.isChecked() ?
        DateUtil.getAPIFormat().format(datePicker.getDate()) : null;
    param.categoryId = linkCategoryCheckbox.isChecked() ? param.categoryId : 0L;
    if (!TextUtils.isEmpty(description)) {
      param.description = description;
    }

    if (param.actions.isEmpty()) {
      Toast.makeText(context, getString(R.string.goal_action_added), Toast.LENGTH_SHORT)
          .show();
      return;
    }

    for (int x = 0; x < param.actions.size(); x++) {
      param.actions.get(x).position = x;
    }

    LogUtil.e("AAA " + param.toString());

    Intent intent = new Intent();
    intent.putExtra("goal_param_body", param.toString());
    intent.putExtra("index", index);
    if (!TextUtils.isEmpty(description)) {
      intent.putExtra("description", description);
    }

    setResult(RESULT_OK, intent);
    finish();
  }

  private CreateActionPlanAdapter.OnDeleteListener listener = new CreateActionPlanAdapter
      .OnDeleteListener() {
    @Override
    public void onDelete(int position) {
      Action action = param.actions.get(position);
      if (action.id != 0L) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(getString(R.string.confirmation));
        alertBuilder.setMessage(getString(R.string.delte_action_message, action.title));
        alertBuilder.setPositiveButton(getString(R.string.delete_text),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                // TODO
              }
            });
        alertBuilder.setNegativeButton(getString(R.string.cancel),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            });
        alertBuilder.create().show();
      } else {
        param.actions.remove(position);
        notifyAdapter();
      }
    }
  };

  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        final String name = addPlanEdit.getText().toString();
        if (!TextUtils.isEmpty(name)) {
          Action actionParam = new Action();
          actionParam.title = name;
          param.actions.add(actionParam);
          hideKeyboard();
          notifyAdapter();
          addPlanEdit.setText("");
          addPlanLayout.setVisibility(View.GONE);
        }
        return true;
      }
      return false;
    }
  };

  ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
      ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

    Drawable background;
    int xMarkMargin;
    boolean initiated;

    private void init() {
      background = new ColorDrawable(Color.RED);
      xMarkMargin = (int) getResources().getDimension(R.dimen.ic_clear_margin);
      initiated = true;
    }

    public boolean onMove(RecyclerView recyclerView,
        RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
      param.actions.remove(viewHolder.getLayoutPosition());
      notifyAdapter();
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
        RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
        boolean isCurrentlyActive) {
      View itemView = viewHolder.itemView;

      if (viewHolder.getAdapterPosition() == -1) {
        return;
      }

      if (!initiated) {
        init();
      }

      background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(),
          itemView.getBottom());
      background.draw(c);
      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
  };
}
