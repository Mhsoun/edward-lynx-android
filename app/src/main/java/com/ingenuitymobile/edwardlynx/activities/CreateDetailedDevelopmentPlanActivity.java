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
import android.widget.ScrollView;
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
import com.ingenuitymobile.edwardlynx.api.responses.CategoriesResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 * Activity to create detailed development plan.
 */

public class CreateDetailedDevelopmentPlanActivity extends BaseActivity {

  private ScrollView              scrollView;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_detailed_development_plan);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.goals_details).toUpperCase());

    final String json = getIntent().getStringExtra("goal_param_body");
    param = !TextUtils.isEmpty(json) ?
        new Gson().fromJson(json, Goal.class) : new Goal();
    index = getIntent().getIntExtra("index", -1);

    planId = getIntent().getLongExtra("planId", 0L);


    initViews();

    if (planId != 0L && param.id != 0L) {
      ((TextView) findViewById(R.id.text_done)).setText(getString(R.string.update_goal));
      findViewById(R.id.layout_actions).setVisibility(View.GONE);
    } else if (planId != 0L) {
      param.position = getIntent().getIntExtra("position", 0);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * initViews initializes views used in the activity
   */
  private void initViews() {
    scrollView = (ScrollView) findViewById(R.id.scrollview);
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

    final boolean isLinkCategory = param.categoryId != 0L;
    linkCategoryCheckbox.setChecked(isLinkCategory);
    spinnerLayout.setVisibility(isLinkCategory ? View.VISIBLE : View.GONE);

    getCategories();

    Calendar calendar = Calendar.getInstance();
    try {
      if (isRemind) {
        calendar.setTime(DateUtil.getAPIFormat().parse(param.dueDate));
      }
    } catch (Exception e) {
      LogUtil.e("AAA ", e);
    }

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

  /**
   * setListeners sets the view listeners for the current activity
   */
  private void setListeners() {
    remindCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        datePicker.setVisibility(b ? View.VISIBLE : View.GONE);
        if (b) {
          scrollView.post(new Runnable() {
            @Override
            public void run() {
              scrollView.fullScroll(View.FOCUS_DOWN);
            }
          });
        }
      }
    });

    linkCategoryCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (Shared.categories.isEmpty()) {
          linkCategoryCheckbox.setChecked(false);
          Toast.makeText(
              context,
              getString(R.string.no_categories_available), Toast.LENGTH_SHORT
          ).show();
        } else {
          spinnerLayout.setVisibility(b ? View.VISIBLE : View.GONE);
          if (b) {
            scrollView.post(new Runnable() {
              @Override
              public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
              }
            });
          }
        }
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

  /**
   * notifyAdapter notifies the list adapter of data changes
   */
  private void notifyAdapter() {
    emptyText.setVisibility(param.actions.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  /**
   * addPlan adds a new plan to the list,
   * used in CreateDetailedDevelopmentPlanActivity
   * @param v
   */
  public void addPlan(View v) {
    if (addPlanLayout.getVisibility() == View.GONE) {
      addPlanLayout.setVisibility(View.VISIBLE);
      addPlanEdit.requestFocus();
    } else {
      addPlanEdit.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }
  }

  /**
   * save closes this activity and returns to the AddDevelopmentPlan with data,
   * used in CreateDetailedDevelopmentPlanActivity
   * @param v
   */
  public void save(View v) {
    final String developmentPlan = developmentNameEdit.getText().toString();
    final String description = descriptionEdit.getText().toString();

    if (TextUtils.isEmpty(developmentPlan)) {
      Toast.makeText(context, getString(R.string.name_required), Toast.LENGTH_SHORT)
          .show();
      return;
    }

    param.title = developmentPlan;
    param.dueDate = "";
    if (remindCheckbox.isChecked()) {
      if (new Date().before(datePicker.getDate())) {
        param.dueDate = DateUtil.getAPIFormat().format(datePicker.getDate());
        findViewById(R.id.text_due_date_error).setVisibility(View.GONE);
      } else {
        findViewById(R.id.text_due_date_error).setVisibility(View.VISIBLE);
        return;
      }
    }

    param.categoryId = linkCategoryCheckbox.isChecked() ? param.categoryId : 0L;
    if (!TextUtils.isEmpty(description)) {
      param.description = description;
    }

    if (param.actions.isEmpty()) {
      Toast.makeText(context, getString(R.string.no_actions_added), Toast.LENGTH_SHORT)
          .show();
      return;
    }

    for (int x = 0; x < param.actions.size(); x++) {
      param.actions.get(x).position = x;
    }

    LogUtil.e("AAA " + param.toString());

    if (planId != 0L && param.id != 0L) {
      updateGoal();
    } else if (planId != 0L) {
      addGoal();
    } else {
      Intent intent = new Intent();
      intent.putExtra("goal_param_body", param.toString());
      intent.putExtra("index", index);
      if (!TextUtils.isEmpty(description)) {
        intent.putExtra("description", description);
      }

      setResult(RESULT_OK, intent);
      finish();
    }
  }

  /**
   * addGoal adds a new development plan goal
   */
  private void addGoal() {
    hideKeyboard();
    progressDialog.show();

    subscription.add(Shared.apiClient.postDevelopmentPlanGoal(planId, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            finish();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA " + e);
            progressDialog.dismiss();
          }

          @Override
          public void onNext(Response response) {
            progressDialog.dismiss();
            Toast.makeText(context, getString(R.string.goal_created), Toast.LENGTH_SHORT)
                .show();
          }
        }));
  }

  /**
   * updateGoal updates the edited goal
   */
  private void updateGoal() {
    hideKeyboard();
    progressDialog.show();

    subscription.add(Shared.apiClient.updateGoal(planId, param.id, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            finish();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA " + e);
            progressDialog.dismiss();
          }

          @Override
          public void onNext(Response response) {
            progressDialog.dismiss();
            Toast.makeText(context, getString(R.string.goal_updated), Toast.LENGTH_SHORT)
                .show();
          }
        }));
  }

  /**
   * getCategories retrieves the categories from API
   */
  private void getCategories() {
    subscription.add(Shared.apiClient.getCategories(new Subscriber<CategoriesResponse>() {
      @Override
      public void onCompleted() {
        List<String> categories = new ArrayList<>();
        int index = 0;
        for (int x = 0; x < Shared.categories.size(); x++) {
          final Category category = Shared.categories.get(x);
          categories.add(category.title);
          if (param.categoryId != 0L && category.id == param.categoryId) {
            index = x;
          }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
            context,
            android.R.layout.simple_spinner_item,
            categories
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(index);
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA " + e);
        progressDialog.dismiss();
      }

      @Override
      public void onNext(CategoriesResponse categoriesResponse) {
        Shared.categories = categoriesResponse.items;
        LogUtil.e("AAA onNext postTokenDevice");
      }
    }));
  }

  /**
   * listener for deleting the action plan
   */
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

  /**
   * listener for saving text from the add plan text field
   */
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

  /**
   * RecyclerView movement callback for the create action plan adapter
   */
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
