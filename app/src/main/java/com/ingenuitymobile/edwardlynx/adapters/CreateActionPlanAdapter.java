package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Action;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class CreateActionPlanAdapter extends
    RecyclerView.Adapter<CreateActionPlanAdapter.ViewHolder> {

  private List<Action>     data;
  private OnDeleteListener listener;

  /**
   * Adapter for creating an action plan.
   * @param data the list of actions
   * @param listener the listener for deleting an action plan
   */
  public CreateActionPlanAdapter(List<Action> data, OnDeleteListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    EditText  nameText;
    ImageView closeImage;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (EditText) itemView.findViewById(R.id.edit_name);
      closeImage = (ImageView) itemView.findViewById(R.id.image_close);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_create_action_plan, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    final Action param = data.get(position);
    holder.nameText.setText(param.title);
    holder.closeImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onDelete(position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public interface OnDeleteListener {
    void onDelete(int position);
  }
}
