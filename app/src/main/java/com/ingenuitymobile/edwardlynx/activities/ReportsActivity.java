package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.fragments.AllReportsFragment;
import com.ingenuitymobile.edwardlynx.fragments.FeedbackReportsFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveyReportsFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 16/01/2017.
 */

public class ReportsActivity extends BaseActivity {


  public static final int ALL      = 0;
  public static final int FEEDBACK = 1;
  public static final int LYNX     = 2;

  private ViewPager viewPager;

  private AllReportsFragment      allFragments;
  private FeedbackReportsFragment feedbackFragments;
  private SurveyReportsFragment   lynxFragments;

  private int position;

  public ReportsActivity() {
    position = 0;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reports);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.results_dashboard).toUpperCase());

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
    final SearchView searchView = (SearchView) findViewById(R.id.searchview);
    searchView.setOnQueryTextListener(onQueryTextListener);

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);

    final TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

    viewPager.setCurrentItem(position);
  }

  /**
   * sets the selected fragment
   */
  private void setSelection() {
    switch (viewPager.getCurrentItem()) {
    case ALL:
      if (allFragments == null) {
        allFragments = new AllReportsFragment();
      }
      allFragments.onResume();
      break;
    case FEEDBACK:
      if (feedbackFragments == null) {
        feedbackFragments = new FeedbackReportsFragment();
      }
      feedbackFragments.onResume();
      break;
    case LYNX:
      if (lynxFragments == null) {
        lynxFragments = new SurveyReportsFragment();
      }
      lynxFragments.onResume();
      break;
    }
  }

  /**
   * listener for the changing of query text in the search view
   */
  private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener
      () {
    @Override
    public boolean onQueryTextSubmit(String query) {
      return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
      if (allFragments == null) {
        allFragments = new AllReportsFragment();
      }
      allFragments.setQueryString(newText);

      if (feedbackFragments == null) {
        feedbackFragments = new FeedbackReportsFragment();
      }
      feedbackFragments.setQueryString(newText);

      if (lynxFragments == null) {
        lynxFragments = new SurveyReportsFragment();
      }
      lynxFragments.setQueryString(newText);
      return false;
    }
  };

  /**
   * listener for the changing of page in the reports view pager
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
   * custom pager adapter for the reports fragment
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
      case ALL:
        if (allFragments == null) {
          allFragments = new AllReportsFragment();
        }
        return allFragments;
      case FEEDBACK:
        if (feedbackFragments == null) {
          feedbackFragments = new FeedbackReportsFragment();
        }
        return feedbackFragments;
      case LYNX:
        if (lynxFragments == null) {
          lynxFragments = new SurveyReportsFragment();
        }
        return lynxFragments;
      default:
        return null;
      }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
      case ALL:
        return getString(R.string.all).toUpperCase();
      case FEEDBACK:
        return getString(R.string.instant_feedback).toUpperCase();
      case LYNX:
        return getString(R.string.lynx_measurement).toUpperCase();
      default:
        return "";
      }
    }
  }
}


