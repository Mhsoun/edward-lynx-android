package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.Id;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 14/02/2017.
 * Activity to add more participants to invite.
 */

public class AddMoreParticipantsActivity extends InviteBaseActivity {

  private long id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.add_more_participants).toUpperCase());

    initViews();

    id = getIntent().getLongExtra("id", 0L);
    LogUtil.e("AAA " + id);
  }

  @Override
  protected void onResume() {
    super.onResume();
    getInstantFeedbackDetailed();
  }

  /**
   * getInstantFeedbackDetailed gets the detailed feedbacks from API
   */
  private void getInstantFeedbackDetailed() {
    subscription.add(Shared.apiClient.getInstantFeedback(id, new Subscriber<Feedback>() {
      @Override
      public void onCompleted() {
        getData();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
        Toast.makeText(
            context,
            context.getString(R.string.cant_connect),
            Toast.LENGTH_SHORT
        ).show();
      }

      @Override
      public void onNext(Feedback feedback) {
        recipients.clear();
        recipients.addAll(feedback.recipients);
        invited = feedback.stats.invited;
      }
    }));
  }

  /**
   * create send invites to target participants
   * @param v
   */
  public void create(View v) {
    if (ids.isEmpty()) {
      Toast.makeText(context, getString(R.string.select_atleast_one), Toast.LENGTH_SHORT).show();
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

      InstantFeedbackBody body = new InstantFeedbackBody();
      body.recipients = recipients;

      progressDialog.show();
      subscription.add(
          Shared.apiClient.postInstantFeedbackParticipants(id, body, new Subscriber<Response>() {
            @Override
            public void onCompleted() {
              LogUtil.e("AAA onCompleted");
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
              Toast.makeText(context, getString(R.string.instant_feedback_created),
                  Toast.LENGTH_SHORT).show();
            }
          }));
    }
  }
}
