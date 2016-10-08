package com.nilhcem.mobilization.data.app.model;

import android.os.Build;
import android.os.Parcel;

import com.nilhcem.mobilization.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ScheduleTest {

    @Test
    public void should_restore_from_parcelable() {
        // Given
        Schedule schedule = new Schedule();
        ScheduleDay day1 = new ScheduleDay(null, null);
        ScheduleDay day2 = new ScheduleDay(null, null);
        schedule.add(day1);
        schedule.add(day2);

        // When
        Parcel parcel = Parcel.obtain();
        schedule.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Schedule fromParcel = Schedule.CREATOR.createFromParcel(parcel);

        // Then
        assertThat(schedule).isEqualTo(fromParcel);
        assertThat(fromParcel).hasSize(2);
    }
}
