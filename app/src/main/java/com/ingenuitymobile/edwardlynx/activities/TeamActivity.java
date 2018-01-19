package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.fragments.IndividualProgressFragment;
import com.ingenuitymobile.edwardlynx.fragments.TeamProgressFragment;
import com.ingenuitymobile.edwardlynx.fragments.TeamReportsFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by memengski on 6/21/17.
 * Activity that displays the the progress of the team.
 */

public class TeamActivity extends BaseActivity {

  public static final int INDIVIDUAL = 0;
  public static final int TEAM       = 1;
  public static final int RESULT     = 2;

  private ViewPager viewPager;

  private IndividualProgressFragment individualFragment;
  private TeamProgressFragment       teamFragment;
  private TeamReportsFragment        resultFragment;

  private int position;

  public TeamActivity() {
    position = 0;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.manager_view).toUpperCase());

    initViews();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * initViews initializes views used in the activity
   */
  private void initViews() {
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    final MyPagerAdapter adapter = new MyPagerAdapter(
        getSupportFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);

    final TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

    viewPager.setCurrentItem(position);

    if (individualFragment == null) {
      individualFragment = new IndividualProgressFragment();
    }
    if (teamFragment == null) {
      teamFragment = new TeamProgressFragment();
    }
    if (resultFragment == null) {
      resultFragment = new TeamReportsFragment();
    }
  }

  /**
   * displays the fragment depending on the current selection of the view pager
   */
  private void setSelection() {
    switch (viewPager.getCurrentItem()) {
    case INDIVIDUAL:
      individualFragment.onResume();
      break;
    case TEAM:
      teamFragment.onResume();
      break;
    case RESULT:
      resultFragment.onResume();
      break;
    }
  }

  /**
   * listener for the view pager on changing the page, updates the current fragment
   * depending on what is currently selected
   */
  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener
      () {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      LogUtil.e("AAA onPageSelected " + position);
      setSelection();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  /**
   * custom pager adapter to render fragment for the currently selected page
   */
  private class MyPagerAdapter extends FragmentPagerAdapter {
    private int NUM_ITEMS = 3;

    MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
      return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
      switch (position) {
      case INDIVIDUAL:
        return individualFragment;
      case TEAM:
        return teamFragment;
      case RESULT:
        return resultFragment;
      default:
        return null;
      }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
      case INDIVIDUAL:
        return getString(R.string.individual).toUpperCase();
      case TEAM:
        return getString(R.string.team).toUpperCase();
      case RESULT:
        return getString(R.string.results_dashboard).toUpperCase();
      default:
        return "";
      }
    }
  }
}
