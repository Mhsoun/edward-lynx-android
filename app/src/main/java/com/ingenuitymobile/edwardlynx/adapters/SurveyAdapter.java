package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.SurveyQuestionsActivity;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

  private SimpleDateFormat format      = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
  private SimpleDateFormat dateFormat  = new SimpleDateFormat("dd");
  private SimpleDateFormat yearFormat  = new SimpleDateFormat("yyyy");

  private List<Survey> data;

  public SurveyAdapter(List<Survey> data) {
    super();
    this.data = data;
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
    holder.descriptionText.setText(
        context.getResources().getString(R.string.lynx_progress_bold));
    holder.evaluateText.setText(survey.personsEvaluatedText);

    try {
      Date date = format.parse(survey.endDate);
      holder.monthText.setText(monthFormat.format(date));
      holder.dateTExt.setText(dateFormat.format(date));
      holder.yearText.setText(yearFormat.format(date));
      holder.isExpired = new Date().compareTo(date) == 1;
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
          Intent intent = new Intent(context, SurveyQuestionsActivity.class);
          intent.putExtra("id", survey.id);
          context.startActivity(intent);
        }
      }
    });


    switch (survey.status) {
    case Survey.OPEN:
      holder.statusText.setText("OPEN");
      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.unfinished_status));
      break;
    case Survey.UNFINISHED:
      holder.statusText.setText("UNFINISHED");
      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.unfinished_status));
      break;
    case Survey.COMPLETED:
      holder.statusText.setText("COMPLETED");
      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.done_color));
      break;
    case Survey.NOT_INVITED:
      holder.statusText.setText("NOT INVITED");
      holder.statusText.setBackgroundColor(
          context.getResources().getColor(R.color.unfinished_status));
      break;
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
