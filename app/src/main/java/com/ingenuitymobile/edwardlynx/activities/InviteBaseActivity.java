package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.UsersAdapter;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.StringUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Random;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 23/01/2017.
 */

public class InviteBaseActivity extends BaseActivity {

  private ArrayList<User> displayData;

  private TextView   countText;
  private TextView   selectText;
  private SearchView searchView;

  protected ArrayList<String> ids;

  private ArrayList<User> data;
  private UsersAdapter    adapter;

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
    searchView = (SearchView) findViewById(R.id.searchview);

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

  private void search(String newText) {
    displayData.clear();

    for (User user : data) {
      if (user.name.toLowerCase().contains(newText.toLowerCase()) ||
          user.email.toLowerCase().contains(newText.toLowerCase())) {
        displayData.add(user);
      }
    }

    updateUI();
    notifyAdapter();
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
          search(newText);
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


  public void invite(View v) {
    final EditText input = new EditText(context);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    input.setLayoutParams(lp);
    input.setHint(getString(R.string.email_address));
    input.setTextColor(getResources().getColor(R.color.black));
    input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    input.setImeOptions(EditorInfo.IME_ACTION_DONE);

    final AlertDialog alertDialog = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.add_user))
        .setMessage(getString(R.string.enter_email))
        .setPositiveButton(getString(R.string.add_email), null)
        .setNegativeButton(getString(R.string.cancel), null)
        .setView(input,
            ViewUtil.dpToPx(16, getResources()), 0, ViewUtil.dpToPx(16, getResources()), 0)
        .create();

    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(final DialogInterface dialogInterface) {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                final String email = input.getText().toString();

                if (TextUtils.isEmpty(email)) {
                  input.setError(getString(R.string.email_required));
                } else if (!StringUtil.isValidEmail(email)) {
                  input.setError(getString(R.string.valid_email_required));
                } else {
                  LogUtil.e("AAA " + email);
                  dialogInterface.dismiss();

                  long LOWER_RANGE = 10000; //assign lower range value
                  long UPPER_RANGE = 1000000; //assign upper range value
                  Random random = new Random();

                  long randomValue = LOWER_RANGE +
                      (long) (random.nextDouble() * (UPPER_RANGE - LOWER_RANGE));

                  User user = new User();
                  user.id = randomValue;
                  user.name = "Invited by email";
                  user.email = email;
                  data.add(user);
                  ids.add(String.valueOf(user.id));
                  search(searchView.getQuery().toString());
                }
              }
            });
      }
    });
    alertDialog.show();
  }
}
