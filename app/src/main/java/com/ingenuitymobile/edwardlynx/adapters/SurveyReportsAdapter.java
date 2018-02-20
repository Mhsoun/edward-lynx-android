package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyReportsAdapter extends RecyclerView.Adapter<SurveyReportsAdapter.ViewHolder> {

  private List<Survey>             data;
  private OnSelectFeedbackListener listener;

  /**
   * Adapter for displaying survey reports.
   * @param data the list of surveys
   * @param listener the listener for selecting an item
   */
  public SurveyReportsAdapter(List<Survey> data, OnSelectFeedbackListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView dateText;
    TextView descriptionText;
    TextView nameText;
    TextView countText;
    TextView reportType;

    ViewHolder(View itemView) {
      super(itemView);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      countText = (TextView) itemView.findViewById(R.id.text_count);
      reportType = (TextView) itemView.findViewById(R.id.text_report_type);
      reportType.setBackgroundColor(
          itemView.getContext().getResources().getColor(R.color.lynx_color));
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_feedback_reports, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Survey survey = data.get(position);
    final Context context = holder.itemView.getContext();

    try {
      Date date = DateUtil.getAPIFormat().parse(survey.endDate);
      holder.dateText.setText(DateUtil.getDisplayFormat().format(date));
    } catch (Exception e) {
      LogUtil.e("AAA " + e);
    }

    holder.countText.setText(survey.stats.answered + "/" + survey.stats.invited);
    holder.nameText.setText(survey.name);
    holder.descriptionText.setText(survey.personsEvaluatedText);
    holder.reportType.setText(survey.getType(context, true));

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        listener.onSelect(survey.id, survey.name, survey.endDate, survey.stats.invited, survey.stats.answered);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public interface OnSelectFeedbackListener {
    void onSelect(long id, String surveyName, String surveyEndDate, int surveyInvited, int surveyAnswered);
  }
}