package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashishkumarpatel on 1/23/2017.
 */
public class Receiver implements Parcelable {
    private int id;
    private String email;

    protected Receiver(Parcel in) {
        id = in.readInt();
        email = in.readString();
    }

    public static final Creator<Receiver> CREATOR = new Creator<Receiver>() {
        @Override
        public Receiver createFromParcel(Parcel in) {
            return new Receiver(in);
        }

        @Override
        public Receiver[] newArray(int size) {
            return new Receiver[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(email);
    }
}
