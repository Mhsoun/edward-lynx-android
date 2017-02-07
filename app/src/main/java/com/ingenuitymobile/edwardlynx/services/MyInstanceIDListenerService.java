package com.ingenuitymobile.edwardlynx.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 06/02/2017.
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    LogUtil.e("AAA Refreshed token: " + refreshedToken);
    SessionStore.saveFirebaseToken(refreshedToken, this);
  }
}
