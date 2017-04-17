package com.ingenuitymobile.edwardlynx.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.GoalAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ActionParam;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.fragments.PopupDialogFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class DevelopmentPlanDetailedActivity extends BaseActivity {

  private long id;
  private long goalId;

  private RecyclerView    recyclerView;
  private GoalAdapter     adapter;
  private ArrayList<Goal> data;
  private ProgressDialog  dialog;

  private FitChart progressFitChart;
  private TextView percentageText;
  private TextView goalsText;

  public DevelopmentPlanDetailedActivity() {
    data = new ArrayList<>();
    goalId = 0L;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_development_plan_detailed);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);
    LogUtil.e("AAA id" + id);
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
    percentageText = (TextView) findViewById(R.id.text_percentage);
    progressFitChart = (FitChart) findViewById(R.id.fitchart_progress);
    goalsText = (TextView) findViewById(R.id.text_goals);

    progressFitChart.setMinValue(0f);
    progressFitChart.setMaxValue(100f);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    goalsText.setText(context.getString(R.string.goals_details, 0, 0));
    percentageText.setText(0 + "%");
  }

  private List<ParentListItem> generateCategories(List<Goal> goals) {
    List<ParentListItem> parentListItems = new ArrayList<>();
    for (Goal goal : goals) {
      for (Action action : goal.actions) {
        action.goal = goal;
      }
      parentListItems.add(goal);
    }
    return parentListItems;
  }

  private void getData() {
    subscription.add(Shared.apiClient.getDevelopmentPlan(id, new Subscriber<DevelopmentPlan>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted getData");
        if (dialog != null) {
          dialog.dismiss();
        }

        if (goalId != 0L) {
          for (final Goal goal : data) {
            if (goal.id == goalId && goal.checked == 1) {
              PopupDialogFragment dialogFragment = PopupDialogFragment.newInstance(true, goal.title,
                  getString(R.string.goal_completed), getString(R.string.label_popup));
              dialogFragment.show(getSupportFragmentManager(), "Popup");
              return;
            }
          }
        }
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError getData " + e);
      }

      @Override
      public void onNext(DevelopmentPlan developmentPlan) {
        setTitle(developmentPlan.name);
        LogUtil.e("AAA onNext getData");
        data.clear();
        data.addAll(developmentPlan.goals);
        recyclerView.setAdapter(null);
        adapter = new GoalAdapter(generateCategories(data), listener);
        recyclerView.setAdapter(adapter);
        setData(developmentPlan);
      }
    }));
  }

  private void setData(DevelopmentPlan plan) {
    final int size = plan.goals.size();
    int count = 0;
    float progress = 0;
    if (plan.goals != null) {
      for (Goal goal : plan.goals) {
        if (goal.checked == 1) {
          count++;
        }

        if (goal.actions != null) {
          final int actionSize = goal.actions.size();
          int actionCount = 0;
          for (Action action : goal.actions) {
            if (action.checked == 1) {
              actionCount++;
            }
          }

          progress = progress + ((float) (actionCount) / (float) actionSize);
        }
      }
      progress = ((progress) / (float) size) * 100;
    }

    Collection<FitChartValue> values = new ArrayList<>();
    values.add(new FitChartValue(progress, context.getResources().getColor(R.color.colorAccent)));
    progressFitChart.setValues(values);
    percentageText.setText(((int) progress) + "%");
    goalsText.setText(context.getString(R.string.goals_details, count, size));
  }

  private void patchAction(long goalId, long actionId, ActionParam param) {
    this.goalId = goalId;
    dialog = ProgressDialog.show(context, "", getString(R.string.loading));

    LogUtil.e("AAA " + param.toString());
    subscription.add(Shared.apiClient.updateActionPlan(id, goalId, actionId, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted patchAction");
            getData();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError patchAction " + e);
            if (dialog != null) {
              dialog.show();
            }
          }

          @Override
          public void onNext(Response response) {
            LogUtil.e("AAA onNext patchAction");
          }
        }));
  }

  private GoalAdapter.OnSelectActionListener listener = new GoalAdapter.OnSelectActionListener() {
    @Override
    public void onSelectedAction(final Action action) {
      for (final Goal goal : data) {
        for (Action acn : goal.actions) {
          if (acn.id == action.id) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle(getString(R.string.confirmation));
            alertBuilder.setMessage(getString(R.string.complete_action_message, action.title));
            alertBuilder.setPositiveButton(getString(R.string.complete),
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    ActionParam param = new ActionParam();
                    param.checked = 1;
                    param.position = action.position;
                    patchAction(goal.id, action.id, param);
                  }
                });
            alertBuilder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                  }
                });
            alertBuilder.create().show();
            return;
          }
        }
      }
    }
  };
}
