package com.ingenuitymobile.edwardlynx.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.SurveyQuestionsAdapter;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerBody;
import com.ingenuitymobile.edwardlynx.api.bodyparams.AnswerParam;
import com.ingenuitymobile.edwardlynx.api.models.Category;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.api.models.Questions;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.fragments.PopupDialogFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveyDetailedFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveyQuestionsFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by memengski on 4/7/17.
 * Activity to display the survey questions to be answered.
 */

public class SurveyQuestionsActivity extends BaseActivity {

  private TextView submitText;
  private TextView saveDraftsText;

  private long                id;
  private PagerAdapter        adapter;
  private CirclePageIndicator circleIndicator;
  private List<Category>      data;
  private List<AnswerBody>    bodies;
  private Survey              survey;
  private String              key;

  private ViewPager viewPager;
  private ImageView previousImage;
  private ImageView nextImage;

  private SurveyDetailedFragment surveyDetailedFragment;

  public SurveyQuestionsActivity() {
    data = new ArrayList<>();
    bodies = new ArrayList<>();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_survey_questions);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    id = getIntent().getLongExtra("id", 0L);
    key = getIntent().getStringExtra("key");

    initViews();
    getData();
    setTitle(getString(R.string.answer_survey).toUpperCase());

    NotificationManager notificationManager =
        (NotificationManager) getApplicationContext()
            .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancel((int) id);
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
    submitText = (TextView) findViewById(R.id.text_submit);
    saveDraftsText = (TextView) findViewById(R.id.text_save_drafts);

    previousImage = (ImageView) findViewById(R.id.image_previous);
    nextImage = (ImageView) findViewById(R.id.image_next);

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new MyPagerAdapter(getSupportFragmentManager());
    circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

    previousImage.setOnClickListener(onClickListener);
    nextImage.setOnClickListener(onClickListener);

    if (surveyDetailedFragment == null) {
      surveyDetailedFragment = new SurveyDetailedFragment();
    }
  }

  /**
   * retrieves the survey details from the API based on the survey id
   */
  private void getData() {
    LogUtil.e("AAA getData Survey details");
    subscription.add(Shared.apiClient.getSurvey(id, key, new Subscriber<Survey>() {
      @Override
      public void onCompleted() {
        LogUtil.e("AAA Survey details onCompleted ");
        getSurveyQuestions();
      }

      @Override
      public void onError(Throwable e) {
        LogUtil.e("AAA Survey details onError " + e);
      }

      @Override
      public void onNext(Survey surveyResponse) {
        LogUtil.e("AAA Survey details onNext ");
        survey = surveyResponse;
        surveyDetailedFragment.setSurvey(survey);
      }
    }));
  }

  /**
   * retrieves the survey questions from the API based on the survey id and key
   */
  private void getSurveyQuestions() {
    LogUtil.e("AAA getData questions");
    subscription.add(Shared.apiClient.getSurveyQuestions(id, key, new Subscriber<Questions>() {
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
      public void onNext(Questions questions) {
        LogUtil.e("AAA questions onNext");

        data.clear();
        data.addAll(questions.items);

        for (Category category : data) {
          for (Question question : category.questions) {
            if (question.id != 0L && question.value != null) {
              AnswerBody body = new AnswerBody();
              body.question = question.id;
              body.answer = String.valueOf(question.value);
              bodies.add(body);
            }
          }
          Question question = new Question();
          question.isSectionHeader = true;
          question.text = category.title;
          category.questions.add(0, question);
        }
      }
    }));
  }

  /**
   * updates the navigation views and screen components when the page is changed
   * @param position
   */
  private void setNavigation(int position) {
    findViewById(R.id.layout_navigation).setVisibility(View.VISIBLE);

    previousImage.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
    nextImage.setVisibility((position - 1) == data.size() - 1 ? View.INVISIBLE : View.VISIBLE);

    if (survey.status != Survey.COMPLETED) {
      submitText.setVisibility((position - 1) != data.size() - 1 ? View.GONE : View.VISIBLE);
      saveDraftsText.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }
  }

  /**
   * action to be invoked when the submit button is clicked,
   * verifies if the survey questions are all answered and submits to the API for saving
   * @param v the current activity view
   */
  public void submit(View v) {
    if (TextUtils.isEmpty(survey.key)) {
      Toast.makeText(context, getString(R.string.no_access), Toast.LENGTH_SHORT).show();
      return;
    }

    final TextView textView = (TextView) v;
    AnswerParam param = new AnswerParam();
    param.answers = bodies;
    param.key = survey.key;
    param.isFinal = v.getId() == R.id.text_submit;

    LogUtil.e("AAA id " + id);
    LogUtil.e("AAA " + param.toString());
    progressDialog.show();
    subscription.add(Shared.apiClient.postAnswerSurvey(id, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted");
            progressDialog.dismiss();

            if (textView.getId() == R.id.text_submit) {
              PopupDialogFragment dialogFragment = PopupDialogFragment.newInstance(false,
                  survey.name,
                  getString(R.string.survey_completed), getString(R.string.label_popup));
              dialogFragment.show(getSupportFragmentManager(), "Popup");
            } else {
              Toast.makeText(SurveyQuestionsActivity.this,
                  getString(R.string.saved_to_drafts), Toast.LENGTH_SHORT).show();
              finish();
            }
          }

          @Override
          public void onError(Throwable e) {
            progressDialog.dismiss();
            final RetrofitError retrofitError = (RetrofitError) e;
            final Response response = (Response) retrofitError.getBodyAs(Response.class);
            if (!TextUtils.isEmpty(response.message)) {
              Toast.makeText(SurveyQuestionsActivity.this, response.message,
                      Toast.LENGTH_SHORT).show();
            }
            textView.setText(
                textView.getId() == R.id.text_submit ? R.string.submit : R.string.save_to_drafts);
            LogUtil.e("AAA onError" + e);
          }

          @Override
          public void onNext(Response response) {
            LogUtil.e("AAA onNext");
            progressDialog.dismiss();
          }
        }));
  }

  /**
   * listener for clicking the navigation helpers which are the go to next or previous page
   */
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

  /**
   * listener for the changing of the page in the questions view pager,
   * updates the navigation view when the page is changed
   */
  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener
      () {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      LogUtil.e("AAA onPageSelected " + position);
      setNavigation(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  /**
   * listener for the answering the surveys in the survey questions adapter and
   * updates the parameter body to be submitted to the API for saving
   */
  private SurveyQuestionsAdapter.OnAnswerItemListener listener = new SurveyQuestionsAdapter
      .OnAnswerItemListener() {
    @Override
    public void onAnswer(long id, String value) {
      for (int x = 0; x < bodies.size(); x++) {
        AnswerBody body = bodies.get(x);
        if (id == body.question) {
          body.answer = value;
          return;
        }
      }

      AnswerBody body = new AnswerBody();
      body.question = id;
      body.answer = value;
      bodies.add(body);
    }

    @Override
    public void onExplanation(long id, String explanation) {
      for (int x = 0; x < bodies.size(); x++) {
        AnswerBody body = bodies.get(x);
        if (id == body.question) {
          body.explanation = explanation;
          return;
        }
      }

      AnswerBody body = new AnswerBody();
      body.question = id;
      body.explanation = explanation;
      bodies.add(body);
    }
  };

  /**
   * custom pager adapter for the survey fragment to facilitate the display
   * of questions and survey details
   */
  private class MyPagerAdapter extends FragmentPagerAdapter {

    MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
      return data.size() + 1;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
      switch (position) {
      case 0:
        return surveyDetailedFragment;
      default:
        SurveyQuestionsFragment fragment = new SurveyQuestionsFragment();
        fragment.setData(
            data.get(position - 1).questions,
            bodies,
            listener,
            survey.status != Survey.COMPLETED);
        return fragment;
      }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
      return super.getPageTitle(position);
    }
  }
}
