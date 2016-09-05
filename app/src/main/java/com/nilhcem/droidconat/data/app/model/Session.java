package com.nilhcem.droidconat.data.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import lombok.Value;

@Value
public class Session implements Parcelable {

    public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        public Session createFromParcel(Parcel source) {
            return new Session(source);
        }

        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    int id;
    String room;
    List<Speaker> speakers;
    String title;
    String description;
    LocalDateTime fromTime;
    LocalDateTime toTime;

    public Session(int id, String room, List<Speaker> speakers, String title, String description, LocalDateTime fromTime, LocalDateTime toTime) {
        this.id = id;
        this.room = room;
        this.speakers = speakers;
        this.title = title;
        this.description = description;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    protected Session(Parcel in) {
        id = in.readInt();
        room = in.readString();
        speakers = in.createTypedArrayList(Speaker.CREATOR);
        title = in.readString();
        description = in.readString();
        fromTime = (LocalDateTime) in.readSerializable();
        toTime = (LocalDateTime) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(room);
        dest.writeTypedList(speakers);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeSerializable(fromTime);
        dest.writeSerializable(toTime);
    }
}
