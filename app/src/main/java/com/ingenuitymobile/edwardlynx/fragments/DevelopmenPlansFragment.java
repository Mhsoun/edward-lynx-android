package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
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

  private View mainView;

  private ArrayList<DevelopmentPlan> data;
  private ArrayList<DevelopmentPlan> unfinishedData;
  private ArrayList<DevelopmentPlan> completedData;
  private int                        type;

  private TextView allText;
  private TextView unfinishedText;
  private TextView completedText;

  private ViewPager viewPager;

  protected DevelopmentPlanListFragment allFragment;
  protected DevelopmentPlanListFragment unfinishedFragment;
  protected DevelopmentPlanListFragment completedFragment;

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
    type = 0;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    mainView = inflater.inflate(R.layout.fragment_surveys, container, false);
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
    allText = (TextView) mainView.findViewById(R.id.text_all);
    unfinishedText = (TextView) mainView.findViewById(R.id.text_unfinished);
    completedText = (TextView) mainView.findViewById(R.id.text_completed);

    allText.setOnClickListener(listener);
    unfinishedText.setOnClickListener(listener);
    completedText.setOnClickListener(listener);
    allText.setSelected(true);

    viewPager = (ViewPager) mainView.findViewById(R.id.viewpager);
    MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(onPageChangeListener);
  }


  private void setSelection() {
    allText.setSelected(false);
    unfinishedText.setSelected(false);
    completedText.setSelected(false);

    switch (type) {
    case ALL:
      allText.setSelected(true);
      if (allFragment == null) {
        allFragment = new DevelopmentPlanListFragment();
      }
      allFragment.setData(data);
      break;
    case UNFINISHED:
      unfinishedText.setSelected(true);
      if (unfinishedFragment == null) {
        unfinishedFragment = new DevelopmentPlanListFragment();
      }
      unfinishedFragment.setData(unfinishedData);
      break;
    case COMPLETED:
      completedText.setSelected(true);
      if (completedFragment == null) {
        completedFragment = new DevelopmentPlanListFragment();
      }
      completedFragment.setData(completedData);
      break;
    }
  }

  private void getData() {
    LogUtil.e("AAA getData Development plans");
    subscription.add(
        Shared.apiClient.getDevelopmentPlans(new Subscriber<DevelopmentPlansResponse>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            unfinishedData.clear();
            completedData.clear();

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
                unfinishedData.add(plan);
              }
            }
            setSelection();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
          }

          @Override
          public void onNext(final DevelopmentPlansResponse response) {
            LogUtil.e("AAA onNext ");
            data.clear();
            data.addAll(response.items);
          }
        }));
  }

  private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager
      .OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      type = position;
      setSelection();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  private View.OnClickListener listener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.text_all:
        type = ALL;
        break;
      case R.id.text_unfinished:
        type = UNFINISHED;
        break;
      case R.id.text_completed:
        type = COMPLETED;
        break;
      }

      setSelection();
      viewPager.setCurrentItem(type);
    }
  };

  class MyPagerAdapter extends FragmentPagerAdapter {
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
      case 0:
        if (allFragment == null) {
          allFragment = new DevelopmentPlanListFragment();
          allFragment.setData(data);
        }
        return allFragment;
      case 1:
        if (unfinishedFragment == null) {
          unfinishedFragment = new DevelopmentPlanListFragment();
          unfinishedFragment.setData(unfinishedData);
        }
        return unfinishedFragment;
      case 2: // Fragment # 1 - This will show SecondFragment
        if (completedFragment == null) {
          completedFragment = new DevelopmentPlanListFragment();
          completedFragment.setData(completedData);
        }
        return completedFragment;
      default:
        return null;
      }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
      return "Page " + position;
    }

  }
}
