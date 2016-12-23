package com.ingenuitymobile.edwardlynx.api;

import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 */

public class ApiClient {

  public Service service;

  private String consumerKey;
  private String consumerSecret;
  private String baseUrl;
  private String refreshToken;

  private OnRefreshTokenListener refreshListener;

  public ApiClient(String consumerKey, String consumerSecret, String baseUrl,
      OnRefreshTokenListener refreshListener) {
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.baseUrl = baseUrl;
    this.refreshListener = refreshListener;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setAccessToken(String accessToken) {
    service = createRetrofitService(accessToken);
  }

  private Service createRetrofitService(final String accessToken) {
    return new RestAdapter.Builder()
        .setEndpoint(baseUrl)
        .setRequestInterceptor(new RequestInterceptor() {
          @Override
          public void intercept(RequestFacade request) {
            if (accessToken != null) {
              request.addHeader("Authorization", "Bearer " + accessToken);
              request.addHeader("Accept", "application/json");
            }
          }
        })
        .build()
        .create(Service.class);
  }

  public Observable<Authentication> postLogin(final String username, final String password) {
    Map<String, String> map = new HashMap<>();
    map.put("grant_type", "password");
    map.put("username", username);
    map.put("password", password);
    map.put("client_id", consumerKey);
    map.put("client_secret", consumerSecret);
    return service.postLogin(map);
  }

  public Authentication postRefreshToken(String refreshToken) {
    Map<String, String> map = new HashMap<>();
    map.put("grant_type", "refresh_token");
    map.put("refresh_token", refreshToken);
    map.put("client_id", consumerKey);
    map.put("client_secret", consumerSecret);
    return service.postRefreshToken(map);
  }

  public void getMe(final Subscriber<User> subscriber) {
    service.getMe()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getMe(subscriber);
          }
        }));
  }

  public void updateUser(final UserBody body,
      final Subscriber<User> subscriber) {
    service.updateProfile(body)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            updateUser(body, subscriber);
          }
        }));
  }


  private class CustomSubscriber<T> extends Subscriber<T> {
    Subscriber<T>       subscriber;
    OnPostAgainListener listener;

    CustomSubscriber(Subscriber<T> subscriber, OnPostAgainListener listener) {
      this.subscriber = subscriber;
      this.listener = listener;
    }


    @Override
    public void onCompleted() {
      subscriber.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
      if (((RetrofitError) e).getResponse().getStatus() == 401) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            final Authentication authentication = postRefreshToken(refreshToken);
            if (authentication != null) {
              refreshListener.onRefreshToken(authentication);
              listener.onPostAgain();
            }
          }
        }).start();
      } else {
        subscriber.onError(e);
      }
    }

    @Override
    public void onNext(T t) {
      subscriber.onNext(t);
    }
  }


  interface OnPostAgainListener {
    void onPostAgain();
  }

  public interface OnRefreshTokenListener {
    void onRefreshToken(Authentication authentication);
  }
}
