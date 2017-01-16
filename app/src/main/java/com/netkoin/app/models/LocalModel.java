package com.netkoin.app.models;

import com.netkoin.app.entities.TotalKoin;
import com.netkoin.app.location.LocationModel;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class LocalModel {

    private String token;

    //below data will be used to send latitude and longitude in api call
    private LocationModel locationModel;

    //    var selectedStore:Store?
//    var selectedProductBarcode:ProductBarcode?
//    var selectedPurchaseBarcode:PurchaseBarcode?
//    var barcodeValue:String?
//
    private TotalKoin totalKoins;
    private String pushToken;

//
//    var nearbyStoreDistance:String?
//    var nearbyTrendingAdsDistance:String?
//    var nearByCategoryAdsDistance:String?


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TotalKoin getTotalKoins() {
        return totalKoins;
    }

    public void setTotalKoins(TotalKoin totalKoins) {
        this.totalKoins = totalKoins;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }
}
