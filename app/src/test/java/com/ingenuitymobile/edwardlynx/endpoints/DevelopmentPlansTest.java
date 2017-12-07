package com.ingenuitymobile.edwardlynx.endpoints;

import com.ingenuitymobile.edwardlynx.api.bodyparams.CreateDevelopmentPlanParam;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import rx.observers.TestSubscriber;

/**
 * Created by mEmEnG-sKi on 08/02/2017.
 * Test case for development plans.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DevelopmentPlansTest extends BaseTest {
  private static final int TEST_DEVELOPMENT_PLAN_ID = 62;

  @Test
  public void test1postDevelopmentPlan() throws Exception {
    System.out.println("\u2713 submitting one development plan...");
    client = login();
    TestSubscriber<Response> testSubscriber = new TestSubscriber<>();
    CreateDevelopmentPlanParam planParam = new CreateDevelopmentPlanParam();
    Goal goal = new Goal();
    Action action = new Action();
    List<Action> actions = new ArrayList<>();
    List<Goal> goals = new ArrayList<>();
    goal.title = "test goal 1";
    goal.position = 0;
    action.title = "test action 1";
    action.position = 0;
    actions.add(action);
    goal.actions = actions;
    goals.add(goal);
    planParam.name = "Android Dev-plan Testing";
    planParam.goals = goals;

    client.service.postDevelopmentPlans(planParam)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }


  @Test
  public void test2GetDevelopmentPlans() throws Exception {
    System.out.println("\u2713 retrieving development plan...");
    client = login();
    TestSubscriber<DevelopmentPlansResponse> testSubscriber = new TestSubscriber<>();
    client.service.getDevelopmentPlans()
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }

  @Test
  public void test3GetDevelopmentPlanById() throws Exception {
    System.out.println("\u2713 retrieving development plan by id...");
    client = login();
    TestSubscriber<DevelopmentPlan> testSubscriber = new TestSubscriber<>();
    client.service.getDevelopmentPlan(TEST_DEVELOPMENT_PLAN_ID)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }
}
