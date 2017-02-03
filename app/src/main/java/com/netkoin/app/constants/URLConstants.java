package com.netkoin.app.constants;

import com.netkoin.app.BuildConfig;

/**
 * Created by sid on 07/08/2016.
 */
public interface URLConstants {
    //String BASE_URL="http://192.168.1.157/storeads";
//    String BASE_URL = "https://test2.netkoin.com";
    //String BASE_URL="https://biz.netkoin.com";

    String BASE_URL = BuildConfig.BASE_URL;

    String URL_IMAGE = BASE_URL + "/uploads/files/";

    String URL_SIGNUP = BASE_URL + "/api/users.json";
    String URL_SIGNIN = BASE_URL + "/api/users/token.json";
    String URL_FACEBOOK_SIGNIN = BASE_URL + "/api/users/facebookLogin.json";
    String URL_GPLUS_SIGNIN = BASE_URL + "/api/users/googleLogin.json";

    String URL_HOME_FEATURED_BANNER = BASE_URL + "/api/home_featured_banners.json";
    String URL_STORE_FEATURED_BANNER = BASE_URL + "/api/store_featured_banners.json";
    String URL_STORES = BASE_URL + "/api/stores.json";

    String URL_UPDATE_USER_LOCATION = BASE_URL + "/api/users/updateLocation.json";
    String URL_REDEEM_KOIN = BASE_URL + "/api/redeemed_koins/redeemKoin.json";


    String URL_CHECK_IN_WALKIN = BASE_URL + "/api/users/walkins.json";
//    String URL_TOTAL_KOINS = BASE_URL +  "api/users/totalKoins.json"

    String URL_FIND_STORES = BASE_URL + "/api/stores/findStore.json";

    String URL_ADS = BASE_URL + "/api/ads.json";
    String URL_TREANDING_ADS = BASE_URL + "/api/ads/findAds.json";

    String URL_CATALOGUES = BASE_URL + "/api/catalogues.json";

    String URL_CHECK_IN_PRODUCTS = BASE_URL + "/api/users/products.json";
    String URL_CHECK_IN_PURCHASES = BASE_URL + "/api/users/purchases.json";

    String URL_PRODUCT_BARCODES = BASE_URL + "/api/product_barcodes.json";
    String URL_PURCHASE_BARCODES = BASE_URL + "/api/purchase_barcodes.json";


    //url to get cateogries
    String URL_GET_CATEGORIES = BASE_URL + "/api/categories.json";

    //koin management
    String URL_GET_TOTAL_KOINS = BASE_URL + "/api/users/totalKoins.json";
    String URL_POST_TRANSER_KOINS = BASE_URL + "/api/users/transferKoins.json";
    String URL_GET_ACTIVITY_LOGS = BASE_URL + "/api/activity_logs.json";
    String URL_GET_ACTIVITY_LOGS_UNREAD_COUNT = BASE_URL + "/api/activity_logs/unreadCount.json";
    String URL_GET_ACTIVITY_LOGS_SET_READ_ALL = BASE_URL + "/api/activity_logs/readAllActivityLog.json";
    String URL_GET_SENT_MESSAGES = BASE_URL + "/api/users/sent_messages.json";
    String URL_GET_RECIEVED_MESSAGES = BASE_URL + "/api/users/received_messages.json";

    //save settings
    String URL_POST_SETTINGS = BASE_URL + "/api/user_settings";
    String URL_POST_LOGOUT = BASE_URL + "/api/users/logout.json";

    String URL_TUTORIALS = "https://youtu.be/Z7O2mI79FC8";
    String URL_FAQ = "http://www.netkoin.com/faq";
    String URL_TERMS = "http://www.netkoin.com/terms";
    String URL_POLICY = "http://www.netkoin.com/privacy";

    String URL_FACEBOOK_NETKOIN = "https://www.facebook.com/Netkoin/";
    String URL_TWITTER_NETKOIN = "https://twitter.com/netkoin";
    String URL_INSTAGRAM_NETKOIN = "https://www.instagram.com/netkoin/";

}
