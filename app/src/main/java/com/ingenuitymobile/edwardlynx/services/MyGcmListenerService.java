package com.ingenuitymobile.edwardlynx.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.AnswerFeedbackActivity;
import com.ingenuitymobile.edwardlynx.activities.DevelopmentPlanDetailedActivity;
import com.ingenuitymobile.edwardlynx.activities.SplashActivity;
import com.ingenuitymobile.edwardlynx.activities.SurveyQuestionsActivity;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import static android.app.Notification.PRIORITY_MAX;

/**
 * Created by mEmEnG-sKi on 06/02/2017.
 */

public class MyGcmListenerService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // Check if message contains a data payload.
    LogUtil.e("AAA Receive notification");
    if (remoteMessage.getData().size() > 0) {
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

      final boolean isActive = LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

      switch (type) {
      case Shared.DEV_PLAN:
        intent = new Intent(this, DevelopmentPlanDetailedActivity.class);
        break;
      case Shared.INSTANT_FEEDBACK_REQUEST:
        intent = new Intent(this, AnswerFeedbackActivity.class);
        break;
      case Shared.SURVEY:
        intent = new Intent(this, SurveyQuestionsActivity.class);
        break;
      default:
        intent = new Intent(this, SplashActivity.class);
      }

      intent.putExtras(bundle);
      intent.putExtra("id", Long.parseLong(id));

      final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
          isActive ? PendingIntent.FLAG_CANCEL_CURRENT : PendingIntent.FLAG_ONE_SHOT);

      final NotificationManager notificationManager =
          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      final NotificationCompat.Builder notificationBuilder = getNotificationBuilder(isActive,
          pendingIntent, title, message);

      notificationManager.notify(0, notificationBuilder.build());
    }
  }

  public NotificationCompat.Builder getNotificationBuilder(boolean isActive,
      PendingIntent pendingIntent, String title, String message) {
    RemoteViews notificationView = new RemoteViews(
        getPackageName(),
        R.layout.view_custom_notiff
    );

    notificationView.setImageViewResource(R.id.imagenotileft, R.mipmap.ic_launcher);

    notificationView.setTextViewText(R.id.title, title);
    notificationView.setTextViewText(R.id.text, message);
    if (isActive) {
      return new NotificationCompat.Builder(this)
          .setContent(notificationView)
          .setPriority(PRIORITY_MAX)
          .setVibrate(new long[]{100, 100, 100, 100, 100})
          .setSmallIcon(R.mipmap.ic_launcher)
          .setWhen(System.currentTimeMillis())
          .setAutoCancel(true)
          .setContentTitle(title)
          .setContentText(message)
          .setContentIntent(pendingIntent);
    } else {
      return new NotificationCompat.Builder(this)
          .setSmallIcon(R.mipmap.ic_launcher)
          .setContentTitle(title)
          .setContentText(message)
          .setAutoCancel(false)
          .setContentIntent(pendingIntent);
    }
  }
}

