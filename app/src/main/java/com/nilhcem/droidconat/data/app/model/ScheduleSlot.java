package com.nilhcem.droidconat.data.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import lombok.Value;

@Value
public class ScheduleSlot implements Parcelable {

    public static final Parcelable.Creator<ScheduleSlot> CREATOR = new Parcelable.Creator<ScheduleSlot>() {
        public ScheduleSlot createFromParcel(Parcel source) {
            return new ScheduleSlot(source);
        }

        public ScheduleSlot[] newArray(int size) {
            return new ScheduleSlot[size];
        }
    };

    LocalDateTime time;
    List<Session> sessions;

    public ScheduleSlot(LocalDateTime time, List<Session> sessions) {
        this.time = time;
        this.sessions = sessions;
    }

    protected ScheduleSlot(Parcel in) {
        time = (LocalDateTime) in.readSerializable();
        sessions = in.createTypedArrayList(Session.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(time);
        dest.writeTypedList(sessions);
    }
}
