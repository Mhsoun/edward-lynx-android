package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Base activity for the InviteActivity, includes all listeners, components and action handlers
 * for the invite activity.
 */

public class InviteBaseActivity extends BaseActivity {

  protected ArrayList<User> displayData;
  protected UsersAdapter    adapter;
  protected int             invited;

  private TextView   countText;
  private CheckBox   selectCheckbox;
  private SearchView searchView;

  protected ArrayList<String> ids;
  protected ArrayList<String> deletedIds;
  protected ArrayList<User>   data;
  protected ArrayList<User>   recipients;

  public InviteBaseActivity() {
    data = new ArrayList<>();
    displayData = new ArrayList<>();
    recipients = new ArrayList<>();
    ids = new ArrayList<>();
    deletedIds = new ArrayList<>();
    invited = 0;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * initViews initializes views used in the activity
   */
  protected void initViews() {
    final RecyclerView usersList = (RecyclerView) findViewById(R.id.list_users);
    searchView = (SearchView) findViewById(R.id.searchview);
    searchView.setOnFocusChangeListener(onFocusChangeListener);

    countText = (TextView) findViewById(R.id.text_count);
    selectCheckbox = (CheckBox) findViewById(R.id.checkbox);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayoutManager.VERTICAL);
    usersList.addItemDecoration(dividerItemDecoration);
    usersList.setHasFixedSize(true);
    usersList.setLayoutManager(new LinearLayoutManager(this));

    adapter = new UsersAdapter(displayData, ids, listener);
    usersList.setAdapter(adapter);

    searchView.setOnQueryTextListener(onQueryTextListener);
    searchView.setOnCloseListener(onCloseListener);
    selectCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
    updateUI();
  }

  /**
   * retrieves the list of users from the API
   */
  protected void getData() {
    LogUtil.e("AAA getData users");
    subscription.add(Shared.apiClient.getUsers("list", new Subscriber<UsersResponse>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted ");
        displayData.clear();
        displayData.addAll(data);
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
      }

      @Override
      public void onNext(final UsersResponse response) {
        LogUtil.e("AAA onNext ");
        if (response.users == null) {
          return;
        }

        for (User user : response.users) {
          if (user.email.equals(Shared.user.email)) {
            response.users.remove(user);
            break;
          }
        }

        data.clear();
        data.addAll(response.users);

        if (!recipients.isEmpty()) {
          for (User recipientUser : recipients) {
            if (recipientUser.isUser) {
              for (int x = 0; x < data.size(); x++) {
                if (data.get(x).id == recipientUser.id) {
                  data.get(x).isDisabled = true;
                  break;
                }
              }
            } else {
              recipientUser.isDisabled = true;
              data.add(recipientUser);
            }
          }
        }
      }
    }));
  }

  /**
   * updates the view when a user is clicked from the list
   */
  protected void updateUI() {
    selectCheckbox.setOnCheckedChangeListener(null);
    selectCheckbox.setChecked((ids.size() != 0 && ids.size() + invited >= adapter.getItemCount()));
    selectCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);

    countText.setText(getString(R.string.number_people_selected, ids.size()));
    notifyAdapter();
  }

  /**
   * notifyAdapter notifies the list adapter of data changes
   */
  private void notifyAdapter() {
    Handler handler = new Handler();
    final Runnable r = new Runnable() {
      public void run() {
        adapter.notifyDataSetChanged();
      }
    };

    handler.post(r);
  }

  /**
   * action invoked when a new character is typed into the search text field
   * @param newText new search query
   */
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

  /**
   * listener for the select all checkbox
   */
  private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton
      .OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      for (User user : displayData) {
        if (isChecked) {
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

  /**
   * listener for the adapter when a user is selected from the list
   */
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

  /**
   * listener for the search view when a query text is changed
   */
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

  /**
   * listener for the search view when it is closed
   */
  private SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
    @Override
    public boolean onClose() {
      displayData.clear();
      displayData.addAll(data);
      notifyAdapter();
      return false;
    }
  };

  /**
   * action invoked when the invite button is pressed
   * @param v
   */
  public void invite(View v) {
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.VERTICAL);

    final EditText nameEdit = new EditText(context);
    nameEdit.setHint(getString(R.string.name));
    nameEdit.setTextColor(getResources().getColor(R.color.black));
    nameEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
    nameEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    layout.addView(nameEdit);

    final EditText emailEdit = new EditText(context);
    emailEdit.setLayoutParams(lp);
    emailEdit.setHint(getString(R.string.email));
    emailEdit.setTextColor(getResources().getColor(R.color.black));
    emailEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    emailEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
    layout.addView(emailEdit);

    final AlertDialog alertDialog = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.add_user))
        .setMessage(getString(R.string.enter_email))
        .setPositiveButton(getString(R.string.add_user), null)
        .setNegativeButton(getString(R.string.cancel), null)
        .setView(layout,
            ViewUtil.dpToPx(16, getResources()), 0, ViewUtil.dpToPx(16, getResources()), 0)
        .create();

    alertDialog.setCanceledOnTouchOutside(false);
    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(final DialogInterface dialogInterface) {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                final String name = nameEdit.getText().toString();
                final String email = emailEdit.getText().toString();

                if (TextUtils.isEmpty(name)) {
                  nameEdit.setError(getString(R.string.name_required));
                } else if (TextUtils.isEmpty(email)) {
                  emailEdit.setError(getString(R.string.email_required));
                } else if (!StringUtil.isValidEmail(email)) {
                  emailEdit.setError(getString(R.string.valid_email_required));
                } else if (!email.equals(Shared.user.email)) {
                  dialogInterface.dismiss();

                  long LOWER_RANGE = 10000; //assign lower range value
                  long UPPER_RANGE = 1000000; //assign upper range value
                  Random random = new Random();

                  long randomValue = LOWER_RANGE +
                      (long) (random.nextDouble() * (UPPER_RANGE - LOWER_RANGE));

                  User user = new User();
                  user.id = randomValue;
                  user.name = name;
                  user.email = email;
                  user.isAddedbyEmail = true;
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
