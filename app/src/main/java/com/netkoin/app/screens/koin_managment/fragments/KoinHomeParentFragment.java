package com.netkoin.app.screens.koin_managment.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.controller.FragmentNavigationViewController;

/**
 * Created by siddharthyadav on 08/01/17.
 */

public class KoinHomeParentFragment extends AbstractBaseFragment {

    private View view;


    public KoinHomeParentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationController = null;
        makeView();
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_screen, null, false);
        setUPFragmentNavController();
    }

    public void setUPFragmentNavController() {
        if (navigationController == null) {
            navigationController = new FragmentNavigationViewController(getChildFragmentManager(), R.id.fragmentBodyLayout);
        } else {
            navigationController.setFragmentManager(getChildFragmentManager());
        }
    }

    public void launchFirstFragment() {
        if (navigationController.getFragmentsStack().size() == 0) {
            navigationController.push(FragmentNavigationViewController.FRAGMENT_KOIN_HOME, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        launchFirstFragment();
        return view;

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
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {

    }
}
