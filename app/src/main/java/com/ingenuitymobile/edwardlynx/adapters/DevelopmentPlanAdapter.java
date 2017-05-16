package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.DevelopmentPlanDetailedActivity;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.views.fitchart.FitChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class DevelopmentPlanAdapter extends
    RecyclerView.Adapter<DevelopmentPlanAdapter.ViewHolder> {

  private SimpleDateFormat format        = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  private SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy");

  private List<DevelopmentPlan> data;

  public DevelopmentPlanAdapter(List<DevelopmentPlan> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView nameText;
    TextView countText;
    TextView dateText;
    FitChart progressFitChart;
    TextView percentageText;
    BarChart goalBarChart;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      countText = (TextView) itemView.findViewById(R.id.text_count);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
      percentageText = (TextView) itemView.findViewById(R.id.text_percentage);
      progressFitChart = (FitChart) itemView.findViewById(R.id.fitchart_progress);
      progressFitChart.setMinValue(0f);
      progressFitChart.setMaxValue(100f);

      goalBarChart = (BarChart) itemView.findViewById(R.id.barchart_goal);
      goalBarChart.setDrawBarShadow(false);
      goalBarChart.setDrawValueAboveBar(false);
      goalBarChart.getDescription().setEnabled(false);
      goalBarChart.setMaxVisibleValueCount(15);

      goalBarChart.setPinchZoom(false);
      goalBarChart.setDoubleTapToZoomEnabled(false);

      goalBarChart.setDrawGridBackground(false);

      XAxis xAxis = goalBarChart.getXAxis();
      xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
      xAxis.setDrawGridLines(false);
      xAxis.setGranularity(1f); // only intervals of 1 day
      xAxis.setLabelCount(10);
      xAxis.setValueFormatter(new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
          return String.valueOf((int) value);
        }
      });

      YAxis leftAxis = goalBarChart.getAxisLeft();
      leftAxis.setDrawGridLines(false);
      leftAxis.setDrawAxisLine(false);
      leftAxis.setAxisMinimum(0f);
      leftAxis.setAxisMaximum(100f);

      YAxis rightAxis = goalBarChart.getAxisRight();
      rightAxis.setDrawGridLines(false);
      rightAxis.setDrawAxisLine(false);
      rightAxis.setAxisMinimum(0f);
      rightAxis.setAxisMaximum(100f);

      goalBarChart.getXAxis().setTextColor(Color.WHITE);
      goalBarChart.getXAxis().setAxisLineColor(Color.BLACK);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new DevelopmentPlanAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_development_plan, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final Context context = holder.itemView.getContext();
    final DevelopmentPlan plan = data.get(position);

    holder.nameText.setText(plan.name);

    try {
      Date date = format.parse(plan.updatedAt);
      holder.dateText.setText(displayFormat.format(date));
    } catch (Exception e) {
      holder.dateText.setText("");
    }

    ArrayList<BarEntry> goalBars = new ArrayList<>();

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

          final float actionProgress = ((float) (actionCount) / (float) actionSize);

          progress = progress + actionProgress;
          goalBars.add(new BarEntry(goalBars.size() + 1, actionProgress * 100));
        }
      }

      progress = ((progress) / (float) size) * 100;
    }

    holder.countText.setText(context.getString(R.string.completed_details, count, size));
    holder.countText.setTextColor(context.getResources()
        .getColor(count == size ? R.color.colorAccent : R.color.dev_plan_color));

    holder.progressFitChart.setValue(progress);
    holder.progressFitChart.clearAnimation();

    holder.percentageText.setText(((int) progress) + "%");

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, DevelopmentPlanDetailedActivity.class);
        intent.putExtra("id", plan.id);
        context.startActivity(intent);
      }
    });


    final MyBarDataSet set = new MyBarDataSet(goalBars, "");
    set.setHighlightEnabled(false);
    set.setColors(new int[]{
        context.getResources().getColor(R.color.colorAccent),
        context.getResources().getColor(R.color.dev_plan_color),
    });

    final ArrayList<IBarDataSet> dataSets = new ArrayList<>();
    dataSets.add(set);

    final BarData data = new BarData(dataSets);
    data.setBarWidth(0.9f);
    data.setDrawValues(false);

    holder.goalBarChart.setData(data);
    holder.goalBarChart.getAxisLeft().setDrawLabels(false);
    holder.goalBarChart.getAxisRight().setDrawLabels(false);
    holder.goalBarChart.getLegend().setEnabled(false);

    holder.goalBarChart.setVisibleXRangeMaximum(10);
    holder.goalBarChart.setVisibleXRangeMinimum(10);
    holder.goalBarChart.setHighlightPerTapEnabled(false);
    holder.goalBarChart.clearAnimation();
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public class MyBarDataSet extends BarDataSet {

    private List<BarEntry> yVals;


    public MyBarDataSet(List<BarEntry> yVals, String label) {
      super(yVals, label);
      this.yVals = yVals;
    }

    @Override
    public int getColor(int index) {
      if (yVals.get(index).getY() == 100) {
        return mColors.get(0);
      } else {
        return mColors.get(1);
      }
    }
  }
}
