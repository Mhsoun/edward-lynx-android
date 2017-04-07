package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyReportsAdapter extends RecyclerView.Adapter<SurveyReportsAdapter.ViewHolder> {

  private SimpleDateFormat format        = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  private SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy");

  private List<Survey>             data;
  private OnSelectFeedbackListener listener;

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

      reportType.setText(
          itemView.getContext().getResources().getString(R.string.lynx_progress_bold));
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
      Date date = format.parse(survey.endDate);
      holder.dateText.setText(displayFormat.format(date));
    } catch (Exception e) {
      LogUtil.e("AAA " + e);
    }

    holder.countText.setText("0/0");
    holder.nameText.setText(survey.name);
    holder.descriptionText.setText(survey.personsEvaluatedText);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        listener.onSelect(survey.id, survey.key);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public interface OnSelectFeedbackListener {
    void onSelect(long id, String key);
  }
}
