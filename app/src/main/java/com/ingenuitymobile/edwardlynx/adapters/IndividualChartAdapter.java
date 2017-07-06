package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.DevelopmentPlanDetailedActivity;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.views.fitchart.FitChart;

import java.util.List;

/**
 * Created by memengski on 7/6/17.
 */

public class IndividualChartAdapter extends
    RecyclerView.Adapter<IndividualChartAdapter.ViewHolder> {

  private List<DevelopmentPlan> data;

  public IndividualChartAdapter(List<DevelopmentPlan> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView nameText;
    FitChart progressFitChart;
    TextView percentageText;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      percentageText = (TextView) itemView.findViewById(R.id.text_percentage);
      progressFitChart = (FitChart) itemView.findViewById(R.id.fitchart_progress);
      progressFitChart.setMinValue(0f);
      progressFitChart.setMaxValue(100f);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_individual_chart, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final Context context = holder.itemView.getContext();
    final DevelopmentPlan plan = data.get(position);

    holder.nameText.setText(plan.name);


    final int size = plan.goals.size();
    float progress = 0;
    int actionSize = 0;
    if (plan.goals != null) {
      for (Goal goal : plan.goals) {

        int actionCount = 0;
        if (goal.actions != null && !goal.actions.isEmpty()) {
          actionSize = goal.actions.size();
          for (Action action : goal.actions) {
            if (action.checked == 1) {
              actionCount++;
            }
          }
        }

        final float actionProgress =
            (actionCount != 0 && actionSize != 0) ?
                ((float) (actionCount) / (float) actionSize) : 0;

        progress = progress + actionProgress;
      }

      progress = ((progress) / (float) size) * 100;
    }
    holder.progressFitChart.setValue(progress);
    holder.progressFitChart.clearAnimation();
    holder.percentageText.setText(((int) progress) + "%");

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, DevelopmentPlanDetailedActivity.class);
        intent.putExtra("id", plan.id);
        intent.putExtra("isFromTeam", true);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
