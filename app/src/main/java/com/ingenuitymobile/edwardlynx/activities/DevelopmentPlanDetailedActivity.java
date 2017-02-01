package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 02/02/2017.
 */

public class DevelopmentPlanDetailedActivity extends BaseActivity {

  private long id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_development_plan_detailed);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume() {
    super.onResume();
    getData();
  }

  private void getData() {
    subscription.add(Shared.apiClient.getDevelopmentPlan(id, new Subscriber<DevelopmentPlan>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onNext(DevelopmentPlan developmentPlan) {
        setTitle(developmentPlan.name);
        LogUtil.e("AAA " + developmentPlan.toString());
      }
    }));
  }
}
