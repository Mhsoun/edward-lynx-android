package com.ingenuitymobile.edwardlynx.endpoints;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by mEmEnG-sKi on 17/10/2016.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticatonTest extends BaseTest {

  @Test
  public void postLogin() throws Exception {
    login();
  }

  @Test
  public void postRefreshToken() throws Exception {
//    ApiClient client = login();
//    Map<String, String> map = new HashMap<>();
//    map.put("grant_type", "refresh_token");
//    map.put("refresh_token", client.getRefreshToken());
//    map.put("client_id", APIClientFactory.CONSUMER_KEY);
//    map.put("client_secret", APIClientFactory.CONSUMER_SECRET);
//    Authentication response = client.service.postRefreshToken(map);
//    assertNotNull(response);
  }
}
