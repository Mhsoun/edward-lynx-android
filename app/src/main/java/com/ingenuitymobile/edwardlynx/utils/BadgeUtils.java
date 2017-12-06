package com.ingenuitymobile.edwardlynx.utils;

import android.content.Context;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by memengski on 5/23/17.
 * Utility class for setting launcher unread notification count.
 */

public class BadgeUtils {

  public static void setBadge(Context context, int count) {
    ShortcutBadger.applyCount(context, count);
  }

  public static void clearBadge(Context context) {
    ShortcutBadger.removeCount(context);
  }
}

