package com.ingenuitymobile.edwardlynx.activities;

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
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.InviteUserAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InviteUserParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserParam;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.StringUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski on 5/24/17.
 */

public class InvitePeopleActivity extends BaseActivity {

  private long     id;
  private String   evaluate;
  private EditText nameEdit;
  private EditText emailEdit;
  private Spinner  spinner;
  private TextView emptyText;

  private InviteUserAdapter    adapter;
  private ArrayList<UserParam> userParams;
  private String[]             roles;

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
    setTitle(getString(R.string.invite_people).toUpperCase());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

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

  private void notifyAdapter() {
    emptyText.setVisibility(userParams.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

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
              finish();
            }

            @Override
            public void onError(Throwable e) {
              progressDialog.dismiss();
              LogUtil.e("AAA onError " + e);
            }

            @Override
            public void onNext(Response response) {
              LogUtil.e("AAA onNext");
              Toast.makeText(
                  context,
                  getString(R.string.instant_feedback_created),
                  Toast.LENGTH_SHORT
              ).show();
            }
          }));
    }
  }

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
