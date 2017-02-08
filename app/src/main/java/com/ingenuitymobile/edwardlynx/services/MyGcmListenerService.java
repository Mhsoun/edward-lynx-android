package com.ingenuitymobile.edwardlynx.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.SplashActivity;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 06/02/2017.
 */

public class MyGcmListenerService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // Check if message contains a data payload.
    LogUtil.e("AAA Receive notification");
    if (remoteMessage.getData().size() > 0) {
      // delay
      try { Thread.sleep(3000);} catch (InterruptedException e) {}

      LogUtil.e("AAA id " + remoteMessage.getData().get("id"));
      LogUtil.e("AAA type " + remoteMessage.getData().get("type"));

      final String type = remoteMessage.getData().get("type");
      final String id = remoteMessage.getData().get("id");
      final String title = remoteMessage.getNotification().getTitle();
      final String message = remoteMessage.getNotification().getBody();

      LogUtil.e("AAA Notiff title:  " + title);
      LogUtil.e("AAA Notiff message: " + message);

      Intent intent = new Intent(Shared.UPDATE_DASHBOARD);


      Bundle bundle = new Bundle();
      bundle.putString("title", title);
      bundle.putString("message", message);
      bundle.putString("type", type);
      bundle.putString("id", id);

      intent.putExtras(bundle);
      if (!LocalBroadcastManager.getInstance(this).sendBroadcast(intent)) {
        intent = new Intent(this, SplashActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */,
            intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
      }
    }
  }
}

