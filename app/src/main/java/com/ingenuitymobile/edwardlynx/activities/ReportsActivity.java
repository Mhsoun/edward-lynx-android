package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.FeedbackAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Feedback;
import com.ingenuitymobile.edwardlynx.api.responses.FeedbacksResponse;
import com.ingenuitymobile.edwardlynx.fragments.AllReportsFragment;
import com.ingenuitymobile.edwardlynx.fragments.AllSurveysFragment;
import com.ingenuitymobile.edwardlynx.fragments.BaseFragment;
import com.ingenuitymobile.edwardlynx.fragments.FeedbackReportsFragment;
import com.ingenuitymobile.edwardlynx.fragments.FeedbackRequestsFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveyReportsFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveysFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveysListFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

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

    initViews();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    final SearchView searchView = (SearchView) findViewById(R.id.searchview);
    searchView.setQueryHint("Search Reports");
    searchView.setOnQueryTextListener(onQueryTextListener);

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);

    final TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

    viewPager.setCurrentItem(position);
  }

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
        return getString(R.string.all_bold);
      case FEEDBACK:
        return getString(R.string.instant_feedback_bold);
      case LYNX:
        return getString(R.string.lynx_measurement);
      default:
        return "";
      }
    }
  }
}


