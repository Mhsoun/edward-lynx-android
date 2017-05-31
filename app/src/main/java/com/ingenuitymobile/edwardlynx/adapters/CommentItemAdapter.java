package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.CommentItem;

import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.ViewHolder> {

  private List<CommentItem> data;

  public CommentItemAdapter(List<CommentItem> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView commentText;

    ViewHolder(View itemView) {
      super(itemView);
      commentText = (TextView) itemView.findViewById(R.id.text_comment);
    }
  }


  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_comment_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final CommentItem commentItem = data.get(position);

    holder.commentText.setText(commentItem.text);
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
