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
import com.ingenuitymobile.edwardlynx.adapters.DevelopmentPlanAdapter;
import com.ingenuitymobile.edwardlynx.api.models.DevelopmentPlan;
import com.ingenuitymobile.edwardlynx.api.responses.DevelopmentPlansResponse;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 31/01/2017.
 */

public class DevelopmenPlansFragment extends BaseFragment {


  private View                   mainView;
  private SwipeRefreshLayout     refreshLayout;
  private DevelopmentPlanAdapter adapter;
  private TextView               emptyText;
  private List<DevelopmentPlan>  data;


  public static DevelopmenPlansFragment newInstance(Context ctx) {
    DevelopmenPlansFragment fragment = new DevelopmenPlansFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.development_plans_bold));
    fragment.setArguments(bundle);
    return fragment;
  }

  public DevelopmenPlansFragment() {
    data = new ArrayList<>();
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
    final RecyclerView surveyList = (RecyclerView) mainView.findViewById(R.id.list_survey);
    final RelativeLayout filterLayout = (RelativeLayout) mainView.findViewById(R.id.layout_filter);
    final RelativeLayout sortLayout = (RelativeLayout) mainView.findViewById(R.id.layout_sort);

    final SearchView searchView = (SearchView) mainView.findViewById(R.id.searchview);
    searchView.setQueryHint("Search Development Plan");

    emptyText = (TextView) mainView.findViewById(R.id.text_empty_state);
    refreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);

    final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
        LinearLayoutManager.VERTICAL);
    surveyList.addItemDecoration(dividerItemDecoration);
    surveyList.setHasFixedSize(true);
    surveyList.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new DevelopmentPlanAdapter(data);
    surveyList.setAdapter(adapter);
    refreshLayout.setOnRefreshListener(refreshListener);
    refreshLayout.setRefreshing(true);
    mainView.findViewById(R.id.text_all).setSelected(true);

    filterLayout.setOnClickListener(onClickListener);
    sortLayout.setOnClickListener(onClickListener);
  }

  private void notifyAdapter() {
    emptyText.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    adapter.notifyDataSetChanged();
  }

  private void getData() {
    LogUtil.e("AAA getData Development plans");
    subscription.add(
        Shared.apiClient.getDevelopmentPlans(new Subscriber<DevelopmentPlansResponse>() {
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
          public void onNext(final DevelopmentPlansResponse response) {
            LogUtil.e("AAA onNext ");
            data.clear();
            data.addAll(response.items);
          }
        }));
  }

  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout
      .OnRefreshListener() {
    @Override
    public void onRefresh() {
      getData();
    }
  };

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
}
