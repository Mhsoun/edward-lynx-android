package com.ingenuitymobile.edwardlynx.utils;

import java.text.SimpleDateFormat;

/**
 * Created by memengski on 5/18/17.
 * Utility class for parsing display dates.
 */

public class DateUtil {

  public static SimpleDateFormat getAPIFormat() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  }

  public static SimpleDateFormat getDisplayFormat() {
    return new SimpleDateFormat("MMM dd, yyyy");
  }

  public static SimpleDateFormat getMonthFormat() {
    return new SimpleDateFormat("MMM");
  }

  public static SimpleDateFormat getDayFormat() {
    return new SimpleDateFormat("dd");
  }

  public static SimpleDateFormat getYearFormat() {
    return new SimpleDateFormat("yyyy");
  }
}
