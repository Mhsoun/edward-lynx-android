package com.ingenuitymobile.edwardlynx.api;

import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
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

  @Headers("Content-Type: application/json")
  @PATCH("/api/v1/user")
  Observable<User> updateProfile(@Body UserBody body);
}
