package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.content.Intent;
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

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 13/12/2016.
 */

public class SplashActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    Shared.init(this);

    if (SessionStore.restoreAccessToken(this) != null) {
      LogUtil.e("AAA " + SessionStore.restoreAccessToken(this));
      getUser();
    } else {
      Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
      startActivity(intent);
      SplashActivity.this.finish();
    }
  }

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
      }
    }));
  }

  private void postTokenDevice() {
    if (!TextUtils.isEmpty(SessionStore.restoreFirebaseToken(SplashActivity.this))) {
      subscription.add(Shared.apiClient.postTokenDevice(
          new TokenParam(SessionStore.restoreFirebaseToken(SplashActivity.this),
              StringUtil.getDeviceId(SplashActivity.this)), new Subscriber<Response>() {
            @Override
            public void onCompleted() {
              openMainPage();
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
      openMainPage();
    }
  }

  private void openMainPage() {
    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
    if (getIntent().getExtras() != null) {
      intent.putExtras(getIntent().getExtras());
    }
    startActivity(intent);
    SplashActivity.this.finish();
  }

  private void showError(Throwable e) {
    LogUtil.e("AAA " + e);
    final retrofit.client.Response error = ((RetrofitError) e).getResponse();
    if (error != null && error.getStatus() == 401) {
      SessionStore.saveRefreshToken(null, SplashActivity.this);
      SessionStore.saveAccessToken(null, SplashActivity.this);
    }

    ViewUtil.showAlert(SplashActivity.this, "", getString(R.string.cant_connect),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            SplashActivity.this.finish();
          }
        });
  }
}
