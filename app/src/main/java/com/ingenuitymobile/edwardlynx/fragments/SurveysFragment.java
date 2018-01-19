package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.activities.CreateFeedbackActivity;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class SurveysFragment extends BaseFragment {

  public static final int ALL      = 0;
  public static final int FEEDBACK = 1;
  public static final int LYNX     = 2;

  private View      mainView;
  private ViewPager viewPager;
  private ImageView createImage;

  private AllSurveysFragment       allFragment;
  private FeedbackRequestsFragment feedbackFragment;
  private SurveysListFragment      lynxFragment;

  private SearchView searchView;

  private int position;

  /**
   * Fragment to display all surveys grouped according to categories.
   */
  public SurveysFragment() {
    position = 0;
  }

  public static SurveysFragment newInstance(Context ctx) {
    SurveysFragment fragment = new SurveysFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.drawer_surveys).toUpperCase());
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    mainView = inflater.inflate(R.layout.fragment_surveys, container, false);
    initViews();
    LogUtil.e("AAA onCreateView SurveysFragment");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    LogUtil.e("AAA onResume SurveysFragment");
    searchView.setQuery("", false);
    searchView.setIconified(true);
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    searchView = (SearchView) mainView.findViewById(R.id.searchview);
    searchView.setQueryHint(getString(R.string.search_survey));
    searchView.setOnQueryTextListener(onQueryTextListener);

    viewPager = (ViewPager) mainView.findViewById(R.id.viewpager);
    final MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);

    final TabLayout tabLayout = (TabLayout) mainView.findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

    createImage = (ImageView) mainView.findViewById(R.id.image_create);
    createImage.setOnClickListener(onClickListener);

    viewPager.setCurrentItem(position);
    createImage.setVisibility(position == FEEDBACK ? View.VISIBLE : View.GONE);
  }

  /**
   * sets the position of the view pager
   * @param position the page of the view pager to be set
   */
  public void setPosition(final int position) {
    this.position = position;
    if (viewPager != null) {
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          viewPager.setCurrentItem(position);
          createImage.setVisibility(position == FEEDBACK ? View.VISIBLE : View.GONE);
        }
      }, 100);
      LogUtil.e("AAA setPosition " + position);
    }
  }

  /**
   * listener for changing the page of the view pager
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
      createImage.setVisibility(position == FEEDBACK ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  /**
   * sets the selected view based on the selected view pager position
   */
  private void setSelection() {
    switch (viewPager.getCurrentItem()) {
    case ALL:
      if (allFragment == null) {
        allFragment = new AllSurveysFragment();
      }
      allFragment.onResume();
      break;
    case FEEDBACK:
      if (feedbackFragment == null) {
        feedbackFragment = new FeedbackRequestsFragment();
      }
      feedbackFragment.onResume();
      break;
    case LYNX:
      if (lynxFragment == null) {
        lynxFragment = new SurveysListFragment();
      }
      lynxFragment.onResume();
      break;
    }
  }

  /**
   * listener for changing the query string in the text view and updates the list
   */
  private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener
      () {
    @Override
    public boolean onQueryTextSubmit(String query) {
      return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
      if (allFragment == null) {
        allFragment = new AllSurveysFragment();
      }
      allFragment.setQueryString(newText);

      if (feedbackFragment == null) {
        feedbackFragment = new FeedbackRequestsFragment();
      }
      feedbackFragment.setQueryString(newText);

      if (lynxFragment == null) {
        lynxFragment = new SurveysListFragment();
      }
      lynxFragment.setQueryString(newText);
      return false;
    }
  };

  /**
   * listener for clicking an item in the list, opens the create
   * feedback activity
   */
  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.image_create:
        startActivity(new Intent(getActivity(), CreateFeedbackActivity.class));
        break;
      }
    }
  };

  /**
   * custom pager adapter
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
        if (allFragment == null) {
          allFragment = new AllSurveysFragment();
        }
        return allFragment;
      case FEEDBACK:
        if (feedbackFragment == null) {
          feedbackFragment = new FeedbackRequestsFragment();
        }
        return feedbackFragment;
      case LYNX:
        if (lynxFragment == null) {
          lynxFragment = new SurveysListFragment();
        }
        return lynxFragment;
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
