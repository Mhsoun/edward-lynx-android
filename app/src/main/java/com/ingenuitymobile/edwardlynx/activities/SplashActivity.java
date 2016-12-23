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
      SessionStore.saveAccessToken(
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImYwNDgyZmNhMDkyNGNjMTU0NThmMDNjMDczNzhmZDNlNTUwMWZmNzdhNmRkZDQzY2UwOTEwZWQzYjYzNWNiMzg5NmNhNGFkZTg1MGI1MThkIn0.eyJhdWQiOiIyIiwianRpIjoiZjA0ODJmY2EwOTI0Y2MxNTQ1OGYwM2MwNzM3OGZkM2U1NTAxZmY3N2E2ZGRkNDNjZTA5MTBlZDNiNjM1Y2IzODk2Y2E0YWRlODUwYjUxOGQiLCJpYXQiOjE0ODIxNDc4NDgsIm5iZiI6MTQ4MjE0Nzg0OCwiZXhwIjoxNDgyMjM0MjQ4LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.UGrWOwBRC82-ytb25WKVJnwF9kLnyr3pLFHoMB3hQ0uBlIlFCZn6XCdwbgVRZGT5a5VQw0OAUql9lv56OiQAb3lwQLFlyfOjpXz2DkXEEbtkaGTkkJ-feuP__LKdmyRJv8TFvcNZLPDooUsJMqktoiCWqetsiwFskrN-U3nMSgLAjPj8sVlCAO68pQAvzA9Xdrrq59nPLoLxs-mrksY6K-LZGbMxyhDTvF_hRFv7dNJeX2OLX_SSmP2XYJntVCxcv7tb1Cjkc3cAjGiU-E7UXG1547nuGuXNs9TJKWMMqiBhaN_zPbkOZgIk3vs1oimgYx7MdT9w-r7gKpqv2s3JLMmbUMcuv4GG9rn31jq0_5xG8lLntBqJl5__0doDpAJlVK26mNn9gmFDM4xF7F25ZmpFyRfW0CyKGM8Dvs1TSfBueITqIHIe5018V3o5m8U-f2MUiFn7b3KhSSsRAYrhfX4XH_M2V4sMNvdO5xZ-DqixnF4kW_fsUDztMCghiwCuWrDWAhbmg2dudNrv-XUX2kRk0aHVzOOeW1npcgZReDQjqgozlV8ryB6L0nkkofN0zYV8AfAuR9jdROU_wU4KeDxdn9-OWO39WS9cYLFZ8h0Ecrk_vbKqFY_yBF1Q_z1A_qCIFpTnTUVdkPPtuunVd3gDvIsrkY_5rTbJQ4gx-L8",
          this);
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
