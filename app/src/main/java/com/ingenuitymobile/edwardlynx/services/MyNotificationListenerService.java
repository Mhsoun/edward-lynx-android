package com.ingenuitymobile.edwardlynx.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.ingenuitymobile.edwardlynx.utils.BadgeUtils;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by memengski on 6/15/17.
 */

@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyNotificationListenerService extends NotificationListenerService {

  @Override
  public void onCreate() {
    super.onCreate();
    LogUtil.e("AAA MyNotificationListenerService onCreate");
    int count = getNotificationCount();
    if (count <= 0) {
      BadgeUtils.clearBadge(this);
    } else {
      BadgeUtils.setBadge(this, count);
    }
  }

  @Override
  public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    if (sbn.getPackageName().equals(getPackageName())) {
      LogUtil.e("AAA MyNotificationListenerService onNotificationPosted");
      BadgeUtils.setBadge(this, getNotificationCount());
    }
  }

  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
    super.onNotificationRemoved(sbn);
    if (sbn.getPackageName().equals(getPackageName())) {
      LogUtil.e("AAA MyNotificationListenerService onNotificationRemoved");
      int count = getNotificationCount();
      if (count <= 0) {
        BadgeUtils.clearBadge(this);
      } else {
        BadgeUtils.setBadge(this, count);
      }
    }
  }

  private int getNotificationCount() {
    StatusBarNotification[] notifications = getActiveNotifications();

    int count = 0;
    if (notifications.length != 0) {
      for (StatusBarNotification notification : notifications) {
        if (notification.getPackageName().equals(getPackageName())) {
          count++;
        }
      }
    }
    return count;
  }
}
