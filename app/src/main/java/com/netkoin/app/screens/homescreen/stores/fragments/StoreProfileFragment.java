package com.netkoin.app.screens.homescreen.stores.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.controller.FragmentNavigationViewController;
import com.netkoin.app.entities.Store;
import com.netkoin.app.screens.homescreen.stores.adapters.StoreBannerViewPagerAdapter;
import com.netkoin.app.servicemodels.StoreServiceModel;
import com.netkoin.app.servicemodels.UserServiceModel;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by siddharthyadav on 31/12/16.
 */

public class StoreProfileFragment extends AbstractBaseFragment {

    private TextView walkInCountTextView;
    private TextView productCountTextView;
    private TextView purchaseCountTextView;

    private TextView featuredCountTextView;
    private TextView catalougeCountTextView;

    private LinearLayout walkInImageLinLayout;
    private LinearLayout productImageLinLayout;
    private LinearLayout purchaseImageLinLayout;
    private LinearLayout featuredLinLayout;
    private LinearLayout catalougeLinLayout;

    private RelativeLayout storeProfileHeaderRelativeLayout;
    private ViewPager storeBannerViewPager;
    private CircleIndicator viewPagerCircleIndicator;
    private ProgressBar progressBarCenter;

    private StoreBannerViewPagerAdapter storeBannerViewPagerAdapter;
    private StoreServiceModel storeServiceModel;

    //passed store using parcel
    private Store store;

    public StoreProfileFragment() {
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
    }

    private void makeView() {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_store_profile_layout, null, false);
        initActionBarView(R.drawable.back, store.getName());
        initViews();
        registerEvents();
        updateUI();

        //asking for store banner
        storeServiceModel = new StoreServiceModel(getActivity(), this);
        storeServiceModel.loadStoreProfileFeaturedBanner(store.getId());
    }

    private void updateUI() {
        //setting counts
        if (store.getStore_koins() != null && store.getStore_koins().size() > 0) {
            walkInCountTextView.setText(store.getStore_koins().get(0).getWalkins() + "");
            productCountTextView.setText(store.getStore_koins().get(0).getProducts() + "");
            purchaseCountTextView.setText(store.getStore_koins().get(0).getPurchases() + "");
        }

        if (store.getAds() != null && store.getAds().size() > 0) {
            featuredCountTextView.setText(store.getAds().get(0).getCount() + " Items");
        }

        if (store.getCatalogues() != null && store.getCatalogues().size() > 0) {
            catalougeCountTextView.setText(store.getCatalogues().get(0).getCount() + " Items");
        }
    }


    private void registerEvents() {
        walkInImageLinLayout.setOnClickListener(this);
        productImageLinLayout.setOnClickListener(this);
        purchaseImageLinLayout.setOnClickListener(this);
        featuredLinLayout.setOnClickListener(this);
        catalougeLinLayout.setOnClickListener(this);
    }

    private void initViews() {
        storeBannerViewPager = (ViewPager) view.findViewById(R.id.storeBannerViewPager);
        viewPagerCircleIndicator = (CircleIndicator) view.findViewById(R.id.viewPagerCircleIndicator);
        storeProfileHeaderRelativeLayout = (RelativeLayout) view.findViewById(R.id.storeProfileHeaderRelativeLayout);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);

        walkInCountTextView = (TextView) view.findViewById(R.id.walkInCountTextView);
        productCountTextView = (TextView) view.findViewById(R.id.productCountTextView);
        purchaseCountTextView = (TextView) view.findViewById(R.id.purchaseCountTextView);

        featuredCountTextView = (TextView) view.findViewById(R.id.featuredCountTextView);
        catalougeCountTextView = (TextView) view.findViewById(R.id.catalougeCountTextView);

        walkInImageLinLayout = (LinearLayout) view.findViewById(R.id.walkInImageLinLayout);
        productImageLinLayout = (LinearLayout) view.findViewById(R.id.productImageLinLayout);
        purchaseImageLinLayout = (LinearLayout) view.findViewById(R.id.purchaseImageLinLayout);
        featuredLinLayout = (LinearLayout) view.findViewById(R.id.featuredLinLayout);
        catalougeLinLayout = (LinearLayout) view.findViewById(R.id.catalougeLinLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return view;

    }

    private void updateStoreBannerAdatper() {
        //set banner view
        if (storeBannerViewPagerAdapter == null) {
            storeBannerViewPagerAdapter = new StoreBannerViewPagerAdapter(getActivity(), storeServiceModel.getStoresProfileFeaturedBanner());
            storeBannerViewPager.setAdapter(storeBannerViewPagerAdapter);
            viewPagerCircleIndicator.setViewPager(storeBannerViewPager);
        } else {
            storeBannerViewPagerAdapter.refreshAdapter(storeServiceModel.getStoresProfileFeaturedBanner());
        }
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
        switch (v.getId()) {
            case R.id.walkInImageLinLayout:
                onWalkInImageClick();
                break;
            case R.id.productImageLinLayout:
                onProductImageClick();
                break;
            case R.id.purchaseImageLinLayout:
                onPurchaseImageClick();
                break;
            case R.id.featuredLinLayout:
                onFeaturedLayoutClick();
                break;
            case R.id.catalougeLinLayout:
                onCatalougeLayoutClick();
                break;
        }
    }

    private void onCatalougeLayoutClick() {
        navigationController.push(FragmentNavigationViewController.FRAGMENT_CATALOUGE, store);
    }

    private void onFeaturedLayoutClick() {
        navigationController.push(FragmentNavigationViewController.FRAGMENT_FEATURED_BANNER, store);
    }

    private void onPurchaseImageClick() {
        navigationController.push(FragmentNavigationViewController.FRAGMENT_PURCHASE, store);
    }

    private void onProductImageClick() {
        navigationController.push(FragmentNavigationViewController.FRAGMENT_PRODUCT, store);
    }

    private void onWalkInImageClick() {
        UserServiceModel userServiceModel = new UserServiceModel(getActivity(), this);
        userServiceModel.checkInWalkins(store.getId());
    }


    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_STORE_FEATURED_BANNERS:
                onFeaturedBannerResponse(isSuccess, errorString);
                break;
            case RequestConstants.REQIEST_ID_CHECKIN_WALKIN:
                onCheckInWalkinResponse(isSuccess, errorString);
                break;
            default:
                break;
        }
    }

    public void onFeaturedBannerResponse(boolean isSuccess, String errorString) {
        progressBarCenter.setVisibility(View.GONE);
        if (isSuccess) {
            if (storeServiceModel.getStoresProfileFeaturedBanner() != null && storeServiceModel.getStoresProfileFeaturedBanner().size() > 0) {
                //load store banner refresh adapter
                storeProfileHeaderRelativeLayout.setVisibility(View.GONE);
                updateStoreBannerAdatper();
            } else {
                //load single banner
                loadStoreSingleBanner();
            }
        } else {
            //load single banner
            loadStoreSingleBanner();
        }
    }

    private void loadStoreSingleBanner() {
        storeProfileHeaderRelativeLayout.setVisibility(View.VISIBLE);

        ImageView storeBannerImageView = (ImageView) storeProfileHeaderRelativeLayout.findViewById(R.id.storeBannerImageView);
        ImageView verifiedImageView = (ImageView) storeProfileHeaderRelativeLayout.findViewById(R.id.verifiedImageView);
        ImageView storeLogoImageView = (ImageView) storeProfileHeaderRelativeLayout.findViewById(R.id.storeLogoImageView);

        TextView storeNameTextView = (TextView) storeProfileHeaderRelativeLayout.findViewById(R.id.storeNameTextView);
        TextView storeDesTextView = (TextView) storeProfileHeaderRelativeLayout.findViewById(R.id.storeDesTextView);


        if (store.getBanner() != null) {
            String bannerURL = URLConstants.URL_IMAGE + store.getBanner().getName();
            Glide.with(this).load(bannerURL).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(storeBannerImageView);
        }

        if (store.getLogo() != null) {
            String logoURL = URLConstants.URL_IMAGE + store.getLogo().getName();
            Glide.with(this).load(logoURL).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).into(storeLogoImageView);
        }

        //setting store details
        storeNameTextView.setText(store.getName());
        storeDesTextView.setText(store.getShort_desc());

    }

    public void onCheckInWalkinResponse(boolean isSuccess, String errorString) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), errorString, Snackbar.LENGTH_LONG)
                .show();

    }
}