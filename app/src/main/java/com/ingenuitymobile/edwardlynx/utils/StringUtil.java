package com.ingenuitymobile.edwardlynx.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.ingenuitymobile.edwardlynx.Shared;

/**
 * Created by mEmEnG-sKi on 15/12/2016.
 * Utility class for strings.
 */

public class StringUtil {

  /**
   * checks if the passed string is a valid email address
   * @param email string to be checked
   * @return true if the passed string is valid
   */
  public static boolean isValidEmail(CharSequence email) {
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
        .matches();
  }

  /**
   * retrieves a string for identifying the device
   * @param context the context to be used
   * @return the generated device id
   */
  public static String getDeviceId(Context context) {
    String deviceId = "android:";
    deviceId = deviceId.concat(Build.MANUFACTURER.concat(":"));
    deviceId = deviceId.concat(Build.MODEL.concat(":"));
    deviceId = deviceId.concat(String.valueOf(Shared.user.id).concat(":"));
    deviceId = deviceId.concat(Settings.Secure.getString(context.getContentResolver(),
        Settings.Secure.ANDROID_ID));
    return deviceId;
  }
}
