package com.netkoin.app.screens.homescreen.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.controller.FragmentNavigationViewController;
import com.netkoin.app.volly.ErrorResponse;

/**
 * Created by siddharthyadav on 31/12/16.
 */

public class TrendingHomeScreenFragment extends AbstractBaseFragment {

    private View view;
    private static TrendingHomeScreenFragment trendingHomeScreenFragment;

    public static TrendingHomeScreenFragment newInstance() {
        trendingHomeScreenFragment = new TrendingHomeScreenFragment();
        Bundle args = new Bundle();
        trendingHomeScreenFragment.setArguments(args);
        return trendingHomeScreenFragment;
    }

    /**
     * Gets the current instance.
     */
    public static TrendingHomeScreenFragment getInstance() {
        return trendingHomeScreenFragment;
    }

    public TrendingHomeScreenFragment() {
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_screen, null, false);
        setUPFragmentNavController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        launchFirstFragment();
        return view;

    }

    public void launchFirstFragment() {
        if (navigationController.getFragmentsStack().size() == 0) {
            navigationController.push(FragmentNavigationViewController.FRAGMENT_TRENDINGS, null);
        }
    }

    public void setUPFragmentNavController() {
        if (navigationController == null) {
            navigationController = new FragmentNavigationViewController(getChildFragmentManager(), R.id.fragmentBodyLayout);
        } else {
            navigationController.setFragmentManager(getChildFragmentManager());
        }
    }

    @Override
    public void onActionBarLeftBtnClick() {
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