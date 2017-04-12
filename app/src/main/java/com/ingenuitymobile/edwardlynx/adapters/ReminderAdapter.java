package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.AnswerFeedbackActivity;
import com.ingenuitymobile.edwardlynx.activities.MainActivity;
import com.ingenuitymobile.edwardlynx.activities.SurveyQuestionsActivity;
import com.ingenuitymobile.edwardlynx.api.models.Reminder;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by memengski on 3/31/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");

  private List<Reminder>                        data;
  private MainActivity.OnChangeFragmentListener listener;

  public ReminderAdapter(List<Reminder> data, MainActivity.OnChangeFragmentListener listener) {
    super();
    this.data = data;
    this.listener = listener;
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

    holder.typeText.setText(reminder.getType(context));


    try {
      Date date = format.parse(reminder.due);
      long diff = date.getTime() - new Date().getTime();

      long seconds = diff / 1000;
      long minutes = seconds / 60;
      long hours = minutes / 60;
      long days = hours / 24;
      if (days <= 0) {
        holder.dueDateLayout.setVisibility(View.GONE);
        holder.nowText.setVisibility(View.VISIBLE);
      } else {
        holder.dueDateLayout.setVisibility(View.VISIBLE);
        holder.nowText.setVisibility(View.GONE);

        holder.dueDateText.setText(
            context.getResources().getString(days == 1 ? R.string.day : R.string.days, days));
      }
    } catch (Exception e) {
      LogUtil.e("Error", e);
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (reminder.type.equals(Reminder.Type.INVITE_FEEDBACK.toString())) {
          Intent intent = new Intent(context, AnswerFeedbackActivity.class);
          intent.putExtra("id", reminder.id);
          context.startActivity(intent);
        } else if (reminder.type.equals(Reminder.Type.SURVEY.toString())) {
          Intent intent = new Intent(context, SurveyQuestionsActivity.class);
          intent.putExtra("id", reminder.id);
          context.startActivity(intent);
        } else {
          listener.onChange(MainActivity.ChangeFragment.DEVPLANS);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
