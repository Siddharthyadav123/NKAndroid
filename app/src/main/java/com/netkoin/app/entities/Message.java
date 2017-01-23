package com.netkoin.app.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharth on 1/23/2017.
 */
public class Message implements Parcelable {
    private int koins;
    private String message;
    private String created;

    private Sender sender = null;
    private Receiver receiver = null;

    protected Message(Parcel in) {
        koins = in.readInt();
        message = in.readString();
        created = in.readString();
        receiver = in.readParcelable(Receiver.class.getClassLoader());
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int getKoins() {
        return koins;
    }

    public void setKoins(int koins) {
        this.koins = koins;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(koins);
        parcel.writeString(message);
        parcel.writeString(created);
        parcel.writeParcelable(receiver, i);
    }
}
