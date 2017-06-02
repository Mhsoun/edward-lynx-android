package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.QuestionRateFragment;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.responses.SurveyResultsResponse;
import com.ingenuitymobile.edwardlynx.fragments.AveragesFragment;
import com.ingenuitymobile.edwardlynx.fragments.BlindspotFragment;
import com.ingenuitymobile.edwardlynx.fragments.BreakdownFragments;
import com.ingenuitymobile.edwardlynx.fragments.CommentsFragment;
import com.ingenuitymobile.edwardlynx.fragments.DetailedSummaryFragment;
import com.ingenuitymobile.edwardlynx.fragments.RadarFragment;
import com.ingenuitymobile.edwardlynx.fragments.ResponseRateFragment;
import com.ingenuitymobile.edwardlynx.fragments.YesNoFragment;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Date;

import rx.Subscriber;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyReportActivity extends BaseActivity {
  private long   id;
  private Survey survey;

  private PagerAdapter        adapter;
  private CirclePageIndicator circleIndicator;

  private ViewPager viewPager;
  private ImageView previousImage;
  private ImageView nextImage;

  private TextView dateText;
  private TextView detailsText;

  private ArrayList<Fragment> fragments;

  public SurveyReportActivity() {
    fragments = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_survey_report);

    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.findViewById(R.id.image_share).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO
      }
    });

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    id = getIntent().getLongExtra("id", 0L);
    initViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getData();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    dateText = (TextView) findViewById(R.id.text_date);
    detailsText = (TextView) findViewById(R.id.text_date_details);

    previousImage = (ImageView) findViewById(R.id.image_previous);
    nextImage = (ImageView) findViewById(R.id.image_next);

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new MyPagerAdapter(getSupportFragmentManager());
    circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

    previousImage.setOnClickListener(onClickListener);
    nextImage.setOnClickListener(onClickListener);

    dateText.setText("");
    detailsText.setText(getString(R.string.details_circle_chart, 0, 0));
  }

  private void getData() {
    LogUtil.e("AAA getData Survey details " + id);
    subscription.add(Shared.apiClient.getSurvey(id, new Subscriber<Survey>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA Survey details onCompleted ");

        detailsText.setText(getString(R.string.details_circle_chart, survey.stats.invited,
            survey.stats.answered));

        if (survey.stats.answered > 0) {
          getSurveyQuestions();
        } else {
          findViewById(R.id.text_empty).setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA Survey details onError " + e);
      }

      @Override
      public void onNext(Survey surveyResponse) {

        survey = surveyResponse;
        setTitle(surveyResponse.name);

        try {
          Date date = DateUtil.getAPIFormat().parse(survey.endDate);
          dateText.setText(DateUtil.getDisplayFormat().format(date));
        } catch (Exception e) {
          dateText.setText("");
        }
      }
    }));
  }

  private void getSurveyQuestions() {
    LogUtil.e("AAA getData questions " + id);
    subscription.add(Shared.apiClient.getSurveyResults(id, new Subscriber<SurveyResultsResponse>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA questions onCompleted ");

        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setOnPageChangeListener(pageChangeListener);
        setNavigation(0);
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA questions onError " + e);
      }

      @Override
      public void onNext(SurveyResultsResponse response) {
        setData(response);
      }
    }));
  }

  private void setNavigation(int index) {
    findViewById(R.id.layout_navigation).setVisibility(View.VISIBLE);

    previousImage.setVisibility(index == 0 ? View.INVISIBLE : View.VISIBLE);
    nextImage.setVisibility(index == fragments.size() - 1 ? View.INVISIBLE : View.VISIBLE);
  }

  private void setData(SurveyResultsResponse response) {
    if (response == null) {
      return;
    }

    if (response.responseRates != null && !response.responseRates.isEmpty()) {
      final ResponseRateFragment responseRateFragment = new ResponseRateFragment();
      responseRateFragment.setDataSet(response.responseRates);
      fragments.add(responseRateFragment);
    }

    final AveragesFragment averagesFragment = new AveragesFragment();
    averagesFragment.setDataSet(response.averages, response.ioc);
    fragments.add(averagesFragment);

    if (response.radarDiagrams != null && !response.radarDiagrams.isEmpty()) {
      final RadarFragment radarFragment = new RadarFragment();
      radarFragment.setDataSet(response.radarDiagrams);
      fragments.add(radarFragment);
    }

    if (response.comments != null && !response.comments.isEmpty()) {
      final CommentsFragment commentsFragment = new CommentsFragment();
      commentsFragment.setDataSet(response.comments);
      fragments.add(commentsFragment);
    }

    QuestionRateFragment highestManagerFragment = null;
    QuestionRateFragment highestOthersFragment = null;
    QuestionRateFragment lowerstManagerFragment = null;
    QuestionRateFragment lowestOthersFragment = null;
    if (response.rate != null) {
      if (response.rate.highest != null) {
        if (response.rate.highest.managers != null && !response.rate.highest.managers.isEmpty()) {
          LogUtil.e("AAA " + response.rate.highest.managers.isEmpty());
          highestManagerFragment = new QuestionRateFragment();
          highestManagerFragment.setDataSet(
              response.rate.highest.managers,
              getString(R.string.highest_rate_manager)
          );
        }

        if (response.rate.highest.others != null && !response.rate.highest.others.isEmpty()) {
          highestOthersFragment = new QuestionRateFragment();
          highestOthersFragment.setDataSet(
              response.rate.highest.others,
              getString(R.string.highest_rate_others)
          );
        }
      }

      if (response.rate.lowest != null) {
        if (response.rate.lowest.managers != null && !response.rate.lowest.managers.isEmpty()) {
          lowerstManagerFragment = new QuestionRateFragment();
          lowerstManagerFragment.setDataSet(
              response.rate.lowest.managers,
              getString(R.string.lowest_rate_manager)
          );
        }

        if (response.rate.lowest.others != null && !response.rate.lowest.others.isEmpty()) {
          lowestOthersFragment = new QuestionRateFragment();
          lowestOthersFragment.setDataSet(
              response.rate.lowest.others,
              getString(R.string.lowest_rate_others)
          );
        }
      }
    }

    if (highestManagerFragment != null) {
      fragments.add(highestManagerFragment);
    }

    if (lowerstManagerFragment != null) {
      fragments.add(lowerstManagerFragment);
    }

    if (highestOthersFragment != null) {
      fragments.add(highestOthersFragment);
    }

    if (lowestOthersFragment != null) {
      fragments.add(lowestOthersFragment);
    }

    if (response.blindspot != null) {
      if (response.blindspot.overestimated != null && !response.blindspot.overestimated.isEmpty()) {
        final BlindspotFragment overBlindspotFragment = new BlindspotFragment();
        overBlindspotFragment.setDataSet(
            response.blindspot.overestimated,
            getString(R.string.overestimated_attributes),
            getString(R.string.overestimated_attributes_details)
        );
        fragments.add(overBlindspotFragment);
      }

      if (response.blindspot.underestimated != null &&
          !response.blindspot.underestimated.isEmpty()) {
        final BlindspotFragment underBlindspotFragment = new BlindspotFragment();
        underBlindspotFragment.setDataSet(
            response.blindspot.underestimated,
            getString(R.string.underestimated_attributes),
            getString(R.string.underestimated_attributes_details)
        );

        fragments.add(underBlindspotFragment);
      }
    }

    if (response.breakdown != null && !response.breakdown.isEmpty()) {
      final BreakdownFragments breakdownFragments = new BreakdownFragments();
      breakdownFragments.setDataSet(response.breakdown);
      fragments.add(breakdownFragments);
    }

    if (response.detailedSummaries != null && !response.detailedSummaries.isEmpty()) {
      DetailedSummaryFragment detailedSummaryFragment = new DetailedSummaryFragment();
      detailedSummaryFragment.setDataSet(response.detailedSummaries);
      fragments.add(detailedSummaryFragment);
    }

    if (response.yesNos != null && !response.yesNos.isEmpty()) {
      YesNoFragment yesNoFragment = new YesNoFragment();
      yesNoFragment.setDataSet(response.yesNos);
      fragments.add(yesNoFragment);
    }
  }

  private ViewPager.OnPageChangeListener pageChangeListener =
      new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
          LogUtil.e("AAA onPageSelected " + position);
          setNavigation(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
      };

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
      case R.id.image_next:
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
          }
        }, 100);
        break;
      case R.id.image_previous:
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
          }
        }, 100);
        break;
      }
    }
  };

  private class MyPagerAdapter extends FragmentPagerAdapter {

    MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
      return fragments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
      return super.getPageTitle(position);
    }
  }
}
