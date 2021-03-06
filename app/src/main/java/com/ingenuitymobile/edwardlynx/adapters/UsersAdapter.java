package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

  /**
   * Adapter for displaying list of users
   * @param data the list of users to be displayed
   * @param ids the list of ids linked to the users
   * @param listener the listener for selecting a user
   */
  public UsersAdapter(List<User> data, List<String> ids, OnSelectUserListener listener) {
    super();
    this.data = data;
    this.ids = ids;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView  emailText;
    TextView  nameText;
    ImageView checkImage;

    ViewHolder(View itemView) {
      super(itemView);
      emailText = (TextView) itemView.findViewById(R.id.text_email);
      nameText = (TextView) itemView.findViewById(R.id.text_name);
      checkImage = (ImageView) itemView.findViewById(R.id.image_check);
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
    holder.nameText.setTextColor(context.getResources()
        .getColor(user.isDisabled ? R.color.unfinished_status : R.color.white));
    holder.emailText.setTextColor(context.getResources()
        .getColor(user.isDisabled ? R.color.unfinished_status : R.color.white));

    holder.checkImage.setVisibility(
        ids.contains(String.valueOf(user.id)) || user.isDisabled ? View.VISIBLE : View.GONE);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!user.isDisabled) {
          final boolean selected = holder.checkImage.getVisibility() == View.VISIBLE;
          holder.checkImage.setVisibility(!selected ? View.VISIBLE : View.GONE);
          listener.onSelect(String.valueOf(user.id), !selected);
        }
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
