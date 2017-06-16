package com.ingenuitymobile.edwardlynx.endpoints;

import com.ingenuitymobile.edwardlynx.api.bodyparams.CreateDevelopmentPlanParam;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.api.responses.Response;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import rx.observers.TestSubscriber;

/**
 * Created by mEmEnG-sKi on 08/02/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DevelopmentPlansTest extends BaseTest {

  @Test
  public void test1postDevelopmentPlan() throws Exception {
    client = login();
    TestSubscriber<Response> testSubscriber = new TestSubscriber<>();
    CreateDevelopmentPlanParam planParam = new CreateDevelopmentPlanParam();
    planParam.name = "Android Dev-plan Testing";
    planParam.goals = new ArrayList<>();

    client.service.postDevelopmentPlans(planParam)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }


  @Test
  public void test2GetDevelopmentPlans() throws Exception {
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
    client = login();
    TestSubscriber<DevelopmentPlan> testSubscriber = new TestSubscriber<>();
    client.service.getDevelopmentPlan(1)
        .first()
        .toBlocking()
        .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
  }
}
