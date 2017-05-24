package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserParam;

import java.util.List;

/**
 * Created by memengski on 5/24/17.
 */

public class InviteUserAdapter extends RecyclerView.Adapter<InviteUserAdapter.ViewHolder> {

  private List<UserParam> data;


  public InviteUserAdapter(List<UserParam> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView emailText;
    TextView nameText;
    TextView roleText;

    ViewHolder(View itemView) {
      super(itemView);
      emailText = (TextView) itemView.findViewById(R.id.text_email);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      roleText = (TextView) itemView.findViewById(R.id.text_role);
    }
  }

  @Override
  public InviteUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new InviteUserAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_user_invite, parent, false));
  }

  @Override
  public void onBindViewHolder(final InviteUserAdapter.ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final UserParam user = data.get(position);

    holder.nameText.setText(user.name);
    holder.emailText.setText(user.email);
    holder.roleText.setText(user.getRole(context));
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
