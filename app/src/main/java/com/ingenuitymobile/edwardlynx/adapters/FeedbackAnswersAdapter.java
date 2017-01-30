package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.api.models.FeedbackFrequency;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 */

public class FeedbackAnswersAdapter extends
    RecyclerView.Adapter<FeedbackAnswersAdapter.ViewHolder> {

  private List<FeedbackFrequency> data;
  private int                     totalAnswers;
  private int                     type;

  public FeedbackAnswersAdapter(List<FeedbackFrequency> data) {
    super();
    this.data = data;
    totalAnswers = 0;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView    descriptionText;
    TextView    answerCountText;
    ProgressBar progressBar;

    ViewHolder(View itemView) {
      super(itemView);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      answerCountText = (TextView) itemView.findViewById(R.id.text_answer_count);
      progressBar = (ProgressBar) itemView.findViewById(R.id.progress_answer);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new FeedbackAnswersAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_answers, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final FeedbackFrequency frequency = data.get(position);


    if (type == Answer.CUSTOM_TEXT) {
      holder.descriptionText.setText(frequency.value);
    } else {
      holder.descriptionText.setText(frequency.description);
    }

    holder.answerCountText.setText("Total answers: " + frequency.count);
    holder.progressBar.setProgress(
        (int) (((float) (frequency.count) / (float) totalAnswers) * 100));
    holder.progressBar.setScaleY(3f);
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void setTotalAnswers(int totalAnswers) {
    this.totalAnswers = totalAnswers;
    notifyDataSetChanged();
  }

  public void setType(int type) {
    this.type = type;
    notifyDataSetChanged();
  }
}