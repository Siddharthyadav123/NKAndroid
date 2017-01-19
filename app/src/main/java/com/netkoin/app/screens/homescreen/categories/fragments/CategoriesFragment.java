package com.netkoin.app.screens.homescreen.categories.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.Constants;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.controller.ActivityController;
import com.netkoin.app.controller.FragmentNavigationViewController;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.screens.homescreen.categories.adapters.CategoriesGridViewAdapter;
import com.netkoin.app.servicemodels.CateogoriesServiceModel;

public class CategoriesFragment extends AbstractBaseFragment {

    private GridView categoriesGridView;
    private CategoriesGridViewAdapter categoriesGridViewAdapter;
    private TextView currentLocationTextView;
    private CateogoriesServiceModel cateogoriesServiceModel;
    private ProgressBar progressBarCenter;

    private int previousCatDistance;


    public CategoriesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeView();
        previousCatDistance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_CAT_ADS);
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_cateogries_layout, null, false);
        currentLocationTextView = (TextView) view.findViewById(R.id.currentLocationTextView);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        initActionBarView(R.drawable.koin_mgmnt, "Categories");

        initViews();
        registerEvents();
        cateogoriesServiceModel = new CateogoriesServiceModel(getActivity(), this);
        cateogoriesServiceModel.loadCateogries();

        String currentLocation = sharedPref.getString(SharedPref.KEY_SELECTED_LOC);
        if (currentLocation == null) {
            currentLocationTextView.setText(Constants.CURRENT_LOCATION_TEXT);
        } else {
            currentLocationTextView.setText(currentLocation);
        }

    }

    private void setSettingsAdapter() {
        categoriesGridViewAdapter = new CategoriesGridViewAdapter(getContext(), cateogoriesServiceModel.getCategories());
        categoriesGridView.setAdapter(categoriesGridViewAdapter);
    }


    private void initViews() {
        categoriesGridView = (GridView) view.findViewById(R.id.categoriesGridView);
    }

    private void registerEvents() {
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigationController.push(FragmentNavigationViewController.FRAGMENT_CATEGORY_DETAIL, cateogoriesServiceModel.getCategories().get(position));
            }
        });

        currentLocationTextView.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        String currentLocation = sharedPref.getString(SharedPref.KEY_SELECTED_LOC);
        int distance = sharedPref.getSettingDistanceByKey(SharedPref.KEY_SETTING_DIS_CAT_ADS);

        if (!previousLocation.equals(currentLocation) || previousCatDistance != distance) {
            if (currentLocation == null) {
                currentLocationTextView.setText(Constants.CURRENT_LOCATION_TEXT);
            } else {
                currentLocationTextView.setText(currentLocation);
            }
            previousLocation = currentLocation;
            previousCatDistance = distance;
            System.out.println(">>request 111");
            //hit API on location change from user
            cateogoriesServiceModel.loadCateogries();
        }


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
        cateogoriesServiceModel.setCategories(null);
        cateogoriesServiceModel.loadCateogries();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentLocationTextView:
                ActivityController.getInstance().handleEvent(getActivity(), ActivityController.ACTIVITY_SEARCH_LOCATION);
                break;
        }
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_CATEOGRIES_LIST:
                onCategoriesResponse(isSuccess, errorString);
                break;
            default:
                break;
        }
    }

    public void onCategoriesResponse(boolean isSuccess, String errorString) {
        progressBarCenter.setVisibility(View.GONE);
        if (retryView != null) {
            retryView.setVisibility(View.GONE);
        }

        if (isSuccess) {
            setSettingsAdapter();
            if (cateogoriesServiceModel.getCategories() == null || cateogoriesServiceModel.getCategories().size() == 0) {
                showRetryView("No category found.", true);
            }
        } else {
            if (cateogoriesServiceModel.getCategories() == null || cateogoriesServiceModel.getCategories().size() == 0) {
                showRetryView(errorString, true);
            }
        }


    }
}