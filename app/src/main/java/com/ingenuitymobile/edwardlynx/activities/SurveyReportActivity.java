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
import com.ingenuitymobile.edwardlynx.api.models.Breakdown;
import com.ingenuitymobile.edwardlynx.api.models.Comment;
import com.ingenuitymobile.edwardlynx.api.models.CommentItem;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.responses.SurveyResultsResponse;
import com.ingenuitymobile.edwardlynx.fragments.AveragesFragment;
import com.ingenuitymobile.edwardlynx.fragments.BreakdownFragments;
import com.ingenuitymobile.edwardlynx.fragments.CommentsFragment;
import com.ingenuitymobile.edwardlynx.fragments.DetailedSummaryFragment;
import com.ingenuitymobile.edwardlynx.fragments.RadarFragment;
import com.ingenuitymobile.edwardlynx.fragments.ResponseRateFragment;
import com.ingenuitymobile.edwardlynx.utils.DateUtil;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;
import rx.Subscriber;

/**
 * Created by memengski on 4/7/17.
 */

public class SurveyReportActivity extends BaseActivity {
  private long   id;
  private Survey survey;

  private PagerAdapter    adapter;
  private CircleIndicator circleIndicator;

  private ViewPager viewPager;
  private ImageView previousImage;
  private ImageView nextImage;

  private TextView dateText;
  private TextView detailsText;

  private ResponseRateFragment    responseRateFragment;
  private AveragesFragment        averagesFragment;
  private RadarFragment           radarFragment;
  private CommentsFragment        commentsFragment;
  private BreakdownFragments      breakdownFragments;
  private DetailedSummaryFragment detailedSummaryFragment;

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
    circleIndicator = (CircleIndicator) findViewById(R.id.indicator);

    previousImage.setOnClickListener(onClickListener);
    nextImage.setOnClickListener(onClickListener);

    dateText.setText("");
    detailsText.setText(getString(R.string.details_circle_chart, 0, 0));

    if (responseRateFragment == null) {
      responseRateFragment = new ResponseRateFragment();
    }

    if (averagesFragment == null) {
      averagesFragment = new AveragesFragment();
    }


    if (radarFragment == null) {
      radarFragment = new RadarFragment();
    }

    if (commentsFragment == null) {
      commentsFragment = new CommentsFragment();
    }

    if (breakdownFragments == null) {
      breakdownFragments = new BreakdownFragments();
    }

    if (detailedSummaryFragment == null) {
      detailedSummaryFragment = new DetailedSummaryFragment();
    }
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

        viewPager.setOnPageChangeListener(pageChangeListener);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        setNavigation();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA questions onError " + e);
      }

      @Override
      public void onNext(SurveyResultsResponse response) {
        responseRateFragment.setDataSet(response.responseRates);
        averagesFragment.setDataSet(response.averages, response.ioc);
        radarFragment.setDataSet(response.radarDiagrams);
        commentsFragment.setDataSet(response.comments);
        breakdownFragments.setDataSet(response.breakdown);
        detailedSummaryFragment.setDataSet(response.detailedSummaries);
      }
    }));
  }

  private void setNavigation() {
    findViewById(R.id.layout_navigation).setVisibility(View.VISIBLE);

    previousImage.setVisibility(viewPager.getCurrentItem() == 0 ? View.INVISIBLE : View.VISIBLE);
    nextImage.setVisibility(
        viewPager.getCurrentItem() == MyPagerAdapter.SIZE - 1 ? View.INVISIBLE : View.VISIBLE);
  }

  private ViewPager.OnPageChangeListener pageChangeListener =
      new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
          LogUtil.e("AAA onPageSelected " + position);
          setNavigation();
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
    static final int SIZE = 6;

    MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
      return SIZE;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
      switch (position) {
      case 0:
        return responseRateFragment;
      case 1:
        return averagesFragment;
      case 2:
        return radarFragment;
      case 3:
        return commentsFragment;
      case 4:
        return breakdownFragments;
      case 5:
        return detailedSummaryFragment;
      default:
        return null;
      }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
      return super.getPageTitle(position);
    }
  }
}
