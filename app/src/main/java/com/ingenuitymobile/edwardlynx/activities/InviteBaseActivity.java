package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.UsersAdapter;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 23/01/2017.
 */

public class InviteBaseActivity extends BaseActivity {


  private ArrayList<User> data;
  private ArrayList<User> displayData;
  private UsersAdapter    adapter;

  private TextView countText;
  private TextView selectText;

  protected ArrayList<String> ids;

  public InviteBaseActivity() {
    data = new ArrayList<>();
    displayData = new ArrayList<>();
    ids = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share_report);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  protected void initViews() {
    final RecyclerView usersList = (RecyclerView) findViewById(R.id.list_users);
    final SearchView searchView = (SearchView) findViewById(R.id.searchview);

    countText = (TextView) findViewById(R.id.text_count);
    selectText = (TextView) findViewById(R.id.text_select_all);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    usersList.addItemDecoration(dividerItemDecoration);
    usersList.setHasFixedSize(true);
    usersList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new UsersAdapter(displayData, ids, listener);
    usersList.setAdapter(adapter);

    searchView.setOnQueryTextListener(onQueryTextListener);
    searchView.setOnCloseListener(onCloseListener);
    selectText.setOnClickListener(onClickListener);
  }

  protected void getData() {
    LogUtil.e("AAA getData users");
    subscription.add(Shared.apiClient.getUsers("list", new Subscriber<UsersResponse>() {
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
        displayData.clear();
        displayData.addAll(data);
      }
    }));
  }

  private void updateUI() {
    if (ids.size() >= adapter.getItemCount()) {
      selectText.setText("Deselect all");
    } else {
      selectText.setText("Select all");
    }

    countText.setText("No. of people selected: " + ids.size());

    notifyAdapter();
  }

  private void notifyAdapter() {
    Handler handler = new Handler();
    final Runnable r = new Runnable() {
      public void run() {
        adapter.notifyDataSetChanged();
      }
    };

    handler.post(r);
  }

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      final boolean isSelected = selectText.getText().toString().equals("Select all");
      for (User user : displayData) {
        if (isSelected) {
          if (!ids.contains(String.valueOf(user.id))) {
            ids.add(String.valueOf(user.id));
          }
        } else {
          ids.remove(String.valueOf(user.id));
        }
      }
      updateUI();
    }
  };

  public UsersAdapter.OnSelectUserListener listener = new UsersAdapter.OnSelectUserListener() {
    @Override
    public void onSelect(String id, boolean isSelected) {
      LogUtil.e("AAA isSelected + " + isSelected + " " + id);
      if (isSelected) {
        LogUtil.e("AAA isSelected ");
        if (!ids.contains(id)) {
          LogUtil.e("AAA isSelected add");
          ids.add(id);
        }
      } else {
        LogUtil.e("AAA not isSelected ");
        ids.remove(id);
      }
      updateUI();
    }
  };

  private SearchView.OnQueryTextListener onQueryTextListener =
      new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
          LogUtil.e("AAA onQueryTextSubmit" + query);
          return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
          displayData.clear();

          for (User user : data) {
            if (user.name.toLowerCase().contains(newText.toLowerCase()) ||
                user.email.toLowerCase().contains(newText.toLowerCase())) {
              displayData.add(user);
            }
          }

          updateUI();
          notifyAdapter();
          return true;
        }
      };

  private SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
    @Override
    public boolean onClose() {
      displayData.clear();
      displayData.addAll(data);
      notifyAdapter();
      return false;
    }
  };
}
