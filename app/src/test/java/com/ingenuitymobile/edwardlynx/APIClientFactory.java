package com.ingenuitymobile.edwardlynx;


import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;

/**
 * Created by mEmEnG-sKi on 14/10/2016.
 */

public class APIClientFactory {
  public static final String CONSUMER_KEY    = "6";
  public static final String CONSUMER_SECRET = "C2o9x70fFHjq99M0OvXAfXdZTJTSjbuiRlfbNoEx";

  private static final String BASE_URL = "https://lynxtool.edwardlynx.com";

  public ApiClient createClient() {
    return createClient(null);
  }

  private ApiClient createClient(String acessToken) {
    ApiClient client = new ApiClient(
            CONSUMER_KEY,
            CONSUMER_SECRET,
            BASE_URL,
            new ApiClient.OnRefreshTokenListener() {
              @Override
              public void onRefreshToken(Authentication authentication) {
                System.out.println("TTT OnRefreshToken");
              }
            },
            new ApiClient.OnDisplayErrorListener() {
              @Override
              public void onDisplayError(Throwable e) {
                System.out.print("TTT OnDisplayError");
              }
            }
    );

    client.setAccessToken(acessToken);
    return client;
  }
}
