package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.fragments.DevelopmenPlansFragment;

/**
 * Created by memengski on 7/6/17.
 */

public class IndividualDevelopmentPlanActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_individual_plan);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setTitle(getString(R.string.drawer_development_plans).toUpperCase());

    FragmentManager fragmentManager = getSupportFragmentManager();

    DevelopmenPlansFragment developmenPlansFragment = DevelopmenPlansFragment
        .newInstance(context, true, getIntent().getLongExtra("id", 0L));

    fragmentManager
        .beginTransaction()
        .replace(R.id.content, developmenPlansFragment)
        .commit();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
}
