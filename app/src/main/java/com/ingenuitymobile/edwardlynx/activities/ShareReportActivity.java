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
import com.ingenuitymobile.edwardlynx.api.bodyparams.Id;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ShareParam;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 */

public class ShareReportActivity extends BaseActivity {

  private long            id;
  private ArrayList<User> data;
  private ArrayList<Long> ids;
  private UsersAdapter    adapter;

  public ShareReportActivity() {
    data = new ArrayList<>();
    ids = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share_report);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);

    initViews();
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
    subscription = Shared.apiClient.getUsers("list", new Subscriber<UsersResponse>() {
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

  public void share(View v) {
    if (ids.isEmpty()) {
      Toast.makeText(ShareReportActivity.this, "Please select atleast one", Toast.LENGTH_SHORT)
          .show();
    } else {
      List<Id> recipients = new ArrayList<>();
      for (Long id : ids) {
        recipients.add(new Id(id));
      }

      ShareParam param = new ShareParam();
      param.users = ids;
      LogUtil.e("AAA " + param.toString());

      subscription = Shared.apiClient.postShareInstantFeedback(id, param,
          new Subscriber<Response>() {
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
              Toast.makeText(ShareReportActivity.this, "Shared to other people", Toast.LENGTH_SHORT)
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
