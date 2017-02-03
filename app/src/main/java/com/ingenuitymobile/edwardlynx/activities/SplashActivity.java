package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.CategoriesResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import retrofit.RetrofitError;
import rx.Subscriber;

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
      getUser();
    } else {
      Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
      startActivity(intent);
      SplashActivity.this.finish();
    }
  }

  private void getUser() {
    Shared.apiClient.getMe(new Subscriber<User>() {
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
    });
  }

  private void getCategories() {
    Shared.apiClient.getCategories(new Subscriber<CategoriesResponse>() {
      @Override
      public void onCompleted() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
      }

      @Override
      public void onError(Throwable e) {
        showError(e);
      }

      @Override
      public void onNext(CategoriesResponse categoriesResponse) {
        Shared.categories = categoriesResponse.items;
      }
    });
  }

  private void showError(Throwable e) {
    if (((RetrofitError) e).getResponse().getStatus() == 401) {
      SessionStore.saveRefreshToken(null, SplashActivity.this);
      SessionStore.saveAccessToken(null, SplashActivity.this);
    }

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        ViewUtil.showAlert(SplashActivity.this, "", getString(R.string.cant_connect),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                SplashActivity.this.finish();
              }
            });
      }
    });
  }
}
