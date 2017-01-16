package com.netkoin.app.screens.homescreen.home.tabview;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netkoin.app.R;


public class TabController implements View.OnClickListener {

    public static final byte TAB1_STORES = 0;
    public static final byte TAB2_TRENDINGS = 1;
    public static final byte TAB3_CATEGORIES = 2;
    public static final byte TAB4_SETTINGS = 3;


    private Context context;
    private TabCallback tabCallback;
    private RelativeLayout tabView;

    //tabs
    private RelativeLayout storesTabRL;
    private RelativeLayout trendingTabRL;
    private RelativeLayout categoriesTabRL;
    private RelativeLayout settingsTabRL;

    //tab images
    private ImageView storeImageView;
    private ImageView trendingImageView;
    private ImageView categoriesImageView;
    private ImageView settingsImageView;

    //tab text
    private TextView storeTextView;
    private TextView trendingTextView;
    private TextView categoryTextView;
    private TextView settingTextView;


    public TabController(Context context, TabCallback tabCallback, RelativeLayout tabView) {
        this.context = context;
        this.tabView = tabView;
        this.tabCallback = tabCallback;
        initViews();
        registerEvents();
    }


    private void initViews() {
        storesTabRL = (RelativeLayout) tabView.findViewById(R.id.storesTabRL);
        trendingTabRL = (RelativeLayout) tabView.findViewById(R.id.trendingTabRL);
        categoriesTabRL = (RelativeLayout) tabView.findViewById(R.id.categoriesTabRL);
        settingsTabRL = (RelativeLayout) tabView.findViewById(R.id.settingsTabRL);

        storeImageView = (ImageView) tabView.findViewById(R.id.storeImageView);
        trendingImageView = (ImageView) tabView.findViewById(R.id.trendingImageView);
        categoriesImageView = (ImageView) tabView.findViewById(R.id.categoriesImageView);
        settingsImageView = (ImageView) tabView.findViewById(R.id.settingsImageView);

        storeTextView = (TextView) tabView.findViewById(R.id.storeTextView);
        trendingTextView = (TextView) tabView.findViewById(R.id.trendingTextView);
        categoryTextView = (TextView) tabView.findViewById(R.id.categoryTextView);
        settingTextView = (TextView) tabView.findViewById(R.id.settingTextView);
    }

    private void registerEvents() {
        storesTabRL.setOnClickListener(this);
        trendingTabRL.setOnClickListener(this);
        categoriesTabRL.setOnClickListener(this);
        settingsTabRL.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.storesTabRL:
                onStoreTabsClick(true);
                break;
            case R.id.trendingTabRL:
                onTrendingTabsClick(true);
                break;
            case R.id.categoriesTabRL:
                onCategoriesTabsClick(true);
                break;
            case R.id.settingsTabRL:
                onSettingsTabsClick(true);
                break;
        }
    }

    private void onSettingsTabsClick(boolean callBackRequired) {
        storeImageView.setImageResource(R.drawable.stores_line);
        trendingImageView.setImageResource(R.drawable.trending_line);
        categoriesImageView.setImageResource(R.drawable.categpries_line);
        settingsImageView.setImageResource(R.drawable.settings_fill);

        storeTextView.setTypeface(null, Typeface.NORMAL);
        trendingTextView.setTypeface(null, Typeface.NORMAL);
        categoryTextView.setTypeface(null, Typeface.NORMAL);
        settingTextView.setTypeface(null, Typeface.BOLD);

        storeTextView.setTextColor(context.getResources().getColor(R.color.gray));
        trendingTextView.setTextColor(context.getResources().getColor(R.color.gray));
        categoryTextView.setTextColor(context.getResources().getColor(R.color.gray));
        settingTextView.setTextColor(context.getResources().getColor(R.color.theamPurpleDark));


        storesTabRL.setAlpha(0.8f);
        trendingTabRL.setAlpha(0.8f);
        categoriesTabRL.setAlpha(0.8f);
        settingsTabRL.setAlpha(1.0f);


        if (callBackRequired && tabCallback != null) {
            tabCallback.onTabClick(TAB4_SETTINGS);
        }
    }

    private void onCategoriesTabsClick(boolean callBackRequired) {
        storeImageView.setImageResource(R.drawable.stores_line);
        trendingImageView.setImageResource(R.drawable.trending_line);
        categoriesImageView.setImageResource(R.drawable.categories_fill);
        settingsImageView.setImageResource(R.drawable.settings_line);

        storeTextView.setTypeface(null, Typeface.NORMAL);
        trendingTextView.setTypeface(null, Typeface.NORMAL);
        categoryTextView.setTypeface(null, Typeface.BOLD);
        settingTextView.setTypeface(null, Typeface.NORMAL);

        storeTextView.setTextColor(context.getResources().getColor(R.color.gray));
        trendingTextView.setTextColor(context.getResources().getColor(R.color.gray));
        categoryTextView.setTextColor(context.getResources().getColor(R.color.theamPurpleDark));
        settingTextView.setTextColor(context.getResources().getColor(R.color.gray));

        storesTabRL.setAlpha(0.8f);
        trendingTabRL.setAlpha(0.8f);
        categoriesTabRL.setAlpha(1.0f);
        settingsTabRL.setAlpha(0.8f);

        if (callBackRequired && tabCallback != null) {
            tabCallback.onTabClick(TAB3_CATEGORIES);
        }
    }

    private void onTrendingTabsClick(boolean callBackRequired) {

        storeImageView.setImageResource(R.drawable.stores_line);
        trendingImageView.setImageResource(R.drawable.trending_fill);
        categoriesImageView.setImageResource(R.drawable.categpries_line);
        settingsImageView.setImageResource(R.drawable.settings_line);

        storeTextView.setTypeface(null, Typeface.NORMAL);
        trendingTextView.setTypeface(null, Typeface.BOLD);
        categoryTextView.setTypeface(null, Typeface.NORMAL);
        settingTextView.setTypeface(null, Typeface.NORMAL);

        storeTextView.setTextColor(context.getResources().getColor(R.color.gray));
        trendingTextView.setTextColor(context.getResources().getColor(R.color.theamPurpleDark));
        categoryTextView.setTextColor(context.getResources().getColor(R.color.gray));
        settingTextView.setTextColor(context.getResources().getColor(R.color.gray));

        storesTabRL.setAlpha(0.8f);
        trendingTabRL.setAlpha(1.0f);
        categoriesTabRL.setAlpha(0.8f);
        settingsTabRL.setAlpha(0.8f);


        if (callBackRequired && tabCallback != null) {
            tabCallback.onTabClick(TAB2_TRENDINGS);
        }
    }

    private void onStoreTabsClick(boolean callBackRequired) {
        storeImageView.setImageResource(R.drawable.stores_fill);
        trendingImageView.setImageResource(R.drawable.trending_line);
        categoriesImageView.setImageResource(R.drawable.categpries_line);
        settingsImageView.setImageResource(R.drawable.settings_line);

        storeTextView.setTypeface(null, Typeface.BOLD);
        trendingTextView.setTypeface(null, Typeface.NORMAL);
        categoryTextView.setTypeface(null, Typeface.NORMAL);
        settingTextView.setTypeface(null, Typeface.NORMAL);

        storeTextView.setTextColor(context.getResources().getColor(R.color.theamPurpleDark));
        trendingTextView.setTextColor(context.getResources().getColor(R.color.gray));
        categoryTextView.setTextColor(context.getResources().getColor(R.color.gray));
        settingTextView.setTextColor(context.getResources().getColor(R.color.gray));

        storesTabRL.setAlpha(1.0f);
        trendingTabRL.setAlpha(0.8f);
        categoriesTabRL.setAlpha(0.8f);
        settingsTabRL.setAlpha(0.8f);


        if (callBackRequired && tabCallback != null) {
            tabCallback.onTabClick(TAB1_STORES);
        }
    }

    public void onPageChange(int pageNo) {
        switch (pageNo) {
            case TabController.TAB1_STORES:
                onStoreTabsClick(false);
                break;
            case TabController.TAB2_TRENDINGS:
                onTrendingTabsClick(false);
                break;
            case TabController.TAB3_CATEGORIES:
                onCategoriesTabsClick(false);
                break;
            case TabController.TAB4_SETTINGS:
                onSettingsTabsClick(false);
                break;

        }
    }
}
