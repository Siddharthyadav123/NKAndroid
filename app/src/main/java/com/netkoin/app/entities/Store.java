package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ashishkumarpatel on 02/01/17.
 */

public class Store implements Parcelable {
    private int id;
    private String distance;
    private String name;
    private String short_desc;
    private Banner banner;
    private Logo logo;
    private ArrayList<StoreKoin> store_koins = new ArrayList<>();

    private double latitude;
    private double longitude;

    private ArrayList<StoreItemCount> ads = new ArrayList<>();
    private ArrayList<StoreItemCount> catalogues = new ArrayList<>();
    private City city;
    private State state;
    private Country country;

    protected Store(Parcel in) {
        id = in.readInt();
        distance = in.readString();
        name = in.readString();
        short_desc = in.readString();
        banner = in.readParcelable(Banner.class.getClassLoader());
        logo = in.readParcelable(Logo.class.getClassLoader());
        store_koins = in.createTypedArrayList(StoreKoin.CREATOR);
        latitude = in.readDouble();
        longitude = in.readDouble();
        ads = in.createTypedArrayList(StoreItemCount.CREATOR);
        catalogues = in.createTypedArrayList(StoreItemCount.CREATOR);
        city = in.readParcelable(City.class.getClassLoader());
        state = in.readParcelable(State.class.getClassLoader());
        country = in.readParcelable(Country.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(distance);
        dest.writeString(name);
        dest.writeString(short_desc);
        dest.writeParcelable(banner, flags);
        dest.writeParcelable(logo, flags);
        dest.writeTypedList(store_koins);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeTypedList(ads);
        dest.writeTypedList(catalogues);
        dest.writeParcelable(city, flags);
        dest.writeParcelable(state, flags);
        dest.writeParcelable(country, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public ArrayList<StoreKoin> getStore_koins() {
        return store_koins;
    }

    public void setStore_koins(ArrayList<StoreKoin> store_koins) {
        this.store_koins = store_koins;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<StoreItemCount> getAds() {
        return ads;
    }

    public void setAds(ArrayList<StoreItemCount> ads) {
        this.ads = ads;
    }

    public ArrayList<StoreItemCount> getCatalogues() {
        return catalogues;
    }

    public void setCatalogues(ArrayList<StoreItemCount> catalogues) {
        this.catalogues = catalogues;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
