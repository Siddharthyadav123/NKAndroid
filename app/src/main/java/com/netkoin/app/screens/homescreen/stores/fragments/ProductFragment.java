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
import com.netkoin.app.entities.ProductBarcode;
import com.netkoin.app.entities.Store;
import com.netkoin.app.screens.homescreen.stores.adapters.StoreProfileCommonListAdapter;
import com.netkoin.app.servicemodels.ProductBarcodesModel;

import java.util.ArrayList;

/**
 * Created by siddharthyadav on 07/01/17.
 */

public class ProductFragment extends AbstractBaseFragment {
    private ProgressBar progressBarCenter;
    private ListView productListView;
    private StoreProfileCommonListAdapter storeProfileCommonListAdapter;

    private ArrayList<ProductBarcode> productBarcodes = null;
    private Store store;
    private ProductBarcodesModel productBarcodesModel;

    public ProductFragment() {
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

        productBarcodesModel = new ProductBarcodesModel(getActivity(), this);
        productBarcodesModel.loadProductBarcodes(store.getId());
    }


    private void updateProductAdapter() {
        if (storeProfileCommonListAdapter == null) {
            storeProfileCommonListAdapter = new StoreProfileCommonListAdapter(getActivity(), productBarcodes, StoreProfileCommonListAdapter.TYPE_PRODUCT);
            productListView.setAdapter(storeProfileCommonListAdapter);
        } else {
            storeProfileCommonListAdapter.refreshProductAdapter(productBarcodes);
        }
    }


    private void registerEvents() {
        productListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        productBarcodesModel.loadProductBarcodes(store.getId());
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
                productBarcodesModel.resetPage();
                productBarcodesModel.loadProductBarcodes(store.getId());
            }
        });
    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_product_layout, null, false);
        productListView = (ListView) view.findViewById(R.id.productListView);
        refreshLayout = (CustomSwipeToRefresh) view.findViewById(R.id.refreshLayout);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        progressBarListLoading = (ProgressBar) view.findViewById(R.id.progressBarListLoading);

        initActionBarView(R.drawable.back, "Products");
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
    public void onRetryBtnClick() {
        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }
        progressBarCenter.setVisibility(View.VISIBLE);
        productBarcodesModel.loadProductBarcodes(store.getId());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_PRODUCT_BARCODES:
                onProductListResponse(isSuccess, result, errorString);
                break;
            default:
                break;
        }
    }

    private void onProductListResponse(boolean isSuccess, Object result, String errorString) {
        progressBarListLoading.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);

        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }

        if (isSuccess) {
            if (productBarcodesModel.getPage() == 1) {
                productBarcodes = null;
            }

            ArrayList<ProductBarcode> newProductsBarcodes = productBarcodesModel.getProductBarcodes();
            if (productBarcodes == null) {
                productBarcodes = newProductsBarcodes;
            } else {
                productBarcodes.addAll(newProductsBarcodes);
            }

            updateProductAdapter();

            if (newProductsBarcodes.size() >= productBarcodesModel.getLimit() && newProductsBarcodes.size() > 0) {
                canLoadMoreListItems = true;
                productBarcodesModel.incrementPage();
            } else {
                canLoadMoreListItems = false;
            }

            //showing info window
            if (productBarcodes == null || productBarcodes.size() == 0) {
                showRetryView("No products found.", true);
            }
        } else {
            if (productBarcodes == null || productBarcodes.size() == 0) {
                showRetryView(errorString, true);
            }

        }
        progressBarCenter.setVisibility(View.GONE);
    }
}
