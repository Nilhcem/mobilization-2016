package com.nilhcem.droidconat.data.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Schedule extends ArrayList<ScheduleDay> implements Parcelable {

    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public Schedule() {
    }

    protected Schedule(Parcel in) {
        addAll(in.createTypedArrayList(ScheduleDay.CREATOR));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this);
    }
}
