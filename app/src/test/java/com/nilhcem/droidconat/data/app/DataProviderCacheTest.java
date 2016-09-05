package com.nilhcem.droidconat.data.app;

import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;

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
public class DataProviderCacheTest {

    private final DataProviderCache cache = new DataProviderCache();

    private final Session session1 = new Session(1, null, null, null, null, null, null);
    private final Session session2 = new Session(2, null, null, null, null, null, null);
    private final List<Session> sessions = Arrays.asList(session1, session2);

    private final Speaker speaker1 = new Speaker(1, null, null, null, null, null, null, null);
    private final Speaker speaker2 = new Speaker(1, null, null, null, null, null, null, null);
    private final List<Speaker> speakers = Arrays.asList(speaker1, speaker2);

    @Test
    public void should_save_sessions_in_memory_keeping_save_time() {
        // Given When
        LocalDateTime before = LocalDateTime.now();
        cache.saveSessions(sessions);
        LocalDateTime after = LocalDateTime.now();

        // Then
        assertThat(cache.sessions).hasSize(2);
        assertThat(cache.sessionsFetchedTime).isAtLeast(before);
        assertThat(cache.sessionsFetchedTime).isAtMost(after);
    }

    @Test
    public void should_return_sessions_when_cache_time_is_still_active() {
        // Given
        cache.sessionsFetchedTime = LocalDateTime.now();
        cache.sessions = sessions;

        // When
        List<Session> result = cache.getSessions();

        // Then
        assertThat(result).isEqualTo(sessions);
    }

    @Test
    public void should_return_null_sessions_when_cache_has_expired() {
        // Given
        cache.sessionsFetchedTime = LocalDateTime.now().minusYears(1);
        cache.sessions = sessions;

        // When
        List<Session> result = cache.getSessions();

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void should_save_speakers_in_memory_keeping_save_time() {
        // Given When
        LocalDateTime before = LocalDateTime.now();
        cache.saveSpeakers(speakers);
        LocalDateTime after = LocalDateTime.now();

        // Then
        assertThat(cache.speakers).hasSize(2);
        assertThat(cache.speakersFetchedTime).isAtLeast(before);
        assertThat(cache.speakersFetchedTime).isAtMost(after);
    }

    @Test
    public void should_return_speakers_when_cache_time_is_still_active() {
        // Given
        cache.speakersFetchedTime = LocalDateTime.now();
        cache.speakers = speakers;

        // When
        List<Speaker> result = cache.getSpeakers();

        // Then
        assertThat(result).isEqualTo(speakers);
    }

    @Test
    public void should_return_null_speakers_when_cache_has_expired() {
        // Given
        cache.speakersFetchedTime = LocalDateTime.now().minusYears(1);
        cache.speakers = speakers;

        // When
        List<Speaker> result = cache.getSpeakers();

        // Then
        assertThat(result).isNull();
    }
}
