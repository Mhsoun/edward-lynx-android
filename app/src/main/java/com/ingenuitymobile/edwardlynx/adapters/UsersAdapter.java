package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.User;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 11/01/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

  private List<User>   data;
  private List<String> ids;

  private OnSelectUserListener listener;

  public UsersAdapter(List<User> data, List<String> ids, OnSelectUserListener listener) {
    super();
    this.data = data;
    this.ids = ids;
    this.listener = listener;
  }


  class ViewHolder extends RecyclerView.ViewHolder {
    TextView          emailText;
    TextView          nameText;
    AppCompatCheckBox selectedCheckbox;

    ViewHolder(View itemView) {
      super(itemView);
      emailText = (TextView) itemView.findViewById(R.id.text_email);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      selectedCheckbox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox_selected);
    }
  }


  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_users, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final User user = data.get(position);

    holder.nameText.setText(user.name);
    holder.emailText.setText(user.email);

    holder.selectedCheckbox.setOnCheckedChangeListener(null);
    holder.selectedCheckbox.setChecked(ids.contains(String.valueOf(user.id)));

    holder.selectedCheckbox.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean selected) {
            listener.onSelect(String.valueOf(user.id), selected);
          }
        });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public interface OnSelectUserListener {
    void onSelect(String id, boolean isSelected);
  }
}
