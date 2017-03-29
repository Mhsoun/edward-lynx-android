package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.activities.CreateDevelopmentPlanActivity;
import com.ingenuitymobile.edwardlynx.activities.CreateFeedbackActivity;
import com.ingenuitymobile.edwardlynx.activities.FeedbackRequestsActivity;
import com.ingenuitymobile.edwardlynx.activities.ReportsActivity;
import com.ingenuitymobile.edwardlynx.adapters.DevelopmentPlanAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.models.Goal;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
  private DevelopmentPlanListFragment expiredFragment;

  private ArrayList<DevelopmentPlan> data;
  private ArrayList<DevelopmentPlan> unfinishedData;
  private ArrayList<DevelopmentPlan> completedData;
  private ArrayList<DevelopmentPlan> expiredData;

  public static DevelopmenPlansFragment newInstance(Context ctx) {
    DevelopmenPlansFragment fragment = new DevelopmenPlansFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.development_plans_bold));
    fragment.setArguments(bundle);
    return fragment;
  }

  public DevelopmenPlansFragment() {
    data = new ArrayList<>();
    unfinishedData = new ArrayList<>();
    completedData = new ArrayList<>();
    expiredData = new ArrayList<>();
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
    getData();
    LogUtil.e("AAA onResume survey");
  }

  private void initViews() {
    final SearchView searchView = (SearchView) mainView.findViewById(R.id.searchview);
    searchView.setQueryHint("Search Development Plan");

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
    imageView.setOnClickListener(onClickListener);
  }

  private void getData() {
    LogUtil.e("AAA getData Development plans");
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
            LogUtil.e("AAA onNext ");
            data.clear();
            unfinishedData.clear();
            completedData.clear();
            expiredData.clear();
            data.addAll(response.items);

            for (DevelopmentPlan plan : data) {
              final int size = plan.goals.size();
              int count = 0;
              if (plan.goals != null) {
                for (Goal goal : plan.goals) {
                  if (goal.checked == 1) {
                    count++;
                  }
                }
              }
              if (count == size) {
                completedData.add(plan);
              } else {
                try {
                  final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
                  final Date date = sdf.parse(plan.updatedAt);
                  if (date.getTime() < System.currentTimeMillis()) {
                    expiredData.add(plan);
                  } else {
                    unfinishedData.add(plan);
                  }
                } catch (Exception e) {
                  LogUtil.e("AAA", e);
                }
              }

            }
          }
        }));
  }

  private void setSelection() {
    switch (viewPager.getCurrentItem()) {
    case ALL:
      if (allFragment == null) {
        allFragment = new DevelopmentPlanListFragment();
      }
      allFragment.setData(data);
      break;
    case UNFINISHED:
      if (unfinishedFragment == null) {
        unfinishedFragment = new DevelopmentPlanListFragment();
      }
      unfinishedFragment.setData(unfinishedData);
      break;
    case COMPLETED:
      if (completedFragment == null) {
        completedFragment = new DevelopmentPlanListFragment();
      }
      completedFragment.setData(completedData);
      break;
    case EXPIRED:
      if (expiredFragment == null) {
        expiredFragment = new DevelopmentPlanListFragment();
      }
      expiredFragment.setData(expiredData);
      break;
    }
  }

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

  private SwipeRefreshLayout.OnRefreshListener refreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          getData();
        }
      };

  private class MyPagerAdapter extends FragmentPagerAdapter {
    private int NUM_ITEMS = 4;

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
          allFragment = new DevelopmentPlanListFragment();
        }
        allFragment.setData(data);
        return allFragment;
      case UNFINISHED:
        if (unfinishedFragment == null) {
          unfinishedFragment = new DevelopmentPlanListFragment();
        }
        unfinishedFragment.setData(unfinishedData);
        return unfinishedFragment;
      case COMPLETED:
        if (completedFragment == null) {
          completedFragment = new DevelopmentPlanListFragment();
        }
        completedFragment.setData(completedData);
        return completedFragment;
      case EXPIRED:
        if (expiredFragment == null) {
          expiredFragment = new DevelopmentPlanListFragment();
        }
        expiredFragment.setData(expiredData);
        return expiredFragment;
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
      case UNFINISHED:
        return getString(R.string.in_progress_bold);
      case COMPLETED:
        return getString(R.string.completed_bold);
      case EXPIRED:
        return getString(R.string.expired_bold);
      default:
        return "";
      }
    }
  }
}
