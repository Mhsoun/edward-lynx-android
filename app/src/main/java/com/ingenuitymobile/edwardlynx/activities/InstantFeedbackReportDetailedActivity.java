package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackRespondentsAdapter;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackRespondentsSection;
import com.ingenuitymobile.edwardlynx.api.models.Answer;
import com.ingenuitymobile.edwardlynx.api.models.FeedbackFrequency;
import com.ingenuitymobile.edwardlynx.api.models.Submission;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbackAnswerResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import rx.Subscriber;

/**
 * Created by jeremypacabis on February 09, 2018.
 *
 * @author Jeremy Patrick Pacabis <jeremy@ingenuity.ph>
 *         com.ingenuitymobile.edwardlynx.activities <edward-lynx-android>
 */

public class InstantFeedbackReportDetailedActivity extends BaseActivity {

    private RecyclerView feedbackList;
    private SectionedRecyclerViewAdapter feedbackAndNamesAdapter;

    private FeedbackAnswerResponse feedbackResponse;
    private List<FeedbackRespondents> feedbackRespondents;
    private int feedbackType;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_feedback_report_detailed);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Intent intent = getIntent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.respondents_text).toUpperCase());

        feedbackType = intent.getIntExtra("feedbackType", -1);
        id = intent.getLongExtra("id", -1);

        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRespondentsData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * this initializes the feedbackList RecyclerView for displaying the
     * list of respondents
     */
    private void initializeViews() {
        feedbackList = findViewById(R.id.list_feedback);
        feedbackList.setLayoutManager(new LinearLayoutManager(context));
        feedbackList.setHasFixedSize(true);
    }

    private void getRespondentsData() {
        progressDialog.show();
        getAnswers();
    }

    /**
     * retrieves the instant feedback answers from the API
     */
    private void getAnswers() {
        LogUtil.e("AAA getData instant feedback answers");
        subscription.add(
                Shared.apiClient.getInstantFeedbackAnswers(id, new Subscriber<FeedbackAnswerResponse>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                        loadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        LogUtil.e("AAA onError " + e);
                    }

                    @Override
                    public void onNext(final FeedbackAnswerResponse response) {
                        feedbackResponse = response;
                        LogUtil.e("AAA onNext ");
                    }
                }));
    }

    /**
     * this would be called after feedback answer retrieval from API,
     * this method will sort out the feedback answers and display the
     * correct adapter for the list
     */
    private void loadData() {
        String feedback;
        List<String> submissions;
        feedbackRespondents = new ArrayList<>(feedbackResponse.frequencies.size());

        for (FeedbackFrequency frequency : feedbackResponse.frequencies) {
            feedback = feedbackType == Answer.CUSTOM_TEXT ? frequency.value : frequency.description;
            if (frequency.submissions != null) {
                submissions = new ArrayList<>(frequency.submissions.size());
                for (Submission submission : frequency.submissions) {
                    submissions.add(submission.name);
                }
            } else {
                submissions = new ArrayList<>();
            }

            feedbackRespondents.add(new FeedbackRespondents(feedback, submissions));
        }

        if (feedbackType == Answer.CUSTOM_TEXT) {
            loadCustomText();
        } else {
            loadOtherTypes();
        }
    }

    /**
     * this would set the adapter to the FeedbackRespondentsAdapter which
     * displays the list of feedback and the person who gave the feedback
     * either anonymous or not, since this is for the free text type of
     * instant feedback
     */
    private void loadCustomText() {
        List<String> feedback = new ArrayList<>(feedbackRespondents.size());
        List<String> names = new ArrayList<>(feedbackRespondents.size());
        for (FeedbackRespondents respondents : feedbackRespondents) {
            feedback.add(respondents.getFeedback());
            names.add(respondents.getFeedbackSubmissions().size() == 0 ?
                    getString(R.string.anonymous) :
                    respondents.getFeedbackSubmissions().get(0)
            );
        }

        final FeedbackRespondentsAdapter feedbackRespondentsAdapter =
                new FeedbackRespondentsAdapter(feedback, names);
        feedbackList.addItemDecoration(new DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL));
        feedbackList.setAdapter(feedbackRespondentsAdapter);
    }

    /**
     * this would set the adapter to the sectioned adapter for displaying the
     * feedback description and the list non-anonymous people who answered
     */
    private void loadOtherTypes() {
        feedbackAndNamesAdapter = new SectionedRecyclerViewAdapter();
        for (FeedbackRespondents respondents : feedbackRespondents) {
            if (respondents.getFeedbackSubmissions().size() > 0) {
                feedbackAndNamesAdapter.addSection(
                        new FeedbackRespondentsSection(
                                respondents.getFeedback(),
                                respondents.getFeedbackSubmissions()
                        )
                );
            }
        }

        feedbackList.setAdapter(feedbackAndNamesAdapter);
    }

    /**
     * class for storing feedback and respondents for easy access
     */
    class FeedbackRespondents {
        private String feedback;
        private List<String> feedbackSubmissions;

        FeedbackRespondents(String feedback, List<String> feedbackSubmissions) {
            this.feedback = feedback;
            this.feedbackSubmissions = feedbackSubmissions;
        }

        public String getFeedback() {
            return feedback;
        }

        List<String> getFeedbackSubmissions() {
            return feedbackSubmissions;
        }
    }
}
