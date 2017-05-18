package com.ingenuitymobile.edwardlynx.utils;

import java.text.SimpleDateFormat;

/**
 * Created by memengski on 5/18/17.
 */

public class DateUtil {

  public static SimpleDateFormat getAPIFormat() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
  }

  public static SimpleDateFormat getDisplayFormat() {
    return new SimpleDateFormat("MMM dd, yyyy");
  }
}
