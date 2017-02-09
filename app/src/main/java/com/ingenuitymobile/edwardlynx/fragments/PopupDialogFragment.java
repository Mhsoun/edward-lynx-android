package com.ingenuitymobile.edwardlynx.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 09/02/2017.
 */

public class PopupDialogFragment extends DialogFragment {

  private String title, description, label;

  public static PopupDialogFragment newInstance(String title, String description, String label) {
    PopupDialogFragment fragment = new PopupDialogFragment();
    fragment.title = title;
    fragment.description = description;
    fragment.label = label;
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    LogUtil.e("AAA onCreateView");
    View v = inflater.inflate(R.layout.layout_popup_dialog, container, false);

    final TextView titleText = (TextView) v.findViewById(R.id.text_title);
    final TextView descriptionText = (TextView) v.findViewById(R.id.text_description);
    final TextView labelText = (TextView) v.findViewById(R.id.text_label);
    final ImageView imageView = (ImageView) v.findViewById(R.id.imageview);

    titleText.setText(title);
    descriptionText.setText(description);
    labelText.setText(label);
    return v;
  }

}
