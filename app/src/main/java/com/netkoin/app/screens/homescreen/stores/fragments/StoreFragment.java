
package com.netkoin.app.screens.homescreen.stores.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.controller.AppController;
import com.netkoin.app.controller.FragmentNavigationViewController;
import com.netkoin.app.custom_views.parallex.ParallaxListView;
import com.netkoin.app.custom_views.pull_to_refresh.CustomSwipeToRefresh;
import com.netkoin.app.entities.Store;
import com.netkoin.app.screens.homescreen.stores.adapters.StoreBannerViewPagerAdapter;
import com.netkoin.app.screens.homescreen.stores.adapters.StoreListViewAdapter;
import com.netkoin.app.servicemodels.StoreServiceModel;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by siddharthyadav on 31/12/16.
 */

public class StoreFragment extends AbstractBaseFragment {

    private ParallaxListView storesListView;

    private View mHeaderView;
    private ViewPager storeBannerViewPager;
    private CircleIndicator viewPagerCircleIndicator;

    private ProgressBar progressBarHeader;
    private ProgressBar progressBarFooter;


    private StoreListViewAdapter storeListViewAdapter;
    private StoreBannerViewPagerAdapter storeBannerViewPagerAdapter;

    private LinearLayout currentLocationLinLayout;

    private StoreServiceModel storeServiceModel;

    private ArrayList<Store> stores = null;

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeServiceModel = new StoreServiceModel(getActivity(), this);
        makeView();
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_store_layout, null, false);
        initActionBarView(R.drawable.koin_mgmnt, "200");
        initViews();
        registerEvents();
        requestWebAPIs();
    }

    private void requestWebAPIs() {
        //loading total koin
        storeServiceModel.loadTotalKoins();

        //requestBanner
        storeServiceModel.loadStoreFeaturedBanner();

        //setting page to 1
        storeServiceModel.setPage(1);

        //requesting stores
        storeServiceModel.loadStores();
    }

    private void updateStoreListAdapter() {
        //set list view adapter
        if (storeListViewAdapter == null) {
            storeListViewAdapter = new StoreListViewAdapter(getContext(), stores);
            storesListView.setAdapter(storeListViewAdapter);
        } else {
            storeListViewAdapter.refreshAdapter(stores);
        }


    }

    private void updateStoreBannerAdatper() {
        //set banner view
        if (storeBannerViewPagerAdapter == null) {
            storeBannerViewPagerAdapter = new StoreBannerViewPagerAdapter(getActivity(), storeServiceModel.getStoresFeaturedBanner());
            storeBannerViewPager.setAdapter(storeBannerViewPagerAdapter);
            viewPagerCircleIndicator.setViewPager(storeBannerViewPager);
        } else {
            storeBannerViewPagerAdapter.refreshAdapter(storeServiceModel.getStoresFeaturedBanner());
        }
    }


    private void initViews() {
        storesListView = (ParallaxListView) view.findViewById(R.id.storesListView);
        refreshLayout = (CustomSwipeToRefresh) view.findViewById(R.id.refreshLayout);
        currentLocationLinLayout = (LinearLayout) view.findViewById(R.id.currentLocationLinLayout);
        //setting header in list-view
        mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.store_header_view_pager_layout, null);
        storeBannerViewPager = (ViewPager) mHeaderView.findViewById(R.id.storeBannerViewPager);
        viewPagerCircleIndicator = (CircleIndicator) mHeaderView.findViewById(R.id.viewPagerCircleIndicator);

        progressBarHeader = (ProgressBar) view.findViewById(R.id.progressBarHeader);
        progressBarFooter = (ProgressBar) view.findViewById(R.id.progressBarFooter);
        progressBarListLoading = (ProgressBar) view.findViewById(R.id.progressBarListLoading);

        storesListView.addParallaxedHeaderView(mHeaderView);

    }

    private void registerEvents() {
        storesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigationController.push(FragmentNavigationViewController.FRAGMENT_STORE_PROFILE, stores.get(position - 1));
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWebAPIs();
            }
        });

        storesListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //for header visibility
                if (firstVisibleItem > 0) {
                    if (currentLocationLinLayout.getVisibility() == View.GONE) {
                        currentLocationLinLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    currentLocationLinLayout.setVisibility(View.GONE);
                }

                //for pagination
                if (totalItemCount > 0 && canLoadMoreListItems) {
                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                        progressBarListLoading.setVisibility(View.VISIBLE);
                        //requesting stores
                        canLoadMoreListItems = false;
                        storeServiceModel.loadStores();
                    } else {
                        progressBarListLoading.setVisibility(View.GONE);
                    }
                }

            }
        });

        currentLocationLinLayout.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.currentLocationLinLayout:
                ActivityController.getInstance().handleEvent(getActivity(), ActivityController.ACTIVITY_SEARCH_LOCATION);
                break;
        }
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_STORES:
                onStoreResponse(isSuccess, result, errorString);
                break;
            case RequestConstants.REQUEST_ID_GET_HOME_BANNER:
                onFeaturedBannerResponse(isSuccess);
                break;
            case RequestConstants.REQUEST_ID_GET_TOTAL_KOIN:
                titleTextView.setText(AppController.getInstance().getModelFacade().getLocalModel().getTotalKoins().getTotal_koins() + "");
                break;

            default:
                break;
        }
    }

    private void onFeaturedBannerResponse(boolean isSuccess) {
        progressBarHeader.setVisibility(View.GONE);
        if (isSuccess) {
            updateStoreBannerAdatper();
        }
    }


    public void onStoreResponse(boolean isSuccess, Object responseObject, String errorString) {
        currentLocationLinLayout.setVisibility(View.GONE);
        progressBarFooter.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);

        if (isSuccess) {
            ArrayList<Store> newStores = storeServiceModel.getStores();

            if (storeServiceModel.getPage() == 1) {
                stores = null;
            }

            if (stores == null) {
                stores = newStores;
            } else {
                stores.addAll(newStores);
            }

            updateStoreListAdapter();

            if (newStores.size() >= storeServiceModel.getLimit() && newStores.size() > 0) {
                canLoadMoreListItems = true;
                storeServiceModel.incrementPage();
            } else {
                canLoadMoreListItems = false;
            }
//
//            hideRetryView();

            //showing info window
//            if (self.stores == nil || self.stores ?.count == 0)
//            {
//                shorStoreNotFoundRetry("No stores found nearby. Please change your location or increase distance from Settings", needRetryButton:
//                true);
//            }


        } else {
//            self.tableView.tableFooterView = nil

//            if (self.stores == nil || self.stores ?.count == 0)
//            {
//                shorStoreNotFoundRetry("No stores found nearby. Please change your location or turn the same also increase distance from Settings", needRetryButton:
//                true);
//            }
        }
        progressBarListLoading.setVisibility(View.GONE);
    }
}