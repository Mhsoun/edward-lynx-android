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
 * Test case for surveys.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SurveysTest extends BaseTest {
  private static final Long TEST_SURVEY_ID = 449L;
  private static final String TEST_SURVEY_KEY = "qlIVeyppMXNsNrS9v4l8ovOfSFW4X2Nq";

  @Test
  public void test1GetSurveys() throws Exception {
    System.out.println("\u2713 retrieving surveys...");
    client = login();
    TestSubscriber<Surveys> testSubscriber = new TestSubscriber<>();
    client.service.getSurveys(1, 1, "to_answer")
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test2GetCategories() throws Exception {
    System.out.println("\u2713 retrieving categories...");
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
    System.out.println("\u2713 retrieving survey with id " + TEST_SURVEY_ID + "...");
    client = login();
    TestSubscriber<Survey> testSubscriber = new TestSubscriber<>();
    client.service.getSurvey(TEST_SURVEY_ID, TEST_SURVEY_KEY)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test4GetSurveyQuestions() throws Exception {
    System.out.println("\u2713 retrieving survey questions with id " + TEST_SURVEY_ID + "...");
    client = login();
    TestSubscriber<Questions> testSubscriber = new TestSubscriber<>();
    client.service.getSurveyQuestions(TEST_SURVEY_ID, TEST_SURVEY_KEY)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }
}
