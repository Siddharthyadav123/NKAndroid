package com.netkoin.app.screens.homescreen.stores.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netkoin.app.R;
import com.netkoin.app.constants.URLConstants;
import com.netkoin.app.entities.StoreFeaturedBanner;

import java.util.ArrayList;

/**
 * Created by siddharth on 1/5/2017.
 */
public class StoreBannerViewPagerAdapter extends PagerAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ArrayList<StoreFeaturedBanner> storesProfileFeaturedBanner;

    public StoreBannerViewPagerAdapter(Activity activity, ArrayList<StoreFeaturedBanner> storesProfileFeaturedBanner) {
        this.activity = activity;
        this.storesProfileFeaturedBanner = storesProfileFeaturedBanner;
        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return storesProfileFeaturedBanner.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        ViewGroup currentView = (ViewGroup) layoutInflater.inflate(R.layout.store_header_layout, view, false);

        ImageView storeBannerImageView = (ImageView) currentView.findViewById(R.id.storeBannerImageView);
        TextView bannerNameTextView = (TextView) currentView.findViewById(R.id.bannerNameTextView);


        StoreFeaturedBanner storeFeaturedBanner = storesProfileFeaturedBanner.get(position);
        bannerNameTextView.setText(storeFeaturedBanner.getName());

        String imageUrl = URLConstants.URL_IMAGE + storeFeaturedBanner.getBanner().getName();

        Glide.with(activity).load(imageUrl).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.place_holder).into(storeBannerImageView);


        ((ViewPager) view).addView(currentView, 0);
        return currentView;
    }

    public void refreshAdapter(ArrayList<StoreFeaturedBanner> storesProfileFeaturedBanner) {
        this.storesProfileFeaturedBanner = storesProfileFeaturedBanner;
        notifyDataSetChanged();
    }
}
