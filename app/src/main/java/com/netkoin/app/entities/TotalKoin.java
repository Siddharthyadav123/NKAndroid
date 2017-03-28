package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashishkumarpatel on 02/01/17.
 */

public class TotalKoin implements Parcelable {
    private int total_koins = 0;
    private int earned_koins_last_1_day = 0;
    private int earned_koins_last_30_days = 0;

    public TotalKoin() {
        
    }

    protected TotalKoin(Parcel in) {
        total_koins = in.readInt();
        earned_koins_last_1_day = in.readInt();
        earned_koins_last_30_days = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total_koins);
        dest.writeInt(earned_koins_last_1_day);
        dest.writeInt(earned_koins_last_30_days);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TotalKoin> CREATOR = new Creator<TotalKoin>() {
        @Override
        public TotalKoin createFromParcel(Parcel in) {
            return new TotalKoin(in);
        }

        @Override
        public TotalKoin[] newArray(int size) {
            return new TotalKoin[size];
        }
    };

    public int getTotal_koins() {
        return total_koins;
    }

    public void setTotal_koins(int total_koins) {
        this.total_koins = total_koins;
    }

    public int getEarned_koins_last_1_day() {
        return earned_koins_last_1_day;
    }

    public void setEarned_koins_last_1_day(int earned_koins_last_1_day) {
        this.earned_koins_last_1_day = earned_koins_last_1_day;
    }

    public int getEarned_koins_last_30_days() {
        return earned_koins_last_30_days;
    }

    public void setEarned_koins_last_30_days(int earned_koins_last_30_days) {
        this.earned_koins_last_30_days = earned_koins_last_30_days;
    }
}
