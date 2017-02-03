package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.GoalAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class DevelopmentPlanDetailedActivity extends BaseActivity {

  private long id;

  private RecyclerView recyclerView;
  private GoalAdapter  adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_development_plan_detailed);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);
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

  protected void initViews() {
    recyclerView = (RecyclerView) findViewById(R.id.list_development_plan);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  private List<ParentListItem> generateCategories(List<Goal> goals) {
    List<ParentListItem> parentListItems = new ArrayList<>();
    for (Goal goal : goals) {
      parentListItems.add(goal);
    }
    return parentListItems;
  }

  private void getData() {
    subscription.add(Shared.apiClient.getDevelopmentPlan(id, new Subscriber<DevelopmentPlan>() {
      @Override
      public void onCompleted() {
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onNext(DevelopmentPlan developmentPlan) {
        setTitle(developmentPlan.name);
        adapter = new GoalAdapter(generateCategories(developmentPlan.goals));
        recyclerView.setAdapter(adapter);
      }
    }));
  }
}
