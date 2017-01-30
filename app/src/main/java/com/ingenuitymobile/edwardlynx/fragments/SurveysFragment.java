package com.ingenuitymobile.edwardlynx.fragments;

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
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class SurveysFragment extends BaseFragment {

  private static final int ALL        = 0;
  private static final int UNFINISHED = 1;
  private static final int COMPLETED  = 2;

  private View mainView;

  private ArrayList<Survey> data;
  private ArrayList<Survey> unfinishedData;
  private ArrayList<Survey> completedData;
  private int               type;

  private TextView allText;
  private TextView unfinishedText;
  private TextView completedText;

  private ViewPager viewPager;

  protected SurveyListFragment allFragment;
  protected SurveyListFragment unfinishedFragment;
  protected SurveyListFragment completedFragment;

  public static SurveysFragment newInstance() {
    SurveysFragment fragment = new SurveysFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", "Surveys");
    fragment.setArguments(bundle);
    return fragment;
  }

  public SurveysFragment() {
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
      allFragment.setData(data);
      break;
    case UNFINISHED:
      unfinishedText.setSelected(true);
      if (unfinishedFragment == null) {
        unfinishedFragment = new SurveyListFragment();
      }
      unfinishedFragment.setData(unfinishedData);
      break;
    case COMPLETED:
      completedText.setSelected(true);
      if (completedFragment == null) {
        completedFragment = new SurveyListFragment();
      }
      completedFragment.setData(completedData);
      break;
    }
  }

  private void getData() {
    LogUtil.e("AAA getData survey");
    subscription.add(Shared.apiClient.getSurveys(1, new Subscriber<Surveys>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA onCompleted ");
        unfinishedData.clear();
        completedData.clear();

        for (Survey survey : data) {
          if (survey.status == Survey.UNFINISHED) {
            unfinishedData.add(survey);
          } else if (survey.status == Survey.COMPLETED) {
            completedData.add(survey);
          }
        }

        setSelection();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA onError " + e);
      }

      @Override
      public void onNext(final Surveys surveys) {
        LogUtil.e("AAA onNext ");
        data.clear();
        data.addAll(surveys.items);
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
          allFragment = new SurveyListFragment();
          allFragment.setData(data);
        }
        return allFragment;
      case 1:
        if (unfinishedFragment == null) {
          unfinishedFragment = new SurveyListFragment();
          unfinishedFragment.setData(unfinishedData);
        }
        return unfinishedFragment;
      case 2: // Fragment # 1 - This will show SecondFragment
        if (completedFragment == null) {
          completedFragment = new SurveyListFragment();
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
