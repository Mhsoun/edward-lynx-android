package com.ingenuitymobile.edwardlynx.activities;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.GoalAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.DevPlanBody;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.fragments.PopupDialogFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;
import com.ingenuitymobile.edwardlynx.views.fitchart.FitChart;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class DevelopmentPlanDetailedActivity extends BaseActivity {

  private long              id;
  private long              goalId;
  private boolean           isFromTeam;
  private ArrayList<String> expandedIds;

  private RecyclerView        recyclerView;
  private LinearLayoutManager manager;
  private GoalAdapter         adapter;
  private ArrayList<Goal>     data;
  private ProgressDialog      dialog;

  private FitChart progressFitChart;
  private TextView percentageText;
  private TextView goalsText;

  private PopupMenu popupGoal;
  private PopupMenu popupAction;

  private LinearLayout shareToManagerLayout;
  private CheckBox     shareToManagerCheckbox;

  public DevelopmentPlanDetailedActivity() {
    data = new ArrayList<>();
    goalId = 0L;
    expandedIds = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_development_plan_detailed);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);
    isFromTeam = getIntent().getBooleanExtra("isFromTeam", false);
    LogUtil.e("AAA id" + id);
    initViews();

    NotificationManager notificationManager =
        (NotificationManager) getApplicationContext()
            .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancel((int) id);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume() {
    super.onResume();
    getData();
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (popupGoal != null) {
      popupGoal.dismiss();
    }

    if (popupAction != null) {
      popupAction.dismiss();
    }
  }

  /**
   * initViews initializes views used in the activity
   */
  protected void initViews() {
    recyclerView = (RecyclerView) findViewById(R.id.list_development_plan);
    percentageText = (TextView) findViewById(R.id.text_percentage);
    progressFitChart = (FitChart) findViewById(R.id.fitchart_progress);
    goalsText = (TextView) findViewById(R.id.text_goals);
    shareToManagerLayout = (LinearLayout) findViewById(R.id.layout_share_to_manager);
    shareToManagerCheckbox = (CheckBox) findViewById(R.id.checkbox_is_anonymous);

    progressFitChart.setMinValue(0f);
    progressFitChart.setMaxValue(100f);

    manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);

    goalsText.setText(context.getString(R.string.number_of_goals, 0, 0));
    percentageText.setText(0 + "%");

    shareToManagerLayout.setVisibility(isFromTeam ? View.GONE : View.VISIBLE);

    findViewById(R.id.image_add).setVisibility(isFromTeam ? View.GONE : View.VISIBLE);
  }

  /**
   * generates a list of categories based on the goals
   * @param goals the goals which includes the categories
   * @return
   */
  private List<ParentListItem> generateCategories(List<Goal> goals) {
    List<ParentListItem> parentListItems = new ArrayList<>();
    for (Goal goal : goals) {
      final int size = goal.actions.size();

      int count = 0;
      for (Action actionGoal : goal.actions) {
        if (actionGoal.checked == 1) {
          count++;
        }
      }

      for (Action action : goal.actions) {
        action.isCompleted = count == size;
      }
      goal.isInitiallyExpanded = expandedIds.contains(String.valueOf(goal.id));

      parentListItems.add(goal);
    }
    return parentListItems;
  }

  /**
   * retrieves the development plan from the API based on the given id
   */
  private void getData() {
    subscription.add(Shared.apiClient.getDevelopmentPlan(id, new Subscriber<DevelopmentPlan>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted getData");
        if (dialog != null) {
          dialog.dismiss();
        }

        if (goalId != 0L) {
          for (final Goal goal : data) {
            if (goal.id == goalId && goal.checked == 1) {
              PopupDialogFragment dialogFragment = PopupDialogFragment.newInstance(true, goal.title,
                  getString(R.string.goal_completed), getString(R.string.label_popup));
              dialogFragment.show(getSupportFragmentManager(), "Popup");
              break;
            }
          }
        }
        goalId = 0L;
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError getData " + e);
        if (dialog != null) {
          dialog.dismiss();
        }
      }

      @Override
      public void onNext(DevelopmentPlan developmentPlan) {
        setTitle(developmentPlan.name);
        LogUtil.e("AAA onNext getData");
        data.clear();
        data.addAll(developmentPlan.goals);
        recyclerView.setAdapter(null);
        adapter = new GoalAdapter(generateCategories(new ArrayList<>(data)), listener, isFromTeam);
        adapter.setExpandCollapseListener(collapseListener);
        recyclerView.setAdapter(adapter);
        setData(developmentPlan);
      }
    }));
  }

  /**
   * sets the data for the given development plan
   * @param plan the development plan to be displayed
   */
  private void setData(DevelopmentPlan plan) {
    final int size = plan.goals.size();
    int count = 0;
    float progress = 0;
    int actionSize = 0;
    if (plan.goals != null) {
      for (Goal goal : plan.goals) {

        int actionCount = 0;
        if (goal.actions != null && !goal.actions.isEmpty()) {
          actionSize = goal.actions.size();
          for (Action action : goal.actions) {
            if (action.checked == 1) {
              actionCount++;
            }
          }
        }

        if (actionCount != 0 && actionCount == actionSize) {
          count++;
        }

        final float actionProgress =
            (actionCount != 0 && actionSize != 0) ?
                ((float) (actionCount) / (float) actionSize) : 0;

        progress = progress + actionProgress;
      }
      progress = ((progress) / (float) size) * 100;
    }

    progressFitChart.setValue(progress);
    percentageText.setText(((int) progress) + "%");
    goalsText.setText(context.getString(R.string.number_of_goals, count, size));
    shareToManagerCheckbox.setChecked(plan.shared == 1);
    shareToManagerCheckbox.setOnCheckedChangeListener(
        shareToManagerCheckbox.getVisibility() == View.VISIBLE ? onCheckedChangeListener : null);
  }

  /**
   * adds an action to the development plan
   * @param goalId the id of the goal to be added
   * @param param the action details to be added
   */
  private void addAction(long goalId, Action param) {
    dialog = ProgressDialog.show(context, "", getString(R.string.loading));

    LogUtil.e("AAA " + param.toString());
    subscription.add(Shared.apiClient.postActionPlan(id, goalId, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted patchAction");
            getData();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError patchAction " + e);
            if (dialog != null) {
              dialog.dismiss();
            }
          }

          @Override
          public void onNext(Response response) {
            Toast.makeText(context,
                getString(R.string.action_added), Toast.LENGTH_SHORT)
                .show();
          }
        }));
  }

  /**
   * updates the action of the goal of the development plan
   * @param goalId the id of the goal to be updated
   * @param actionId the id of the action to be updated
   * @param param the action details to be updated
   * @param isComplete the state of completion of the goal
   */
  private void patchAction(long goalId, long actionId, Action param, final boolean isComplete) {
    this.goalId = isComplete ? goalId : 0L;

    dialog = ProgressDialog.show(context, "", getString(R.string.loading));

    LogUtil.e("AAA " + param.toString());
    subscription.add(Shared.apiClient.updateActionPlan(id, goalId, actionId, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted patchAction");
            getData();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError patchAction " + e);
            if (dialog != null) {
              dialog.dismiss();
            }
          }

          @Override
          public void onNext(Response response) {
            if (isComplete) {
              Toast.makeText(context,
                  getString(R.string.action_updated), Toast.LENGTH_SHORT)
                  .show();
            }
          }
        }));
  }

  /**
   * deletes the goal from the development plan
   * @param goalId the id of the goal to be deleted
   */
  private void deleteGoal(long goalId) {
    progressDialog.show();
    subscription.add(Shared.apiClient.deleteDevelopmentPlanGoal(id, goalId,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            progressDialog.dismiss();
            onResume();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError patchAction " + e);
            if (progressDialog != null) {
              progressDialog.dismiss();
            }
          }

          @Override
          public void onNext(Response response) {
            Toast.makeText(context,
                getString(R.string.goal_delete), Toast.LENGTH_SHORT)
                .show();
          }
        }));
  }

  /**
   * deletes the action from the goal
   * @param goalId the id of the goal in which the action is to be deleted from
   * @param actionId the id of the action to be deleted from the goal
   */
  private void deleteAction(long goalId, long actionId) {
    progressDialog.show();
    subscription.add(Shared.apiClient.deleteActionPlan(id, goalId, actionId,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            progressDialog.dismiss();
            onResume();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError patchAction " + e);
            if (progressDialog != null) {
              progressDialog.dismiss();
            }
          }

          @Override
          public void onNext(Response response) {
            Toast.makeText(context,
                getString(R.string.action_delete), Toast.LENGTH_SHORT)
                .show();
          }
        }));
  }

  /**
   * retrieves the id of the goal based on the given action
   * @param action the action in which the goal id is to be extracted
   * @return
   */
  private long getGoalId(Action action) {
    for (final Goal goal : data) {
      for (Action acn : goal.actions) {
        if (acn.id == action.id) {
          return goal.id;
        }
      }
    }
    return 0L;
  }

  /**
   * opens the CreateDetailedDevelopmentPlanActivity to edit a goal
   * @param goal the goal to be edited
   */
  private void editGoal(Goal goal) {
    Intent intent = new Intent(context, CreateDetailedDevelopmentPlanActivity.class);
    intent.putExtra("goal_param_body", goal.toString());
    intent.putExtra("planId", id);
    startActivity(intent);
  }

  /**
   * deletes the goal
   * @param goal the goal to be deleted
   */
  private void deleteGoal(final Goal goal) {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
    alertBuilder.setTitle(getString(R.string.confirmation));
    alertBuilder.setMessage(getString(R.string.delete_goal_message, goal.title));
    alertBuilder.setPositiveButton(getString(R.string.delete_text),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            deleteGoal(goal.id);
          }
        });
    alertBuilder.setNegativeButton(getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    alertBuilder.create().show();
  }

  /**
   * updates the action
   * @param action the action to be updated
   */
  public void updateAction(final Action action) {
    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.VERTICAL);

    final EditText nameEdit = new EditText(context);
    nameEdit.setHint(getString(R.string.name));
    nameEdit.setText(action.title);
    nameEdit.setTextColor(getResources().getColor(R.color.black));
    nameEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
    nameEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    nameEdit.setSelection(nameEdit.getText().length());
    layout.addView(nameEdit);

    final AlertDialog alertDialog = new AlertDialog.Builder(context)
        .setTitle(getString(R.string.update_action))
        .setMessage(getString(R.string.enter_new_name_for))
        .setPositiveButton(getString(R.string.update), null)
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

                if (TextUtils.isEmpty(name)) {
                  nameEdit.setError(getString(R.string.name_required));
                } else {
                  dialogInterface.dismiss();

                  Action param = new Action();
                  param.title = name;
                  param.checked = action.checked;
                  param.position = action.position;
                  patchAction(getGoalId(action), action.id, param, false);
                }
              }
            });
      }
    });
    alertDialog.show();
  }

  /**
   * deletes the action
   * @param action the action to be deleted
   */
  public void deleteAction(final Action action) {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
    alertBuilder.setTitle(getString(R.string.confirmation));
    alertBuilder.setMessage(getString(R.string.delte_action_message, action.title));
    alertBuilder.setPositiveButton(getString(R.string.delete_text),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            deleteAction(getGoalId(action), action.id);
          }
        });
    alertBuilder.setNegativeButton(getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    alertBuilder.create().show();
  }

  /**
   * opens the CreateDetailedDevelopmentPlanActivity to create a goal
   * @param v
   */
  public void addGoal(View v) {
    Intent intent = new Intent(context, CreateDetailedDevelopmentPlanActivity.class);
    intent.putExtra("planId", id);
    intent.putExtra("position", (data.size() + 1));
    startActivity(intent);
  }

  /**
   * listener for the sharing of the development plan to manager
   */
  private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
          dialog = ProgressDialog.show(context, "", getString(R.string.loading));

          DevPlanBody body = new DevPlanBody();
          body.shared = isChecked ? 1 : 0;

          subscription.add(Shared.apiClient.updateDevelopmentPlan(id, body,
              new Subscriber<DevelopmentPlan>() {
                @Override
                public void onCompleted() {
                  LogUtil.e("AAA onCompleted getData");
                  if (dialog != null) {
                    dialog.dismiss();
                  }
                }

                @Override
                public void onError(Throwable e) {
                  LogUtil.e("AAA onError getData " + e);
                  if (dialog != null) {
                    dialog.dismiss();
                  }
                }

                @Override
                public void onNext(DevelopmentPlan developmentPlan) {
                  Toast.makeText(context,
                      getString(isChecked ?
                          R.string.shared_to_manager : R.string.not_shared_to_manager),
                      Toast.LENGTH_SHORT)
                      .show();
                }
              }));
        }
      };

  /**
   * listener for the expandable recycler when an item is collapsed
   */
  private ExpandableRecyclerAdapter.ExpandCollapseListener collapseListener =
      new ExpandableRecyclerAdapter.ExpandCollapseListener() {
        @Override
        public void onListItemExpanded(int position) {
          expandedIds.add(String.valueOf(data.get(position).id));
          manager.scrollToPositionWithOffset(position, 0);
        }

        @Override
        public void onListItemCollapsed(int position) {
          expandedIds.remove(String.valueOf(data.get(position).id));
        }
      };

  /**
   * listener for the selection action of the goals list
   */
  private GoalAdapter.OnSelectActionListener listener =
      new GoalAdapter.OnSelectActionListener() {

        @Override
        public void onPopupGoal(final Goal goal, View view) {
          popupGoal = new PopupMenu(context, view);
          popupGoal.inflate(R.menu.menu_option_goal);
          popupGoal.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              switch (item.getItemId()) {
              case R.id.update_goal:
                editGoal(goal);
                break;
              case R.id.delete_goal:
                deleteGoal(goal);
                break;
              }
              return false;
            }
          });
          popupGoal.show();
        }

        @Override
        public void onPopupAction(final Action action, View view) {
          popupAction = new PopupMenu(context, view);
          popupAction.inflate(R.menu.menu_option_action);
          popupAction.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              switch (item.getItemId()) {
              case R.id.update_action:
                updateAction(action);
                break;
              case R.id.delete_action:
                deleteAction(action);
                break;
              }
              return false;
            }
          });
          popupAction.show();
        }

        @Override
        public void onSelectedAction(final Action action) {
          AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
          alertBuilder.setTitle(getString(R.string.confirmation));
          alertBuilder.setMessage(getString(R.string.complete_action_message, action.title));
          alertBuilder.setPositiveButton(getString(R.string.complete),
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  Action param = new Action();
                  param.checked = 1;
                  param.position = action.position;
                  patchAction(getGoalId(action), action.id, param, true);
                }
              });
          alertBuilder.setNegativeButton(getString(R.string.cancel),
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
                }
              });
          alertBuilder.create().show();
        }

        @Override
        public void onAddGoal(final Action action) {
          LinearLayout layout = new LinearLayout(context);
          layout.setOrientation(LinearLayout.VERTICAL);

          final EditText nameEdit = new EditText(context);
          nameEdit.setHint(getString(R.string.name));
          nameEdit.setTextColor(getResources().getColor(R.color.black));
          nameEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
          nameEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
          nameEdit.setSelection(nameEdit.getText().length());
          layout.addView(nameEdit);

          final AlertDialog alertDialog = new AlertDialog.Builder(context)
              .setTitle(getString(R.string.create_action))
              .setMessage(getString(R.string.enter_new_action))
              .setPositiveButton(getString(R.string.add_action), null)
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

                      if (TextUtils.isEmpty(name)) {
                        nameEdit.setError(getString(R.string.name_required));
                      } else {
                        dialogInterface.dismiss();

                        Action param = new Action();
                        param.title = name;
                        param.checked = 0;
                        param.position = action.position;
                        addAction(action.id, param);
                      }
                    }
                  });
            }
          });
          alertDialog.show();
        }
      };
}
