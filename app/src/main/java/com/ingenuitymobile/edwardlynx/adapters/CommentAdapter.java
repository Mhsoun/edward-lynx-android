package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Comment;

import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

  private List<Comment> data;

  /**
   * Adapter for the comments to be loaded into the view.
   * @param data the list of comments
   */
  public CommentAdapter(List<Comment> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView     titleText;
    RecyclerView recyclerView;

    ViewHolder(View itemView) {
      super(itemView);
      titleText = (TextView) itemView.findViewById(R.id.text_title);
      recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);

      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
      recyclerView.setNestedScrollingEnabled(false);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_comment, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final Comment comment = data.get(position);

    holder.titleText.setText(comment.question);
    final CommentItemAdapter adapter = new CommentItemAdapter(comment.answer);
    holder.recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
