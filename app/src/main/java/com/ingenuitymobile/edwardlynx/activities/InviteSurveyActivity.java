package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.adapters.SurveyAdapter;
import com.ingenuitymobile.edwardlynx.api.models.Survey;
import com.ingenuitymobile.edwardlynx.api.models.Surveys;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by memengski.
 * Activity for inviting people to answer the survey.
 */

public class InviteSurveyActivity extends BaseActivity {

  private final static int NUM = 10;

  private ArrayList<Survey>  data;
  private SurveyAdapter      adapter;
  private TextView           emptyText;
  private SwipeRefreshLayout refreshLayout;

  private int                 page;
  private LinearLayoutManager manager;

  private boolean loading;
  private int     previousTotal;

  public InviteSurveyActivity() {
    data = new ArrayList<>();
    loading = true;
    page = 1;
    previousTotal = 0;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite_survey);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(getString(R.string.invite).toUpperCase());

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getData(true);
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
    final RecyclerView surveyList = (RecyclerView) findViewById(R.id.list_survey);

    emptyText = (TextView) findViewById(R.id.text_empty_state);
    refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
        context,
        LinearLayoutManager.VERTICAL
    );

    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);

    manager = new LinearLayoutManager(context);
    surveyList.setLayoutManager(manager);

    adapter = new SurveyAdapter(data);
    adapter.setIsnvite(true);
    surveyList.setAdapter(adapter);

    refreshLayout.setOnRefreshListener(refreshListener);
    surveyList.setOnScrollListener(onScrollListener);

    refreshLayout.setRefreshing(true);
  }

  /**
   * retrieves the surveys from the API and displays them in a list
   * @param isRefresh
   */
  private void getData(final boolean isRefresh) {
    if (isRefresh) {
      page = 1;
      previousTotal = 0;
    } else {
      page++;
    }

    LogUtil.e("AAA getData survey");
    subscription.add(
        Shared.apiClient.getSurveys(page, NUM, "answerable", new Subscriber<Surveys>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted ");
            refreshLayout.setRefreshing(false);
            notifyAdapter();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError " + e);
            refreshLayout.setRefreshing(false);
          }

          @Override
          public void onNext(final Surveys surveys) {
            LogUtil.e("AAA onNext ");
            if (isRefresh) {
              data.clear();
            }

            for (Survey survey : surveys.items) {
              if (survey.permissions.canInvite) {
                data.add(survey);
              }
            }
          }
        }));
  }

  /**
   * notifyAdapter notifies the list adapter of data changes
   */
  private void notifyAdapter() {
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  /**
   * listener for the swipe to refresh functionality, reloads the list data on action
   */
  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      page = 1;
      getData(true);
    }
  };

  /**
   * listener for the recycler view when the user scrolls the list
   */
  private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);
      if (dy > 0) {
        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = manager.getItemCount();
        final int firstVisibleItem = manager.findFirstVisibleItemPosition();

        if (loading) {
          if (totalItemCount > previousTotal) {
            loading = false;
            previousTotal = totalItemCount;
          }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + NUM)) {
          getData(false);
          LogUtil.e("AAA loading");
          loading = true;
        }
      }
    }
  };
}

