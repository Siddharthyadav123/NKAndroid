package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class Logo implements Parcelable {
    private int id;
    private String name;

    protected Logo(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Logo> CREATOR = new Creator<Logo>() {
        @Override
        public Logo createFromParcel(Parcel in) {
            return new Logo(in);
        }

        @Override
        public Logo[] newArray(int size) {
            return new Logo[size];
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
}
