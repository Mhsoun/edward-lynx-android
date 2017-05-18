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

import java.util.Date;
import java.util.List;

/**
 * Created by memengski on 4/4/17.
 */

public class FeedbackReportsAdapter extends
    RecyclerView.Adapter<FeedbackReportsAdapter.ViewHolder> {

  private List<Feedback>           data;
  private OnSelectFeedbackListener listener;

  public FeedbackReportsAdapter(List<Feedback> data, OnSelectFeedbackListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView dateText;
    TextView descriptionText;
    TextView nameText;
    TextView countText;

    ViewHolder(View itemView) {
      super(itemView);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      countText = (TextView) itemView.findViewById(R.id.text_count);
    }
  }

  @Override
  public FeedbackReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_feedback_reports, parent, false));
  }

  @Override
  public void onBindViewHolder(FeedbackReportsAdapter.ViewHolder holder, int position) {
    final Feedback feedback = data.get(position);
    final Context context = holder.itemView.getContext();

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
      holder.descriptionText.setText(question.answer.decscription);

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

