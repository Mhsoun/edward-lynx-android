package com.ingenuitymobile.edwardlynx.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.InviteUserAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InviteUserParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserParam;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by memengski on 5/24/17.
 * Activity to add people and invite them to answer a survey.
 */

public class InvitePeopleActivity extends BaseActivity {

  private long     id;
  private String   evaluate;
  private EditText nameEdit;
  private EditText emailEdit;
  private Spinner  spinner;
  private TextView emptyText;
  private String   key;

  private InviteUserAdapter    adapter;
  private ArrayList<UserParam> userParams;
  private String[]             roles;
  private Set<String>          disallowedRecipients;

  public InvitePeopleActivity() {
    userParams = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite_people);

    context = this;

    final String title = getIntent().getStringExtra("title");
    id = getIntent().getLongExtra("id", 0L);
    key = getIntent().getStringExtra("key");
    evaluate = getIntent().getStringExtra("evaluate");

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(title);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    roles = new String[]{
        getString(R.string.colleague),
        getString(R.string.manager),
        getString(R.string.customer),
        getString(R.string.matrix_manager),
        getString(R.string.other_stakeholder),
        getString(R.string.direct_report)
    };
    initViews();
    getDisallowedRecipients();
    setTitle(getString(R.string.invite_people).toUpperCase());

    NotificationManager notificationManager =
        (NotificationManager) getApplicationContext()
            .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancel((int) id * -1);
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
  private void initViews() {
    final TextView evaluatedText = (TextView) findViewById(R.id.text_evaluated);
    evaluatedText.setText(evaluate);

    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_users);
    recyclerView.setNestedScrollingEnabled(false);

    nameEdit = (EditText) findViewById(R.id.edit_name);
    emailEdit = (EditText) findViewById(R.id.edit_email);
    emptyText = (TextView) findViewById(R.id.text_empty);

    nameEdit.setOnFocusChangeListener(onFocusChangeListener);
    emailEdit.setOnFocusChangeListener(onFocusChangeListener);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
        context,
        LinearLayoutManager.VERTICAL
    );

    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));

    final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

    adapter = new InviteUserAdapter(userParams);
    recyclerView.setAdapter(adapter);

    spinner = (Spinner) findViewById(R.id.spinner);
    final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
        this,
        R.layout.spinner_style,
        roles
    );

    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(dataAdapter);
  }

  private void getDisallowedRecipients() {
    subscription.add(
      Shared.apiClient.getSurvey(
              id,
              key,
              new Subscriber<Survey>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                  disallowedRecipients = new HashSet<>();
                }

                @Override
                public void onNext(Survey survey) {
                  if (survey.disallowedRecipients != null) {
                    disallowedRecipients = new HashSet<>(survey.disallowedRecipients);
                  } else {
                    disallowedRecipients = new HashSet<>();
                  }
                }
              }
      )
    );
  }

  /**
   * checks if the email user is already invited
   * @param email the email string to be checked if it is invited
   * @return true if the email is allowed to be invited
   */
  private boolean isInviteAllowed(String email) {
    return !disallowedRecipients.contains(email);
  }

  /**
   * notifyAdapter notifies the list adapter of data changes
   */
  private void notifyAdapter() {
    emptyText.setVisibility(userParams.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  /**
   * action invoked when the add user is pressed
   * @param v
   */
  public void addUser(View v) {
    final String name = nameEdit.getText().toString();
    final String email = emailEdit.getText().toString();

    if (TextUtils.isEmpty(name)) {
      Toast.makeText(context, getString(R.string.name_required), Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(email)) {
      Toast.makeText(context, getString(R.string.email_required), Toast.LENGTH_SHORT).show();
      return;
    }

    if (!StringUtil.isValidEmail(email)) {
      Toast.makeText(context, getString(R.string.valid_email_required), Toast.LENGTH_SHORT).show();
      return;
    }

    if (email.equals(SessionStore.restoreUsername(context)) && !isInviteAllowed(SessionStore.restoreUsername(context))) {
      Toast.makeText(context, getString(R.string.disallowed_invite_self), Toast.LENGTH_SHORT).show();
      return;
    }

    if (!isInviteAllowed(email)) {
      Toast.makeText(context, getString(R.string.disallowed_invite_user), Toast.LENGTH_SHORT).show();
      return;
    }

    UserParam userParam = new UserParam();
    userParam.name = name;
    userParam.email = email;
    userParam.role = userParam.getRole(context, roles[spinner.getSelectedItemPosition()]);
    userParams.add(userParam);

    emailEdit.setText("");
    nameEdit.setText("");
    spinner.setSelection(0);

    notifyAdapter();
    hideKeyboard();
    nameEdit.clearFocus();
    emailEdit.clearFocus();
  }

  /**
   * action invoked when the send invite is pressed
   * @param v
   */
  public void sendInvites(View v) {
    if (userParams.isEmpty()) {
      Toast.makeText(context, getString(R.string.select_atleast_one), Toast.LENGTH_SHORT).show();
    } else {
      InviteUserParam inviteUserParam = new InviteUserParam();
      inviteUserParam.recipients = userParams;

      LogUtil.e("AAA " + inviteUserParam.toString());
      progressDialog.show();
      subscription.add(
          Shared.apiClient.postSurveyRecipient(id, inviteUserParam, new Subscriber<Response>() {
            @Override
            public void onCompleted() {
              LogUtil.e("AAA onCompleted");
              progressDialog.dismiss();
              finish();
            }

            @Override
            public void onError(Throwable e) {
              progressDialog.dismiss();
              LogUtil.e("AAA onError " + e);
              if (e != null && ((RetrofitError) e).getResponse().getStatus() == 403) {
                Toast.makeText(context,
                        getString(R.string.survey_action_unauthorized),
                        Toast.LENGTH_SHORT
                ).show();
              } else {
                Toast.makeText(context,
                        getString(R.string.survey_sending_failed),
                        Toast.LENGTH_SHORT
                ).show();
              }
            }

            @Override
            public void onNext(Response response) {
              LogUtil.e("AAA onNext");
              Toast.makeText(
                  context,
                  getString(R.string.successfully_invite_people_to_rate_you),
                  Toast.LENGTH_SHORT
              ).show();
            }
          }));
    }
  }

  /**
   * listener for swiping an item from the list of people to be invited
   */
  ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
      ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

    Drawable background;
    int xMarkMargin;
    boolean initiated;

    private void init() {
      background = new ColorDrawable(Color.RED);
      xMarkMargin = (int) getResources().getDimension(R.dimen.ic_clear_margin);
      initiated = true;
    }

    public boolean onMove(RecyclerView recyclerView,
        RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
      userParams.remove(viewHolder.getLayoutPosition());
      notifyAdapter();
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
        RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
        boolean isCurrentlyActive) {
      View itemView = viewHolder.itemView;

      if (viewHolder.getAdapterPosition() == -1) {
        return;
      }

      if (!initiated) {
        init();
      }

      background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(),
          itemView.getBottom());
      background.draw(c);
      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
  };

}
