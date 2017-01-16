package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class State implements Parcelable {
    private int id;
    private String name;

    protected State(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
