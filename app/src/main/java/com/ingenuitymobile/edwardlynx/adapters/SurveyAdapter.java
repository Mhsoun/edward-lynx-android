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

import java.util.List;

/**
 * Created by mEmEnG-sKi on 09/01/2017.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

  private List<Survey> data;

  public SurveyAdapter(List<Survey> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView dateText;
    TextView nameText;

    ViewHolder(View itemView) {
      super(itemView);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
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
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, SurveyQuestionsActivity.class);
        intent.putExtra("id", survey.id);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
