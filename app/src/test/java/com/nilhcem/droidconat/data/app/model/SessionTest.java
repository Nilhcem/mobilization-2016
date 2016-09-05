package com.nilhcem.droidconat.data.app.model;

import android.os.Build;
import android.os.Parcel;

import com.nilhcem.droidconat.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDateTime;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.singletonList;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SessionTest {

    @Test
    public void should_restore_from_parcelable() {
        // Given
        List<Speaker> speakers = singletonList(new Speaker(1, null, null, null, null, null, null, null));
        LocalDateTime fromTime = LocalDateTime.now().minusDays(1);
        LocalDateTime toTime = fromTime.plusMinutes(45);
        Session session = new Session(42, "ROOM1", speakers, "TITLE", "DESCRIPTION", fromTime, toTime);

        // When
        Parcel parcel = Parcel.obtain();
        session.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Session fromParcel = Session.CREATOR.createFromParcel(parcel);

        // Then
        assertThat(fromParcel.getId()).isEqualTo(42);
        assertThat(fromParcel.getRoom()).isEqualTo("ROOM1");
        assertThat(fromParcel.getSpeakers()).hasSize(1);
        assertThat(fromParcel.getTitle()).isEqualTo("TITLE");
        assertThat(fromParcel.getDescription()).isEqualTo("DESCRIPTION");
        assertThat(fromParcel.getFromTime()).isEqualTo(fromTime);
        assertThat(fromParcel.getToTime()).isEqualTo(toTime);
    }
}
