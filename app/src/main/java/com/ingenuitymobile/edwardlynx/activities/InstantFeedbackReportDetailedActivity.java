package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackRespondentsSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by jeremypacabis on February 09, 2018.
 *
 * @author Jeremy Patrick Pacabis <jeremy@ingenuity.ph>
 *         com.ingenuitymobile.edwardlynx.activities <edward-lynx-android>
 */

public class InstantFeedbackReportDetailedActivity extends BaseActivity {

    private RecyclerView feedbackList;
    private SectionedRecyclerViewAdapter feedbackAndNamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_feedback_report_detailed);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.respondents_text).toUpperCase());

        initializeViews();
        loadDummyData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        feedbackList = findViewById(R.id.list_feedback);
        feedbackList.setLayoutManager(new LinearLayoutManager(context));
        feedbackList.setHasFixedSize(true);
        feedbackAndNamesAdapter = new SectionedRecyclerViewAdapter();

        String[] feedback = new String[]{
                "Yes",
                "No",
                "Maybe"
        };

        List<String> names;

        for (int x = 0; x < feedback.length; x++) {
            int y = new Random().nextInt(100) + 1;
            names = new ArrayList<>(y);
            for (int z = 0; z < y; z++) {
                String name = String.valueOf(new Random().nextInt());
                names.add(name);
            }

            feedbackAndNamesAdapter.addSection(new FeedbackRespondentsSection(feedback[x], names));
        }

        feedbackList.setAdapter(feedbackAndNamesAdapter);

//        List<String> feedback, names;
//        feedback = new ArrayList<>();
//        names = new ArrayList<>();
//
//        for (int x = 0; x < 20; x++) {
//            names.add(String.valueOf(new Random().nextInt(10)));
//            feedback.add(String.valueOf(new Random().nextInt()));
//        }
//
//        FeedbackRespondentsAdapter feedbackRespondentsAdapter =
//                new FeedbackRespondentsAdapter(feedback, names);
//        feedbackList.addItemDecoration(new DividerItemDecoration(context,
//                LinearLayoutManager.VERTICAL));
//        feedbackList.setAdapter(feedbackRespondentsAdapter);
    }

    private void loadDummyData() {

    }

    public enum InstantFeedbackType {
        YES_NO,
        FREE_TEXT,
        NUMERIC_SCALE,
        CUSTOM_SCALE
    }
}
