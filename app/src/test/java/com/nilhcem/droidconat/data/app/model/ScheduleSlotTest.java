package com.nilhcem.droidconat.data.app.model;

import android.os.Build;
import android.os.Parcel;

import com.nilhcem.droidconat.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ScheduleSlotTest {

    @Test
    public void should_restore_from_parcelable() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        List<Session> sessions = Arrays.asList(new Session(1, null, null, null, null, null, null), new Session(2, null, null, null, null, null, null), new Session(3, null, null, null, null, null, null));
        ScheduleSlot slot = new ScheduleSlot(time, sessions);

        // When
        Parcel parcel = Parcel.obtain();
        slot.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ScheduleSlot fromParcel = ScheduleSlot.CREATOR.createFromParcel(parcel);

        // Then
        assertThat(fromParcel.getTime()).isEqualTo(time);
        assertThat(fromParcel.getSessions()).hasSize(3);
    }
}
