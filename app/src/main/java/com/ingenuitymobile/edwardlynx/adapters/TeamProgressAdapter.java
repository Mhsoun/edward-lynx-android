package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.TeamDevPlanDetailedActivity;
import com.ingenuitymobile.edwardlynx.api.models.TeamDevPlan;
import com.ingenuitymobile.edwardlynx.views.fitchart.FitChart;

import java.util.List;

/**
 * Created by memengski on 7/5/17.
 */

public class TeamProgressAdapter extends
    RecyclerView.Adapter<TeamProgressAdapter.ViewHolder> {

  private List<TeamDevPlan> data;

  public TeamProgressAdapter(List<TeamDevPlan> data) {
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
      progressFitChart.setMaxValue(1f);
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
    final TeamDevPlan teamDevPlan = data.get(position);

    holder.nameText.setText(teamDevPlan.name);
    holder.progressFitChart.setValue(teamDevPlan.progress);
    holder.progressFitChart.clearAnimation();
    holder.percentageText.setText(((int) (teamDevPlan.progress * 100)) + "%");

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, TeamDevPlanDetailedActivity.class);
        intent.putExtra("id", teamDevPlan.id);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
