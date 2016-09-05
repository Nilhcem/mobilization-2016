package com.nilhcem.droidconat.data.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Value;

@Value
public class Speaker implements Parcelable {

    public static final Parcelable.Creator<Speaker> CREATOR = new Parcelable.Creator<Speaker>() {
        public Speaker createFromParcel(Parcel source) {
            return new Speaker(source);
        }

        public Speaker[] newArray(int size) {
            return new Speaker[size];
        }
    };

    int id;
    String name;
    String title;
    String bio;
    String website;
    String twitter;
    String github;
    String photo;

    public Speaker(int id, String name, String title, String bio, String website, String twitter, String github, String photo) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.bio = bio;
        this.website = website;
        this.twitter = twitter;
        this.github = github;
        this.photo = photo;
    }

    protected Speaker(Parcel in) {
        id = in.readInt();
        name = in.readString();
        title = in.readString();
        bio = in.readString();
        website = in.readString();
        twitter = in.readString();
        github = in.readString();
        photo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(bio);
        dest.writeString(website);
        dest.writeString(twitter);
        dest.writeString(github);
        dest.writeString(photo);
    }
}
