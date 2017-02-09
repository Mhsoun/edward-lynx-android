package com.ingenuitymobile.edwardlynx.endpoints;

import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.User;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import rx.observers.TestSubscriber;

/**
 * Created by mEmEnG-sKi on 08/02/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersTest extends BaseTest {

  @Test
  public void test1GetUser() throws Exception {
    getUser(login());
  }

  @Test
  public void test2UpdateUser() throws Exception {
    client = login();
    User user = getUser(client);

    UserBody body = new UserBody();
    body.name = user.name;
    body.info = user.info;
    body.department = user.department;
    body.role = user.role;
    body.gender = user.gender;
    body.city = user.city;
    body.country = user.country;
    TestSubscriber<User> testSubscriber = new TestSubscriber<>();
    client.service.updateProfile(body)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test3PostChangePassword() throws Exception {
    client = login();

    UserBody body = new UserBody();
    body.currentPassword = "password123";
    body.password = "password123";
    TestSubscriber<User> testSubscriber = new TestSubscriber<>();
    client.service.updateProfile(body)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  private User getUser(ApiClient client) {
    TestSubscriber<User> testSubscriber = new TestSubscriber<>();
    client.service.getMe()
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();

    return testSubscriber.getOnNextEvents().get(0);
  }
}
