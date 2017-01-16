package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class Ads implements Parcelable {

    private int id;
    private String name;
    private String short_desc;

    private int store_id;
    private int category_id;
    private String distance;

    private GeoPoints store;
    private Category category;
    private Logo logo;
    private Banner banner;
    private String valid_to;

    protected Ads(Parcel in) {
        id = in.readInt();
        name = in.readString();
        short_desc = in.readString();
        store_id = in.readInt();
        category_id = in.readInt();
        distance = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        logo = in.readParcelable(Logo.class.getClassLoader());
        banner = in.readParcelable(Banner.class.getClassLoader());
        valid_to = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(short_desc);
        dest.writeInt(store_id);
        dest.writeInt(category_id);
        dest.writeString(distance);
        dest.writeParcelable(category, flags);
        dest.writeParcelable(logo, flags);
        dest.writeParcelable(banner, flags);
        dest.writeString(valid_to);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ads> CREATOR = new Creator<Ads>() {
        @Override
        public Ads createFromParcel(Parcel in) {
            return new Ads(in);
        }

        @Override
        public Ads[] newArray(int size) {
            return new Ads[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public GeoPoints getStore() {
        return store;
    }

    public void setStore(GeoPoints store) {
        this.store = store;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }
}
