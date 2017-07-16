package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.models.TeamCategory;
import com.ingenuitymobile.edwardlynx.views.fitchart.FitChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 7/9/17.
 */

public class TeamDevplanDetailedAdapter extends
    RecyclerView.Adapter<TeamDevplanDetailedAdapter.ViewHolder> {

  private List<TeamCategory> data;

  public TeamDevplanDetailedAdapter(List<TeamCategory> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView     nameText;
    FitChart     progressFitChart;
    TextView     percentageText;
    RecyclerView recyclerView;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      percentageText = (TextView) itemView.findViewById(R.id.text_percentage);
      progressFitChart = (FitChart) itemView.findViewById(R.id.fitchart_progress);
      recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_goals);
      recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
      recyclerView.setNestedScrollingEnabled(false);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_detailed_team_plan, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final Context context = holder.itemView.getContext();
    final TeamCategory teamCategory = data.get(position);

    holder.nameText.setText(teamCategory.name);

    final int size = teamCategory.goals.size();
    float progress = 0;
    int actionSize = 0;
    if (teamCategory.goals != null) {
      for (Goal goal : teamCategory.goals) {

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

    final GoalAdapter adapter = new GoalAdapter(
        generateCategories(new ArrayList<>(teamCategory.goals)),
        null,
        true);
    holder.recyclerView.setAdapter(adapter);
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  private List<ParentListItem> generateCategories(List<Goal> goals) {
    List<ParentListItem> parentListItems = new ArrayList<>();
    for (Goal goal : goals) {
      final int size = goal.actions.size();

      int count = 0;
      for (Action actionGoal : goal.actions) {
        if (actionGoal.checked == 1) {
          count++;
        }
      }

      for (Action action : goal.actions) {
        action.isCompleted = count == size;
      }

      parentListItems.add(goal);
    }
    return parentListItems;
  }
}
