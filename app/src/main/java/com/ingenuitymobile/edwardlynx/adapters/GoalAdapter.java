package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class GoalAdapter extends
    ExpandableRecyclerAdapter<GoalAdapter.ParentView, GoalAdapter.ChildView> {

  private OnSelectActionListener listener;
  private boolean                isFromTeam;

  /**
   * Adapter for the displaying the goals
   * @param data the list of parent list items and child goals
   * @param listener the listener for selecting an item
   * @param isFromTeam indicator if the goal is from team
   */
  public GoalAdapter(List<ParentListItem> data, OnSelectActionListener listener,
      boolean isFromTeam) {
    super(data);
    this.listener = listener;
    this.isFromTeam = isFromTeam;
  }

  class ParentView extends ParentViewHolder {
    RelativeLayout bodyLayout;
    TextView       nameText;
    TextView       descriptionText;
    TextView       countText;
    TextView       dateText;
    ProgressBar    progressBar;
    ImageView      optionImage;


    ParentView(View itemView) {
      super(itemView);
      bodyLayout = (RelativeLayout) itemView.findViewById(R.id.layout_body);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      countText = (TextView) itemView.findViewById(R.id.text_count);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
      progressBar = (ProgressBar) itemView.findViewById(R.id.progress_development);
      optionImage = (ImageView) itemView.findViewById(R.id.image_option);
    }

    @Override
    public void setExpanded(boolean expanded) {
      super.setExpanded(expanded);
      final Context ctx = bodyLayout.getContext();
      bodyLayout.setBackground(ctx.getResources().getDrawable(
          expanded ? R.drawable.bg_round_top_dashboard : R.drawable.bg_round_dev_plan));
    }
  }

  class ChildView extends ChildViewHolder {
    RelativeLayout bodyLayout;
    TextView       nameText;
    ImageView      imageView;
    ImageView      optionImage;
    TextView       addText;
    RelativeLayout detailsLayout;

    ChildView(View itemView) {
      super(itemView);
      bodyLayout = (RelativeLayout) itemView.findViewById(R.id.layout_body);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      imageView = (ImageView) itemView.findViewById(R.id.image_circle);
      optionImage = (ImageView) itemView.findViewById(R.id.image_option);
      detailsLayout = (RelativeLayout) itemView.findViewById(R.id.layout_details);
      addText = (TextView) itemView.findViewById(R.id.text_add);
    }
  }

  @Override
  public ParentView onCreateParentViewHolder(ViewGroup parent) {
    return new ParentView(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_goal, parent, false));
  }

  @Override
  public ChildView onCreateChildViewHolder(ViewGroup childViewGroup) {
    return new ChildView(LayoutInflater.from(childViewGroup.getContext())
        .inflate(R.layout.list_action_plan, childViewGroup, false));
  }

  @Override
  public void onBindParentViewHolder(final ParentView holder, int position,
      ParentListItem parentListItem) {
    final Goal goal = (Goal) parentListItem;
    final Context context = holder.itemView.getContext();

    holder.nameText.setText(goal.title);
    holder.descriptionText.setText(goal.description);

    final int size = goal.actions.size();
    int count = 0;
    if (goal.actions != null) {
      for (Action action : goal.actions) {
        if (action.checked == 1) {
          count++;
        }
      }
    }

    if (count != 0 && count == size) {
      holder.dateText.setText(context.getResources().getString(R.string.completed_text));
    } else {
      try {
        Date date = DateUtil.getAPIFormat().parse(goal.dueDate);
        holder.dateText.setText(
            context
                .getResources()
                .getString(R.string.due_date_goal,
                    DateUtil.getDisplayFormat().format(date)));
      } catch (Exception e) {
        LogUtil.e("AAA " + e);
        holder.dateText.setText("");
      }
    }

    holder.countText.setText(context.getString(R.string.completed_details, count, size));
    holder.bodyLayout.setSelected(count != 0 && count == size);

    holder.progressBar.setProgress((int) (((float) (count) / (float) size) * 100));
    holder.progressBar.setScaleY(3f);
    holder.progressBar
        .getProgressDrawable()
        .setColorFilter(context.getResources()
                .getColor(count != 0 && count == size
                    ? R.color.colorAccent : R.color.dev_plan_color),
            PorterDuff.Mode.SRC_IN);

    holder.optionImage.setVisibility(isFromTeam ? View.GONE : View.VISIBLE);
    holder.optionImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onPopupGoal(goal, holder.optionImage);
      }
    });
  }

  @Override
  public void onBindChildViewHolder(final ChildView holder, int position, Object childListItem) {
    final Action action = (Action) childListItem;
    final Context context = holder.itemView.getContext();

    holder.addText.setVisibility(action.
        isAddAction ? View.VISIBLE : View.GONE);
    holder.detailsLayout.setVisibility(action.isAddAction ? View.GONE : View.VISIBLE);
    holder.itemView.setVisibility(action.isAddAction && isFromTeam ? View.GONE : View.VISIBLE);

    holder.nameText.setText(action.title);
    holder.imageView.setImageDrawable(holder.imageView.getContext().getResources()
        .getDrawable(action.checked == 1 ? R.drawable.ic_check : R.drawable.ic_circle));

    holder.imageView.setColorFilter(
        ContextCompat.getColor(context, action.checked == 1 ? R.color.done_color : R.color.white));

    holder.bodyLayout.setSelected(action.isCompleted);
    holder.bodyLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (isFromTeam) {
          return;
        }
        if (action.isAddAction) {
          listener.onAddGoal(action);
        } else if (action.checked == 0) {
          listener.onSelectedAction(action);
        }
      }
    });

    holder.optionImage.setVisibility(isFromTeam ? View.GONE : View.VISIBLE);
    holder.optionImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onPopupAction(action, holder.optionImage);
      }
    });
  }

  public interface OnSelectActionListener {
    void onPopupGoal(Goal goal, View view);

    void onPopupAction(Action action, View view);

    void onSelectedAction(Action action);

    void onAddGoal(Action action);
  }
}
