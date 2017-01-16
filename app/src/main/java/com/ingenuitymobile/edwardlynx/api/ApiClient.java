package com.ingenuitymobile.edwardlynx.api;

import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Questions;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;

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

  public void getUsers(final String type, final Subscriber<UsersResponse> subscriber) {
    service.getUsers(type)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getUsers(type, subscriber);
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

  public void getSurveys(final int page, final Subscriber<Surveys> subscriber) {
    service.getSurveys(page)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getSurveys(page, subscriber);
          }
        }));
  }

  public void getSurvey(final long id, final Subscriber<Survey> subscriber) {
    service.getSurvey(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getSurvey(id, subscriber);
          }
        }));
  }

  public void getSurveyQuestions(final long id, final Subscriber<Questions> subscriber) {
    service.getSurveyQuestions(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getSurveyQuestions(id, subscriber);
          }
        }));
  }

  public void postInstantFeedback(final InstantFeedbackBody body,
      final Subscriber<Response> subscriber) {
    service.postInstantFeedback(body)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postInstantFeedback(body, subscriber);
          }
        }));
  }

  public void getInstantFeedbacks(final String filter,
      final Subscriber<FeedbacksResponse> subscriber) {
    service.getInstantFeedbacks(filter)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getInstantFeedbacks(filter, subscriber);
          }
        }));
  }

  public void getInstantFeedback(final long id, final Subscriber<Feedback> subscriber) {
    service.getInstantFeedback(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getInstantFeedback(id, subscriber);
          }
        }));
  }

  public void postInstantFeedbackAnswers(final long id, final AnswerParam param,
      final Subscriber<Response> subscriber) {
    service.getInstantFeedback(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postInstantFeedbackAnswers(id, param, subscriber);
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
    public void onError(final Throwable e) {
      if (((RetrofitError) e).getResponse().getStatus() == 401) {
        try {
          new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                final Authentication authentication = postRefreshToken(refreshToken);
                if (authentication != null) {
                  refreshListener.onRefreshToken(authentication);
                  listener.onPostAgain();
                }
              } catch (Exception ex) {
                subscriber.onError(e);
              }
            }
          }).start();
        } catch (Exception exception) {
          subscriber.onError(e);
        }
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
