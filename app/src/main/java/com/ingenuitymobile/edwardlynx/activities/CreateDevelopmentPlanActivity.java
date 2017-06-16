package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.CreateDevelopmentPlanAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.CreateDevelopmentPlanParam;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class CreateDevelopmentPlanActivity extends BaseActivity {

  public final int REQUEST_CODE = 0;

  private List<Goal>                   data;
  public  CreateDevelopmentPlanAdapter adapter;

  public TextView emptyText;
  public TextView doneText;

  public CreateDevelopmentPlanActivity() {
    data = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_development_plan);
    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.create_development_plan).toUpperCase());

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
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
      final int index = data.getIntExtra("index", -1);
      final Goal param = new Gson().fromJson(data.getStringExtra("goal_param_body"),
          Goal.class);
      LogUtil.e("AAA " + param.toString());
      if (index == -1) {
        this.data.add(param);
      } else {
        this.data.set(index, param);
      }
      notifyAdapter();
    }
  }

  private void notifyAdapter() {
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  private void initViews() {
    emptyText = (TextView) findViewById(R.id.text_empty_state);
    doneText = (TextView) findViewById(R.id.text_done);
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_development_plan);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_divider));
    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

    adapter = new CreateDevelopmentPlanAdapter(data, this);
    recyclerView.setAdapter(adapter);
    notifyAdapter();

    findViewById(R.id.edit_text).setOnFocusChangeListener(onFocusChangeListener);
  }

  public void addGoal(View v) {
    startActivityForResult(new Intent(context, CreateDetailedDevelopmentPlanActivity.class),
        REQUEST_CODE);
  }

  public void done(View v) {
    final String name = ((EditText) findViewById(R.id.edit_text)).getText().toString();

    if (TextUtils.isEmpty(name)) {
      Toast.makeText(context, getString(R.string.name_required), Toast.LENGTH_SHORT)
          .show();
      return;
    }

    hideKeyboard();
    if (data.isEmpty()) {
      Toast.makeText(context, getString(R.string.goals_added), Toast.LENGTH_SHORT).show();
      return;
    }

    CreateDevelopmentPlanParam planParam = new CreateDevelopmentPlanParam();
    planParam.name = name;
    planParam.goals = data;

    for (int x = 0; x < planParam.goals.size(); x++) {
      planParam.goals.get(x).position = x;
    }

    LogUtil.e("AAA " + planParam.toString());
    progressDialog.show();
    subscription.add(Shared.apiClient.postDevelopmentPlans(planParam, new Subscriber<Response>() {
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
        Toast.makeText(context, getString(R.string.development_plan_created), Toast.LENGTH_SHORT)
            .show();
      }
    }));
  }

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
      data.remove(viewHolder.getLayoutPosition());
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
