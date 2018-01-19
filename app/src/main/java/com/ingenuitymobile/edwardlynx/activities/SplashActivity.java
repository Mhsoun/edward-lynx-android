package com.ingenuitymobile.edwardlynx.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.TokenParam;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.CategoriesResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.StringUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import java.util.List;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 13/12/2016.
 * Activity to be displayed on application startup to check current user access and API
 * connectivity, once connection is established, data will be retrieved for loading.
 * If no user is logged in, login page will be displayed, else, user details will be
 * retrieved and intent data will be checked and redirect to the page depending on the
 * type of intent and data passed.
 */

public class SplashActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    Shared.init(this);

    if (SessionStore.restoreAccessToken(this) != null) {
      LogUtil.e("AAA " + SessionStore.restoreAccessToken(this));

      NotificationManager notificationManager =
          (NotificationManager) getApplicationContext()
              .getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.cancel(0);
      getUser();
    } else {
      Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
      startActivity(intent);
      SplashActivity.this.finish();
    }
  }

  /**
   * retrieves the user details from the API and stores it locally
   */
  private void getUser() {
    subscription.add(Shared.apiClient.getMe(new Subscriber<User>() {
      @Override
      public void onCompleted() {
        getCategories();
      }

      @Override
      public void onError(Throwable e) {
        showError(e);
      }

      @Override
      public void onNext(User userResponse) {
        Shared.user = userResponse;
      }
    }));
  }

  /**
   * retrieves the categories from the API
   */
  private void getCategories() {
    subscription.add(Shared.apiClient.getCategories(new Subscriber<CategoriesResponse>() {
      @Override
      public void onCompleted() {
        postTokenDevice();
      }

      @Override
      public void onError(Throwable e) {
        showError(e);
      }

      @Override
      public void onNext(CategoriesResponse categoriesResponse) {
        Shared.categories = categoriesResponse.items;
        LogUtil.e("AAA onNext postTokenDevice");
      }
    }));
  }

  /**
   * posts the Firebase token to the API for GCM async
   */
  private void postTokenDevice() {
    if (!TextUtils.isEmpty(SessionStore.restoreFirebaseToken(SplashActivity.this))) {
      subscription.add(Shared.apiClient.postTokenDevice(
          new TokenParam(SessionStore.restoreFirebaseToken(SplashActivity.this),
              StringUtil.getDeviceId(SplashActivity.this)), new Subscriber<Response>() {
            @Override
            public void onCompleted() {
              checkIntent();
            }

            @Override
            public void onError(Throwable e) {
              showError(e);
            }

            @Override
            public void onNext(Response response) {
              LogUtil.e("AAA onNext postTokenDevice");
            }
          }));
    } else {
      checkIntent();
    }
  }

  /**
   * checks if the application is launched via an Intent and retrieves all intent details,
   * details will be used to determine which screen will be opened if a type exists
   */
  private void checkIntent() {
    Uri data = getIntent().getData();
    if (data != null) {
      final List<String> segments = data.getPathSegments();
      LogUtil.e("AAA " + segments.get(0));
      if (segments.get(0).equals(Shared.SURVEY_ANSWER)) {
        subscription.add(Shared.apiClient.getSurveyId(
            segments.get(2),
            "answer",
            new Subscriber<Response>() {
              @Override
              public void onCompleted() {
                openMainPage();
              }

              @Override
              public void onError(Throwable e) {
                displayNotAuthorize(e, getString(R.string.no_access));
              }

              @Override
              public void onNext(Response response) {
                Bundle surveyBundle = new Bundle();
                surveyBundle.putString("type", segments.get(0));
                surveyBundle.putString("id", String.valueOf(response.surveyId));
                surveyBundle.putString("key", segments.get(2));
                getIntent().putExtras(surveyBundle);
              }
            }));
      } else if(segments.get(0).equals(Shared.SURVEY_INVITE)) {
          subscription.add(Shared.apiClient.getSurveyId(
              segments.get(1),
              "invite",
              new Subscriber<Response>() {
                  @Override
                  public void onCompleted() {
                      openMainPage();
                  }

                  @Override
                  public void onError(Throwable e) {
                      displayNotAuthorize(e, getString(R.string.no_access_invite));
                  }

                  @Override
                  public void onNext(Response response) {
                      Bundle surveyBundle = new Bundle();
                      surveyBundle.putString("type", segments.get(0));
                      surveyBundle.putString("id", String.valueOf(response.surveyId));
                      surveyBundle.putString("key", segments.get(1));
                      getIntent().putExtras(surveyBundle);
                  }
              }));
      } else if (segments.get(0).equals(Shared.EMAIL_FEEDBACK_REQUEST)) {
        subscription.add(Shared.apiClient.getFeedbackId(segments.get(2),
            new Subscriber<Response>() {
              @Override
              public void onCompleted() {
                openMainPage();
              }

              @Override
              public void onError(Throwable e) {
                displayNotAuthorize(e, getString(R.string.no_access_feedback));
              }

              @Override
              public void onNext(Response response) {
                Bundle bundle = new Bundle();
                bundle.putString("type", Shared.INSTANT_FEEDBACK_REQUEST);
                bundle.putString("id", String.valueOf(response.feedbackId));
                getIntent().putExtras(bundle);
              }
            }));
      } else if (segments.get(0).equals(Shared.DEV_PLAN)) {
        Bundle bundle = new Bundle();
        bundle.putString("type", Shared.DEV_PLAN);
        bundle.putString("id", segments.get(1));
        getIntent().putExtras(bundle);
        openMainPage();
      }
    } else {
      openMainPage();
    }
  }

  /**
   * opens the main screen of the app passing data to be processed
   */
  private void openMainPage() {
    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
    if (getIntent().getExtras() != null) {
      intent.putExtras(getIntent().getExtras());
    }
    startActivity(intent);
    SplashActivity.this.finish();
  }

  /**
   * displays an error message to the user
   * @param e the error thrown by the caller method
   */
  private void showError(Throwable e) {
    LogUtil.e("AAA " + e);
    if (e != null) {
      final retrofit.client.Response error = ((RetrofitError) e).getResponse();
      if (error != null && error.getStatus() == 401) {
        SessionStore.saveRefreshToken(null, SplashActivity.this);
        SessionStore.saveAccessToken(null, SplashActivity.this);
      }
    }

    ViewUtil.showAlert(SplashActivity.this, "", getString(R.string.cant_connect),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            SplashActivity.this.finish();
          }
        });
  }

  /**
   * displays a message telling the user that an action is unauthorized
   * @param e the error thrown by the caller method
   * @param errorMessage the user readable error message thrown by the caller method
   */
  private void displayNotAuthorize(Throwable e, String errorMessage) {
    if (e != null) {
      final retrofit.client.Response error = ((RetrofitError) e).getResponse();
      if (error != null && error.getStatus() == 404) {
        ViewUtil.showAlert(
            SplashActivity.this,
            "",
            errorMessage,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                openMainPage();
              }
            });
      }
    }
  }
}
