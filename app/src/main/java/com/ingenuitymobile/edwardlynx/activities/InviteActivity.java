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
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

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

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();

    final TextView textView = (TextView) findViewById(R.id.text_button_bottom);
    textView.setText(getString(R.string.send_invites));
    getData();

    body = new Gson().fromJson(getIntent().getStringExtra("body"), InstantFeedbackBody.class);
  }

  public void create(View v) {
    if (ids.isEmpty()) {
      Toast.makeText(context, getString(R.string.select_atleast_one), Toast.LENGTH_SHORT).show();
    } else if (ids.size() < 3) {
      ViewUtil.showAlert(this, null, getString(R.string.invite_info));
    } else {
      deletedIds.clear();
      List<Id> recipients = new ArrayList<>();
      for (User user : data) {
        if (user.isAddedbyEmail) {
          ids.remove(String.valueOf(user.id));
          deletedIds.add(String.valueOf(user.id));
          recipients.add(new Id(user.name, user.email));
        }
      }

      for (String id : ids) {
        recipients.add(new Id(id));
      }

      body.recipients = recipients;
      LogUtil.e("AAA " + body.toString());

      progressDialog.show();
      subscription.add(Shared.apiClient.postInstantFeedback(body, new Subscriber<Response>() {
        @Override
        public void onCompleted() {
          LogUtil.e("AAA onCompleted");
          setResult(RESULT_OK);
          finish();
        }

        @Override
        public void onError(Throwable e) {
          progressDialog.dismiss();
          ids.addAll(deletedIds);
          LogUtil.e("AAA onError " + e);
        }

        @Override
        public void onNext(Response response) {
          LogUtil.e("AAA onNext");
          progressDialog.dismiss();
          Toast.makeText(InviteActivity.this, getString(R.string.instant_feedback_created),
              Toast.LENGTH_SHORT).show();
        }
      }));
    }
  }
}

