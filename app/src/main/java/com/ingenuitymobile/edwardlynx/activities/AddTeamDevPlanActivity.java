package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.AddTeamPlanAdapter;
import com.ingenuitymobile.edwardlynx.views.draggable.OnStartDragListener;
import com.ingenuitymobile.edwardlynx.views.draggable.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

/**
 * Created by memengski on 7/9/17.
 */

public class AddTeamDevPlanActivity extends BaseActivity implements OnStartDragListener {

  private EditText nameEdit;
  private TextView emptyText;

  private ItemTouchHelper itemTouchHelper;


  private ArrayList<String> data;

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

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();

    data.add("1");
    data.add("2");
    data.add("3");
    data.add("4");
    data.add("5");
    notifyAdapter();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
    itemTouchHelper.startDrag(viewHolder);
  }

  private void initViews() {
    nameEdit = (EditText) findViewById(R.id.edit_name);
    emptyText = (TextView) findViewById(R.id.text_empty_state);

    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));

    adapter = new AddTeamPlanAdapter(data, this);
    recyclerView.setAdapter(adapter);

    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
    itemTouchHelper = new ItemTouchHelper(callback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
  }

  private void notifyAdapter() {
    adapter.notifyDataSetChanged();
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
  }

  public void addTeamPlan(View v) {
    final String name = nameEdit.getText().toString();

    if (TextUtils.isEmpty(name)) {
      nameEdit.setError(getString(R.string.name_required));
      nameEdit.requestFocus();
      return;
    }

    hideKeyboard(nameEdit);

    // TODO api call
  }

  public void update(View v) {

  }
}
