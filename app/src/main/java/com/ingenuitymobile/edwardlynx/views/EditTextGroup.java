package com.ingenuitymobile.edwardlynx.views;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mEmEnG-sKi on 15/12/2016.
 * Custom view for group edit text.
 */

public class EditTextGroup {

  private EditText  editText;
  private ImageView imageView;
  private TextView  textView;

  public EditTextGroup(EditText editText, ImageView imageView, TextView textView) {
    this.editText = editText;
    this.imageView = imageView;
    this.textView = textView;
    init();
  }

  private void init() {
    editText.setText("");
    textView.setVisibility(View.INVISIBLE);
    imageView.setColorFilter(null);
  }

  public String getEditTextSting() {
    return editText.getText().toString();
  }

  public void removeError() {
    textView.setVisibility(View.INVISIBLE);
    imageView.setColorFilter(null);
  }

  public void setError(String text) {
    textView.setVisibility(View.VISIBLE);
    textView.setText(text);
    imageView.setColorFilter(Color.RED);
  }

  public void requestFocus() {
    editText.requestFocus();
  }
}
