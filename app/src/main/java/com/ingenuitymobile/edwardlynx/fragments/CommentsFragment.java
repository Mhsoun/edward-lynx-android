package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.adapters.CommentAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Comment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 5/31/17.
 */

public class CommentsFragment extends BaseFragment {

  private View mainView;

  private List<Comment> comments;

  private CommentAdapter adapter;

  /**
   * Fragment to display the comments in the survey reports.
   */
  public CommentsFragment() {
    comments = new ArrayList<>();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (mainView != null) {
      ViewGroup parent = (ViewGroup) mainView.getParent();
      if (parent == null) {
        parent = container;
      }
      parent.removeView(mainView);
      LogUtil.e("AAA onCreateView CommentsFragment");
      return mainView;
    }

    mainView = inflater.inflate(R.layout.fragment_comments, container, false);
    LogUtil.e("AAA onCreateView CommentsFragment");
    initViews();
    setData();
    return mainView;
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    final RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new CommentAdapter(comments);
    recyclerView.setAdapter(adapter);
  }

  /**
   * sets the data to be used in the fragment
   * @param comments the list of comments to be displayed
   */
  public void setDataSet(List<Comment> comments) {
    this.comments = comments;
    if (mainView != null) {
      setData();
    }
  }

  /**
   * updates the data to be displayed in the view
   */
  private void setData() {
    adapter.notifyDataSetChanged();
    mainView.findViewById(R.id.text_empty).setVisibility(
        comments.isEmpty() ? View.VISIBLE : View.GONE);
  }
}
