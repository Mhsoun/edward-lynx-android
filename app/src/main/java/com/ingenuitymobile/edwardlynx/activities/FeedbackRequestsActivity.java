package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class FeedbackRequestsActivity extends BaseActivity {

  private ArrayList<Feedback> data;
  private FeedbackAdapter     adapter;

  public FeedbackRequestsActivity() {
    data = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_requests);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getData();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    final RecyclerView feedbackList = (RecyclerView) findViewById(R.id.list_feedback);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    feedbackList.addItemDecoration(dividerItemDecoration);
    feedbackList.setHasFixedSize(true);
    feedbackList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new FeedbackAdapter(data, listener);
    feedbackList.setAdapter(adapter);
  }

  private void getData() {
    LogUtil.e("AAA getData FeedbackRequestsActivity");
    subscription.add(
        Shared.apiClient.getInstantFeedbacks("to_answer", new Subscriber<FeedbacksResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
          }

          @Override
          public void onNext(final FeedbacksResponse response) {
            LogUtil.e("AAA onNext ");
            data.clear();
            data.addAll(response.items);
          }
        }));
  }

  private FeedbackAdapter.OnSelectFeedbackListener listener = new FeedbackAdapter
      .OnSelectFeedbackListener() {
    @Override
    public void onSelect(long id, String key) {
      Intent intent = new Intent(context, AnswerFeedbackActivity.class);
      intent.putExtra("id", id);
      startActivity(intent);
    }
  };

}
