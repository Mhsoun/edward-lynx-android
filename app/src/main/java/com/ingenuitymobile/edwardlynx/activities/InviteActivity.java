package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.Id;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class InviteActivity extends InviteBaseActivity {

  private InstantFeedbackBody body;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();
    getData();

    body = new Gson().fromJson(getIntent().getStringExtra("body"), InstantFeedbackBody.class);
  }

  public void invite(View v) {
    final TextView textView = (TextView) v;
    if (ids.isEmpty()) {
      Toast.makeText(InviteActivity.this, "Please select atleast one", Toast.LENGTH_SHORT)
          .show();
    } else {
      List<Id> recipients = new ArrayList<>();
      for (String id : ids) {
        recipients.add(new Id(id));
      }
      body.recipients = recipients;
      LogUtil.e("AAA " + body.toString());

      textView.setText("Loading. . .");
      subscription.add(Shared.apiClient.postInstantFeedback(body, new Subscriber<Response>() {
        @Override
        public void onCompleted() {
          LogUtil.e("AAA onCompleted");
          setResult(RESULT_OK);
          finish();
        }

        @Override
        public void onError(Throwable e) {
          textView.setText("Create Instant Feedback");
          LogUtil.e("AAA onError");
        }

        @Override
        public void onNext(Response response) {
          LogUtil.e("AAA onNext");
          Toast.makeText(InviteActivity.this, "Instant Feedback created", Toast.LENGTH_SHORT)
              .show();
        }
      }));
    }
  }
}

