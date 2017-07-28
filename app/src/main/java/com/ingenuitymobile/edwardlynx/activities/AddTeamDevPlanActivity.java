package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.AddTeamPlanAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.PostTeamDevPlanBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.TeamDevPlanBodies;
import com.ingenuitymobile.edwardlynx.api.bodyparams.TeamDevPlanBody;
import com.ingenuitymobile.edwardlynx.api.models.TeamDevPlan;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.TeamDevPlanResponse;
import com.ingenuitymobile.edwardlynx.api.responses.TeamDevPlansResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.views.draggable.ItemTouchHelperAdapter;
import com.ingenuitymobile.edwardlynx.views.draggable.OnStartDragListener;
import com.ingenuitymobile.edwardlynx.views.draggable.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;

import rx.Subscriber;

/**
 * Created by memengski on 7/9/17.
 */

public class AddTeamDevPlanActivity extends BaseActivity {

  private EditText   nameEdit;
  private RadioGroup radioGroup;
  private TextView   emptyText;

  private ItemTouchHelper itemTouchHelper;

  private ArrayList<TeamDevPlan> data;

  private AddTeamPlanAdapter adapter;

  public AddTeamDevPlanActivity() {
    data = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_team_plan);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(getString(R.string.manage_plans).toUpperCase());

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    nameEdit = (EditText) findViewById(R.id.edit_name);
    radioGroup = (RadioGroup) findViewById(R.id.radiogroup_type);
    emptyText = (TextView) findViewById(R.id.text_empty_state);

    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));

    adapter = new AddTeamPlanAdapter(data, listener);
    recyclerView.setAdapter(adapter);

    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemTouchHelperAdapter);
    itemTouchHelper = new ItemTouchHelper(callback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

    setDefaultState();
  }

  private void getData() {
    subscription.add(Shared.apiClient.getTeamCategories(new Subscriber<TeamDevPlansResponse>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted ");
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
      }

      @Override
      public void onNext(TeamDevPlansResponse response) {
        data.clear();
        data.addAll(response.items);
        notifyAdapter();
      }
    }));
  }

  private void deleteTeamPlan(final int position) {
    final TeamDevPlan teamDevPlan = data.get(position);

    progressDialog.show();
    subscription.add(Shared.apiClient.deleteTeamCategory(teamDevPlan.id,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            progressDialog.dismiss();
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
            progressDialog.dismiss();
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onNext(Response response) {
            data.remove(position);
            adapter.notifyItemRemoved(position);
          }
        }));
  }

  private void notifyAdapter() {
    adapter.notifyDataSetChanged();
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
  }

  private void setDefaultState() {
    nameEdit.setText("");
    ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
  }

  public void addTeamPlan(View v) {
    final String name = nameEdit.getText().toString();
    String language = "";
    for (int i = 0; i < radioGroup.getChildCount(); i++) {
      RadioButton radioButton = ((RadioButton) radioGroup.getChildAt(i));

      if (radioButton.isChecked()) {
        language = (String) radioButton.getTag();
        break;
      }
    }

    if (TextUtils.isEmpty(name)) {
      Toast.makeText(context, getString(R.string.name_required), Toast.LENGTH_SHORT).show();
      nameEdit.requestFocus();
      return;
    }

    if (TextUtils.isEmpty(language)) {
      Toast.makeText(context, getString(R.string.language_required), Toast.LENGTH_SHORT).show();
      radioGroup.requestFocus();
      return;
    }

    hideKeyboard(nameEdit);

    PostTeamDevPlanBody body = new PostTeamDevPlanBody();
    body.lang = language;
    body.name = name;

    LogUtil.e("AAA " + body.toString());
    progressDialog.show();
    subscription.add(Shared.apiClient.postTeamCategory(body, new Subscriber<TeamDevPlanResponse>() {
      @Override
      public void onCompleted() {
        progressDialog.dismiss();
        notifyAdapter();
        setDefaultState();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
        progressDialog.dismiss();
      }

      @Override
      public void onNext(TeamDevPlanResponse response) {
        TeamDevPlan teamDevPlan = new TeamDevPlan();
        teamDevPlan.id = response.id;
        teamDevPlan.categoryId = response.categoryId;
        teamDevPlan.ownerId = response.ownerId;
        teamDevPlan.name = response.name;
        teamDevPlan.visible = response.visible ? 1 : 0;
        teamDevPlan.position = response.position;
        teamDevPlan.progress = response.progress;
        teamDevPlan.checked = response.checked;
        teamDevPlan.goals = response.goals;
        data.add(0, teamDevPlan);

        Toast.makeText(
            context,
            getString(R.string.successfully_created_team_plan),
            Toast.LENGTH_SHORT
        ).show();
      }
    }));
  }

  public void update(View v) {
    TeamDevPlanBodies bodies = new TeamDevPlanBodies();
    bodies.items = new ArrayList<>();

    for (int x = 0; x < data.size(); x++) {
      TeamDevPlan teamDevPlan = data.get(x);
      bodies.items.add(new TeamDevPlanBody(teamDevPlan.id, x, teamDevPlan.visible));
    }

    LogUtil.e("AAA " + bodies.toString());
    progressDialog.show();
    subscription.add(
        Shared.apiClient.updateTeamCategories(bodies, new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            finish();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
            progressDialog.dismiss();
          }

          @Override
          public void onNext(Response response) {
            progressDialog.dismiss();
            Toast.makeText(
                context,
                getString(R.string.successfully_updated_team_plan),
                Toast.LENGTH_SHORT
            ).show();
          }
        }));
  }

  private OnStartDragListener listener = new OnStartDragListener() {
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
      itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onSelectedItem(int position) {
      data.get(position).visible = data.get(position).visible == 1 ? 0 : 1;
      notifyAdapter();
    }
  };

  private ItemTouchHelperAdapter itemTouchHelperAdapter = new ItemTouchHelperAdapter() {
    @Override
    public void onItemDismiss(int position) {
      deleteTeamPlan(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
      Collections.swap(data, fromPosition, toPosition);
      adapter.notifyItemMoved(fromPosition, toPosition);
      return true;
    }

    @Override
    public void onNotifyAdapter() {
      adapter.notifyDataSetChanged();
    }
  };
}
