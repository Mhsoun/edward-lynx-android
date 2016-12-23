package com.ingenuitymobile.edwardlynx.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;

/**
 * Created by mEmEnG-sKi on 20/12/2016.
 */

public class BaseMainActivity extends BaseActivity implements
    NavigationView.OnNavigationItemSelectedListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initViews();
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

    if (id == R.id.profile) {
      startActivity(new Intent(BaseMainActivity.this, ProfileActivity.class));
    } else if (id == R.id.settings) {
      startActivity(new Intent(BaseMainActivity.this, ChangePasswordActivity.class));
    } else if (id == R.id.logout) {
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
      alertBuilder.setTitle("Confirmation");
      alertBuilder.setMessage(getString(R.string.logout_confirmation_message));
      alertBuilder.setPositiveButton(getString(R.string.logout),
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              SessionStore.saveAccessToken(null, BaseMainActivity.this);
              SessionStore.saveRefreshToken(null, BaseMainActivity.this);
              Intent intent = new Intent(BaseMainActivity.this, SplashActivity.class);
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

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void initViews() {
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

}
