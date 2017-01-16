package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.UsersAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.Id;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.QuestionBody;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class InviteActivity extends BaseActivity {

  private ArrayList<User> data;
  private ArrayList<Long> ids;
  private UsersAdapter    adapter;

  private InstantFeedbackBody body;

  public InviteActivity() {
    data = new ArrayList<>();
    ids = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();
    getData();

    body = new InstantFeedbackBody();
    body.lang = Shared.user.lang;
    QuestionBody question = new QuestionBody();
    question.text = getIntent().getStringExtra("question");
    question.isNA = 0;
    AnswerBody answerBody = new AnswerBody();
    answerBody.type = Integer.parseInt(getIntent().getStringExtra("question_type"));
    body.questionBodies.add(question);
    question.answerBody = answerBody;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    final RecyclerView usersList = (RecyclerView) findViewById(R.id.list_users);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    usersList.addItemDecoration(dividerItemDecoration);
    usersList.setHasFixedSize(true);
    usersList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new UsersAdapter(data, ids, listener);
    usersList.setAdapter(adapter);
  }

  private void getData() {
    LogUtil.e("AAA getData users");
    Shared.apiClient.getUsers("list", new Subscriber<UsersResponse>() {
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
      public void onNext(final UsersResponse response) {
        LogUtil.e("AAA onNext ");
        data.clear();
        data.addAll(response.users);
      }
    });
  }

  public void invite(View v) {
    if (ids.isEmpty()) {
      Toast.makeText(InviteActivity.this, "Please select atleast one", Toast.LENGTH_SHORT)
          .show();
    } else {
      List<Id> recipients = new ArrayList<>();
      for (Long id : ids) {
        recipients.add(new Id(id));
      }
      body.recipients = recipients;
      LogUtil.e("AAA " + body.toString());

      Shared.apiClient.postInstantFeedback(body, new Subscriber<Response>() {
        @Override
        public void onCompleted() {
          LogUtil.e("AAA onCompleted");
          setResult(RESULT_OK);
          finish();
        }

        @Override
        public void onError(Throwable e) {
          LogUtil.e("AAA onError");
        }

        @Override
        public void onNext(Response response) {
          LogUtil.e("AAA onNext");
          Toast.makeText(InviteActivity.this, "Instant Feedback created", Toast.LENGTH_SHORT)
              .show();
        }
      });
    }
  }

  public UsersAdapter.OnSelectUserListener listener = new UsersAdapter.OnSelectUserListener() {
    @Override
    public void onSelect(long id, boolean isSelected) {
      LogUtil.e("AAA isSelected +" + isSelected);
      if (isSelected) {
        LogUtil.e("AAA isSelected ");
        ids.add(id);
      } else {
        LogUtil.e("AAA not isSelected ");
        ids.remove(id);
      }
      adapter.notifyDataSetChanged();
    }
  };
}

