package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ActionParam;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class CreateActionPlanAdapter extends
    RecyclerView.Adapter<CreateActionPlanAdapter.ViewHolder> {

  private List<ActionParam> data;

  public CreateActionPlanAdapter(List<ActionParam> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    EditText nameText;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (EditText) itemView.findViewById(R.id.edit_name);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_create_action_plan, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    final ActionParam param = data.get(position);
    holder.nameText.setText(param.title);
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
