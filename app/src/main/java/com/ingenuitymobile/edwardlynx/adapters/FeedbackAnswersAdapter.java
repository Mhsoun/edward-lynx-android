package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
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

  /**
   * Adapter for displaying the feedback answers.
   * @param data the list of feedback frequencies
   */
  public FeedbackAnswersAdapter(List<FeedbackFrequency> data) {
    super();
    this.data = data;
    totalAnswers = 0;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView commentText;

    ViewHolder(View itemView) {
      super(itemView);
      commentText = (TextView) itemView.findViewById(R.id.text_comment);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new FeedbackAnswersAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_comment_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final FeedbackFrequency frequency = data.get(position);


    if (type == Answer.CUSTOM_TEXT) {
      holder.commentText.setText(frequency.value);
    } else {
      holder.commentText.setText(frequency.description);
    }
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
