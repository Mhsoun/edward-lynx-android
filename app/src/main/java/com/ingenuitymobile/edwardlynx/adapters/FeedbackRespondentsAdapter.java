package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;

import java.util.List;

/**
 * Created by jeremypacabis on February 09, 2018.
 *
 * @author Jeremy Patrick Pacabis <jeremy@ingenuity.ph>
 *         com.ingenuitymobile.edwardlynx.adapters <edward-lynx-android>
 */

public class FeedbackRespondentsAdapter extends
        RecyclerView.Adapter<FeedbackRespondentsAdapter.ViewHolder> {

    private List<String> feedback;
    private List<String> names;

    public FeedbackRespondentsAdapter(List<String> feedback, List<String> names) {
        this.feedback = feedback;
        this.names = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_freetext_feedback, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.feedbackText.setText(Html.fromHtml(holder
                .itemView
                .getContext()
                .getString(R.string.free_text_feedback, feedback.get(position))));
        holder.nameText.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return feedback.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView feedbackText, nameText;

        public ViewHolder(View itemView) {
            super(itemView);
            feedbackText = itemView.findViewById(R.id.feedback_text);
            nameText = itemView.findViewById(R.id.name_text);
        }
    }
}
