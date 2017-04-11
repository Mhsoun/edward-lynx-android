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
import com.ingenuitymobile.edwardlynx.api.models.AllSurveys;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by memengski on 4/3/17.
 */

public class AllSurveysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private SimpleDateFormat format      = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
  private SimpleDateFormat dateFormat  = new SimpleDateFormat("dd");
  private SimpleDateFormat yearFormat  = new SimpleDateFormat("yyyy");

  private final static int FEEDBACK = 0;
  private final static int SURVEY   = 1;

  public  List<AllSurveys>                         data;
  private FeedbackAdapter.OnSelectFeedbackListener listener;

  public AllSurveysAdapter(List<AllSurveys> data,
      FeedbackAdapter.OnSelectFeedbackListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class SurveyViewHolder extends RecyclerView.ViewHolder {
    TextView monthText;
    TextView dateTExt;
    TextView yearText;
    TextView nameText;
    TextView descriptionText;
    TextView statusText;
    TextView reactivateText;
    TextView evaluateText;
    boolean  isExpired;

    SurveyViewHolder(View itemView) {
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

  class FeedbackViewHolder extends RecyclerView.ViewHolder {
    TextView monthText;
    TextView dateTExt;
    TextView yearText;
    TextView descriptionText;
    TextView nameText;

    FeedbackViewHolder(View itemView) {
      super(itemView);
      monthText = (TextView) itemView.findViewById(R.id.text_month);
      dateTExt = (TextView) itemView.findViewById(R.id.text_date);
      yearText = (TextView) itemView.findViewById(R.id.text_year);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
    case FEEDBACK:
      return new FeedbackViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_feedback, parent, false));
    default:
      return new SurveyViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_survey, parent, false));
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder view, int position) {
    final AllSurveys allSurvey = data.get(position);
    final Context context = view.itemView.getContext();


    if (allSurvey.feedback != null) {
      FeedbackViewHolder holder = (FeedbackViewHolder) view;
      final Feedback feedback = allSurvey.feedback;

      holder.descriptionText.setText(
          context.getResources().getString(R.string.instant_feedback_bold));

      try {
        Date date = format.parse(feedback.createdAt);
        holder.monthText.setText(monthFormat.format(date));
        holder.dateTExt.setText(dateFormat.format(date));
        holder.yearText.setText(yearFormat.format(date));
      } catch (Exception e) {
        LogUtil.e("AAA " + e);
      }

      if (!feedback.questions.isEmpty()) {
        Question question = feedback.questions.get(0);

        holder.nameText.setText(question.text);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            listener.onSelect(feedback.id, feedback.key);
          }
        });
      }
    } else {
      final SurveyViewHolder holder = (SurveyViewHolder) view;
      final Survey survey = allSurvey.survey;
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
        holder.statusText.setText(context.getResources().getString(R.string.open_status_bold));
        holder.statusText.setBackgroundColor(
            context.getResources().getColor(R.color.unfinished_status));
        break;
      case Survey.UNFINISHED:
        holder.statusText.setText(
            context.getResources().getString(R.string.unfinished_status_bold));
        holder.statusText.setBackgroundColor(
            context.getResources().getColor(R.color.unfinished_status));
        break;
      case Survey.COMPLETED:
        holder.statusText.setText(context.getResources().getString(R.string.completed_bold));
        holder.statusText.setBackgroundColor(
            context.getResources().getColor(R.color.done_color));
        break;
      case Survey.NOT_INVITED:
        holder.statusText.setText(
            context.getResources().getString(R.string.not_invited_status_bold));
        holder.statusText.setBackgroundColor(
            context.getResources().getColor(R.color.unfinished_status));
        break;
      }
    }
  }

  @Override
  public int getItemViewType(int position) {
    final AllSurveys survey = data.get(position);
    if (survey.feedback != null) {
      return FEEDBACK;
    } else {
      return SURVEY;
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}