package com.nilhcem.droidconat.data.app.model;

import android.os.Build;
import android.os.Parcel;

import com.nilhcem.droidconat.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SpeakerTest {

    @Test
    public void should_restore_from_parcelable() {
        // Given
        Speaker speaker = new Speaker(24, "Name", "Title", "Bio", "website", "Twitter", "Github", "photoUrl");

        // When
        Parcel parcel = Parcel.obtain();
        speaker.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Speaker fromParcel = Speaker.CREATOR.createFromParcel(parcel);

        // Then
        assertThat(fromParcel.getId()).isEqualTo(24);
        assertThat(fromParcel.getName()).isEqualTo("Name");
        assertThat(fromParcel.getTitle()).isEqualTo("Title");
        assertThat(fromParcel.getBio()).isEqualTo("Bio");
        assertThat(fromParcel.getWebsite()).isEqualTo("website");
        assertThat(fromParcel.getTwitter()).isEqualTo("Twitter");
        assertThat(fromParcel.getGithub()).isEqualTo("Github");
        assertThat(fromParcel.getPhoto()).isEqualTo("photoUrl");
    }
}
