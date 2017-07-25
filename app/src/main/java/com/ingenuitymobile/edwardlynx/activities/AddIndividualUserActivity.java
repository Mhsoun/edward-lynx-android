package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AddUserBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.Id;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 7/6/17.
 */

public class AddIndividualUserActivity extends InviteBaseActivity {

  private int count;

  public AddIndividualUserActivity() {
    count = 0;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share_report);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.users).toUpperCase());

    final TextView textView = (TextView) findViewById(R.id.text_button_bottom);
    textView.setText(getString(R.string.update));

    initViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getIndividualUsers();
  }

  private void getIndividualUsers() {
    subscription.add(Shared.apiClient.getIndividualUsers(new Subscriber<UsersResponse>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted ");
        displayData.clear();
        displayData.addAll(data);
        adapter.notifyDataSetChanged();
        updateUI();
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

        for (User user : data) {
          if (user.managed) {
            ids.add(String.valueOf(user.id));
            count++;
          }
        }
      }
    }));
  }

  public void share(View v) {
    AddUserBody userBody = new AddUserBody();
    userBody.users = new ArrayList<>();

    for (String id : ids) {
      userBody.users.add(new Id(id));
    }

    LogUtil.e("AAA " + userBody.toString());
    progressDialog.show();
    subscription.add(Shared.apiClient.putIndividualUsers(userBody,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted");
            finish();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError");
            progressDialog.dismiss();
            Toast.makeText(
                context,
                getString(R.string.failed_update_users_team_plan),
                Toast.LENGTH_SHORT
            ).show();
          }

          @Override
          public void onNext(Response response) {
            LogUtil.e("AAA onNext");
            progressDialog.dismiss();

            Toast.makeText(AddIndividualUserActivity.this,
                getString(count > ids.size() ? R.string.disabled_users : R.string.enabled_user),
                Toast.LENGTH_SHORT)
                .show();
          }
        }));
  }
}

