package com.ingenuitymobile.edwardlynx;

import android.content.Context;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.models.Category;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 * Utility class for storing shared strings and static methods and classes.
 */

public class Shared {
  public static final String UPDATE_DASHBOARD = "update-dashboard";

  public static final String DEV_PLAN                 = "dev-plan";
  public static final String INSTANT_FEEDBACK_REQUEST = "instant-request";
  public static final String EMAIL_FEEDBACK_REQUEST   = "instant-feedbacks";
  public static final String SURVEY_ANSWER            = "survey";
  public static final String SURVEY_INVITE            = "survey-invite";

  public static ApiClient apiClient;

  public static User           user;
  public static List<Category> categories;

  /**
   * initializes the application
   * @param context the context to be used in the method
   */
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

  /**
   * class for listening to errors
   */
  private static class DisplayErrorListener implements ApiClient.OnDisplayErrorListener
  {

    private Context context;

    DisplayErrorListener(Context context) {
      this.context = context;
    }

    @Override
    public void onDisplayError(Throwable e) {
      String text = context.getString(R.string.no_internet_connection);

      if (NetUtil.hasActiveConnection(context)) {
        final retrofit.client.Response error = ((RetrofitError) e).getResponse();
        Response response = error.getStatus() == 500 ?
            null
            : (Response) error.getBody();
        if (response != null) {
          text = response.message;
        } else {
          text = context.getString(R.string.cant_connect);
        }
      }
      Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * class for listening to refresh token generation
   */
  private static class RefreshTokenListener implements ApiClient.OnRefreshTokenListener {

    private Context context;

    RefreshTokenListener(Context context) {
      this.context = context;
    }

    @Override
    public void onRefreshToken(Authentication authentication) {
      SessionStore.saveRefreshToken(authentication.refresh_token, context);
      SessionStore.saveAccessToken(authentication.accessToken, context);
      LogUtil.e("AAA " + authentication.accessToken);
    }
  }
}
