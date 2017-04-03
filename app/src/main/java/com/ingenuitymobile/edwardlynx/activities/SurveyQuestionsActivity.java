package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.ingenuitymobile.edwardlynx.fragments.DevelopmentPlanListFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveyQuestionsFragment;
import com.ingenuitymobile.edwardlynx.fragments.SurveysListFragment;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import rx.Subscriber;

public class SurveyQuestionsActivity extends BaseActivity {

  private TextView submitText;

  private long             id;
  private PagerAdapter     adapter;
  private CircleIndicator  circleIndicator;
  private List<Category>   data;
  private List<AnswerBody> bodies;
  private Survey           survey;

  private ViewPager viewPager;
  private ImageView previousImage;
  private ImageView nextImage;

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

    initViews();
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
    submitText = (TextView) findViewById(R.id.text_submit);

    previousImage = (ImageView) findViewById(R.id.image_previous);
    nextImage = (ImageView) findViewById(R.id.image_next);

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new MyPagerAdapter(getSupportFragmentManager());
    circleIndicator = (CircleIndicator) findViewById(R.id.indicator);

    previousImage.setOnClickListener(onClickListener);
    nextImage.setOnClickListener(onClickListener);
  }

  private void getData() {
    LogUtil.e("AAA getData Survey details");
    subscription.add(Shared.apiClient.getSurvey(id, new Subscriber<Survey>() {
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
        survey = surveyResponse;
        setTitle(surveyResponse.name);
        submitText.setVisibility(survey.status == Survey.COMPLETED ? View.GONE : View.VISIBLE);
      }
    }));
  }

  private void getSurveyQuestions() {
    LogUtil.e("AAA getData questions");
    subscription.add(Shared.apiClient.getSurveyQuestions(id, new Subscriber<Questions>() {
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
      public void onNext(Questions questions) {
        Category cat = questions.items.get(0);
        List<Question> questions1 = questions.items.get(0).questions;
        LogUtil.e("AAA questions onNext");

        data.clear();
//         data.addAll(questions.items);
        Category c = new Category();
        c.title = cat.title;
        c.questions = new ArrayList<>(questions1);
        data.add(c);

        c = new Category();
        c.title = cat.title;
        c.questions = new ArrayList<>(questions1);
        data.add(c);

        c = new Category();
        c.title = cat.title;
        c.questions = new ArrayList<>(questions1);
        data.add(c);

        c = new Category();
        c.title = cat.title;
        c.questions = new ArrayList<>(questions1);
        data.add(c);


        for (Category category : data) {
          Question question = new Question();
          question.isSectionHeader = true;
          question.text = category.title;
          category.questions.add(0, question);
        }
      }
    }));
  }

  private void setNavigation() {
    findViewById(R.id.layout_navigation).setVisibility(data.size() == 1 ? View.GONE : View.VISIBLE);

    previousImage.setVisibility(viewPager.getCurrentItem() == 0 ? View.INVISIBLE : View.VISIBLE);
    nextImage.setVisibility(
        viewPager.getCurrentItem() == data.size() - 1 ? View.INVISIBLE : View.VISIBLE);
  }

  public void submit(View v) {
    AnswerParam param = new AnswerParam();
    param.answers = bodies;
    param.key = survey.key;
    param.isFinal = bodies.size() == data.size();

    LogUtil.e("AAA id " + id);
    LogUtil.e("AAA " + param.toString());
    submitText.setText(getString(R.string.loading));
    subscription.add(Shared.apiClient.postAnswerSurvey(id, param,
        new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted");
            finish();
          }

          @Override
          public void onError(Throwable e) {
            submitText.setText(R.string.submit);
            LogUtil.e("AAA onError" + e);
            Toast.makeText(SurveyQuestionsActivity.this, getString(R.string.cant_connect),
                Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onNext(Response response) {
            LogUtil.e("AAA onNext");
            Toast.makeText(SurveyQuestionsActivity.this,
                getString(R.string.survey_answers_submitted), Toast.LENGTH_SHORT).show();
          }
        }));
  }

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

  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener
      () {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      LogUtil.e("AAA onPageSelected " + position);
      setNavigation();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

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
  };

  private class MyPagerAdapter extends FragmentPagerAdapter {

    MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
      return data.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
      SurveyQuestionsFragment fragment = new SurveyQuestionsFragment();
      fragment.setData(
          data.get(position).questions,
          bodies,
          listener,
          survey.status != Survey.COMPLETED);
      return fragment;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
      return super.getPageTitle(position);
    }
  }
}
