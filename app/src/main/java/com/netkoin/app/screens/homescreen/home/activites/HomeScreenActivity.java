package com.netkoin.app.screens.homescreen.home.activites;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.netkoin.app.R;
import com.netkoin.app.application.MyApplication;
import com.netkoin.app.base_classes.AbstractBaseActivity;
import com.netkoin.app.base_classes.AbstractBaseFragment;
import com.netkoin.app.screens.homescreen.home.fragments.CategoriesHomeScreenFragment;
import com.netkoin.app.screens.homescreen.home.fragments.SettingsHomeScreenFragments;
import com.netkoin.app.screens.homescreen.home.fragments.StoresHomeScreenFragment;
import com.netkoin.app.screens.homescreen.home.fragments.TrendingHomeScreenFragment;
import com.netkoin.app.screens.homescreen.home.tabview.TabCallback;
import com.netkoin.app.screens.homescreen.home.tabview.TabController;
import com.netkoin.app.utils.Utils;

public class HomeScreenActivity extends AbstractBaseActivity implements TabCallback {
    //views
    private RelativeLayout tabView;
    //members
    private TabController tabController;

    //homescreen fragments
    private StoresHomeScreenFragment storesHomeScreenFragment;
    private TrendingHomeScreenFragment trendingHomeScreenFragment;
    private CategoriesHomeScreenFragment categoriesHomeScreenFragment;
    private SettingsHomeScreenFragments settingsHomeScreenFragments;

    //currently open fragment
    private AbstractBaseFragment currentOpenAbstractBaseFragment;

    @Override
    public View initView() {
        destroyFragmentMemoryIfExist();
        View view = getLayoutInflater().inflate(R.layout.activity_main, null);
        tabView = (RelativeLayout) view.findViewById(R.id.tabs);
        tabController = new TabController(this, this, tabView);
        Utils.getInstance().enableGPS(this);
        checkPermissions(REQUEST_MARSHMELLO_PERMISSIONS, mustPermissions, null);
        return view;
    }

    private void destroyFragmentMemoryIfExist() {
        storesHomeScreenFragment = null;
        trendingHomeScreenFragment = null;
        categoriesHomeScreenFragment = null;
        settingsHomeScreenFragments = null;
    }

    @Override
    public void registerEvents() {

    }


    @Override
    public void updateUI() {
        onTabClick(TabController.TAB1_STORES);
    }

    @Override
    public void onActionBarLeftBtnClick() {

    }

    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onPermissionResult(int requestCode, boolean isGranted, Object extras) {

    }


    @Override
    public void onTabClick(byte position) {
        FragmentTransaction childFragTrans = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case TabController.TAB1_STORES:

                if (storesHomeScreenFragment == null) {
                    storesHomeScreenFragment = StoresHomeScreenFragment.newInstance();
                }
                this.currentOpenAbstractBaseFragment = storesHomeScreenFragment;
                childFragTrans.replace(R.id.homeScreenFragmentFrameLayout, storesHomeScreenFragment);
                break;

            case TabController.TAB2_TRENDINGS:
                if (trendingHomeScreenFragment == null) {
                    trendingHomeScreenFragment = TrendingHomeScreenFragment.newInstance();
                }
                this.currentOpenAbstractBaseFragment = trendingHomeScreenFragment;
                childFragTrans.replace(R.id.homeScreenFragmentFrameLayout, trendingHomeScreenFragment);
                break;

            case TabController.TAB3_CATEGORIES:
                if (categoriesHomeScreenFragment == null) {
                    categoriesHomeScreenFragment = CategoriesHomeScreenFragment.newInstance();
                }
                this.currentOpenAbstractBaseFragment = categoriesHomeScreenFragment;
                childFragTrans.replace(R.id.homeScreenFragmentFrameLayout, categoriesHomeScreenFragment);
                break;

            case TabController.TAB4_SETTINGS:
                if (settingsHomeScreenFragments == null) {
                    settingsHomeScreenFragments = SettingsHomeScreenFragments.newInstance();
                }
                this.currentOpenAbstractBaseFragment = settingsHomeScreenFragments;
                childFragTrans.replace(R.id.homeScreenFragmentFrameLayout, settingsHomeScreenFragments);
                break;
        }

        childFragTrans.addToBackStack(null);
        childFragTrans.commit();
    }

    @Override
    public void onBackPressed() {
        if (!currentOpenAbstractBaseFragment.getNavigationController().pop()) {
            moveTaskToBack(true);
        }
    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setHomeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.getInstance().setHomeActivity(null);
    }
}
