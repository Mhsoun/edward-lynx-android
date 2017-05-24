package com.ingenuitymobile.edwardlynx.endpoints;

import com.ingenuitymobile.edwardlynx.APIClientFactory;
import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.observers.TestSubscriber;

/**
 * Created by mEmEnG-sKi on 14/10/2016.
 */

class BaseTest {
  ApiClient client;

  ApiClient login() throws Exception {
    client = new APIClientFactory().createClient();
    TestSubscriber<Authentication> testSubscriber = new TestSubscriber<>();
    Map<String, String> map = new HashMap<>();
    map.put("grant_type", "password");
    map.put("username", "admin@edwardlynx.com");
    map.put("password", "password123");
    map.put("client_id", APIClientFactory.CONSUMER_KEY);
    map.put("client_secret", APIClientFactory.CONSUMER_SECRET);
    client.service.postLogin(map)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
    List<Authentication> authentications = testSubscriber.getOnNextEvents();
    client.setRefreshToken("admin@edwardlynx.com","password");
    client.setAccessToken(authentications.get(0).accessToken);
//    System.out.println("AAA accesstoken " + authentications.get(0).accessToken);
//    System.out.println("AAA refreshtoken " + authentications.get(0).accessToken);
    return client;
  }
}
