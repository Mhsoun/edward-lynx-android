package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.DevelopmentPlanDetailedActivity;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class DevelopmentPlanAdapter extends
    RecyclerView.Adapter<DevelopmentPlanAdapter.ViewHolder> {

  private SimpleDateFormat format        = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  private SimpleDateFormat displayFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

  private List<DevelopmentPlan> data;

  public DevelopmentPlanAdapter(List<DevelopmentPlan> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView    nameText;
    TextView    countText;
    TextView    dateText;
    ProgressBar progressBar;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      countText = (TextView) itemView.findViewById(R.id.text_count);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
      progressBar = (ProgressBar) itemView.findViewById(R.id.progress_development);
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
      LogUtil.e("AAA " + e);
      holder.dateText.setText("");
    }

    final int size = plan.goals.size();
    int count = 0;
    if (plan.goals != null) {
      for (Goal goal : plan.goals) {
        if (goal.checked == 1) {
          count++;
        }
      }
    }

    holder.countText.setText(context.getString(R.string.completed_details, count, size));
    holder.progressBar.setProgress(
        (int) (((float) (count) / (float) size) * 100));
    holder.progressBar.setScaleY(3f);

    if (holder.progressBar.getProgress() == 100) {
      holder.progressBar
          .getProgressDrawable()
          .setColorFilter(context.getResources().getColor(R.color.dashboard_green),
              PorterDuff.Mode.SRC_OUT);
    } else {
      holder.progressBar.getProgressDrawable().clearColorFilter();
    }


    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, DevelopmentPlanDetailedActivity.class);
        intent.putExtra("id", plan.id);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
