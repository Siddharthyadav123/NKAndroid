package com.netkoin.app.screens.homescreen.trending.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.entities.Ads;
import com.netkoin.app.screens.homescreen.trending.adapters.TrendingListviewAdapter;
import com.netkoin.app.servicemodels.TrendingServiceModel;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by siddharthyadav on 05/01/17.
 */

public class TrendingFragment extends AbstractBaseFragment {

    private ListView trendingListView;
    private TrendingListviewAdapter trendingListviewAdapter;
    private SegmentedGroup segmentGroup;
    private ProgressBar progressBarCenter;

    public final int NEAR_BY_TRENDING = 0;
    public final int NATION_WIDE_TRENDING = 1;
    public int currentSegmentFilter = NEAR_BY_TRENDING;

    private ArrayList<Ads> trendingAds = null;

    private TrendingServiceModel trendingServiceModel;

    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeView();
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_trending_layout, null, false);
        initActionBarView(R.drawable.koin_mgmnt, "Trendings");
        initViews();
        registerEvents();

        trendingServiceModel = new TrendingServiceModel(getContext(), this);
        requestTrending();
    }

    private void requestTrending() {
        if (currentSegmentFilter == NEAR_BY_TRENDING) {
            trendingServiceModel.loadTreandingAds();
        } else {
            trendingServiceModel.loadNationWideTrending();
        }
    }

    private void updateTrendingAdapter() {
        if (trendingListviewAdapter == null) {
            trendingListviewAdapter = new TrendingListviewAdapter(getContext(), trendingAds);

            if (currentSegmentFilter == NEAR_BY_TRENDING) {
                trendingListviewAdapter.setNearBy(true);
            } else {
                trendingListviewAdapter.setNearBy(false);
            }
            trendingListView.setAdapter(trendingListviewAdapter);
        } else {

            if (currentSegmentFilter == NEAR_BY_TRENDING) {
                trendingListviewAdapter.setNearBy(true);
            } else {
                trendingListviewAdapter.setNearBy(false);
            }
            trendingListviewAdapter.refreshAdapter(trendingAds);
        }

    }


    private void initViews() {
        trendingListView = (ListView) view.findViewById(R.id.trendingListView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        segmentGroup = (SegmentedGroup) view.findViewById(R.id.segmentGroup);

        progressBarListLoading = (ProgressBar) view.findViewById(R.id.progressBarListLoading);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
    }

    private void registerEvents() {

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //setting page to 1
                trendingServiceModel.resetPage();
                requestTrending();
            }
        });

        segmentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.nearByRadioBtn:
                            onNearByRadioSegementClick();
                            break;
                        case R.id.nationWideRadioBtn:
                            onNationWideRadioSegmentClick();
                            break;
                    }
                }
            }

        });

        trendingListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //for pagination
                if (totalItemCount > 0 && canLoadMoreListItems) {
                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                        progressBarListLoading.setVisibility(View.VISIBLE);
                        //requesting stores
                        canLoadMoreListItems = false;
                        requestTrending();
                    } else {
                        progressBarListLoading.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    private void onNationWideRadioSegmentClick() {
        progressBarCenter.setVisibility(View.VISIBLE);
        this.currentSegmentFilter = NATION_WIDE_TRENDING;
        trendingServiceModel.resetPage();
        progressBarListLoading.setVisibility(View.GONE);
        trendingListView.smoothScrollToPosition(0);
        requestTrending();
    }

    private void onNearByRadioSegementClick() {
        progressBarCenter.setVisibility(View.VISIBLE);
        this.currentSegmentFilter = NEAR_BY_TRENDING;
        trendingServiceModel.resetPage();
        progressBarListLoading.setVisibility(View.GONE);
        trendingListView.smoothScrollToPosition(0);
        requestTrending();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        showRefreshingIfRequestPending();
        return view;

    }

    @Override
    public void onActionBarLeftBtnClick() {
        ActivityController.getInstance().handleEvent(getActivity(), ActivityController.ACTIVITY_KOIN_MANAGEMENT_HOME_SCREEN);
    }


    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_TRENDING_ADS:
                onTreandingAdsResponse(isSuccess, errorString);
                break;
            default:
                break;
        }
    }

    private void onTreandingAdsResponse(boolean isSuccess, String errorString) {
        progressBarListLoading.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);

        if (isSuccess) {
            if (trendingServiceModel.getPage() == 1) {
                trendingAds = null;
            }
            ArrayList<Ads> newTrendings = trendingServiceModel.getTrendingAds();

            if (trendingAds == null) {
                trendingAds = newTrendings;
            } else {
                trendingAds.addAll(newTrendings);
            }

            updateTrendingAdapter();

            if (newTrendings.size() >= trendingServiceModel.getLimit() && newTrendings.size() > 0) {
                canLoadMoreListItems = true;
                trendingServiceModel.incrementPage();
            } else {
                canLoadMoreListItems = false;
            }

//            hideRetryView();
//
//            //showing info window
//            if (self.ads == nil || self.ads ?.count == 0)
//            {
//                showRetryView("No itmes in trending nearby. Please change your location or increase distance from Settings", needRetryButton:
//                false);
//            }

        } else {
//            self.tableView.tableFooterView = nil
//
//            if (self.ads == nil || self.ads ?.count == 0)
//            {
//                showRetryView(errorString, needRetryButton:true);
//            }

        }

        progressBarCenter.setVisibility(View.GONE);

    }
}
