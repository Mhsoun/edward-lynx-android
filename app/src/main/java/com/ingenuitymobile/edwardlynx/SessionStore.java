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

  /**
   * checks if there is a currently saved ApiClient
   * @param apiClient the ApiClient to be checked
   * @param context the context to be used
   * @return indication if there is a saved ApiClient
   */
  static boolean restore(ApiClient apiClient, Context context) {
    final SharedPreferences savedSession = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    apiClient.setRefreshToken(
        savedSession.getString(USERNAME, null),
        savedSession.getString(PASSWORD, null)
    );
    apiClient.setAccessToken(savedSession.getString(ACCESS_TOKEN, null));
    return apiClient.service != null;
  }

  /**
   * saves the access token returned by the API to local app data storage
   * @param accessToken the access token to be saved
   * @param context the context to be used
   * @return true if the access token is saved
   */
  public static boolean saveAccessToken(String accessToken, Context context) {
    Shared.apiClient.setAccessToken(accessToken);
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(ACCESS_TOKEN, accessToken);
    return editor.commit();
  }

  /**
   * retrieves the saved access token
   * @param context the context to be used
   * @return the access token string
   */
  public static String restoreAccessToken(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(ACCESS_TOKEN, null);
  }

  /**
   * saves the refresh token returned by the API to local app data storage
   * @param refreshToken the refresh token to be saved
   * @param context the context to be used
   * @return true if the refresh token is saved
   */
  public static boolean saveRefreshToken(String refreshToken, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(REFRESH_TOKEN, refreshToken);
    return editor.commit();
  }

  /**
   * retrieves the saved refresh token
   * @param context the context to be used
   * @return the refresh token string
   */
  public static String restoreRefreshToken(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(REFRESH_TOKEN, null);
  }

  /**
   * saves the username to local app data storage
   * @param username the username string
   * @param context the context to be used
   * @return true if the username is saved
   */
  public static boolean saveUsername(String username, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(USERNAME, username);
    return editor.commit();
  }

  /**
   * retrieves the saved username
   * @param context the context to be used
   * @return the saved username or empty string if none
   */
  public static String restoreUsername(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(USERNAME, "");
  }

  /**
   * saves the password to local app data storage
   * @param password the password to be saved
   * @param context the context to be used
   * @return true if the password is saved
   */
  public static boolean savePassword(String password, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(PASSWORD, password);
    return editor.commit();
  }

  /**
   * retrieves the saved password
   * @param context the context to be used
   * @return the saved password or empty string if none
   */
  public static String restorePassword(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(PASSWORD, "");
  }

  /**
   * saves the Firebase token to local app data storage
   * @param accessToken the Firebase token
   * @param context the context to be used
   * @return true if the Firebase token is saved
   */
  public static boolean saveFirebaseToken(String accessToken, Context context) {
    final SharedPreferences.Editor editor = context
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(FIREBASE_TOKEN, accessToken);
    return editor.commit();
  }

  /**
   * retrieves the saved Firebase token
   * @param context the context to be used
   * @return the Firebase token or null if none
   */
  public static String restoreFirebaseToken(Context context) {
    final SharedPreferences savedUserId = context.getSharedPreferences(NAME,
        Context.MODE_PRIVATE);
    return savedUserId.getString(FIREBASE_TOKEN, null);
  }
}
