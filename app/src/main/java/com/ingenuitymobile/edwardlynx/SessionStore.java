package com.ingenuitymobile.edwardlynx;

import android.content.Context;
import android.content.SharedPreferences;

import com.ingenuitymobile.edwardlynx.api.ApiClient;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 */

public class SessionStore {

  private static final String NAME = "edwardlynx-session";

  private static final String ACCESS_TOKEN  = "accessToken";
  private static final String REFRESH_TOKEN = "refreshToken";


  static boolean restore(ApiClient apiClient, Context context) {
    final SharedPreferences savedSession = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    apiClient.setRefreshToken(savedSession.getString(REFRESH_TOKEN, null));
    apiClient.setAccessToken(savedSession.getString(ACCESS_TOKEN, null));
    return apiClient.service != null;
  }

  public static boolean saveAccessToken(String accessToken, Context context) {
    Shared.apiClient.setAccessToken(accessToken);
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(ACCESS_TOKEN, accessToken);
    return editor.commit();
  }

  public static String restoreAccessToken(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(ACCESS_TOKEN, null);
  }

  public static boolean saveRefreshToken(String refreshToken, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(REFRESH_TOKEN, refreshToken);
    return editor.commit();
  }

  public static String restoreRefreshToken(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(REFRESH_TOKEN, null);
  }
}
