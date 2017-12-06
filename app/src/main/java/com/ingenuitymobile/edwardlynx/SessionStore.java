package com.ingenuitymobile.edwardlynx;

import android.content.Context;
import android.content.SharedPreferences;

import com.ingenuitymobile.edwardlynx.api.ApiClient;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 * Utility class containing static methods for different saving and retrieving of important
 * application data.
 */

public class SessionStore {

  private static final String NAME = "edwardlynx-session";

  private static final String ACCESS_TOKEN   = "accessToken";
  private static final String REFRESH_TOKEN  = "refreshToken";
  private static final String FIREBASE_TOKEN = "firebaseToken";
  private static final String USERNAME       = "username";
  private static final String PASSWORD       = "password";


  static boolean restore(ApiClient apiClient, Context context) {
    final SharedPreferences savedSession = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    apiClient.setRefreshToken(
        savedSession.getString(USERNAME, null),
        savedSession.getString(PASSWORD, null)
    );
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

  public static boolean saveUsername(String username, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(USERNAME, username);
    return editor.commit();
  }

  public static String restoreUsername(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(USERNAME, "");
  }

  public static boolean savePassword(String password, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(PASSWORD, password);
    return editor.commit();
  }

  public static String restorePassword(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(PASSWORD, "");
  }

  public static boolean saveFirebaseToken(String accessToken, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(FIREBASE_TOKEN, accessToken);
    return editor.commit();
  }

  public static String restoreFirebaseToken(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(FIREBASE_TOKEN, null);
  }
}
