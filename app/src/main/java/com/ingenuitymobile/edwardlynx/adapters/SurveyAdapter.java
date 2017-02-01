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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

  private SimpleDateFormat format        = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  private SimpleDateFormat displayFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

  private List<Survey> data;

  public SurveyAdapter(List<Survey> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView dateText;
    TextView nameText;
    TextView descriptionText;
    TextView statusText;

    ViewHolder(View itemView) {
      super(itemView);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      statusText = (TextView) itemView.findViewById(R.id.text_status);
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
    holder.descriptionText.setText(survey.description);

    try {
      Date date = format.parse(survey.endDate);
      holder.dateText.setText("End date: " + displayFormat.format(date));
    } catch (Exception e) {
      holder.dateText.setText("");
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, SurveyQuestionsActivity.class);
        intent.putExtra("id", survey.id);
        context.startActivity(intent);
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
          context.getResources().getColor(R.color.dashboard_green));
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
