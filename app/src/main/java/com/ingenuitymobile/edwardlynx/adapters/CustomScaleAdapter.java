package com.ingenuitymobile.edwardlynx.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 22/01/2017.
 */

public class CustomScaleAdapter extends RecyclerView.Adapter<CustomScaleAdapter.ViewHolder> {

  private List<String>     data;
  private OnDeleteListener listener;

  public CustomScaleAdapter(List<String> data, OnDeleteListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView  nameText;
    ImageView deleteImage;

    ViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_options);
      deleteImage = (ImageView) itemView.findViewById(R.id.image_delete);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CustomScaleAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_custom_scale, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {

    final String string = data.get(position);

    holder.nameText.setText(string);

    holder.deleteImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
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
