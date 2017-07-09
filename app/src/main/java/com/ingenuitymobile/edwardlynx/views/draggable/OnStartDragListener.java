package com.ingenuitymobile.edwardlynx.views.draggable;

import android.support.v7.widget.RecyclerView;

/**
 * Created by memengski on 7/9/17.
 */

public interface OnStartDragListener {

  /**
   * Called when a view is requesting a start of a drag.
   *
   * @param viewHolder The holder of the view to drag.
   */
  void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
