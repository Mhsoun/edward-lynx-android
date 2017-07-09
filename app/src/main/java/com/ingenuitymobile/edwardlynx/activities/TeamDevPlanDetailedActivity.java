package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.TeamDevplanDetailedAdapter;
import com.ingenuitymobile.edwardlynx.api.models.TeamCategory;
import com.ingenuitymobile.edwardlynx.api.models.TeamDevPlan;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 7/9/17.
 */

public class TeamDevPlanDetailedActivity extends BaseActivity {

  private long id;

  private ArrayList<TeamCategory>    data;
  private TeamDevplanDetailedAdapter adapter;

  private TextView           emptyText;
  private SwipeRefreshLayout refreshLayout;

  public TeamDevPlanDetailedActivity() {
    data = new ArrayList<>();
    id = 0L;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team_plan_detailed);

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

  private void initViews() {
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));

    emptyText = (TextView) findViewById(R.id.text_empty_state);
    refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

    adapter = new TeamDevplanDetailedAdapter(data);
    recyclerView.setAdapter(adapter);
  }

  private void getData() {
    subscription.add(Shared.apiClient.getTeamCategory(id, new Subscriber<TeamDevPlan>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted ");
        refreshLayout.setRefreshing(false);
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
        refreshLayout.setRefreshing(false);
      }

      @Override
      public void onNext(TeamDevPlan teamDevPlan) {
        setTitle(teamDevPlan.name);
        data.clear();
        data.addAll(teamDevPlan.goals);
        adapter.notifyDataSetChanged();
        emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
      }
    }));
  }
}
