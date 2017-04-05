package com.ingenuitymobile.edwardlynx.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mEmEnG-sKi on 15/12/2016.
 */

public class StringUtil {

  public static boolean isValidEmail(CharSequence email) {
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
        .matches();
  }

  public static String getDeviceId(Context context) {
    String deviceId = "android:";
    deviceId = deviceId.concat(Build.MANUFACTURER.concat(":"));
    deviceId = deviceId.concat(Build.MODEL.concat(":"));
    deviceId = deviceId.concat(String.valueOf(Shared.user.id).concat(":"));
    deviceId = deviceId.concat(Settings.Secure.getString(context.getContentResolver(),
        Settings.Secure.ANDROID_ID));
    return deviceId;
  }

  private static final int SECOND_MILLIS = 1000;
  private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
  private static final int HOUR_MILLIS   = 60 * MINUTE_MILLIS;
  private static final int DAY_MILLIS    = 24 * HOUR_MILLIS;

  public static String getTimeAgo(long time, Context ctx) {
    if (time < 1000000000000L) {
      time *= 1000;
    }

    long now = System.currentTimeMillis();

    final long diff = now - time;
    if (diff < MINUTE_MILLIS) {
      return ctx.getResources().getString(R.string.just_now);
    } else if (diff < 2 * MINUTE_MILLIS) {
      return ctx.getResources().getString(R.string.a_minute_ago);
    } else if (diff < 50 * MINUTE_MILLIS) {
      return ctx.getResources().getString(R.string.minutes_ago, diff / MINUTE_MILLIS);
    } else if (diff < 90 * MINUTE_MILLIS) {
      return ctx.getResources().getString(R.string.an_hour_ago);
    } else if (diff < 24 * HOUR_MILLIS) {
      return ctx.getResources().getString(R.string.hours_ago, diff / HOUR_MILLIS);
    } else if (diff < 48 * HOUR_MILLIS) {
      return ctx.getResources().getString(R.string.yesterday);
    } else {
      final long days = diff / DAY_MILLIS;
      if (days == 0) {
        return ctx.getResources().getString(R.string.today);
      } else if (days < 0) {
        Date date = new Date(time);
        return new SimpleDateFormat("MMM dd, yyyy").format(date);
      }
      return ctx.getResources().getString(R.string.days_ago, days);
    }
  }
}
