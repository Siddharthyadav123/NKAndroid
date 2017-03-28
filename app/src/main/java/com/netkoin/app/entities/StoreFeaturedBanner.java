package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashishkumarpatel on 02/01/17.
 */

public class StoreFeaturedBanner implements Parcelable {

    private int id;
    private String name;
    private String short_desc;
    private Logo logo;
    private Banner banner;

    protected StoreFeaturedBanner(Parcel in) {
        id = in.readInt();
        name = in.readString();
        short_desc = in.readString();
        logo = in.readParcelable(Logo.class.getClassLoader());
        banner = in.readParcelable(Banner.class.getClassLoader());
    }

    public static final Creator<StoreFeaturedBanner> CREATOR = new Creator<StoreFeaturedBanner>() {
        @Override
        public StoreFeaturedBanner createFromParcel(Parcel in) {
            return new StoreFeaturedBanner(in);
        }

        @Override
        public StoreFeaturedBanner[] newArray(int size) {
            return new StoreFeaturedBanner[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(short_desc);
        dest.writeParcelable(logo, flags);
        dest.writeParcelable(banner, flags);
    }
}
