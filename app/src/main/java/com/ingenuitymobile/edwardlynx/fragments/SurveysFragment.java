package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
 * Created by mEmEnG-sKi on 04/01/2017.
 */

public class SurveysFragment extends BaseFragment {

  private final static int NUM = 3;

  private View               mainView;
  private ArrayList<Survey>  data;
  private SurveyAdapter      adapter;
  private TextView           emptyText;
  private SwipeRefreshLayout refreshLayout;

  private boolean             loading;
  private int                 page;
  private LinearLayoutManager manager;


  public static SurveysFragment newInstance(Context ctx) {
    SurveysFragment fragment = new SurveysFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.surveys_bold));
    fragment.setArguments(bundle);
    return fragment;
  }

  public SurveysFragment() {
    data = new ArrayList<>();
    loading = false;
    page = 1;
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
    page = 1;
    getData(true);
    LogUtil.e("AAA onResume survey");
  }

  private void initViews() {
    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.list_survey);
    final RelativeLayout filterLayout = (RelativeLayout) mainView.findViewById(R.id.layout_filter);
    final RelativeLayout sortLayout = (RelativeLayout) mainView.findViewById(R.id.layout_sort);

    final SearchView searchView = (SearchView) mainView.findViewById(R.id.searchview);
    searchView.setQueryHint("Search Survey");

    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);

    manager = new LinearLayoutManager(getActivity());
    surveyList.setLayoutManager(manager);

    adapter = new SurveyAdapter(data);
    surveyList.setAdapter(adapter);
    refreshLayout.setRefreshing(true);

    refreshLayout.setOnRefreshListener(refreshListener);
    surveyList.setOnScrollListener(onScrollListener);
    mainView.findViewById(R.id.text_all).setSelected(true);

    filterLayout.setOnClickListener(onClickListener);
    sortLayout.setOnClickListener(onClickListener);
  }

  private void notifyAdapter() {
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  private void getData(final boolean isRefresh) {
    LogUtil.e("AAA getData survey");
    subscription.add(Shared.apiClient.getSurveys(page, NUM, new Subscriber<Surveys>() {
      @Override
      public void onCompleted() {
        refreshLayout.setRefreshing(false);
        LogUtil.e("AAA onCompleted ");
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
        data.addAll(surveys.items);
        notifyAdapter();
      }
    }));
  }


  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.layout_filter:
        final CharSequence[] filterItems =
            {"Open", "Unfinished", "Completed", "Not Invited"};
        final ArrayList seletedItems = new ArrayList();

        AlertDialog FilterDialog = new AlertDialog.Builder(getActivity())
            .setTitle("FILTER BY:")
            .setMultiChoiceItems(filterItems, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                    if (isChecked) {
                      seletedItems.add(i);
                    } else if (seletedItems.contains(i)) {
                      seletedItems.remove(Integer.valueOf(i));
                    }
                  }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                //  Your code when user clicked on OK
                //  You can write the code  to save the selected item here
              }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                //  Your code when user clicked on Cancel
              }
            }).create();
        FilterDialog.show();
        break;
      case R.id.layout_sort:
        final CharSequence[] sortItems =
            {"Date Modified", "Date Posted", "Date Expiry", "Name A to Z"};

        AlertDialog sortDialog = new AlertDialog.Builder(getActivity())
            .setTitle("SORT BY:")
            .setSingleChoiceItems(sortItems, 0, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                LogUtil.e("AAA " + sortItems[i]);
              }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                //  Your code when user clicked on OK
                //  You can write the code  to save the selected item here
              }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                //  Your code when user clicked on Cancel
              }
            }).create();
        sortDialog.show();
        break;
      }

    }
  };

  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      page = 1;
      getData(true);
    }
  };

  private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);
      int totalItem = manager.getItemCount();
      int lastVisibleItem = manager.findLastVisibleItemPosition();

      if (!loading && lastVisibleItem == totalItem - 1) {
        loading = true;
        page++;
        getData(false);
        LogUtil.e("AAA loading");
        loading = false;
      }
    }
  };
}
