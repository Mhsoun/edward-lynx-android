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
 * Test case for users.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersTest extends BaseTest {

  @Test
  public void test1GetUser() throws Exception {
    System.out.println("\u2713 getting user...");
    getUser(login());
  }

  @Test
  public void test2UpdateUser() throws Exception {
    System.out.println("\u2713 updating user...");
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

  /**
   * Assertion Error since change password needs input password to be at least 6 characters
   * @throws Exception test exception
   */
  @Test
  public void test3PostChangePassword() throws Exception {
    System.out.println("\u2713 changing user password...");
    client = login();

    UserBody body = new UserBody();
    body.currentPassword = "1";
    body.password = "1";
    TestSubscriber<User> testSubscriber = new TestSubscriber<>();
    client.service.updateProfile(body)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertError(Throwable.class);
    testSubscriber.assertNotCompleted();
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
