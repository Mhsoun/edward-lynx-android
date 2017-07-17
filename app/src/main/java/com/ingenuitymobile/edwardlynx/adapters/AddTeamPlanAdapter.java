package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.TeamDevPlan;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.views.draggable.ItemTouchHelperAdapter;
import com.ingenuitymobile.edwardlynx.views.draggable.OnStartDragListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by memengski on 7/9/17.
 */

public class AddTeamPlanAdapter extends
    RecyclerView.Adapter<AddTeamPlanAdapter.ViewHolder> {

  private List<TeamDevPlan>   data;
  private OnStartDragListener onStartDragListener;

  public AddTeamPlanAdapter(List<TeamDevPlan> data, OnStartDragListener onStartDragListener) {
    super();
    this.data = data;
    this.onStartDragListener = onStartDragListener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView  nameText;
    ImageView circleImage;
    ImageView handleImage;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      circleImage = (ImageView) itemView.findViewById(R.id.image_circle);
      handleImage = (ImageView) itemView.findViewById(R.id.image_reorder);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_team_plan, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    final Context context = holder.itemView.getContext();
    final TeamDevPlan teamPlan = data.get(position);

    holder.nameText.setText(teamPlan.name);

    holder.circleImage.setVisibility(teamPlan.visible == 1 ? View.VISIBLE : View.INVISIBLE);

    holder.handleImage.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
          onStartDragListener.onStartDrag(holder);
        }
        return true;
      }
    });

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onStartDragListener.onSelectedItem(position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
