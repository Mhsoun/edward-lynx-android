package com.ingenuitymobile.edwardlynx.utils;

import android.text.TextUtils;

/**
 * Created by mEmEnG-sKi on 15/12/2016.
 */

public class StringUtil {

  public static boolean isValidEmail(CharSequence email) {
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
        .matches();
  }
}
