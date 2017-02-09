package com.ingenuitymobile.edwardlynx.endpoints;

import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import rx.observers.TestSubscriber;

/**
 * Created by mEmEnG-sKi on 08/02/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DevelopmentPlansTest extends BaseTest {

  @Test
  public void test1GetDevelopmentPlans() throws Exception {
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
  public void test2GetDevelopmentPlanById() throws Exception {
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
