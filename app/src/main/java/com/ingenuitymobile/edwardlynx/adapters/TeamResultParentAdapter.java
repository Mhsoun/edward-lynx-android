package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.TeamReportItem;
import com.ingenuitymobile.edwardlynx.api.models.TeamReportSurvey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riddick on 7/14/17.
 */

public class TeamResultParentAdapter extends
    RecyclerView.Adapter<TeamResultParentAdapter.ParentTeamResults> {

  List<TeamReportItem> items;

  /**
   * Adapter for displaying team results.
   * @param items the list of team reports
   */
  public TeamResultParentAdapter(List<TeamReportItem> items) {
    this.items = items;
  }

  @Override
  public ParentTeamResults onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ParentTeamResults(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_survey_results_parent, parent, false));
  }

  @Override
  public void onBindViewHolder(ParentTeamResults holder, int position) {
    holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
    holder.username_text.setText(items.get(position).name);

    final TeamResultsAdapter adapter = new TeamResultsAdapter(
        generateResults(items.get(position).surveys));

    holder.recyclerView.setAdapter(adapter);

    adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
      @Override
      public void onListItemExpanded(int position) {
        adapter.collapseAllParents();
        adapter.expandParent(position);
      }

      @Override
      public void onListItemCollapsed(int position) {

      }
    });
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public class ParentTeamResults extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;
    private TextView     username_text;

    public ParentTeamResults(View itemView) {
      super(itemView);
      username_text = (TextView) itemView.findViewById(R.id.text_user);
      recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
    }
  }

  private List<ParentListItem> generateResults(List<TeamReportSurvey> surveys) {
    List<ParentListItem> parentListItems = new ArrayList<>();
    for (TeamReportSurvey survey : surveys) {
      parentListItems.add(survey);
    }

    return parentListItems;
  }
}
