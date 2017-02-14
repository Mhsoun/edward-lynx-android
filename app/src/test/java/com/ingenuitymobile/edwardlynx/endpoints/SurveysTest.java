package com.ingenuitymobile.edwardlynx.endpoints;

import com.ingenuitymobile.edwardlynx.api.models.Questions;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.api.responses.CategoriesResponse;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import rx.observers.TestSubscriber;

/**
 * Created by mEmEnG-sKi on 08/02/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SurveysTest extends BaseTest {

  @Test
  public void test1GetSurveys() throws Exception {
    client = login();
    TestSubscriber<Surveys> testSubscriber = new TestSubscriber<>();
    client.service.getSurveys(1, 1)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test2GetCategories() throws Exception {
    client = login();
    TestSubscriber<CategoriesResponse> testSubscriber = new TestSubscriber<>();
    client.service.getCategories()
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test3GetSurveyById() throws Exception {
    client = login();
    TestSubscriber<Survey> testSubscriber = new TestSubscriber<>();
    client.service.getSurvey(1)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test4GetSurveyQuestions() throws Exception {
    client = login();
    TestSubscriber<Questions> testSubscriber = new TestSubscriber<>();
    client.service.getSurveyQuestions(1)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }
}
