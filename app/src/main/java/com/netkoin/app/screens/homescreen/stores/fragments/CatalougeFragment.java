package com.netkoin.app.screens.homescreen.stores.fragments;

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
import com.netkoin.app.entities.Catalogue;
import com.netkoin.app.entities.Store;
import com.netkoin.app.screens.homescreen.stores.adapters.StoreProfileCommonListAdapter;
import com.netkoin.app.servicemodels.CataloguesServiceModel;

import java.util.ArrayList;

/**
 * Created by siddharthyadav on 07/01/17.
 */

public class CatalougeFragment extends AbstractBaseFragment {


    private ProgressBar progressBarCenter;
    private ListView catalougeListView;
    private StoreProfileCommonListAdapter storeProfileCommonListAdapter;

    private ArrayList<Catalogue> catalouges = null;
    private Store store;
    private CataloguesServiceModel cataloguesServiceModel;

    public CatalougeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = (Store) getParcelExtras();
        makeView();
        registerEvents();

        //request catalouge
        cataloguesServiceModel = new CataloguesServiceModel(getActivity(), this);
        cataloguesServiceModel.loadCatalougesByStore(store.getId());
    }

    private void updateCatalougeAdapter() {
        if (storeProfileCommonListAdapter == null) {
            storeProfileCommonListAdapter = new StoreProfileCommonListAdapter(getActivity(), catalouges, StoreProfileCommonListAdapter.TYPE_CATALOUGE);
            catalougeListView.setAdapter(storeProfileCommonListAdapter);
        } else {
            storeProfileCommonListAdapter.refreshCatalougeAdapter(catalouges);
        }
    }

    private void registerEvents() {
        catalougeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        cataloguesServiceModel.loadCatalougesByStore(store.getId());
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
                cataloguesServiceModel.resetPage();
                cataloguesServiceModel.loadCatalougesByStore(store.getId());
            }
        });
    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_catalouge_layout, null, false);
        catalougeListView = (ListView) view.findViewById(R.id.catalougeListView);
        refreshLayout = (CustomSwipeToRefresh) view.findViewById(R.id.refreshLayout);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        progressBarListLoading = (ProgressBar) view.findViewById(R.id.progressBarListLoading);
        initActionBarView(R.drawable.back, "Catalouge");
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
            case RequestConstants.REQUEST_ID_GET_CATALOUGES:
                onCatelogListResponse(isSuccess, result, errorString);
                break;
            case RequestConstants.REQIEST_ID_REDEEM_KOIN:
                onRedeemKoinResponse(isSuccess, result, errorString);
                break;
            default:
                break;
        }
    }

    private void onCatelogListResponse(boolean isSuccess, Object result, String errorString) {
        progressBarListLoading.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);

        if (isSuccess) {
            if (cataloguesServiceModel.getPage() == 1) {
                catalouges = null;
            }

            ArrayList<Catalogue> newCatalouges = cataloguesServiceModel.getCatalouges();
            if (catalouges == null) {
                catalouges = newCatalouges;
            } else {
                catalouges.addAll(newCatalouges);
            }

            updateCatalougeAdapter();

            if (newCatalouges.size() >= cataloguesServiceModel.getLimit() && newCatalouges.size() > 0) {
                canLoadMoreListItems = true;
                cataloguesServiceModel.incrementPage();
            } else {
                canLoadMoreListItems = false;
            }

//            //showing info window
//            if (self.cataloguesServiceModel == nil || self.catalouges ?.count == 0)
//            {
//                showRetryView("No item found to purchase.", needRetryButton:true);
//            }

        } else {
//            self.tableView.tableFooterView = nil
//
//            if (self.catalouges == nil || self.catalouges ?.count == 0)
//            {
//                showRetryView(errorString !, needRetryButton:true);
//            }

        }
        progressBarCenter.setVisibility(View.GONE);
    }


    private void onRedeemKoinResponse(boolean isSuccess, Object result, String errorString) {

    }


}
