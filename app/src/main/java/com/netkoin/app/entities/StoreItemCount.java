package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashishkumarpatel on 02/01/17.
 */

public class StoreItemCount implements Parcelable {
    private int store_id;
    private int count;

    protected StoreItemCount(Parcel in) {
        store_id = in.readInt();
        count = in.readInt();
    }

    public static final Creator<StoreItemCount> CREATOR = new Creator<StoreItemCount>() {
        @Override
        public StoreItemCount createFromParcel(Parcel in) {
            return new StoreItemCount(in);
        }

        @Override
        public StoreItemCount[] newArray(int size) {
            return new StoreItemCount[size];
        }
    };

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(store_id);
        dest.writeInt(count);
    }
}
