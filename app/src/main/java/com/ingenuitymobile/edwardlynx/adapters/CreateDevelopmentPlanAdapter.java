package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.CreateDetailedDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.activities.CreateDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.api.models.Goal;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class CreateDevelopmentPlanAdapter extends
    RecyclerView.Adapter<CreateDevelopmentPlanAdapter.ViewHolder> {

  private List<Goal>                    data;
  private CreateDevelopmentPlanActivity activity;

  /**
   * Adapter for creating a development plan.
   * @param data the list of goals to be added
   * @param activity the activity context
   */
  public CreateDevelopmentPlanAdapter(List<Goal> data,
      CreateDevelopmentPlanActivity activity) {
    super();
    this.data = data;
    this.activity = activity;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView nameText;
    TextView numberText;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      numberText = (TextView) itemView.findViewById(R.id.text_number);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CreateDevelopmentPlanAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_create_development_plan, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    final Goal param = data.get(position);
    holder.nameText.setText(param.title);
    holder.numberText.setText(position + 1 + ". ");

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(activity, CreateDetailedDevelopmentPlanActivity.class);
        intent.putExtra("goal_param_body", param.toString());
        intent.putExtra("index", position);
        activity.startActivityForResult(intent, activity.REQUEST_CODE);
      }
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
