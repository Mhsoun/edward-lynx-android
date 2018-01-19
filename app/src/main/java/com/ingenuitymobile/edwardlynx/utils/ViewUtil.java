package com.ingenuitymobile.edwardlynx.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;

/**
 * Created by mEmEnG-sKi on 01/09/2016.
 * Utility class for helping in displaying messages.
 */

public class ViewUtil {

  /**
   * displays an alert window
   * @param context the context to be used
   * @param title the title of the alert window
   * @param body the body message of the alert window
   */
  public static void showAlert(Context context, String title, String body) {
    showAlert(context, title, body, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
  }

  /**
   * displays an alert window
   * @param context the context to be used
   * @param title the title of the alert window
   * @param body the body message of the alert window
   * @param dialogInterface the dialog interface for the positive button
   */
  public static void showAlert(Context context, String title, String body,
      DialogInterface.OnClickListener dialogInterface) {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
    alertBuilder.setTitle(title);
    alertBuilder.setMessage(body);
    alertBuilder.setPositiveButton(context.getString(android.R.string.ok), dialogInterface);
    alertBuilder.create().show();
  }

  /**
   * converter of density independent pixels to its equivalent pixels
   * @param dp the input density independent pixels
   * @param resources the resources to be used
   * @return the converted pixel
   */
  public static int dpToPx(float dp, Resources resources) {
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        resources.getDisplayMetrics());
    return (int) px;
  }
}
