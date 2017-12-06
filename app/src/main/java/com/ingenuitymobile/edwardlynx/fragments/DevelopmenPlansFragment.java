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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.CreateDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.api.models.Action;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class DevelopmenPlansFragment extends BaseFragment {

  private static final int ALL        = 0;
  private static final int UNFINISHED = 1;
  private static final int COMPLETED  = 2;
  private static final int EXPIRED    = 3;

  private View               mainView;
  private SwipeRefreshLayout refreshLayout;
  private ViewPager          viewPager;

  private DevelopmentPlanListFragment allFragment;
  private DevelopmentPlanListFragment unfinishedFragment;
  private DevelopmentPlanListFragment completedFragment;

  private ArrayList<DevelopmentPlan> data;
  private ArrayList<DevelopmentPlan> unfinishedData;
  private ArrayList<DevelopmentPlan> completedData;

  private SearchView searchView;

  private int     position;
  private boolean isFromTeam;
  private long    id;

  /**
   * Fragment to display the development plans.
   * @param ctx the context to be used in the fragment
   * @param isFromTeam indicator if the development plans to be displayed is for team
   * @param id user id
   * @return the created development plans fragment
   */
  public static DevelopmenPlansFragment newInstance(Context ctx, boolean isFromTeam, long id) {
    DevelopmenPlansFragment fragment = new DevelopmenPlansFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.drawer_development_plans).toUpperCase());
    fragment.setArguments(bundle);
    fragment.isFromTeam = isFromTeam;
    fragment.id = id;
    return fragment;
  }

  public DevelopmenPlansFragment() {
    data = new ArrayList<>();
    unfinishedData = new ArrayList<>();
    completedData = new ArrayList<>();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    mainView = inflater.inflate(R.layout.fragment_dev_plans, container, false);
    initViews();
    LogUtil.e("AAA onCreateView survey2");
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    searchView.setQuery("", false);
    searchView.setIconified(true);
    getData();
    LogUtil.e("AAA onResume DevelopmenPlansFragment");
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    searchView = (SearchView) mainView.findViewById(R.id.searchview);
    searchView.setQueryHint(getString(R.string.search_development_plans));
    searchView.setOnQueryTextListener(onQueryTextListener);

    viewPager = (ViewPager) mainView.findViewById(R.id.viewpager);
    MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);

    final TabLayout tabLayout = (TabLayout) mainView.findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);

    final ImageView imageView = (ImageView) mainView.findViewById(R.id.image_create_dev_plan);
    imageView.setVisibility(isFromTeam ? View.GONE : View.VISIBLE);
    imageView.setOnClickListener(onClickListener);

    viewPager.setCurrentItem(position);

    if (allFragment == null) {
      allFragment = new DevelopmentPlanListFragment();
      allFragment.isFromTeam = isFromTeam;
    }

    if (unfinishedFragment == null) {
      unfinishedFragment = new DevelopmentPlanListFragment();
      unfinishedFragment.isFromTeam = isFromTeam;
    }

    if (completedFragment == null) {
      completedFragment = new DevelopmentPlanListFragment();
      completedFragment.isFromTeam = isFromTeam;
    }
  }

  /**
   * retrieves the team development plans from the API
   */
  private void getData() {
    LogUtil.e("AAA getData Development plans");
    if (isFromTeam) {
      getUserDevPlan();
    } else {
      subscription.add(
          Shared.apiClient.getDevelopmentPlans(new Subscriber<DevelopmentPlansResponse>() {
            @Override
            public void onCompleted() {
              LogUtil.e("AAA onCompleted ");
              refreshLayout.setRefreshing(false);
              setSelection();
            }

            @Override
            public void onError(Throwable e) {
              LogUtil.e("AAA onError " + e);
              refreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(final DevelopmentPlansResponse response) {
              setResponse(response);
            }
          }));
    }
  }

  /**
   * retrieves the user development plans from the API
   */
  private void getUserDevPlan() {
    subscription.add(Shared.apiClient.getUserDevelopmentPlans(id,
        new Subscriber<DevelopmentPlansResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            refreshLayout.setRefreshing(false);
            setSelection();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
            refreshLayout.setRefreshing(false);
          }

          @Override
          public void onNext(DevelopmentPlansResponse response) {
            setResponse(response);
          }
        }));
  }

  /**
   * updates the data to be used in the fragment
   * @param response the retrieved data from the API
   */
  private void setResponse(DevelopmentPlansResponse response) {
    LogUtil.e("AAA onNext ");
    data.clear();
    unfinishedData.clear();
    completedData.clear();
    data.addAll(response.items);

    for (DevelopmentPlan plan : data) {
      final int size = plan.goals.size();
      int count = 0;
      if (plan.goals != null) {
        boolean isUnfinished = false;
        for (Goal goal : plan.goals) {
          if (goal.isCompleted()) {
            count++;
          } else if (goal.isUnfinished()) {
            isUnfinished = true;
          }
        }

        if (count != 0 && count == size) {
          completedData.add(plan);
        } else if (isUnfinished || count != 0) {
          unfinishedData.add(plan);
        }
      }
    }
  }

  /**
   * sets the view in the fragment based on the selected item in the viewpager
   */
  private void setSelection() {
    switch (viewPager.getCurrentItem()) {
    case ALL:
      allFragment.setData(data);
      break;
    case UNFINISHED:
      unfinishedFragment.setData(unfinishedData);
      break;
    case COMPLETED:
      completedFragment.setData(completedData);
      break;
    }
  }

  /**
   * updates the position of the view pager and updates the fragment view
   * @param position the position of the view pager
   */
  public void setPosition(final int position) {
    this.position = position;
    if (viewPager != null) {
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          viewPager.setCurrentItem(position);
        }
      }, 100);
      LogUtil.e("AAA setPosition " + position);
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
      allFragment.setQueryString(newText);
      unfinishedFragment.setQueryString(newText);
      completedFragment.setQueryString(newText);
      return false;
    }
  };

  /**
   * listener for clicking the create development plan button
   */
  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.image_create_dev_plan:
        startActivity(new Intent(getActivity(), CreateDevelopmentPlanActivity.class));
        break;
      }
    }
  };

  /**
   * listener for changing the page on the view pager
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
   * listener for the pull to refresh functionality
   */
  private SwipeRefreshLayout.OnRefreshListener refreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          getData();
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
        allFragment.setData(data);
        return allFragment;
      case UNFINISHED:
        unfinishedFragment.setData(unfinishedData);
        return unfinishedFragment;
      case COMPLETED:
        completedFragment.setData(completedData);
        return completedFragment;
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
      case UNFINISHED:
        return getString(R.string.in_progress).toUpperCase();
      case COMPLETED:
        return getString(R.string.completed_text).toUpperCase();
      case EXPIRED:
        return getString(R.string.expired).toUpperCase();
      default:
        return "";
      }
    }
  }
}
