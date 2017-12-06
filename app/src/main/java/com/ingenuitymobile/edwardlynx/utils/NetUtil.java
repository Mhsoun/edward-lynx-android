package com.ingenuitymobile.edwardlynx.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mEmEnG-sKi on 15/09/2016.
 * Utility class for checking network connectivity.
 */

public class NetUtil {

  public static boolean hasActiveConnection(Context context) {
    try {
      final NetworkInfo activeNetwork = getNetworkInfo(context);
      return activeNetwork != null && activeNetwork.isConnected();
    } catch (Exception e) {
      return false;
    }
  }

  private static NetworkInfo getNetworkInfo(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    return cm.getActiveNetworkInfo();
  }
}
