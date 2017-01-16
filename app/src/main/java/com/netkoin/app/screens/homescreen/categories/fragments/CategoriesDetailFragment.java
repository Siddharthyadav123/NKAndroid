package com.netkoin.app.screens.homescreen.categories.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.custom_views.pull_to_refresh.CustomSwipeToRefresh;
import com.netkoin.app.entities.Ads;
import com.netkoin.app.entities.MainCategory;
import com.netkoin.app.screens.homescreen.trending.adapters.TrendingListviewAdapter;
import com.netkoin.app.servicemodels.AdsServiceModel;

import java.util.ArrayList;

/**
 * Created by siddharthyadav on 07/01/17.
 */

public class CategoriesDetailFragment extends AbstractBaseFragment {

    private MainCategory category;
    private ListView featuredListView;
    private ProgressBar progressBarCenter;
    private TrendingListviewAdapter trendingListviewAdapter;

    private AdsServiceModel adsServiceModel;
    private ArrayList<Ads> adsList = null;

    public CategoriesDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = (MainCategory) getParcelExtras();
        makeView();
        registereEvents();

        adsServiceModel = new AdsServiceModel(getActivity(), this);
        adsServiceModel.loadAdsByCateogry(category.getId());
    }

    private void registereEvents() {
        featuredListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        adsServiceModel.loadAdsByCateogry(category.getId());
                    } else {
                        progressBarListLoading.setVisibility(View.GONE);
                    }
                }

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //setting page to 1
                adsServiceModel.resetPage();
                adsServiceModel.loadAdsByCateogry(category.getId());
            }
        });
    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_featured_layout, null, false);
        featuredListView = (ListView) view.findViewById(R.id.featuredListView);
        refreshLayout = (CustomSwipeToRefresh) view.findViewById(R.id.refreshLayout);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        progressBarListLoading = (ProgressBar) view.findViewById(R.id.progressBarListLoading);
        initActionBarView(R.drawable.back, category.getName());
    }

    private void updateFeaturedAdapter() {
        if (trendingListviewAdapter == null) {
            trendingListviewAdapter = new TrendingListviewAdapter(getContext(), adsList);
            featuredListView.setAdapter(trendingListviewAdapter);
        } else {
            trendingListviewAdapter.refreshAdapter(adsList);
        }

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
        navigationController.pop();
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
            case RequestConstants.REQUEST_ID_GET_INDIVIDAUL_CATEGORY_DETAILS_BY_CAT_ID:
                onIndividualCatResponse(isSuccess, errorString);
                break;
            default:
                break;
        }
    }

    private void onIndividualCatResponse(boolean isSuccess, String errorString) {
        refreshLayout.setRefreshing(false);
        progressBarListLoading.setVisibility(View.GONE);

        if (isSuccess) {
            if (adsServiceModel.getPage() == 1) {
                this.adsList = null;
            }

            ArrayList<Ads> newAdsList = adsServiceModel.getAdsList();

            if (this.adsList == null) {
                this.adsList = newAdsList;
            } else {

                this.adsList.addAll(newAdsList);

            }
            updateFeaturedAdapter();

            if (newAdsList.size() >= adsServiceModel.getLimit() && newAdsList.size() > 0) {
                canLoadMoreListItems = true;
                adsServiceModel.incrementPage();
            } else {
                canLoadMoreListItems = false;
            }

//
//            //showing info window
//            if (self.ads == nil || self.ads ?.count == 0)
//            {
//                showRetryView("No item found for this cateogry.", needRetryButton:true);
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