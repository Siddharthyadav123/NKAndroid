package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class MainCategory implements Parcelable {
    private int id;
    private String name;
    private Logo logo;
    private Banner banner;

    protected MainCategory(Parcel in) {
        id = in.readInt();
        name = in.readString();
        logo = in.readParcelable(Logo.class.getClassLoader());
        banner = in.readParcelable(Banner.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable(logo, flags);
        dest.writeParcelable(banner, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainCategory> CREATOR = new Creator<MainCategory>() {
        @Override
        public MainCategory createFromParcel(Parcel in) {
            return new MainCategory(in);
        }

        @Override
        public MainCategory[] newArray(int size) {
            return new MainCategory[size];
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
}
