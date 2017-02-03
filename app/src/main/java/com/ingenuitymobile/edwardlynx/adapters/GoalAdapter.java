package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class GoalAdapter extends
    ExpandableRecyclerAdapter<GoalAdapter.ParentView, GoalAdapter.ChildView> {

  private SimpleDateFormat format        = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  private SimpleDateFormat displayFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

  private OnSelectActionListener listener;

  public GoalAdapter(List<ParentListItem> data, OnSelectActionListener listener) {
    super(data);
    this.listener = listener;
  }

  class ParentView extends ParentViewHolder {
    RelativeLayout bodyLayout;
    TextView       nameText;
    TextView       descriptionText;
    TextView       countText;
    TextView       dateText;


    ParentView(View itemView) {
      super(itemView);
      bodyLayout = (RelativeLayout) itemView.findViewById(R.id.layout_body);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      descriptionText = (TextView) itemView.findViewById(R.id.text_description);
      countText = (TextView) itemView.findViewById(R.id.text_count);
      dateText = (TextView) itemView.findViewById(R.id.text_date);
    }

    @Override
    public void setExpanded(boolean expanded) {
      super.setExpanded(expanded);
      final Context ctx = bodyLayout.getContext();
      bodyLayout.setBackground(ctx.getResources().getDrawable(
          expanded ? R.drawable.bg_round_top_dashboard : R.drawable.bg_round_dashboard));
    }
  }

  class ChildView extends ChildViewHolder {
    LinearLayout bodyLayout;
    TextView     nameText;
    ImageView    imageView;

    ChildView(View itemView) {
      super(itemView);
      bodyLayout = (LinearLayout) itemView.findViewById(R.id.layout_body);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      imageView = (ImageView) itemView.findViewById(R.id.image_circle);
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
  public void onBindParentViewHolder(ParentView holder, int position,
      ParentListItem parentListItem) {
    final Goal goal = (Goal) parentListItem;

    holder.nameText.setText(goal.title);
    holder.descriptionText.setText(goal.description);

    try {
      Date date = format.parse(goal.dueDate);
      holder.dateText.setText(displayFormat.format(date));
    } catch (Exception e) {
      LogUtil.e("AAA " + e);
      holder.dateText.setText("");
    }

    final int size = goal.actions.size();
    int count = 0;
    if (goal.actions != null) {
      for (Action action : goal.actions) {
        if (action.checked == 1) {
          count++;
        }
      }
    }

    holder.countText.setText(count + " / " + size + " completed");
  }

  @Override
  public void onBindChildViewHolder(ChildView holder, int position,
      Object childListItem) {
    final Action action = (Action) childListItem;
    holder.nameText.setText(action.title);
    holder.imageView.setImageDrawable(holder.imageView.getContext().getResources()
        .getDrawable(action.checked == 1 ? R.drawable.ic_check : R.drawable.ic_circle));
    holder.bodyLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (action.checked == 0) {
          listener.onSelectedAction(action);
        }
      }
    });
  }

  public interface OnSelectActionListener {
    void onSelectedAction(Action action);
  }
}
