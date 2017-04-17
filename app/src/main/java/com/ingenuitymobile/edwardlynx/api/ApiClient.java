package com.ingenuitymobile.edwardlynx.api;

import com.ingenuitymobile.edwardlynx.api.bodyparams.ActionParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.CreateDevelopmentPlanParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ShareParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.TokenParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Questions;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.api.responses.CategoriesResponse;
import com.ingenuitymobile.edwardlynx.api.responses.DashboardResponse;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbackAnswerResponse;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.SurveyResultsResponse;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 */

public class ApiClient {

  public Service service;

  private String consumerKey;
  private String consumerSecret;
  private String baseUrl;
  private String username;
  private String password;

  private OnRefreshTokenListener refreshListener;

  public ApiClient(String consumerKey, String consumerSecret, String baseUrl,
      OnRefreshTokenListener refreshListener) {
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.baseUrl = baseUrl;
    this.refreshListener = refreshListener;
  }

  public void setRefreshToken(String username, String password) {
    this.username = password;
    this.password = password;
  }


  public void setAccessToken(String accessToken) {
    service = createRetrofitService(accessToken);
  }

  private Service createRetrofitService(final String accessToken) {
    OkHttpClient okHttpClient = new OkHttpClient();
    return new RestAdapter.Builder()
        .setClient(new OkClient(okHttpClient))
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

  public Authentication postRefreshToken(final String username, final String password) {
    Map<String, String> map = new HashMap<>();
    map.put("grant_type", "password");
    map.put("username", username);
    map.put("password", password);
    map.put("client_id", consumerKey);
    map.put("client_secret", consumerSecret);
    return service.postRefreshToken(map);
  }

  public Subscription getMe(final Subscriber<User> subscriber) {
    return service.getMe()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getMe(subscriber);
          }
        }));
  }

  public Subscription getUsers(final String type, final Subscriber<UsersResponse> subscriber) {
    return service.getUsers(type)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getUsers(type, subscriber);
          }
        }));
  }

  public Subscription getUserDashboard(final Subscriber<DashboardResponse> subscriber) {
    return service.getUserDashboard()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getUserDashboard(subscriber);
          }
        }));
  }

  public Subscription updateUser(final UserBody body,
      final Subscriber<User> subscriber) {
    return service.updateProfile(body)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            updateUser(body, subscriber);
          }
        }));
  }

  public Subscription postTokenDevice(final TokenParam param,
      final Subscriber<Response> subscriber) {
    return service.postTokenDevice(param)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postTokenDevice(param, subscriber);
          }
        }));
  }

  public Subscription getSurveys(final int page, final int num,
      final Subscriber<Surveys> subscriber) {
    return service.getSurveys(page, num)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getSurveys(page, num, subscriber);
          }
        }));
  }

  public Subscription getCategories(final Subscriber<CategoriesResponse> subscriber) {
    return service.getCategories()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getCategories(subscriber);
          }
        }));
  }


  public Subscription getSurvey(final long id, final Subscriber<Survey> subscriber) {
    return service.getSurvey(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getSurvey(id, subscriber);
          }
        }));
  }

  public Subscription getSurveyQuestions(final long id, final Subscriber<Questions> subscriber) {
    return service.getSurveyQuestions(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getSurveyQuestions(id, subscriber);
          }
        }));
  }

  public Subscription getSurveyResults(final long id,
      final Subscriber<SurveyResultsResponse> subscriber) {
    return service.getSurveyResults(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getSurveyResults(id, subscriber);
          }
        }));
  }

  public Subscription postAnswerSurvey(final long id, final AnswerParam param,
      final Subscriber<Response> subscriber) {
    return service.postAnswerSurvey(id, param)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postAnswerSurvey(id, param, subscriber);
          }
        }));
  }

  public Subscription updateAnswerSurvey(final long id, final AnswerParam param,
      final Subscriber<Response> subscriber) {
    return service.updateAnswerSurvey(id, param)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            updateAnswerSurvey(id, param, subscriber);
          }
        }));
  }

  public Subscription postInstantFeedbackParticipants(final long id, final InstantFeedbackBody body,
      final Subscriber<Response> subscriber) {
    return service.postInstantFeedbackParticipants(id, body)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postInstantFeedbackParticipants(id, body, subscriber);
          }
        }));
  }

  public Subscription postInstantFeedback(final InstantFeedbackBody body,
      final Subscriber<Response> subscriber) {
    return service.postInstantFeedback(body)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postInstantFeedback(body, subscriber);
          }
        }));
  }

  public Subscription getInstantFeedbacks(final String filter,
      final Subscriber<FeedbacksResponse> subscriber) {
    return service.getInstantFeedbacks(filter)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getInstantFeedbacks(filter, subscriber);
          }
        }));
  }

  public Subscription getInstantFeedback(final long id, final Subscriber<Feedback> subscriber) {
    return service.getInstantFeedback(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getInstantFeedback(id, subscriber);
          }
        }));
  }

  public Subscription postInstantFeedbackAnswers(final long id, final AnswerParam param,
      final Subscriber<Response> subscriber) {
    return service.postInstantFeedbackAnswers(id, param)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postInstantFeedbackAnswers(id, param, subscriber);
          }
        }));
  }

  public Subscription getInstantFeedbackAnswers(final long id,
      final Subscriber<FeedbackAnswerResponse> subscriber) {
    return service.getInstantFeedbackAnswers(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getInstantFeedbackAnswers(id, subscriber);
          }
        }));
  }

  public Subscription postShareInstantFeedback(final long id, final ShareParam param,
      final Subscriber<Response> subscriber) {
    return service.postShareInstantFeedback(id, param)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postShareInstantFeedback(id, param, subscriber);
          }
        }));
  }

  public Subscription getDevelopmentPlans(final Subscriber<DevelopmentPlansResponse> subscriber) {
    return service.getDevelopmentPlans()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getDevelopmentPlans(subscriber);
          }
        }));
  }

  public Subscription getDevelopmentPlan(final long id,
      final Subscriber<DevelopmentPlan> subscriber) {
    return service.getDevelopmentPlan(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            getDevelopmentPlan(id, subscriber);
          }
        }));
  }

  public Subscription postDevelopmentPlans(final CreateDevelopmentPlanParam param,
      final Subscriber<Response> subscriber) {
    return service.postDevelopmentPlans(param)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            postDevelopmentPlans(param, subscriber);
          }
        }));
  }

  public Subscription updateActionPlan(final long planId, final long goalId, final long actionId,
      final ActionParam param, final Subscriber<Response> subscriber) {
    return service.updateActionPlan(planId, goalId, actionId, param)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CustomSubscriber<>(subscriber, new OnPostAgainListener() {
          @Override
          public void onPostAgain() {
            updateActionPlan(planId, goalId, actionId, param, subscriber);
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
      final retrofit.client.Response error = ((RetrofitError) e).getResponse();
      if (error != null && error.getStatus() == 401) {
        LogUtil.e("AAA onError " + e);
        try {
          new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                final Authentication authentication = postRefreshToken(username, password);
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
