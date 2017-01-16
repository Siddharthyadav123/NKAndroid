package com.netkoin.app.screens.koin_managment.fragments;

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
import com.netkoin.app.entities.ActivityLog;
import com.netkoin.app.entities.ProductBarcode;
import com.netkoin.app.entities.Store;
import com.netkoin.app.screens.homescreen.stores.adapters.StoreProfileCommonListAdapter;
import com.netkoin.app.screens.koin_managment.adapters.KoinActivitiesListAdapter;
import com.netkoin.app.servicemodels.KoinManagementServiceModel;
import com.netkoin.app.servicemodels.ProductBarcodesModel;

import java.util.ArrayList;

/**
 * Created by siddharthyadav on 08/01/17.
 */

public class KoinActivitesFragment extends AbstractBaseFragment {

    private ProgressBar progressBarCenter;
    private ListView koinActiviyListView;
    private KoinActivitiesListAdapter koinActivitiesListAdapter;

    private ArrayList<ActivityLog> activityLogs = null;
    private KoinManagementServiceModel koinManagementServiceModel;

    public KoinActivitesFragment() {
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
        registerEvents();

        //loading activity logs
        koinManagementServiceModel = new KoinManagementServiceModel(getActivity(), this);
        koinManagementServiceModel.updateReadActivityStatus();
        koinManagementServiceModel.loadActivityLogs();

    }

    private void updateActivityLogsAdapter() {
        if (koinActivitiesListAdapter == null) {
            koinActivitiesListAdapter = new KoinActivitiesListAdapter(getActivity(), activityLogs);
            koinActiviyListView.setAdapter(koinActivitiesListAdapter);
        } else {
            koinActivitiesListAdapter.refreshAdapter(activityLogs);
        }
    }


    private void registerEvents() {
        koinActiviyListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        koinManagementServiceModel.loadActivityLogs();
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
                koinManagementServiceModel.resetPage();
                koinManagementServiceModel.loadActivityLogs();
            }
        });
    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_product_layout, null, false);
        koinActiviyListView = (ListView) view.findViewById(R.id.productListView);
        refreshLayout = (CustomSwipeToRefresh) view.findViewById(R.id.refreshLayout);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        progressBarListLoading = (ProgressBar) view.findViewById(R.id.progressBarListLoading);
        initActionBarView(R.drawable.back, "Activities");
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
            case RequestConstants.REQUEST_ID_GET_ACTIVITY_LOGS:
                onActivityLogResponse(isSuccess, errorString);
                break;
            default:
                break;
        }
    }

    private void onActivityLogResponse(boolean isSuccess, String errorString) {
        progressBarListLoading.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);

        if (isSuccess) {
            if (koinManagementServiceModel.getPage() == 1) {
                activityLogs = null;
            }

            ArrayList<ActivityLog> newActivityLogs = koinManagementServiceModel.getActivityLogs();
            if (activityLogs == null) {
                activityLogs = newActivityLogs;
            } else {
                activityLogs.addAll(newActivityLogs);
            }

            updateActivityLogsAdapter();

            if (newActivityLogs.size() >= koinManagementServiceModel.getLimit() && newActivityLogs.size() > 0) {
                canLoadMoreListItems = true;
                koinManagementServiceModel.incrementPage();
            } else {
                canLoadMoreListItems = false;
            }

//            //showing info window
//            if(self.activityLogs ==  nil || self.activityLogs?.count == 0)
//            {
//                showRetryView("No activity logs found.", needRetryButton: true);
//            }
//
//        }else{
//            self.tableView.tableFooterView = nil
//
//            if(self.activityLogs ==  nil || self.activityLogs?.count == 0)
//            {
//                showRetryView(errorString, needRetryButton: true);
//            }

        }
        progressBarCenter.setVisibility(View.GONE);
    }
}
