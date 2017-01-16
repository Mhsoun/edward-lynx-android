package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Question;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

  private List<Feedback>           data;
  private OnSelectFeedbackListener listener;

  public FeedbackAdapter(List<Feedback> data, OnSelectFeedbackListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView descriptionText;
    TextView nameText;

    ViewHolder(View itemView) {
      super(itemView);
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

    if (!feedback.questions.isEmpty()) {
      Question question = feedback.questions.get(0);
      holder.nameText.setText(question.text);
      holder.descriptionText.setText(question.answer.decscription);

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onSelect(feedback.id);
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public interface OnSelectFeedbackListener {
    void onSelect(long id);
  }
}
