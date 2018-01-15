package com.ingenuitymobile.edwardlynx.endpoints;

import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbackAnswerResponse;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import rx.observers.TestSubscriber;

/**
 * Created by mEmEnG-sKi on 08/02/2017.
 * Test case for instant feedback.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstantFeedbacksTest extends BaseTest {
  private static final int TEST_INSTANT_FEEDBACK_ID = 1;

  @Test
  public void test1GetFeedbackRequests() throws Exception {
    System.out.println("\u2713 retrieving feedback requests...");
    client = login();
    TestSubscriber<FeedbacksResponse> testSubscriber = new TestSubscriber<>();
    client.service.getInstantFeedbacks("to_answer")
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test2GetMeFeedbacks() throws Exception {
    System.out.println("\u2713 retrieving user feedback requests...");
    client = login();
    TestSubscriber<FeedbacksResponse> testSubscriber = new TestSubscriber<>();
    client.service.getInstantFeedbacks("mine")
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test3GetInstantFeedback() throws Exception {
    System.out.println("\u2713 retrieving instant feedback...");
    client = login();
    TestSubscriber<Feedback> testSubscriber = new TestSubscriber<>();
    client.service.getInstantFeedback(TEST_INSTANT_FEEDBACK_ID)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test4GetInstantFeedbackAnswers() throws Exception {
    System.out.println("\u2713 retrieving instant feedback answers...");
    client = login();
    TestSubscriber<FeedbackAnswerResponse> testSubscriber = new TestSubscriber<>();
    client.service.getInstantFeedbackAnswers(TEST_INSTANT_FEEDBACK_ID)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }
}
