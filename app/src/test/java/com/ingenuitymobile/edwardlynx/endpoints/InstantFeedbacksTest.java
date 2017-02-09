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
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstantFeedbacksTest extends BaseTest {

  @Test
  public void test1GetFeedbackRequests() throws Exception {
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
  public void test2GetInstantFeedback() throws Exception {
    client = login();
    TestSubscriber<Feedback> testSubscriber = new TestSubscriber<>();
    client.service.getInstantFeedback(101)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test2GetInstantFeedbackAnswers() throws Exception {
    client = login();
    TestSubscriber<FeedbackAnswerResponse> testSubscriber = new TestSubscriber<>();
    client.service.getInstantFeedbackAnswers(101)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }
}
