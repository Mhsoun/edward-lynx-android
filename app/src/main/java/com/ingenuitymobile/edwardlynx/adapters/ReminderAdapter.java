package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Reminder;

import java.util.List;

/**
 * Created by memengski on 3/31/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

  private List<Reminder> data;

  public ReminderAdapter(List<Reminder> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout bodyLayout;
    TextView       typeText;
    TextView       descriptionText;
    TextView       nowText;
    LinearLayout   dueDateLayout;
    TextView       dueDateText;

    ViewHolder(View itemView) {
      super(itemView);
      bodyLayout = (RelativeLayout) itemView.findViewById(R.id.layout_body);
      typeText = (TextView) itemView.findViewById(R.id.text_type);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      nowText = (TextView) itemView.findViewById(R.id.text_now);
      dueDateText = (TextView) itemView.findViewById(R.id.text_due_date);
      dueDateLayout = (LinearLayout) itemView.findViewById(R.id.layout_due_date);
    }
  }

  @Override
  public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ReminderAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_reminder, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final Reminder reminder = data.get(position);

    holder.descriptionText.setText(reminder.description);

    final boolean isGoal = reminder.type.equals(Reminder.Type.GOAL.toString());

    holder.bodyLayout.setSelected(isGoal);
    holder.typeText.setSelected(isGoal);

    holder.typeText.setText(reminder.type);

    if ((position % 2) == 0) {
      holder.dueDateLayout.setVisibility(View.GONE);
      holder.nowText.setVisibility(View.VISIBLE);
    } else {
      holder.dueDateLayout.setVisibility(View.VISIBLE);
      holder.nowText.setVisibility(View.GONE);

      holder.dueDateText.setText(context.getResources().getString(R.string.days, position));
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
