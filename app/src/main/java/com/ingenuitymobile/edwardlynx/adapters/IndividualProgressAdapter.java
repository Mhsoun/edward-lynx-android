package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.DevelopmentPlanDetailedActivity;
import com.ingenuitymobile.edwardlynx.activities.IndividualDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.IndividualProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 7/6/17.
 */

public class IndividualProgressAdapter extends
    RecyclerView.Adapter<IndividualProgressAdapter.ViewHolder> {

  private List<IndividualProgress> data;

  /**
   * Adapter for displaying individual progress.
   * @param data the list of individual progress
   */
  public IndividualProgressAdapter(List<IndividualProgress> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView     nameText;
    TextView     seeMoreText;
    RecyclerView recyclerView;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      seeMoreText = (TextView) itemView.findViewById(R.id.text_see_more);
      recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_dev_plans);
      recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
      recyclerView.setNestedScrollingEnabled(false);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_individual, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final Context context = holder.itemView.getContext();
    final IndividualProgress individualProgress = data.get(position);

    holder.nameText.setText(individualProgress.name);
    ArrayList<DevelopmentPlan> plans = new ArrayList<>();

    if (individualProgress.devPlans.size() > 3) {
      for (int x = 0; x < 3; x++) {
        plans.add(individualProgress.devPlans.get(x));
      }
    } else {
      plans.addAll(individualProgress.devPlans);
    }
    IndividualChartAdapter adapter = new IndividualChartAdapter(plans);
    holder.recyclerView.setAdapter(adapter);
    holder.seeMoreText.setVisibility(
        individualProgress.devPlans.size() < 4 ? View.GONE : View.VISIBLE);
    holder.seeMoreText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, IndividualDevelopmentPlanActivity.class);
        intent.putExtra("id", individualProgress.id);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
