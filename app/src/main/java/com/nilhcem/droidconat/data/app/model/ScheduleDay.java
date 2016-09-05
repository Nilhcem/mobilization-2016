package com.nilhcem.droidconat.data.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.util.List;

import lombok.Value;

@Value
public class ScheduleDay implements Parcelable {

    public static final Parcelable.Creator<ScheduleDay> CREATOR = new Parcelable.Creator<ScheduleDay>() {
        public ScheduleDay createFromParcel(Parcel source) {
            return new ScheduleDay(source);
        }

        public ScheduleDay[] newArray(int size) {
            return new ScheduleDay[size];
        }
    };

    LocalDate day;
    List<ScheduleSlot> slots;

    public ScheduleDay(LocalDate day, List<ScheduleSlot> slots) {
        this.day = day;
        this.slots = slots;
    }

    protected ScheduleDay(Parcel in) {
        day = (LocalDate) in.readSerializable();
        slots = in.createTypedArrayList(ScheduleSlot.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(day);
        dest.writeTypedList(slots);
    }
}
