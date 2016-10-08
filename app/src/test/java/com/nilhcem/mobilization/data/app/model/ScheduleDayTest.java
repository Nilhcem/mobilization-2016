package com.nilhcem.mobilization.data.app.model;

import android.os.Build;
import android.os.Parcel;

import com.nilhcem.mobilization.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDate;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.singletonList;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ScheduleDayTest {

    @Test
    public void should_restore_from_parcelable() {
        // Given
        LocalDate day = LocalDate.of(1985, 5, 15);
        List<ScheduleSlot> slots = singletonList(new ScheduleSlot(null, null));
        ScheduleDay scheduleDay = new ScheduleDay(day, slots);

        // When
        Parcel parcel = Parcel.obtain();
        scheduleDay.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ScheduleDay fromParcel = ScheduleDay.CREATOR.createFromParcel(parcel);

        // Then
        assertThat(fromParcel.getDay()).isEqualTo(day);
        assertThat(fromParcel.getSlots()).hasSize(1);
    }
}
