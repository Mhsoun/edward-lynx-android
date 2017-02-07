package com.ingenuitymobile.edwardlynx;

import android.content.Context;

import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.models.Category;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 */

public class Shared {
  public static final String UPDATE_DASHBOARD = "update-dashboard";

  public static final String DEV_PLAN                 = "dev-plan";
  public static final String INSTANT_FEEDBACK_REQUEST = "instant-request";
  public static final String SURVEY                   = "survey";

  public static ApiClient apiClient;

  public static User           user;
  public static List<Category> categories;

  public static void init(final Context context) {
    categories = new ArrayList<>();
    final String consumerKey = context.getResources().getString(R.string.client_id);
    final String consumerSecret = context.getResources().getString(R.string.client_secret);
    final String basUrl = context.getResources()
        .getString(BuildConfig.DEBUG ? R.string.staging_url : R.string.live_url);
    apiClient = new ApiClient(consumerKey, consumerSecret, basUrl,
        new ApiClient.OnRefreshTokenListener() {
          @Override
          public void onRefreshToken(Authentication authentication) {
            SessionStore.saveRefreshToken(authentication.refresh_token, context);
            SessionStore.saveAccessToken(authentication.accessToken, context);
          }
        });

    SessionStore.restore(apiClient, context);
    user = new User();
  }
}
