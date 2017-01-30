package com.netkoin.app.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.screens.homescreen.categories.fragments.CategoriesDetailFragment;
import com.netkoin.app.screens.homescreen.categories.fragments.CategoriesFragment;
import com.netkoin.app.screens.homescreen.settings.fragments.DistanceSettingFragment;
import com.netkoin.app.screens.homescreen.settings.fragments.FAQFragment;
import com.netkoin.app.screens.homescreen.settings.fragments.FollowUsFragment;
import com.netkoin.app.screens.homescreen.settings.fragments.NotificationSettingFragment;
import com.netkoin.app.screens.homescreen.settings.fragments.SettingsFragment;
import com.netkoin.app.screens.homescreen.stores.fragments.CatalougeFragment;
import com.netkoin.app.screens.homescreen.stores.fragments.FeaturedFragment;
import com.netkoin.app.screens.homescreen.stores.fragments.ProductFragment;
import com.netkoin.app.screens.homescreen.stores.fragments.PurchaseFragment;
import com.netkoin.app.screens.homescreen.stores.fragments.StoreFragment;
import com.netkoin.app.screens.homescreen.stores.fragments.StoreProfileFragment;
import com.netkoin.app.screens.homescreen.trending.fragments.TrendingFragment;
import com.netkoin.app.screens.koin_managment.fragments.KoinActivitesFragment;
import com.netkoin.app.screens.koin_managment.fragments.KoinAppTutorialFragment;
import com.netkoin.app.screens.koin_managment.fragments.KoinHomeFragment;
import com.netkoin.app.screens.koin_managment.fragments.KoinMessagesFragment;
import com.netkoin.app.screens.koin_managment.fragments.KoinTransferFragment;

import java.util.ArrayList;

/**
 * Created by siddharthyadav on 31/12/16.
 */

public class FragmentNavigationViewController {

    //home fragments
    public static final byte FRAGMENT_STORE = 0;
    public static final byte FRAGMENT_TRENDINGS = 1;
    public static final byte FRAGMENT_CATEGORIES = 2;
    public static final byte FRAGMENT_SETTINGS = 3;

    //stores specific fragments
    public static final byte FRAGMENT_STORE_PROFILE = 4;
    public static final byte FRAGMENT_FEATURED_BANNER = 5;
    public static final byte FRAGMENT_CATALOUGE = 6;
    public static final byte FRAGMENT_PRODUCT = 7;
    public static final byte FRAGMENT_PURCHASE = 8;

    public static final byte FRAGMENT_CATEGORY_DETAIL = 9;

    //belongs to setting
    public static final byte FRAGMENT_DISTANCE_SETTINGS = 10;
    public static final byte FRAGMENT_FAQ = 11;
    public static final byte FRAGMENT_NOTIFICATION_SETTINGS = 12;

    //belongs to koin managment module
    public static final byte FRAGMENT_KOIN_HOME = 13;
    public static final byte FRAGMENT_KOIN_ACTIVITY = 14;
    public static final byte FRAGMENT_KOIN_TRANSFER = 15;
    public static final byte FRAGMENT_KOIN_APP_TUTORIAL = 16;
    public static final byte FRAGMENT_KOIN_MESSAGES = 17;

    public static final byte FRAGMENT_FOLLOW_US = 18;

    private ArrayList<Fragment> fragmentsStack = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int container_id;


    public FragmentNavigationViewController(@NonNull FragmentManager fragmentManager, int container_id) {
        this.fragmentManager = fragmentManager;
        this.container_id = container_id;
    }

    /**
     * Method to launch the child fragment
     *
     * @param fragmentId
     * @param parcelExtras
     */
    public void push(int fragmentId, @Nullable Object parcelExtras) {
        AbstractBaseFragment abstractBaseFragment = null;
        boolean needPushAnimation = false;

        switch (fragmentId) {
            case FRAGMENT_STORE:
                abstractBaseFragment = new StoreFragment();
                break;

            case FRAGMENT_TRENDINGS:
                abstractBaseFragment = new TrendingFragment();
                break;

            case FRAGMENT_CATEGORIES:
                abstractBaseFragment = new CategoriesFragment();
                break;

            case FRAGMENT_SETTINGS:
                abstractBaseFragment = new SettingsFragment();
                break;

            case FRAGMENT_STORE_PROFILE:
                abstractBaseFragment = new StoreProfileFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_FEATURED_BANNER:
                abstractBaseFragment = new FeaturedFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_CATALOUGE:
                abstractBaseFragment = new CatalougeFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_PRODUCT:
                abstractBaseFragment = new ProductFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_PURCHASE:
                abstractBaseFragment = new PurchaseFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_CATEGORY_DETAIL:
                abstractBaseFragment = new CategoriesDetailFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_DISTANCE_SETTINGS:
                abstractBaseFragment = new DistanceSettingFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_FAQ:
                abstractBaseFragment = new FAQFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_NOTIFICATION_SETTINGS:
                abstractBaseFragment = new NotificationSettingFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_KOIN_HOME:
                abstractBaseFragment = new KoinHomeFragment();
                break;

            case FRAGMENT_KOIN_ACTIVITY:
                abstractBaseFragment = new KoinActivitesFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_KOIN_TRANSFER:
                abstractBaseFragment = new KoinTransferFragment();
                needPushAnimation = true;
                break;

            case FRAGMENT_KOIN_APP_TUTORIAL:
                abstractBaseFragment = new KoinAppTutorialFragment();
                needPushAnimation = true;
                break;
            case FRAGMENT_KOIN_MESSAGES:
                abstractBaseFragment = new KoinMessagesFragment();
                needPushAnimation = true;
                break;
            case FRAGMENT_FOLLOW_US:
                abstractBaseFragment = new FollowUsFragment();
                needPushAnimation = true;
                break;
        }

        //pushing
        abstractBaseFragment.setNavigationController(this);
        abstractBaseFragment.setParcelExtras(parcelExtras);
        push(abstractBaseFragment, needPushAnimation);
    }


    private void push(Fragment newFragmentToShow, boolean needDefaultAnim) {
        fragmentsStack.add(newFragmentToShow);

        FragmentTransaction childFragTrans = fragmentManager.beginTransaction();
        childFragTrans.replace(container_id, newFragmentToShow);

        if (needDefaultAnim) {
            childFragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        childFragTrans.addToBackStack(null);
        childFragTrans.commit();

    }

    /**
     * It replace the current fragment with the back stack if exist any
     *
     * @return : Tells whether any fragment returned or not.
     */
    public boolean pop() {
        //don't remove any fragment if stack have only one
        if (fragmentsStack.size() == 1) {
            return false;
        }

        fragmentsStack.remove(fragmentsStack.size() - 1);
        FragmentTransaction childFragTrans = fragmentManager.beginTransaction();
        childFragTrans.replace(container_id, fragmentsStack.get(fragmentsStack.size() - 1));
        childFragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        childFragTrans.addToBackStack(null);
        childFragTrans.commit();

        return true;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public ArrayList<Fragment> getFragmentsStack() {
        return fragmentsStack;
    }
}
