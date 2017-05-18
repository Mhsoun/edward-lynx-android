package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

  private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
  private SimpleDateFormat dateFormat  = new SimpleDateFormat("dd");
  private SimpleDateFormat yearFormat  = new SimpleDateFormat("yyyy");

  private List<Feedback>           data;
  private OnSelectFeedbackListener listener;

  public FeedbackAdapter(List<Feedback> data, OnSelectFeedbackListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView monthText;
    TextView dateTExt;
    TextView yearText;
    TextView descriptionText;
    TextView nameText;

    ViewHolder(View itemView) {
      super(itemView);
      monthText = (TextView) itemView.findViewById(R.id.text_month);
      dateTExt = (TextView) itemView.findViewById(R.id.text_date);
      yearText = (TextView) itemView.findViewById(R.id.text_year);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new FeedbackAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_feedback, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Feedback feedback = data.get(position);
    final Context context = holder.itemView.getContext();

    holder.descriptionText.setText(
        context.getResources().getString(R.string.instant_feedback_bold));

    try {
      Date date = DateUtil.getAPIFormat().parse(feedback.createdAt);
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
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public interface OnSelectFeedbackListener {
    void onSelect(long id, String key);
  }
}
