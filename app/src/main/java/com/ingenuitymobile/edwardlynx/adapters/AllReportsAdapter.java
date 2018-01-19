package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.InstantFeedbackReportActivity;
import com.ingenuitymobile.edwardlynx.activities.SurveyReportActivity;
import com.ingenuitymobile.edwardlynx.api.models.AllSurveys;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.AnswerTypeUtil;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by memengski on 4/7/17.
 */

public class AllReportsAdapter extends RecyclerView.Adapter<AllReportsAdapter.ViewHolder> {

  private List<AllSurveys> data;

  /**
   * Adapter for displaying all reports.
   * @param data the list of all surveys
   */
  public AllReportsAdapter(List<AllSurveys> data) {
    super();
    this.data = data;
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
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_feedback_reports, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final AllSurveys allSurvey = data.get(position);
    final Context context = holder.itemView.getContext();


    if (allSurvey.survey != null) {
      final Survey survey = allSurvey.survey;
      try {
        Date date = DateUtil.getAPIFormat().parse(survey.endDate);
        holder.dateText.setText(DateUtil.getDisplayFormat().format(date));
      } catch (Exception e) {
        LogUtil.e("AAA " + e);
      }

      holder.countText.setText(survey.stats.answered + "/" + survey.stats.invited);
      holder.nameText.setText(survey.name);
      holder.descriptionText.setText(survey.personsEvaluatedText);

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(context, SurveyReportActivity.class);
          intent.putExtra("id", survey.id);
          intent.putExtra("key", survey.key);
          context.startActivity(intent);
        }
      });
      holder.reportType.setText(survey.getType(context, true));
      holder.reportType.setBackgroundColor(context.getResources().getColor(R.color.lynx_color));
    } else {
      final Feedback feedback = allSurvey.feedback;

      try {
        Date date = DateUtil.getAPIFormat().parse(feedback.createdAt);
        holder.dateText.setText(DateUtil.getDisplayFormat().format(date));
      } catch (Exception e) {
        LogUtil.e("AAA " + e);
      }

      holder.countText.setText(feedback.stats.answered + "/" + feedback.stats.invited);

      if (!feedback.questions.isEmpty()) {
        Question question = feedback.questions.get(0);

        holder.nameText.setText(question.text);
        holder.descriptionText.setText(AnswerTypeUtil.getStringType(context, question.answer.type));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(context, InstantFeedbackReportActivity.class);
            intent.putExtra("id", feedback.id);
            context.startActivity(intent);
          }
        });

        holder.reportType.setText(
            context.getResources().getString(R.string.instant_feedback).toUpperCase()
        );

        holder.reportType.setBackgroundColor(
            context.getResources().getColor(R.color.instant_feedback_color));
      }
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
