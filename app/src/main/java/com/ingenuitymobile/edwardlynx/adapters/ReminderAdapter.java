package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.AnswerFeedbackActivity;
import com.ingenuitymobile.edwardlynx.activities.DevelopmentPlanDetailedActivity;
import com.ingenuitymobile.edwardlynx.activities.MainActivity;
import com.ingenuitymobile.edwardlynx.activities.SurveyQuestionsActivity;
import com.ingenuitymobile.edwardlynx.api.models.Reminder;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by memengski on 3/31/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

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

    holder.descriptionText.setText(reminder.name);

    if (reminder.type.equals(Reminder.Type.GOAL.toString())) {
      holder.bodyLayout.setBackgroundDrawable(
          context.getResources().getDrawable(R.drawable.bg_rounded_reminder_goal));
      holder.typeText.setTextColor(context.getResources().getColor(R.color.dev_plan_color));
    } else if (reminder.type.equals(Reminder.Type.SURVEY.toString())) {
      holder.bodyLayout.setBackgroundDrawable(
          context.getResources().getDrawable(R.drawable.bg_rounded_reminder_survey));
      holder.typeText.setTextColor(context.getResources().getColor(R.color.lynx_color));
    } else {
      holder.bodyLayout.setBackgroundDrawable(
          context.getResources().getDrawable(R.drawable.bg_rounded_reminder_feedback));
      holder.typeText.setTextColor(context.getResources().getColor(R.color.instant_feedback_color));
    }

    holder.typeText.setText(reminder.getType(context));

    try {
      holder.dueDateLayout.setVisibility(View.GONE);
      holder.nowText.setVisibility(View.GONE);

      if (!TextUtils.isEmpty(reminder.due)) {
        Date date = DateUtil.getAPIFormat().parse(reminder.due);
        long diff = date.getTime() - new Date().getTime();

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        if (days <= 0) {
          holder.nowText.setVisibility(View.VISIBLE);
        } else {
          holder.dueDateLayout.setVisibility(View.VISIBLE);
          holder.dueDateText.setText(
              context.getResources().getString(days == 1 ? R.string.day : R.string.days, days));
        }
      }
    } catch (Exception e) {}

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
          Intent intent = new Intent(context, DevelopmentPlanDetailedActivity.class);
          intent.putExtra("id", reminder.id);
          context.startActivity(intent);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
