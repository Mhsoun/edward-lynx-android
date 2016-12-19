package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import retrofit.RetrofitError;

/**
 * Created by mEmEnG-sKi on 13/12/2016.
 */

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    Shared.init(this);

    if (SessionStore.restoreAccessToken(this) != null) {
      LogUtil.e("AAA " + SessionStore.restoreAccessToken(this));
      new Loader().execute(false);
    } else {
      Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
      startActivity(intent);
      SplashActivity.this.finish();
    }
  }

  /* Async Task */
  private class Loader extends AsyncTask<Boolean, Void, Response> {

    @Override
    protected Response doInBackground(Boolean... bool) {
      try {
        if (bool[0]) {
          Shared.init(SplashActivity.this);
          final String refreshToken = SessionStore.restoreRefreshToken(SplashActivity.this);
          final Authentication authentication = Shared.apiClient.postRefreshToken(refreshToken);
          if (authentication != null) {
            SessionStore.saveRefreshToken(authentication.refresh_token, SplashActivity.this);
            SessionStore.saveAccessToken(authentication.accessToken, SplashActivity.this);
          }
        }

//      Get user here
//        final UserResponse userResponse = Shared.apiClient.service.getMe();
//        if (userResponse != null && userResponse.user != null) {
//          Shared.user = userResponse.user;
//
//          }

        return new Response();
      } catch (Exception e) {
        LogUtil.e("AAA " + e);
        Response response = (Response) ((RetrofitError) e).getBody();
        if (response != null) {
          response.isNotAuthenticated =
              ((RetrofitError) e).getResponse().getStatus() == 401;
        }
        return response;
      }
    }

    @Override
    protected void onPostExecute(Response result) {
      if (result != null) {
        if (result.isNotAuthenticated) {
          SessionStore.saveAccessToken(null, SplashActivity.this);
          new Loader().execute(true);
        } else {
          Intent intent = new Intent(SplashActivity.this, MainActivity.class);
          startActivity(intent);
          SplashActivity.this.finish();
        }
      } else {
        ViewUtil.showAlert(SplashActivity.this, "", getString(R.string.cant_connect),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                SplashActivity.this.finish();
              }
            });
      }
    }
  }
}
