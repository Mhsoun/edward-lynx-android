package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by jeremypacabis on February 09, 2018.
 *
 * @author Jeremy Patrick Pacabis <jeremy@ingenuity.ph>
 *         com.ingenuitymobile.edwardlynx.adapters <edward-lynx-android>
 */

public class FeedbackRespondentsSection extends StatelessSection {

    private String feedback;
    private List<String> names;

    public FeedbackRespondentsSection(String feedback, List<String> names) {
        super(new SectionParameters.Builder(R.layout.section_feedback_name_item)
                .headerResourceId(R.layout.section_feedback_name_header)
                .build());
        this.feedback = feedback;
        this.names = names;
    }

    @Override
    public int getContentItemsTotal() {
        return names.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new NameItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        NameItemViewHolder viewHolder = (NameItemViewHolder) holder;
        viewHolder.name.setText(names.get(position));
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new FeedbackHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        FeedbackHeaderViewHolder viewHolder = (FeedbackHeaderViewHolder) holder;
        viewHolder.feedback.setText(feedback);
    }

    class FeedbackHeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView feedback;

        public FeedbackHeaderViewHolder(View itemView) {
            super(itemView);
            feedback = itemView.findViewById(R.id.section_feedback_text);
        }
    }

    class NameItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;

        public NameItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.section_name_text);
        }
    }
}
