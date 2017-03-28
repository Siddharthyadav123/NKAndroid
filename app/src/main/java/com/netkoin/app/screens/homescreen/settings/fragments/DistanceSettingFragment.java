package com.netkoin.app.screens.homescreen.settings.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.screens.homescreen.settings.adapters.DistanceSettingListAdapter;
import com.netkoin.app.volly.ErrorResponse;

/**
 * Created by ashishkumarpatel on 07/01/17.
 */

public class DistanceSettingFragment extends AbstractBaseFragment {

    private ListView distanceSetttingListView;
    private DistanceSettingListAdapter distanceSettingListAdapter;
    private String[] items = {"NEARBY STORES", "NEARBY TRENDING ADS", "CATEGORY ADS"};


    public DistanceSettingFragment() {
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
        setDistanceSettingAdapter();
        registerEvents();
    }

    private void setDistanceSettingAdapter() {
        distanceSettingListAdapter = new DistanceSettingListAdapter(getActivity(), items);
        distanceSetttingListView.setAdapter(distanceSettingListAdapter);
    }

    private void registerEvents() {

    }


    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_distance_layout, null, false);
        distanceSetttingListView = (ListView) view.findViewById(R.id.distanceSetttingListView);
        initActionBarView(R.drawable.back, "Distance");
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

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, ErrorResponse errorResponse) {

    }
}
