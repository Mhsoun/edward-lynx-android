package com.ingenuitymobile.edwardlynx;


import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;

/**
 * Created by mEmEnG-sKi on 14/10/2016.
 */

public class APIClientFactory {
  public static final String CONSUMER_KEY    = "2";
  public static final String CONSUMER_SECRET = "jNl8Akpv79hORetW7jCMCQnKQjwiB6XerZxIxcsy";

  private static final String BASE_URL = "http://edwardlynx.ingenuity.ph";

  public ApiClient createClient() {
    return createClient(null);
  }

  private ApiClient createClient(String acessToken) {
    ApiClient client;
    return null;
  }
}
