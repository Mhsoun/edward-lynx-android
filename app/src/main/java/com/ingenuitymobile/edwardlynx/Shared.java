package com.ingenuitymobile.edwardlynx;

import android.content.Context;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.models.Category;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

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
    final String basUrl = context
        .getResources()
        .getString(BuildConfig.DEBUG ? R.string.staging_url : R.string.live_url);

    apiClient = new ApiClient(
        consumerKey,
        consumerSecret,
        basUrl,
        new RefreshTokenListener(context),
        new DisplayErrorListener(context));

    SessionStore.restore(apiClient, context);
    user = new User();
  }

  private static class DisplayErrorListener implements ApiClient.OnDisplayErrorListener {

    private Context context;

    DisplayErrorListener(Context context) {
      this.context = context;
    }

    @Override
    public void onDisplayError(Throwable e) {
      String text = context.getString(R.string.no_internet_connection);

      if (NetUtil.hasActiveConnection(context)) {
        Response response = (Response) ((RetrofitError) e).getBody();
        if (response != null) {
          text = response.message;
        } else {
          text = context.getString(R.string.cant_connect);
        }
      }
      Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
  }

  private static class RefreshTokenListener implements ApiClient.OnRefreshTokenListener {

    private Context context;

    RefreshTokenListener(Context context) {
      this.context = context;
    }

    @Override
    public void onRefreshToken(Authentication authentication) {
      SessionStore.saveRefreshToken(authentication.refresh_token, context);
      SessionStore.saveAccessToken(authentication.accessToken, context);
    }
  }
}
