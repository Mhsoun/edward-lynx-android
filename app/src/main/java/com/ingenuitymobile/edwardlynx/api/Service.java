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
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbackAnswerResponse;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 */

public interface Service {

  @FormUrlEncoded
  @POST("/oauth/token")
  Observable<Authentication> postLogin(@FieldMap Map<String, String> map);

  @FormUrlEncoded
  @POST("/oauth/token")
  Authentication postRefreshToken(@FieldMap Map<String, String> names);

  @GET("/api/v1/user")
  Observable<User> getMe();

  @GET("/api/v1/users")
  Observable<UsersResponse> getUsers(@Query("type") String type);

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/user")
  Observable<User> updateProfile(@Body UserBody body);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/user/devices")
  Observable<Response> postTokenDevice(@Body TokenParam param);

  @GET("/api/v1/surveys")
  Observable<Surveys> getSurveys(@Query("page") int page);

  @GET("/api/v1/categories")
  Observable<CategoriesResponse> getCategories();

  @GET("/api/v1/surveys/{id}")
  Observable<Survey> getSurvey(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/surveys/{id}/answers")
  Observable<Response> postAnswerSurvey(@Path("id") long id, @Body AnswerParam param);

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/surveys/{id}/answers")
  Observable<Response> updateAnswerSurvey(@Path("id") long id, @Body AnswerParam param);

  @GET("/api/v1/surveys/{id}/questions")
  Observable<Questions> getSurveyQuestions(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/instant-feedbacks")
  Observable<Response> postInstantFeedback(@Body InstantFeedbackBody body);

  @GET("/api/v1/instant-feedbacks")
  Observable<FeedbacksResponse> getInstantFeedbacks(@Query("filter") String type);

  @GET("/api/v1/instant-feedbacks/{id}")
  Observable<Feedback> getInstantFeedback(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/instant-feedbacks/{id}/answers")
  Observable<Response> postInstantFeedbackAnswers(@Path("id") long id, @Body AnswerParam param);

  @GET("/api/v1/instant-feedbacks/{id}/answers")
  Observable<FeedbackAnswerResponse> getInstantFeedbackAnswers(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/instant-feedbacks/{id}/shares")
  Observable<Response> postShareInstantFeedback(@Path("id") long id, @Body ShareParam param);

  @GET("/api/v1/dev-plans")
  Observable<DevelopmentPlansResponse> getDevelopmentPlans();

  @GET("/api/v1/dev-plans/{id}")
  Observable<DevelopmentPlan> getDevelopmentPlan(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/dev-plans")
  Observable<Response> posttDevelopmentPlans(@Body CreateDevelopmentPlanParam body);

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/dev-plans/{planId}/goals/{goalId}/actions/{actionId}")
  Observable<Response> updateActionPlan(@Path("planId") long planId, @Path("goalId") long goalId,
      @Path("actionId") long actionId, @Body ActionParam body);
}
