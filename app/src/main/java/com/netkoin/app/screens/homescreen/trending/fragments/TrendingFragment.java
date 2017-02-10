package com.netkoin.app.screens.homescreen.trending.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.entities.Ads;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.screens.homescreen.trending.adapters.TrendingListviewAdapter;
import com.netkoin.app.servicemodels.TrendingServiceModel;
import com.netkoin.app.utils.Utils;
import com.netkoin.app.volly.ErrorResponse;

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

    private int previousTrendingDistance;

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
        previousTrendingDistance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_NEAR_BY_TRENDING_ADS);
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_trending_layout, null, false);
        initActionBarView(R.drawable.koin_mgmnt, "Trending");
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

        trendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double destinationLat = trendingAds.get(position).getStore().getLatitude();
                double destinationLong = trendingAds.get(position).getStore().getLongitude();
                Utils.getInstance().openMap(getActivity(), destinationLat, destinationLong, trendingAds.get(position).getName());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        String currentLocation = sharedPref.getString(SharedPref.KEY_SELECTED_LOC);
        int distance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_NEAR_BY_TRENDING_ADS);


        if (!previousLocation.equals(currentLocation) || previousTrendingDistance != distance) {
            //hit API on location change from user
            previousLocation = currentLocation;
            previousTrendingDistance = distance;
            //System.out.println(">>request 333");
            progressBarCenter.setVisibility(View.VISIBLE);
            trendingServiceModel.setTrendingAds(null);
            trendingServiceModel.resetPage();
            trendingServiceModel.loadTreandingAds();
        }

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
    public void onRetryBtnClick() {
        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }
        progressBarCenter.setVisibility(View.VISIBLE);
        requestTrending();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_TRENDING_ADS:
                onTreandingAdsResponse(isSuccess, errorResponse);
                break;
            default:
                break;
        }
    }

    private void onTreandingAdsResponse(boolean isSuccess, ErrorResponse errorResponse) {
        progressBarListLoading.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);

        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }

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

            //showing info window
            if (trendingAds == null || trendingAds.size() == 0) {
                showRetryView("No items found. Please change your location or increase the distance from settings.", false);
            }

        } else {

            if (errorResponse.getErrorCode() == ErrorResponse.EROOR_CODE_INTERENT_NOT_FOUND) {

            } else if (trendingAds == null || trendingAds.size() == 0) {
                showRetryView(errorResponse.getErrorString(), true);
            }
        }

        progressBarCenter.setVisibility(View.GONE);

    }
}
