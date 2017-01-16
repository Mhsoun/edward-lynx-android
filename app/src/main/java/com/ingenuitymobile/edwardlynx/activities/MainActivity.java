package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.fragments.DashboardFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveysFragment;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity implements
    NavigationView.OnNavigationItemSelectedListener {

  private Toolbar           toolbar;
  private SurveysFragment   surveysFragment;
  private DashboardFragment dashboardFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Fabric.with(this, new Crashlytics());

    initViews();
    setUserCrashlytics();

    if (savedInstanceState == null) {
      DashboardFragment fragment = DashboardFragment.newInstance();
      changeFragment(fragment);
    }
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.dashboard) {
      if (dashboardFragment == null) {
        dashboardFragment = DashboardFragment.newInstance();
      }
      changeFragment(dashboardFragment);
    } else if (id == R.id.profile) {
      startActivity(new Intent(context, ProfileActivity.class));
    } else if (id == R.id.survey) {
      if (surveysFragment == null) {
        surveysFragment = SurveysFragment.newInstance();
      }
      changeFragment(surveysFragment);
    } else if (id == R.id.development_plans) {

    } else if (id == R.id.settings) {
      startActivity(new Intent(context, ChangePasswordActivity.class));
    } else if (id == R.id.logout) {
      logout();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void changeFragment(Fragment fragment) {
    toolbar.setTitle(fragment.getArguments().getString(getString(R.string.title_key)));
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
  }

  private void initViews() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  private void logout() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    alertBuilder.setTitle("Confirmation");
    alertBuilder.setMessage(getString(R.string.logout_confirmation_message));
    alertBuilder.setPositiveButton(getString(R.string.logout),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            SessionStore.saveAccessToken(null, context);
            SessionStore.saveRefreshToken(null, context);
            Intent intent = new Intent(context, SplashActivity.class);
            startActivity(intent);
            finish();
          }
        });
    alertBuilder.setNegativeButton(getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    alertBuilder.create().show();
  }

  private void setUserCrashlytics() {
    Crashlytics.setUserIdentifier(String.valueOf(Shared.user.id));
    Crashlytics.setUserEmail(Shared.user.email);
    Crashlytics.setUserName(Shared.user.name);
  }
}

