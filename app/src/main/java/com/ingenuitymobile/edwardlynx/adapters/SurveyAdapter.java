package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.InvitePeopleActivity;
import com.ingenuitymobile.edwardlynx.activities.SurveyQuestionsActivity;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

  private boolean isInvite;

  private List<Survey> data;

  public SurveyAdapter(List<Survey> data) {
    super();
    this.data = data;
    isInvite = false;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView monthText;
    TextView dateTExt;
    TextView yearText;
    TextView nameText;
    TextView descriptionText;
    TextView statusText;
    TextView reactivateText;
    TextView evaluateText;
    boolean  isExpired;
    long     days;

    ViewHolder(View itemView) {
      super(itemView);
      monthText = (TextView) itemView.findViewById(R.id.text_month);
      dateTExt = (TextView) itemView.findViewById(R.id.text_date);
      yearText = (TextView) itemView.findViewById(R.id.text_year);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      statusText = (TextView) itemView.findViewById(R.id.text_status);
      reactivateText = (TextView) itemView.findViewById(R.id.text_reactivate);
      evaluateText = (TextView) itemView.findViewById(R.id.text_evaluate);
      isExpired = false;
      days = 0;
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_survey, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final Survey survey = data.get(position);
    holder.nameText.setText(survey.name);
    holder.descriptionText.setText(survey.getType(context, true));
    holder.evaluateText.setText(survey.personsEvaluatedText);

    try {
      Date date = DateUtil.getAPIFormat().parse(survey.endDate);
      holder.monthText.setText(DateUtil.getMonthFormat().format(date));
      holder.dateTExt.setText(DateUtil.getDayFormat().format(date));
      holder.yearText.setText(DateUtil.getYearFormat().format(date));
      holder.isExpired = new Date().after(date);

      long diff = date.getTime() - new Date().getTime();

      long seconds = diff / 1000;
      long minutes = seconds / 60;
      long hours = minutes / 60;
      holder.days = hours / 24;
    } catch (Exception e) {
      LogUtil.e("AAA " + e);
    }

    holder.reactivateText.setVisibility(holder.isExpired ? View.VISIBLE : View.GONE);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (holder.isExpired) {
          LogUtil.e("AAA Expired");
        } else {
          if (isInvite) {
            Intent intent = new Intent(context, InvitePeopleActivity.class);
            intent.putExtra("id", survey.id);
            intent.putExtra("title", survey.name);
            intent.putExtra("evaluate", survey.personsEvaluatedText);
            context.startActivity(intent);
          } else {
            Intent intent = new Intent(context, SurveyQuestionsActivity.class);
            intent.putExtra("id", survey.id);
            context.startActivity(intent);
          }
        }
      }
    });

    switch (survey.status) {
    case Survey.OPEN:
      holder.statusText.setText(
          context.getResources().getString(R.string.open_status)
              .toUpperCase()
      );
      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.unfinished_status));
      holder.monthText.setBackground(
          context.getResources().getDrawable(R.drawable.bg_normal_calendar_text));
      break;
    case Survey.UNFINISHED:
      holder.statusText.setText(
          context.getResources().getString(R.string.unfinished_status)
              .toUpperCase()
      );

      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.unfinished_status));
      holder.monthText.setBackground(
          context.getResources().getDrawable(R.drawable.bg_normal_calendar_text));
      break;
    case Survey.COMPLETED:
      holder.statusText.setText(context.getResources().getString(R.string.text_completed)
          .toUpperCase()
      );
      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.done_color));
      holder.monthText.setBackground(
          context.getResources().getDrawable(R.drawable.bg_done_calendar_text));
      break;
    case Survey.NOT_INVITED:
      holder.statusText.setText(
          context.getResources().getString(R.string.not_invited_status)
              .toUpperCase()
      );
      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.unfinished_status));
      holder.monthText.setBackground(
          context.getResources().getDrawable(R.drawable.bg_normal_calendar_text));
      break;
    }

    holder.reactivateText.setVisibility(View.GONE);
    if (holder.isExpired && survey.status != Survey.COMPLETED &&
        survey.status != Survey.NOT_INVITED) {
      holder.reactivateText.setVisibility(View.VISIBLE);
      holder.monthText.setBackground(
          context.getResources().getDrawable(R.drawable.bg_expired_calendar_text));
    }

    if (holder.days <= 14 && survey.status != Survey.COMPLETED &&
        survey.status != Survey.NOT_INVITED) {
      holder.monthText.setBackground(
          context.getResources().getDrawable(R.drawable.bg_deadline_calendar_text));
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void setIsnvite(boolean isInvite) {
    this.isInvite = isInvite;
  }
}
