package com.nilhcem.droidconat.utils;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;

public class AppTest {

    @Test
    public void should_return_true_when_api_is_compatible() {
        // Given
        int apiLevelCompatible = android.os.Build.VERSION.SDK_INT;
        int apiLevelBelow = android.os.Build.VERSION.SDK_INT - 1;

        // When
        boolean result1 = App.isCompatible(apiLevelCompatible);
        boolean result2 = App.isCompatible(apiLevelBelow);

        // Then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
    }

    @Test
    public void should_return_false_when_api_is_incompatible() {
        // Given
        int apiLevelIncompatible = android.os.Build.VERSION.SDK_INT + 1;

        // When
        boolean result = App.isCompatible(apiLevelIncompatible);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void should_return_formatted_string_version() {
        // Given
        assume().withFailureMessage("Do not test internal builds").that(BuildConfig.INTERNAL_BUILD).isFalse();
        String expected = BuildConfig.VERSION_NAME + " (#" + BuildConfig.VERSION_CODE + ")";

        // When
        String version = App.getVersion();

        // Then
        assertThat(version).isEqualTo(expected);
    }

    @Test
    public void should_return_null_photourl_when_giving_invalid_data() {
        // Given
        Session session1 = null;
        Session session2 = new Session(3, "room1", null, "title", "description", null, null);
        Session session3 = new Session(3, "room1", new ArrayList<>(), "title", "description", null, null);

        // When
        String result1 = App.getPhotoUrl(session1);
        String result2 = App.getPhotoUrl(session2);
        String result3 = App.getPhotoUrl(session3);

        // Then
        assertThat(result1).isNull();
        assertThat(result2).isNull();
        assertThat(result3).isNull();
    }

    @Test
    public void should_return_photo_url_of_first_speaker() {
        // Given
        Speaker speaker1 = new Speaker(1, null, null, null, null, null, null, "photo1");
        Speaker speaker2 = new Speaker(2, null, null, null, null, null, null, "photo2");
        Session session = new Session(1, null, Arrays.asList(speaker1, speaker2), null, null, null, null);

        // When
        String photoUrl = App.getPhotoUrl(session);

        // Then
        assertThat(photoUrl).isEqualTo("photo1");
    }
}
