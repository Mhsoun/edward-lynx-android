package com.ingenuitymobile.edwardlynx.api;

import com.ingenuitymobile.edwardlynx.api.bodyparams.AddUserBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.CreateDevelopmentPlanParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.DevPlanBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ForgotPassword;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InstantFeedbackBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.InviteUserParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.PostTeamDevPlanBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ShareParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.TeamDevPlanBodies;
import com.ingenuitymobile.edwardlynx.api.bodyparams.TokenParam;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.models.Questions;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.api.models.TeamDevPlan;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.api.responses.CategoriesResponse;
import com.ingenuitymobile.edwardlynx.api.responses.DashboardResponse;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbackAnswerResponse;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.api.responses.IndividualProgressResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.api.responses.SurveyResultsResponse;
import com.ingenuitymobile.edwardlynx.api.responses.TeamDevPlanResponse;
import com.ingenuitymobile.edwardlynx.api.responses.TeamDevPlansResponse;
import com.ingenuitymobile.edwardlynx.api.responses.TeamReportResponse;
import com.ingenuitymobile.edwardlynx.api.responses.UsersResponse;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by mEmEnG-sKi on 19/12/2016.
 */

public interface Service {

  // region Authentication
  @FormUrlEncoded
  @POST("/oauth/token")
  Observable<Authentication> postLogin(@FieldMap Map<String, String> map);

  @FormUrlEncoded
  @POST("/oauth/token")
  Authentication postRefreshToken(@FieldMap Map<String, String> map);

  @Headers({
      "Content-Type: application/json",
      "Accept: application/json"
  })
  @POST("/api/v1/user/forgotpassword")
  Observable<Response> postForgotPassword(@Body ForgotPassword param);
  // endregion

  // region Users
  @GET("/api/v1/user")
  Observable<User> getMe();

  @GET("/api/v1/users")
  Observable<UsersResponse> getUsers(@Query("type") String type);

  @GET("/api/v1/user/dashboard")
  Observable<DashboardResponse> getUserDashboard();

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/user")
  Observable<User> updateProfile(@Body UserBody body);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/user/devices")
  Observable<Response> postTokenDevice(@Body TokenParam param);
  // endregion

  // region Surveys
  @GET("/api/v1/surveys")
  Observable<Surveys> getSurveys(@Query("page") int page, @Query("num") int num,
      @Query("filter") String filter);

  @GET("/api/v1/categories")
  Observable<CategoriesResponse> getCategories();

  @GET("/api/v1/surveys/{id}")
  Observable<Survey> getSurvey(@Path("id") long id, @Query("key") String key);

  @GET("/api/v1/surveys/exchange/{key}")
  Observable<Response> getSurveyId(@Path("key") String key, @Query("action") String action);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/surveys/{id}/answers")
  Observable<Response> postAnswerSurvey(@Path("id") long id, @Body AnswerParam param);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/surveys/{id}/recipients")
  Observable<Response> postSurveyRecipients(@Path("id") long id, @Body InviteUserParam param);

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/surveys/{id}/answers")
  Observable<Response> updateAnswerSurvey(@Path("id") long id, @Body AnswerParam param);

  @GET("/api/v1/surveys/{id}/questions")
  Observable<Questions> getSurveyQuestions(@Path("id") long id, @Query("key") String key);

  @GET("/api/v1/surveys/{id}/results")
  Observable<SurveyResultsResponse> getSurveyResults(@Path("id") long id);
  // endregion

  // region InstantFeedback
  @Headers("Content-Type: application/json")
  @POST("/api/v1/instant-feedbacks")
  Observable<Response> postInstantFeedback(@Body InstantFeedbackBody body);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/instant-feedbacks/{id}/recipients")
  Observable<Response> postInstantFeedbackParticipants(@Path("id") long id,
      @Body InstantFeedbackBody body);

  @GET("/api/v1/instant-feedbacks")
  Observable<FeedbacksResponse> getInstantFeedbacks(@Query("filter") String type);

  @GET("/api/v1/instant-feedbacks/{id}")
  Observable<Feedback> getInstantFeedback(@Path("id") long id);

  @GET("/api/v1/instant-feedbacks/exchange/{key}")
  Observable<Response> getFeedbackId(@Path("key") String key);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/instant-feedbacks/{id}/answers")
  Observable<Response> postInstantFeedbackAnswers(@Path("id") long id, @Body AnswerParam param);

  @GET("/api/v1/instant-feedbacks/{id}/answers")
  Observable<FeedbackAnswerResponse> getInstantFeedbackAnswers(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/instant-feedbacks/{id}/shares")
  Observable<Response> postShareInstantFeedback(@Path("id") long id, @Body ShareParam param);
  // endregion

  // region Development Plan
  @GET("/api/v1/dev-plans")
  Observable<DevelopmentPlansResponse> getDevelopmentPlans();

  @GET("/api/v1/dev-plans")
  Observable<DevelopmentPlansResponse> getuserDevelopmentPlans(@Query("user") long id);

  @GET("/api/v1/dev-plans/{id}")
  Observable<DevelopmentPlan> getDevelopmentPlan(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/dev-plans/{id}")
  Observable<DevelopmentPlan> updateDevelopmentPlan(@Path("id") long id, @Body DevPlanBody body);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/dev-plans")
  Observable<Response> postDevelopmentPlans(@Body CreateDevelopmentPlanParam body);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/dev-plans/{id}/goals")
  Observable<Response> postDevelopmentPlanGoal(@Path("id") long id, @Body Goal body);

  @Headers("Content-Type: application/json")
  @DELETE("/api/v1/dev-plans/{planId}/goals/{goalId}")
  Observable<Response> deleteDevelopmentPlanGoal(
      @Path("planId") long planId,
      @Path("goalId") long goalId
  );

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/dev-plans/{planId}/goals/{goalId}")
  Observable<Response> updateGoal(
      @Path("planId") long planId,
      @Path("goalId") long goalId,
      @Body Goal body
  );

  @Headers("Content-Type: application/json")
  @POST("/api/v1/dev-plans/{planId}/goals/{goalId}/actions")
  Observable<Response> postActionPlan(
      @Path("planId") long planId,
      @Path("goalId") long goalId,
      @Body Action body
  );

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/dev-plans/{planId}/goals/{goalId}/actions/{actionId}")
  Observable<Response> updateActionPlan(
      @Path("planId") long planId,
      @Path("goalId") long goalId,
      @Path("actionId") long actionId,
      @Body Action body
  );

  @Headers("Content-Type: application/json")
  @DELETE("/api/v1/dev-plans/{planId}/goals/{goalId}/actions/{actionId}")
  Observable<Response> deleteActionPlan(
      @Path("planId") long planId,
      @Path("goalId") long goalId,
      @Path("actionId") long actionId
  );
  // endregion

  // region Team Manager
  @GET("/api/v1/dev-plans-manager/users?type=sharing")
  Observable<UsersResponse> getIndividualUsers();

  @GET("/api/v1/dev-plans-manager/users")
  Observable<IndividualProgressResponse> getIndividualProgress();

  @GET("/api/v1/dev-plans-manager/teams")
  Observable<TeamDevPlansResponse> getTeamCategories();

  @Headers("Content-Type: application/json")
  @PUT("/api/v1/dev-plans-manager/teams")
  Observable<Response> udpateTeamCategories(@Body TeamDevPlanBodies body);

  @Headers("Content-Type: application/json")
  @POST("/api/v1/dev-plans-manager/teams")
  Observable<TeamDevPlanResponse> postTeamCategory(@Body PostTeamDevPlanBody body);

  @Headers("Content-Type: application/json")
  @DELETE("/api/v1/dev-plans-manager/teams/{id}")
  Observable<Response> deleteTeamCategory(@Path("id") long id);

  @GET("/api/v1/dev-plans-manager/teams/{id}")
  Observable<TeamDevPlan> getTeamCategory(@Path("id") long id);

  @Headers("Content-Type: application/json")
  @PUT("/api/v1/dev-plans-manager/users")
  Observable<Response> putIndividualUsers(@Body AddUserBody body);
  //endregion

  // region Reports
  @GET("/api/v1/dev-plans-manager/reports")
  Observable<TeamReportResponse> getSurveyReports();

  // endregion
}
