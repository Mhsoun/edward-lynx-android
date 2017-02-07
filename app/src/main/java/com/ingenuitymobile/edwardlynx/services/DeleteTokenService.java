package com.ingenuitymobile.edwardlynx.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ingenuitymobile.edwardlynx.SessionStore;

import java.io.IOException;

/**
 * Created by mEmEnG-sKi on 06/02/2017.
 */

public class DeleteTokenService extends IntentService {
  public static final String TAG = DeleteTokenService.class.getSimpleName();

  public DeleteTokenService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    try {
      // Resets Instance ID and revokes all tokens.
      FirebaseInstanceId.getInstance().deleteInstanceId();

      // Clear current saved token
      SessionStore.saveFirebaseToken("", this);
      FirebaseInstanceId.getInstance().getToken();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


