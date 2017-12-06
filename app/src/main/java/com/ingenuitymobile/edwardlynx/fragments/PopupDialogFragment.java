package com.ingenuitymobile.edwardlynx.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.SurveyQuestionsActivity;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 09/02/2017.
 */

public class PopupDialogFragment extends DialogFragment {

  private String title, description, label;
  private boolean isGoal;

  /**
   * Fragment for displaying a popup dialog fragment
   * @param isGOal flag to determine if the fragment is for displaying goal
   * @param title the title of to be displayed in the fragment
   * @param description the description to be displayed in the fragment
   * @param label the label to be displayed in the fragment
   * @return created popup dialog fragment
   */
  public static PopupDialogFragment newInstance(boolean isGOal, String title, String description,
      String label) {
    PopupDialogFragment fragment = new PopupDialogFragment();
    fragment.title = title;
    fragment.isGoal = isGOal;
    fragment.description = description;
    fragment.label = label;
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

    LogUtil.e("AAA onCreateView");
    View v = inflater.inflate(R.layout.layout_popup_dialog, container, false);

    final TextView titleText = (TextView) v.findViewById(R.id.text_title);
    final TextView descriptionText = (TextView) v.findViewById(R.id.text_description);
    final TextView labelText = (TextView) v.findViewById(R.id.text_label);
    final ImageView imageView = (ImageView) v.findViewById(R.id.imageview);
    final ImageView imageView1 = (ImageView) v.findViewById(R.id.imageview1);

    imageView.setVisibility(isGoal ? View.GONE : View.VISIBLE);
    imageView1.setVisibility(isGoal ? View.VISIBLE : View.GONE);

    v.findViewById(R.id.text_close).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    titleText.setText(title);
    descriptionText.setText(description);
    labelText.setText(label);

    getDialog().setCanceledOnTouchOutside(false);
    return v;
  }

  @Override
  public void onDismiss(final DialogInterface dialog) {
    super.onDismiss(dialog);
    if (!isGoal) {
      final SurveyQuestionsActivity activity = (SurveyQuestionsActivity) getActivity();
      activity.finish();
    }
  }
}
